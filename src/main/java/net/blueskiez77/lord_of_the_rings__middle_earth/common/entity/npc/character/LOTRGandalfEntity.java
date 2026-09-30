package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGreyWandererTracker;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRGandalfSmokeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRStoryItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGandalf: the Grey Wanderer -- 30 strong, seeing 40 blocks, of no
 * faction. Nothing but a creative player can hurt him, and no NPC takes him
 * for a free target. He carries his grey staff, drawing Glamdring to fight
 * (with the staff in his other hand), fights only those who attack him and
 * Saruman, and smokes a pipe of magic smoke now and then.
 *
 * <p>He is abroad only while the Grey Wanderer tracker keeps him so: arriving
 * (from an egg too) he tells every player within 64 blocks, with a pop and a
 * puff of smoke; when his time is up and he is not fighting, he says his
 * farewell the same way and is gone.
 *
 * <p>NOT ported yet: his natural arrival beside players and his welcome
 * mini-quest (LOTRGreyWandererTracker, D12/D14); his cape (LOTRCapes.GANDALF,
 * with the NPC capes); his hunting of Balrogs (with Utumno, D15).
 */
public class LOTRGandalfEntity extends LOTRNPCEntity {

    /** handleHealthUpdate 16: his arrival's or departure's puff of smoke. */
    private static final byte EVENT_FX = 16;

    /**
     * onArtificalSpawn's arrival, held for his first tick: the egg's spawn
     * reaches finalizeSpawn before he is in the world, where no one would see
     * the smoke or hear the pop.
     */
    private boolean arrivalPending;

    public LOTRGandalfEntity(EntityType<? extends LOTRGandalfEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.8, false));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new LOTRGandalfSmokeGoal(this, 3000));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.05f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.05f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        int target = addTargetTasks(false);
        this.targetSelector.addGoal(target + 2,
                new LOTRNearestAttackableTargetGoal(this, LOTRSarumanEntity.class, 0, true, null));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (!(source.getEntity() instanceof Player player && player.isCreative())) {
            damage = 0.0f;
        }
        return super.hurtServer(level, source, damage);
    }

    @Override
    public boolean canBeFreelyTargetedBy(Mob attacker) {
        return false;
    }

    /** With Glamdring drawn, his staff in the other hand. */
    @Override
    public ItemStack getHeldItemLeft() {
        if (getMainHandItem().is(LOTRStoryItems.GLAMDRING)) {
            return new ItemStack(LOTRStoryItems.GANDALF_STAFF_GREY);
        }
        return super.getHeldItemLeft();
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND, mode == AttackMode.IDLE
                ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "char/gandalf/friendly" : "char/gandalf/hostile";
    }

    @Override
    public void onArtificalSpawn() {
        LOTRGreyWandererTracker.addNewWanderer(getUUID());
        this.arrivalPending = true;
    }

    /** arriveAt: his greeting to the player he came to, and to everyone within 64 blocks. */
    public void arriveAt(@Nullable Player player) {
        for (Player p : playersToTell(player)) {
            LOTRSpeech.sendSpeechAndChatMessage(p, this, "char/gandalf/arrive");
        }
        doGandalfFX();
    }

    public void depart() {
        for (Player p : playersToTell(null)) {
            LOTRSpeech.sendSpeechAndChatMessage(p, this, "char/gandalf/depart");
        }
        doGandalfFX();
        discard();
    }

    private List<Player> playersToTell(@Nullable Player player) {
        List<Player> players = new ArrayList<>();
        if (player != null) {
            players.add(player);
        }
        for (Player p : level().players()) {
            if (!players.contains(p) && distanceToSqr(p) < 64.0 * 64.0) {
                players.add(p);
            }
        }
        return players;
    }

    public void doGandalfFX() {
        playSound(SoundEvents.ITEM_PICKUP, 2.0f, 0.5f + this.random.nextFloat() * 0.5f);
        level().broadcastEntityEvent(this, EVENT_FX);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_FX) {
            for (int i = 0; i < 20; ++i) {
                level().addParticle(ParticleTypes.POOF,
                        getX() + Mth.randomBetween(this.random, -1.0f, 1.0f) * getBbWidth(),
                        getY() + Mth.randomBetween(this.random, 0.0f, 1.0f) * getBbHeight(),
                        getZ() + Mth.randomBetween(this.random, -1.0f, 1.0f) * getBbWidth(),
                        this.random.nextGaussian() * 0.02, 0.05 + this.random.nextGaussian() * 0.02,
                        this.random.nextGaussian() * 0.02);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** onLivingUpdate: his time up and no one to fight, he goes. */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.arrivalPending) {
            this.arrivalPending = false;
            arriveAt(null);
        }
        if (!level().isClientSide() && isAlive() && !LOTRGreyWandererTracker.isWandererActive(getUUID())
                && getTarget() == null) {
            depart();
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRStoryItems.GLAMDRING));
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRStoryItems.GANDALF_STAFF_GREY));
        return data;
    }
}
