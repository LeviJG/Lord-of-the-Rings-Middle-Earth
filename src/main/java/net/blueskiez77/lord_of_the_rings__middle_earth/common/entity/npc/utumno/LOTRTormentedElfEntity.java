package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityTormentedElf: an elf broken in Utumno's dungeons and turned to its service -- silent, with
 * an Utumno sword or dagger (poisoned or not) and an Utumno bow, leaving no lembas.
 */
public class LOTRTormentedElfEntity extends LOTRElfEntity {

    public LOTRTormentedElfEntity(EntityType<? extends LOTRTormentedElfEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRElfEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public boolean canElfSpawnHere(LevelAccessor level) {
        return true;
    }

    @Override
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
    }

    @Override
    public @Nullable SoundEvent getAttackSound() {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_TORMENTED_ELF;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return "utumno/elf/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item weapon = switch (this.random.nextInt(5)) {
            case 0, 1, 2 -> LOTRCombatItems.UTUMNO_SWORD;
            case 3 -> LOTRCombatItems.UTUMNO_DAGGER;
            default -> LOTRCombatItems.POISONED_UTUMNO_DAGGER;
        };
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapon));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.UTUMNO_BOW));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }
}
