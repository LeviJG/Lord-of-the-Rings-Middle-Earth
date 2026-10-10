package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGrapevineBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRDorwinionBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDorwinionGuard (the Vintner Guard): Dorwinion's watch, always
 * men, seeking out their people's enemies and chasing even what they cannot
 * see, with an iron sword, battleaxe or pike -- one in eight with an iron
 * spear besides -- in Dorwinion armour, three in four helmeted. Its alert
 * over stolen grapes ("GrapeAlert", up to 3) cools by one every thirty
 * seconds.
 *
 * <p>They defend Dorwinion's vineyards (defendGrapevines): a player picking or
 * breaking ripe grapes there may be warned, attacked, and have more guards
 * called in. Slaying a fully alerted guard earns stealDorwinionGrapes.
 *
 * <p>NOT ported yet: throwing the spear (spears keep vanilla's mechanics, user).
 */
public class LOTRDorwinionGuardEntity extends LOTRDorwinionManEntity {

    public static final int MAX_GRAPE_ALERT = 3;
    public int grapeAlert;

    public LOTRDorwinionGuardEntity(EntityType<? extends LOTRDorwinionGuardEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_DORWINION;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createDorwinionAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, true);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "dorwinion/guard/hired" : "dorwinion/guard/friendly";
        }
        return "dorwinion/guard/hostile";
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && this.grapeAlert > 0 && this.tickCount % 600 == 0) {
            --this.grapeAlert;
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("GrapeAlert", this.grapeAlert);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.grapeAlert = input.getIntOr("GrapeAlert", 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {Items.IRON_SWORD, LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.IRON_PIKE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        if (this.random.nextInt(8) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.IRON_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DORWINION_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DORWINION_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DORWINION_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(4) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.DORWINION_HELMET));
        return data;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide() && source.getEntity() instanceof Player player && this.grapeAlert >= MAX_GRAPE_ALERT) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.STEAL_DORWINION_GRAPES);
        }
    }

    /** isFullGrownGrapes: a vine bearing grapes, ripe. */
    public static boolean isFullGrownGrapes(BlockState state) {
        return state.getBlock() instanceof LOTRGrapevineBlock && !state.is(LOTRDecorationBlocks.GRAPEVINE)
                && state.getValue(LOTRGrapevineBlock.AGE) >= LOTRGrapevineBlock.MAX_AGE;
    }

    /**
     * LOTREventHandler's grapevine hooks: breaking ripe grapes, or a vine with ripe grapes up to
     * three above it, defends the grapes (where they hang, for the latter).
     */
    public static void initVineyardDefence() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            if (level instanceof ServerLevel world) {
                if (isFullGrownGrapes(state)) {
                    defendGrapevines(player, world, pos.getX(), pos.getY(), pos.getZ());
                } else {
                    boolean grapesAbove = false;
                    for (int j1 = 1; j1 <= 3; ++j1) {
                        if (isFullGrownGrapes(world.getBlockState(pos.above(j1)))) {
                            grapesAbove = true;
                        }
                    }
                    if (grapesAbove) {
                        defendGrapevines(player, world, pos.getX(), pos.getY() + 1, pos.getZ());
                    }
                }
            }
            return true;
        });
    }

    /**
     * defendGrapevines: grapes taken in a Dorwinion vineyard by a player not in creative. Fewer than
     * eight guards near, and one time in four -- always for an enemy of Dorwinion, else by a chance
     * falling as alignment rises to +2000 -- one to six guards or crossbowers are called in around
     * them. Then every guard within sixteen attacks an enemy at once; others, by that same chance,
     * warn and at the third alert attack. If any were roused, a friend loses a little alignment.
     */
    public static void defendGrapevines(Player entityplayer, ServerLevel world, int i, int j, int k) {
        if (entityplayer.isCreative()) {
            return;
        }
        LOTRWorldChunkManager chunkManager = LOTRWorldChunkManager.of(world);
        LOTRBiomeVariant variant = chunkManager == null ? null : chunkManager.getBiomeVariantAt(i, k);
        if (!(LOTRBiomes.of(world.getBiome(new BlockPos(i, j, k))) instanceof LOTRDorwinionBiome) || variant != LOTRBiomeVariant.VINEYARD) {
            return;
        }
        RandomSource rand = world.getRandom();
        float alignment = LOTRPlayerAlignments.getAlignment(entityplayer, LOTRFaction.DORWINION);
        boolean evil = alignment < 0.0f;
        float limit = 2000.0f;
        float chance = (limit - alignment) / limit;
        chance = Math.max(chance, 0.0f);
        chance = Math.min(chance, 1.0f);
        chance *= chance;
        if ((evil || rand.nextFloat() < chance) && rand.nextInt(4) == 0) {
            int nearbyGuards = 0;
            int spawnRange = 8;
            for (LOTRDorwinionGuardEntity guard : world.getEntitiesOfClass(LOTRDorwinionGuardEntity.class,
                    entityplayer.getBoundingBox().inflate(spawnRange))) {
                if (!guard.hiredNPCInfo.isActive) {
                    ++nearbyGuards;
                }
            }
            if (nearbyGuards < 8) {
                int guardSpawns = 1 + rand.nextInt(6);
                block1:
                for (int l = 0; l < guardSpawns; ++l) {
                    LOTRDorwinionGuardEntity guard = (rand.nextBoolean() ? LOTREntities.DORWINION_CROSSBOWER : LOTREntities.DORWINION_GUARD)
                            .create(world, EntitySpawnReason.EVENT);
                    if (guard == null) {
                        continue;
                    }
                    int attempts = 16;
                    for (int a = 0; a < attempts; ++a) {
                        int i1 = i + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -spawnRange, spawnRange);
                        int j1 = j + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -spawnRange / 2, spawnRange / 2);
                        int k1 = k + LOTRWorldGenUtil.getRandomIntegerInRange(rand, -spawnRange, spawnRange);
                        BlockPos at = new BlockPos(i1, j1, k1);
                        BlockPos below = at.below();
                        boolean belowSolid = world.getBlockState(below).isFaceSturdy(world, below, Direction.UP);
                        if (!belowSolid || world.getBlockState(at).isRedstoneConductor(world, at)
                                || world.getBlockState(at.above()).isRedstoneConductor(world, at.above())) {
                            continue;
                        }
                        guard.snapTo(i1 + 0.5, j1, k1 + 0.5, rand.nextFloat() * 360.0f, 0.0f);
                        guard.liftSpawnRestrictions = true;
                        if (!guard.checkSpawnRules(world, EntitySpawnReason.EVENT)) {
                            continue;
                        }
                        guard.liftSpawnRestrictions = false;
                        guard.spawnRidingHorse = false;
                        guard.finalizeSpawn(world, world.getCurrentDifficultyAt(at), EntitySpawnReason.EVENT, null);
                        world.addFreshEntity(guard);
                        continue block1;
                    }
                }
            }
        }
        int range = 16;
        boolean anyAlert = false;
        for (LOTRDorwinionGuardEntity guard : world.getEntitiesOfClass(LOTRDorwinionGuardEntity.class,
                entityplayer.getBoundingBox().inflate(range))) {
            if (guard.hiredNPCInfo.isActive) {
                continue;
            }
            if (evil) {
                guard.setTarget(entityplayer);
                guard.sendSpeechBank(entityplayer, "dorwinion/guard/grapeAttack");
                guard.grapeAlert = 3;
                anyAlert = true;
                continue;
            }
            if (rand.nextFloat() >= chance) {
                continue;
            }
            ++guard.grapeAlert;
            if (guard.grapeAlert >= 3) {
                guard.setTarget(entityplayer);
                guard.sendSpeechBank(entityplayer, "dorwinion/guard/grapeAttack");
            } else {
                guard.sendSpeechBank(entityplayer, "dorwinion/guard/grapeWarn");
            }
            anyAlert = true;
        }
        if (anyAlert && alignment >= 0.0f) {
            LOTRPlayerAlignments.addAlignment(entityplayer, LOTRAlignmentValues.VINEYARD_STEAL_PENALTY, LOTRFaction.DORWINION,
                    i + 0.5, j + 0.5, k + 0.5);
        }
    }
}
