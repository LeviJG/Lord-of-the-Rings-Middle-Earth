package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRLothlorienBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGaladhrimWarrior: a warrior of Lothlórien in Galadhrim armour --
 * nine in ten with its helmet -- with an elven sword, or now and then a
 * battlestaff or longspear, and a Galadhrim bow shooting from 24 blocks;
 * one in five carries a spear as well, one in four rides a barded horse.
 *
 * <p>One called out to defend the trees slain by a player earns them takeMallornWood.
 *
 * <p>Four to six are called out, one time in three, against a player of ill standing with
 * Lothlórien who fells mallorn there (LOTRBlockWood.removedByPlayer).
 *
 * <p>NOT ported yet: throwing the spear (spears keep vanilla's
 * mechanics, user).
 */
public class LOTRGaladhrimWarriorEntity extends LOTRGaladhrimElfEntity {

    /** Called out to defend Lothlórien's trees. */
    public boolean isDefendingTree;

    public LOTRGaladhrimWarriorEntity(EntityType<? extends LOTRGaladhrimWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_GALADHRIM;
        this.spawnRidingHorse = this.random.nextInt(4) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRElfEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, meleeAttackAI());
    }

    @Override
    protected Goal createElfMeleeAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 30, 40, 24.0f);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "galadhrim/elf/hired" : "galadhrim/warrior/friendly";
        }
        return "galadhrim/warrior/hostile";
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DefendingTree", this.isDefendingTree);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.isDefendingTree = input.getBooleanOr("DefendingTree", false);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        int i = this.random.nextInt(6);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(i == 0 ? LOTRCombatItems.GALADHRIM_BATTLESTAFF
                : i == 1 ? LOTRCombatItems.GALADHRIM_LONGSPEAR : LOTRCombatItems.GALADHRIM_SWORD));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.GALADHRIM_BOW));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GALADHRIM_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GALADHRIM_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GALADHRIM_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GALADHRIM_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GALADHRIM_HELMET));
        }
        return data;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide() && this.isDefendingTree && source.getEntity() instanceof Player player) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TAKE_MALLORN_WOOD);
        }
    }

    /**
     * LOTRBlockWood.removedByPlayer: mallorn felled in Lothlórien by a player not in creative and
     * in ill standing with Lothlórien calls out four to six warriors about them, one time in three --
     * each on firm ground with room, if it may spawn there -- the first telling them why.
     */
    public static void defendTrees(ServerLevel world, Player player, BlockPos pos, BlockState state) {
        if (!state.is(LOTRBuildingBlocks.MALLORN_LOG) || world.getRandom().nextInt(3) != 0
                || !(LOTRBiomes.of(world.getBiome(pos)) instanceof LOTRLothlorienBiome)
                || LOTRPlayerAlignments.getAlignment(player, LOTRFaction.LOTHLORIEN) >= 0.0f || player.isCreative()) {
            return;
        }
        int elves = 4 + world.getRandom().nextInt(3);
        boolean sentMessage = false;
        for (int l = 0; l < elves; ++l) {
            LOTRGaladhrimWarriorEntity elfWarrior = LOTREntities.GALADHRIM_WARRIOR.create(world, EntitySpawnReason.EVENT);
            if (elfWarrior == null) {
                continue;
            }
            int i1 = Mth.floor(player.getX()) - 6 + world.getRandom().nextInt(12);
            int k1 = Mth.floor(player.getZ()) - 6 + world.getRandom().nextInt(12);
            int j1 = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i1, k1);
            BlockPos below = new BlockPos(i1, j1 - 1, k1);
            BlockPos at = below.above();
            if (!world.getBlockState(below).isFaceSturdy(world, below, Direction.UP)
                    || world.getBlockState(at).isRedstoneConductor(world, at)
                    || world.getBlockState(at.above()).isRedstoneConductor(world, at.above())) {
                continue;
            }
            elfWarrior.snapTo(i1 + 0.5, j1, k1 + 0.5, 0.0f, 0.0f);
            if (!elfWarrior.checkSpawnRules(world, EntitySpawnReason.EVENT)) {
                continue;
            }
            elfWarrior.spawnRidingHorse = false;
            elfWarrior.finalizeSpawn(world, world.getCurrentDifficultyAt(at), EntitySpawnReason.EVENT, null);
            world.addFreshEntity(elfWarrior);
            elfWarrior.isDefendingTree = true;
            elfWarrior.setTarget(player);
            if (!sentMessage) {
                elfWarrior.sendSpeechBank(player, "galadhrim/warrior/defendTrees");
                sentMessage = true;
            }
        }
    }
}
