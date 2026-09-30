package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySeagull: a songbird 1.4 times the size, with its own cry, that
 * also steals fish, raw or cooked.
 *
 * <p>NOT ported yet: canBirdSpawnHere's shore check (leaves or sand around,
 * fewer than two gulls within 16 blocks), D12.
 */
public class LOTRSeagullEntity extends LOTRBirdEntity {

    /** SEAGULL_SCALE. */
    public static final float SCALE = 1.4f;

    public LOTRSeagullEntity(EntityType<? extends LOTRSeagullEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setBirdType(BirdType.COMMON);
        return data;
    }

    @Override
    public boolean canStealItems() {
        return true;
    }

    /** Items.fish and Items.cooked_fished, every kind. */
    @Override
    public boolean isStealable(ItemStack stack) {
        if (stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH) || stack.is(Items.PUFFERFISH)
                || stack.is(Items.COOKED_COD) || stack.is(Items.COOKED_SALMON)) {
            return true;
        }
        return super.isStealable(stack);
    }

    @Override
    public String getBirdTextureDir() {
        return "seagull";
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.BIRD_SEAGULL_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.BIRD_SEAGULL_HURT;
    }
}
