package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRangerNorthCaptain: a captain of the Rangers, with an iron sword,
 * on foot, who seeks no one out. He hires out Rangers, on foot or mounted,
 * and banner bearers, to those at +300 or better.
 *
 * <p>NOT ported yet: his warhorn (LOTRInvasions.RANGER_NORTH, D12) and the
 * tradeRangerNorthCaptain achievement (D7).
 */
public class LOTRRangerNorthCaptainEntity extends LOTRRangerNorthEntity implements LOTRUnitTradeable {

    public LOTRRangerNorthCaptainEntity(EntityType<? extends LOTRRangerNorthCaptainEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.RANGER_NORTH_CAPTAIN;
    }

    /** canTradeWith: +300 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 300.0f && isFriendly(player);
    }

    @Override
    public boolean shouldTraderRespawn() {
        return true;
    }

    @Override
    public float getAlignmentBonus() {
        return 5.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "rangerNorth/captain/friendly" : "rangerNorth/captain/neutral";
        }
        return "rangerNorth/ranger/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(Items.IRON_SWORD));
        return data;
    }
}
