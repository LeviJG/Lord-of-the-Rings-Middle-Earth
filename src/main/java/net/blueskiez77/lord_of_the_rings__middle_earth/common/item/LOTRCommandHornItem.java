package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNPCInfo;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;

/**
 * LOTRItemCommandHorn: the horn a captain uses to order his hired units about.
 *
 * <p>Built on {@link InstrumentItem}, which is 26.2's goat horn. That gives the
 * blow itself for free -- the sound, its range, the use duration and the
 * cooldown -- from a datapack instrument at
 * {@code data/lotr/instrument/command_horn.json}, which names the original's
 * own lotr:item.horn sound. 1.7.10 had none of that machinery and simply held
 * the item for 40 ticks and then played a sound at volume 4.
 *
 * <p>The horn has four forms, which were metadata 0 through 3 and are a byte in
 * the stack's custom_data here. Form 0 is the plain horn: right-clicking it
 * opens the selection screen rather than blowing it. The other three are the
 * orders -- halt, ready and summon -- and blowing one gives that order. Halt and
 * ready swap after each blow, exactly as the original did, so one horn toggles a
 * squadron between held and moving.
 *
 * <p>Blown, it gives its order to the player's hired units of its squadron
 * ({@link #command}).
 */
public class LOTRCommandHornItem extends InstrumentItem {

    /** LOTRSquadrons: the NBT key, and the cap it trimmed names to. */
    public static final String TAG_SQUADRON = "LOTRSquadron";
    public static final int SQUADRON_LENGTH_MAX = 200;

    private static final String TAG_MODE = "HornMode";

    /** The instrument in data/lotr/instrument/command_horn.json. */
    public static final ResourceKey<net.minecraft.world.item.Instrument> INSTRUMENT =
            ResourceKey.create(net.minecraft.core.registries.Registries.INSTRUMENT,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "command_horn"));

    /** Metadata 0 through 3, in the original's order. */
    public enum Mode {
        /** The plain horn: it opens the selection screen instead of blowing. */
        SELECT(""),
        HALT(".halt"),
        READY(".ready"),
        SUMMON(".summon");

        private final String suffix;

        Mode(String suffix) {
            this.suffix = suffix;
        }

        /** getUnlocalizedName appended this to the base name. */
        public String suffix() {
            return this.suffix;
        }
    }

    public LOTRCommandHornItem(Properties properties) {
        super(properties);
    }

    public static Mode getMode(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return Mode.SELECT;
        }
        int mode = data.copyTag().getByteOr(TAG_MODE, (byte) 0);
        Mode[] values = Mode.values();
        return mode < 0 || mode >= values.length ? Mode.SELECT : values[mode];
    }

    public static void setMode(ItemStack stack, Mode mode) {
        edit(stack, tag -> tag.putByte(TAG_MODE, (byte) mode.ordinal()));
    }

    /** LOTRSquadrons.getSquadron: which units this horn speaks to. */
    public static String getSquadron(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? "" : data.copyTag().getStringOr(TAG_SQUADRON, "");
    }

    /** LOTRSquadrons.checkAcceptableLength: no more than 200 characters. */
    public static String checkAcceptableLength(String squadron) {
        return squadron.length() > SQUADRON_LENGTH_MAX ? squadron.substring(0, SQUADRON_LENGTH_MAX) : squadron;
    }

    /** LOTRSquadrons.setSquadron, trimming to the same 200 characters. */
    public static void setSquadron(ItemStack stack, String squadron) {
        String trimmed = squadron == null ? "" : squadron;
        if (trimmed.length() > SQUADRON_LENGTH_MAX) {
            trimmed = trimmed.substring(0, SQUADRON_LENGTH_MAX);
        }
        String value = trimmed;
        edit(stack, tag -> tag.putString(TAG_SQUADRON, value));
    }

    private static void edit(ItemStack stack, Consumer<CompoundTag> edit) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA, data.update(edit));
    }

    /** One stack per form, for the creative tab: getSubItems gave all four. */
    public static ItemStack stack(Mode mode) {
        ItemStack stack = new ItemStack(LOTRCombatItems.COMMAND_HORN);
        setMode(stack, mode);
        return stack;
    }

    /** getUnlocalizedName(ItemStack): the form's suffix on the base name. */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId() + getMode(stack).suffix());
    }

    /**
     * onItemRightClick: form 0 opens the screen, the rest blow the horn.
     *
     * <p>The screen is opened client-side, the way the beacon's naming dialog
     * is, so no client class leaks into the common source set; the chosen form
     * comes back as LOTRHornModePayload.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getMode(stack) == Mode.SELECT) {
            // Handled by the client, which opens LOTRHornSelectScreen.
            return InteractionResult.PASS;
        }

        InteractionResult result = super.use(level, player, hand);
        if (!level.isClientSide()) {
            command(level, player, stack);
            toggleHaltReady(stack);
        }
        return result;
    }

    /**
     * onEaten's halt/ready flip: a horn that has just called the halt is ready
     * to call the advance, and the other way about.
     */
    private static void toggleHaltReady(ItemStack stack) {
        Mode mode = getMode(stack);
        if (mode == Mode.HALT) {
            setMode(stack, Mode.READY);
        } else if (mode == Mode.READY) {
            setMode(stack, Mode.HALT);
        }
    }

    /**
     * onEaten: every hired unit of this player, anywhere loaded, whose
     * squadron matches the horn's -- halted, readied or summoned to the
     * player, if it is a warrior not on guard.
     */
    private static void command(Level level, Player player, ItemStack stack) {
        if (!(level instanceof ServerLevel server)) {
            return;
        }
        Mode mode = getMode(stack);
        String squadron = getSquadron(stack);
        for (LOTRNPCEntity npc : server.getEntities(EntityTypeTest.forClass(LOTRNPCEntity.class),
                npc -> npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == player
                        && npc.hiredNPCInfo.isSquadronCompatible(squadron))) {
            LOTRHiredNPCInfo hired = npc.hiredNPCInfo;
            if (mode == Mode.HALT && hired.getObeyHornHaltReady()) {
                hired.halt();
            } else if (mode == Mode.READY && hired.getObeyHornHaltReady()) {
                hired.ready();
            } else if (mode == Mode.SUMMON && hired.getObeyHornSummon()) {
                hired.tryTeleportToHiringPlayer(true);
            }
        }
    }
}
