package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitBounderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBreeRuffian: the Southerners' men who haunt Bree-land, of the
 * hidden Ruffian faction (their influence zone is Isengard's). Always men,
 * named as Dunlendings or Bree-folk half and half, armed with an iron sword,
 * dagger or battleaxe, one in four in a black or brown hat with a white
 * feather. They keep clear of the Rangers and Bree-land's guards, and strike now and then at
 * a player reeling with nausea. Hurt one and those nearby join in -- more
 * of them the more it is hurt. Slain, they leave silver coins and sometimes
 * a skull cup.
 *
 * <p>NOT ported yet: mini-quests and their colour (D14).
 */
public abstract class LOTRBreeRuffianEntity extends LOTRBreeManEntity {

    private int ruffianAngerTick;

    protected LOTRBreeRuffianEntity(EntityType<? extends LOTRBreeRuffianEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        int target = addTargetTasks(false);
        this.targetSelector.addGoal(target + 1, new LOTRNearestAttackableTargetGoal(this, Player.class, 0, true,
                entity -> canRuffianTarget((Player) entity)));
    }

    @Override
    protected void addBreeAvoidAI(int prio) {
        this.goalSelector.addGoal(prio, new AvoidEntityGoal<>(this, LOTRRangerEntity.class, 12.0f, 1.0, 1.5));
        this.goalSelector.addGoal(prio, new AvoidEntityGoal<>(this, LOTRBreeGuardEntity.class, 12.0f, 1.0, 1.5));
    }

    /**
     * A player under nausea may be set upon: never below twenty seconds of it
     * left, rising to a one-in-twenty chance at two minutes.
     */
    public boolean canRuffianTarget(Player player) {
        MobEffectInstance nausea = player.getEffect(MobEffects.NAUSEA);
        if (nausea != null) {
            int nauseaTime = nausea.getDuration() / 20;
            int minNauseaTime = 20;
            int fullNauseaTime = 120;
            float chance = (float) (nauseaTime - minNauseaTime) / (fullNauseaTime - minNauseaTime);
            return this.random.nextFloat() < chance * 0.05f;
        }
        return false;
    }

    /** attackEntityFrom: the ruffians around, out to a range that grows with its anger, turn on the attacker. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (!super.hurtServer(level, source, damage)) {
            return false;
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            this.ruffianAngerTick += 100;
            double range = Math.min(this.ruffianAngerTick / 25.0, 24.0);
            for (LOTRBreeRuffianEntity ruffian : level.getEntitiesOfClass(LOTRBreeRuffianEntity.class,
                    getBoundingBox().inflate(range))) {
                if (!ruffian.isAlive() || ruffian.hiredNPCInfo.isActive && ruffian.hiredNPCInfo.getHiringPlayer() == attacker
                        || ruffian.getTarget() != null) {
                    continue;
                }
                ruffian.setTarget(attacker);
                if (attacker instanceof Player player) {
                    String speech = ruffian.getSpeechBank(player);
                    if (speech != null) {
                        ruffian.sendSpeechBank(player, speech);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && this.ruffianAngerTick > 0) {
            --this.ruffianAngerTick;
        }
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int coins = 2 + this.random.nextInt(3) + this.random.nextInt((looting + 1) * 3);
        for (int l = 0; l < coins; ++l) {
            spawnAtLocation(level, LOTRMiscItems.SILVER_COIN);
        }
        if (this.random.nextInt(5) == 0) {
            spawnAtLocation(level, LOTRVessel.SKULL.emptyStack(), 0.0f);
        }
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.RUFFIAN;
    }

    @Override
    public LOTRFaction getInfluenceZoneFaction() {
        return LOTRFaction.ISENGARD;
    }

    @Override
    public float getAlignmentBonus() {
        return 0.0f;
    }

    @Override
    public boolean isCivilianNPC() {
        return false;
    }

    @Override
    public boolean lootsExtraCoins() {
        return true;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(this.random.nextBoolean()
                ? LOTRNames.getDunlendingName(this.random, this.familyInfo.isMale())
                : LOTRNames.getBreeName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "bree/ruffian/hired" : "bree/ruffian/friendly";
        }
        return "bree/ruffian/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {Items.IRON_SWORD, Items.IRON_SWORD, LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.IRON_BATTLEAXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        if (this.random.nextInt(4) == 0) {
            setItemSlot(EquipmentSlot.HEAD,
                    LOTRHobbitBounderEntity.hat(this.random.nextBoolean() ? 0 : 6834742, 16777215));
        }
        return data;
    }
}
