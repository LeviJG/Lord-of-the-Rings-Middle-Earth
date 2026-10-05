package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRangerNorth: a Ranger of the North, with an iron dagger (two times
 * in five), a bronze one or a Barrow-blade, and a plain or a Ranger's bow;
 * one in twenty rides.
 *
 * <p>NOT ported yet: the killRangerNorth achievement (D7).
 */
public class LOTRRangerNorthEntity extends LOTRRangerEntity {

    public LOTRRangerNorthEntity(EntityType<? extends LOTRRangerNorthEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(20) == 0;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "rangerNorth/ranger/hired" : "rangerNorth/ranger/friendly";
        }
        return "rangerNorth/ranger/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        switch (this.random.nextInt(5)) {
            case 0, 1 -> this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.IRON_DAGGER));
            case 2 -> this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BRONZE_DAGGER));
            case 3 -> this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BARROW_BLADE));
            default -> {
            }
        }
        this.npcItemsInv.setRangedWeapon(new ItemStack(this.random.nextInt(2) == 0 ? Items.BOW : LOTRCombatItems.RANGER_BOW));
        return data;
    }
}
