package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.DyedItemColor;
import java.util.function.Consumer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

/**
 * LOTRItemConquestHorn: a warhorn, and blowing one brings a host down on you.
 *
 * <p>Every horn belongs to somebody. The original stored an invasion type on
 * the stack and, when it was blown, spawned an LOTREntityInvasionSpawner of that
 * type on top of the player -- a warband of that faction, persistent, hostile,
 * and yours to survive. That is the item's whole purpose.
 *
 * <p>It is keyed to an INVASION TYPE, as the original is -- not to a faction.
 * That matters: a faction can field several kinds of warband, so Gondor alone
 * has eight horns, one per fief, and the high elves have Lindon and Rivendell.
 * Forty-five in all, which is what getSubItems offered and what the creative tab
 * offers now. Only LOTRInvasions' mob table is missing; its names and factions
 * are ported.
 *
 * <p>WHAT IT DOES NOT DO, and cannot yet: call the warband. There is no invasion
 * system and no NPCs, so {@link #summon} is where that goes and is empty, as
 * LOTRCommandHornItem's and LOTRCommandSwordItem's are.
 *
 * <p>The colour is the faction's own, and it reaches the sprite through the
 * DYED_COLOR component: the model tints its base layer from it and leaves the
 * overlay alone, which is the two-render-pass trick the original used, said in
 * the way 26.2 says it.
 */
public class LOTRWarhornItem extends Item implements LOTRTooltipItem {

    /** The original's own key. It also read "HornFaction" as a legacy fallback. */
    private static final String TAG_INVASION = "InvasionType";

    public LOTRWarhornItem(Properties properties) {
        super(properties);
    }

    /** getInvasionType, defaulting the way the original did. */
    public static LOTRInvasions getInvasion(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return LOTRInvasions.HOBBIT;
        }
        CompoundTag tag = data.copyTag();
        LOTRInvasions invasion = LOTRInvasions.forName(tag.getStringOr(TAG_INVASION, ""));
        return invasion == null ? LOTRInvasions.HOBBIT : invasion;
    }

    /** One horn, dressed in a warband's name and its faction's colour. */
    public static ItemStack stack(Item horn, LOTRInvasions invasion) {
        ItemStack stack = new ItemStack(horn);
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA,
                data.update(tag -> tag.putString(TAG_INVASION, invasion.codeName())));
        // getColorFromItemStack returned the faction colour for render pass 0.
        stack.set(DataComponents.DYED_COLOR,
                new DyedItemColor(invasion.invasionFaction.getFactionColor()));
        return stack;
    }

    /**
     * getItemStackDisplayName: each horn has a name of its own --
     * "lotr.invasion.<codeName>.horn", Warhorn of the Shire, Warghorn of Angmar.
     */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("lotr.invasion." + getInvasion(stack).codeName() + ".horn");
    }

    /** addInformation: the warband it calls. */
    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(getInvasion(stack).invasionName());
    }

    /**
     * onEaten: blow it, and the warband comes.
     *
     * <p>canUseHorn refused inside Utumno, and refused a faction whose invasion
     * you had not earned the right to call. Both tests, and the spawn itself,
     * wait on the invasion system.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            summon(level, player, player.getItemInHand(hand));
        }
        return InteractionResult.SUCCESS;
    }

    private static void summon(Level level, Player player, ItemStack stack) {
        // Intentionally empty. See the class note.
    }
}
