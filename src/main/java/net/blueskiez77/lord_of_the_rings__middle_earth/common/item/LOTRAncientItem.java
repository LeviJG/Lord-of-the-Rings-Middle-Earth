package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTRItemAncientItem: an ancient sword, dagger or armour piece, which on use
 * becomes one random item from its chest pool, worn and possibly bearing
 * modifiers, with a pop.
 *
 * One time in four the result is also Wraithbane, if it can be. Using one
 * earns craftAncientItem.
 */
public class LOTRAncientItem extends Item {

    private final LOTRChestContents.Pool pool;

    public LOTRAncientItem(LOTRChestContents.Pool pool, Properties properties) {
        super(properties.stacksTo(1));
        this.pool = pool;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel) {
            ItemStack result = LOTRChestContents.pick(this.pool, level.getRandom(), false);
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_ANCIENT_ITEM);
            if (LOTRConfig.enchantingLOTR && !result.isEmpty() && LOTRModifiers.canApply(LOTRModifier.BANE_WRAITH, result, false)
                    && level.getRandom().nextInt(4) == 0) {
                List<LOTRModifier> modifiers = new ArrayList<>(LOTRModifiers.get(result));
                if (!modifiers.contains(LOTRModifier.BANE_WRAITH)) {
                    modifiers.add(LOTRModifier.BANE_WRAITH);
                    LOTRModifiers.set(result, modifiers);
                }
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP,
                    SoundSource.PLAYERS, 0.2f,
                    ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            return InteractionResult.SUCCESS.heldItemTransformedTo(result);
        }
        return InteractionResult.SUCCESS;
    }
}
