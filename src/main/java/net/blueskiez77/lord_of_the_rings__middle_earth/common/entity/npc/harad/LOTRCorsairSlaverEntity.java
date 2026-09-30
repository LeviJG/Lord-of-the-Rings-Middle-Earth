package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCorsairSlaver: a corsair who sells slaves -- Harad slaves, hired
 * to farm -- to anyone Near Harad does not dislike, bare-headed and armed as
 * any corsair. He seeks no one out -- he only answers attacks. Until the
 * branding iron exists he idles with his weapon, where the original gave him
 * the iron.
 *
 * <p>NOT ported yet: the branding iron in his idle hand, with the branding
 * iron item, and the hireHaradSlave achievement (D7).
 */
public class LOTRCorsairSlaverEntity extends LOTRCorsairEntity implements LOTRUnitTradeable {

    public LOTRCorsairSlaverEntity(EntityType<? extends LOTRCorsairSlaverEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.CORSAIR_SLAVER;
    }

    /** canTradeWith: +0 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "nearHarad/umbar/corsairSlaver/friendly" : "nearHarad/umbar/corsairSlaver/neutral";
        }
        return "nearHarad/umbar/corsair/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }
}
