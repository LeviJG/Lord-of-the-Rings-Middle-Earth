package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRInvasionSpawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.DyedItemColor;
import java.util.function.Consumer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

/**
 * LOTRItemConquestHorn: a warhorn -- held to the lips for two seconds, it calls its warband's
 * invasion down about the one who blew it. Only one with 1500 alignment with the warband's faction
 * may blow it, and not where a banner of the faction's enemies, or a respawner against it, holds
 * the land. Every horn is keyed to an invasion type, so Gondor has eight, one for each fief, and the
 * high elves have Lindon's and Rivendell's.
 *
 * <p>The colour is the faction's own, and it reaches the sprite through the DYED_COLOR component: the
 * model tints its base layer from it and leaves the overlay alone, the two render passes the
 * original used.
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

    /** createHorn: a warhorn of this invasion. */
    public static ItemStack createHorn(LOTRInvasions type) {
        return stack(LOTRCombatItems.WARHORN, type);
    }

    /** canUseHorn: never in Utumno; only with 1500 alignment; never where its enemies' banner or a respawner holds the land. */
    public static boolean canUseHorn(ItemStack stack, Level level, Player player, boolean sendMessage) {
        LOTRInvasions invasionType = getInvasion(stack);
        LOTRFaction invasionFaction = invasionType.invasionFaction;
        float alignmentRequired = 1500.0f;
        if (LOTRPlayerAlignments.getAlignment(player, invasionFaction) >= alignmentRequired) {
            boolean blocked = LOTRBannerProtection.isProtected(level, player, LOTRBannerProtection.forFaction(invasionFaction), false)
                    || LOTRNPCRespawnerEntity.isSpawnBlocked(player, invasionFaction);
            if (blocked) {
                if (sendMessage && !level.isClientSide()) {
                    player.sendSystemMessage(Component.translatable("chat.lotr.conquestHornProtected", invasionFaction.factionName()));
                }
                return false;
            }
            return true;
        }
        if (sendMessage && !level.isClientSide()) {
            LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, alignmentRequired, invasionFaction);
        }
        return false;
    }

    /** onItemRightClick: put it to the lips. */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        canUseHorn(player.getItemInHand(hand), level, player, false);
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 40;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    /** onEaten: the warband comes, three blocks above the one who blew it; the horn is spent but in creative. */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!(user instanceof Player player)) {
            return stack;
        }
        LOTRInvasions invasionType = getInvasion(stack);
        if (canUseHorn(stack, level, player, true)) {
            if (level instanceof ServerLevel server) {
                LOTRInvasionSpawnerEntity invasion = LOTREntities.INVASION_SPAWNER.create(server, EntitySpawnReason.TRIGGERED);
                if (invasion != null) {
                    invasion.setInvasionType(invasionType);
                    invasion.isWarhorn = true;
                    invasion.spawnsPersistent = true;
                    invasion.snapTo(player.getX(), player.getY() + 3.0, player.getZ(), 0.0f, 0.0f);
                    server.addFreshEntity(invasion);
                    invasion.startInvasion(player);
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return stack;
    }
}
