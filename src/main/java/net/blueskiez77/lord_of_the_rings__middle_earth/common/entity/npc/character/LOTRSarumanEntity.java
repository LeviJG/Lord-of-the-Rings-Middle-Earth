package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character;

import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySaruman: the original's joke Saruman -- 20 strong and quick (0.5),
 * of Isengard, with the white staff. He eats logs, grunts like an orc ten
 * times a second, and flings away everything within 24 blocks but Gandalf and
 * rabbits, harder the closer (players a third as hard, by a sizeless
 * explosion, bang and all). He conjures rabbits about him, and goes after one
 * now and then to set it on the top of whatever tower of rabbits is already
 * riding him. His name is a hundred random characters, always shown, and
 * rendered in shifting colours, twitching. He may drop bones.
 *
 * <p>The rabbits are vanilla's: the original's own rabbit is dropped as a
 * vanilla duplicate.
 */
public class LOTRSarumanEntity extends LOTRNPCEntity {

    private @Nullable Rabbit targetingRabbit;
    private @Nullable String randomNameTag;

    public LOTRSarumanEntity(EntityType<? extends LOTRSarumanEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LOTREatGoal(this, LOTRFoods.SARUMAN, 200));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 20.0f, 0.05f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ISENGARD;
    }

    /** getCustomNameTag: a hundred random characters, fixed for this instance. */
    @Override
    public Component getCustomName() {
        if (this.randomNameTag == null) {
            StringBuilder tmp = new StringBuilder();
            for (int l = 0; l < 100; ++l) {
                tmp.append((char) this.random.nextInt(1000));
            }
            this.randomNameTag = tmp.toString();
        }
        return Component.literal(this.randomNameTag);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public boolean isCustomNameVisible() {
        return true;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return LOTRSounds.ORC_SAY;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 10;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        if (this.random.nextInt(10) == 0) {
            playSound(LOTRSounds.ORC_SAY, getSoundVolume(), getVoicePitch());
        }
        for (Entity entity : level.getEntities(this, getBoundingBox().inflate(24.0))) {
            if (entity instanceof Rabbit || entity instanceof LOTRGandalfEntity) {
                continue;
            }
            double dSq = distanceToSqr(entity);
            if (dSq <= 0.0) {
                dSq = 1.0E-5;
            }
            float strength = entity instanceof net.minecraft.world.entity.player.Player ? 1.0f / 3.0f : 1.0f;
            double force = strength / dSq;
            Vec3 push = entity.position().subtract(position()).scale(force);
            if (entity instanceof ServerPlayer player) {
                player.connection.send(new ClientboundExplodePacket(position(), 0.0f, 0, Optional.of(push),
                        ParticleTypes.EXPLOSION, SoundEvents.GENERIC_EXPLODE, WeightedList.of()));
                continue;
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(push));
        }
        if (this.random.nextInt(40) == 0) {
            Rabbit rabbit = EntityTypes.RABBIT.create(level, EntitySpawnReason.MOB_SUMMONED);
            if (rabbit != null) {
                int i = Mth.floor(getX()) - this.random.nextInt(16) + this.random.nextInt(16);
                int j = Mth.floor(getBoundingBox().minY) - this.random.nextInt(8) + this.random.nextInt(8);
                int k = Mth.floor(getZ()) - this.random.nextInt(16) + this.random.nextInt(16);
                rabbit.snapTo(i, j, k, 0.0f, 0.0f);
                if (level.noCollision(rabbit) && level.isUnobstructed(rabbit)
                        && !level.containsAnyLiquid(rabbit.getBoundingBox())) {
                    level.addFreshEntity(rabbit);
                }
            }
        }
        if (this.targetingRabbit == null && this.random.nextInt(20) == 0) {
            List<Rabbit> rabbits = level.getEntitiesOfClass(Rabbit.class, getBoundingBox().inflate(24.0));
            if (!rabbits.isEmpty()) {
                Rabbit rabbit = rabbits.get(this.random.nextInt(rabbits.size()));
                if (!rabbit.isPassenger()) {
                    this.targetingRabbit = rabbit;
                }
            }
        }
        if (this.targetingRabbit != null) {
            if (this.targetingRabbit.isAlive()) {
                getNavigation().moveTo(this.targetingRabbit, 1.0);
                if (distanceTo(this.targetingRabbit) < 1.0f) {
                    Entity entityToMount = this;
                    while (entityToMount.getFirstPassenger() != null) {
                        entityToMount = entityToMount.getFirstPassenger();
                    }
                    this.targetingRabbit.startRiding(entityToMount, true, false);
                    this.targetingRabbit = null;
                }
            } else {
                this.targetingRabbit = null;
            }
        }
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int j = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int k = 0; k < j; ++k) {
            spawnAtLocation(level, Items.BONE);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(LOTRStoryItems.GANDALF_STAFF_WHITE));
        return data;
    }
}
