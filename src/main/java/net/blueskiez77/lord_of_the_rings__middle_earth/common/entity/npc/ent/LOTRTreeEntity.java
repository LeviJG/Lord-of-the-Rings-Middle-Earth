package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFangornBiome;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTREntityTree: a creature of wood -- an Ent or a huorn -- of one of three
 * trees: oak (most), beech (one in three of the rest) or birch (one in nine).
 * It shrugs off poison and takes a third of the damage of any blow but fire
 * or an axe swung by hand; it is hard to knock back, never turns on another
 * tree, cannot be re-equipped when hired, drops no rares, and leaves logs of
 * its wood and sticks.
 */
public abstract class LOTRTreeEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Byte> DATA_TREE_TYPE =
            SynchedEntityData.defineId(LOTRTreeEntity.class, EntityDataSerializers.BYTE);

    /** TYPES: oak, beech, birch. */
    public static final String[] TYPES = {"oak", "beech", "birch"};

    protected LOTRTreeEntity(EntityType<? extends LOTRTreeEntity> type, Level level) {
        super(type, level);
        if (this.random.nextInt(9) == 0) {
            setTreeType(2);
        } else if (this.random.nextInt(3) == 0) {
            setTreeType(1);
        } else {
            setTreeType(0);
        }
    }

    /** WOOD_BLOCKS and WOOD_META: its log. */
    public static Block woodBlock(int treeType) {
        return switch (treeType) {
            case 1 -> LOTRBuildingBlocks.BEECH_LOG;
            case 2 -> Blocks.BIRCH_LOG;
            default -> Blocks.OAK_LOG;
        };
    }

    /** LEAF_BLOCKS and LEAF_META: its leaves. */
    public static Block leafBlock(int treeType) {
        return switch (treeType) {
            case 1 -> LOTRDecorationBlocks.BEECH_LEAVES;
            case 2 -> Blocks.BIRCH_LEAVES;
            default -> Blocks.OAK_LEAVES;
        };
    }

    /** SAPLING_BLOCKS and SAPLING_META: its sapling. */
    public static Block saplingBlock(int treeType) {
        return switch (treeType) {
            case 1 -> LOTRDecorationBlocks.BEECH_SAPLING;
            case 2 -> Blocks.BIRCH_SAPLING;
            default -> Blocks.OAK_SAPLING;
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TREE_TYPE, (byte) 0);
    }

    public int getTreeType() {
        int i = this.entityData.get(DATA_TREE_TYPE);
        return i < 0 || i >= TYPES.length ? 0 : i;
    }

    public void setTreeType(int i) {
        this.entityData.set(DATA_TREE_TYPE, (byte) i);
    }

    /** addPotionEffect: no poison takes. */
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    public boolean doTreeDamageCalculation() {
        return true;
    }

    /** isTreeEffectiveDamage: fire, or a blow struck by hand with something that fells trees. */
    public boolean isTreeEffectiveDamage(DamageSource source) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        if (source.getEntity() instanceof LivingEntity attacker && source.getDirectEntity() == attacker) {
            ItemStack held = attacker.getMainHandItem();
            return !held.isEmpty() && held.isCorrectToolForDrops(Blocks.OAK_LOG.defaultBlockState());
        }
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (doTreeDamageCalculation() && !isTreeEffectiveDamage(source)) {
            damage /= 3.0f;
        }
        return super.hurtServer(level, source, damage);
    }

    /** knockBack: halved. */
    @Override
    public void knockback(double strength, double x, double z, DamageSource source, float damage,
                          boolean comesFromEffect) {
        super.knockback(strength, x, z, source, damage, comesFromEffect);
        setDeltaMovement(getDeltaMovement().scale(0.5));
    }

    /** setAttackTarget: never another tree. */
    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        if (target instanceof LOTRTreeEntity) {
            return;
        }
        super.setTarget(target, speak);
    }

    @Override
    public boolean canDropRares() {
        return false;
    }

    /** Logs of its wood, three to ten and more with looting, and sticks. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int logs = Mth.randomBetweenInclusive(this.random, 3, 10) + this.random.nextInt(4 * (looting + 1));
        for (int l = 0; l < logs; ++l) {
            spawnAtLocation(level, woodBlock(getTreeType()));
        }
        int sticks = Mth.randomBetweenInclusive(this.random, 6, 16) + this.random.nextInt(5 * (looting + 1));
        for (int l = 0; l < sticks; ++l) {
            spawnAtLocation(level, Items.STICK);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("EntType", (byte) getTreeType());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setTreeType(input.getByteOr("EntType", (byte) 0));
    }

    /** canReEquipHired: its player cannot dress it. */
    @Override
    public boolean canReEquipHired(int slot, ItemStack stack) {
        return false;
    }

    /** getBlockPathWeight: drawn to its home forest. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return isTreeHomeBiome(biomeAt(level, pos)) ? 20.0f : 0.0f;
    }

    public boolean isTreeHomeBiome(@Nullable LOTRBiome biome) {
        return biome instanceof LOTRFangornBiome;
    }

    /** getCanSpawnHere: above y 62, on grass or dirt (coarse dirt and podzol, the old dirt's metadata, too). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        if (super.checkSpawnRules(level, reason)) {
            if (this.liftSpawnRestrictions) {
                return true;
            }
            BlockPos pos = blockPosition();
            BlockState below = level.getBlockState(pos.below());
            return pos.getY() > 62 && (below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT) || below.is(Blocks.COARSE_DIRT)
                    || below.is(Blocks.PODZOL));
        }
        return false;
    }
}
