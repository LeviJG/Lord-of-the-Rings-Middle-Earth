package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.UUID;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityNPCRideable: an NPC a player can ride -- and, by riding it until
 * it gives in (LOTRUntamedPanicGoal), tame. A tamed, saddled one goes where
 * its player steers (LOTRMountFunctions.move): half speed sideways, a quarter
 * backwards, climbing a full block. It mends a half-heart now and then.
 *
 * <p>NOT ported yet: the mount's inventory screen (openGUI, D16).
 */
public abstract class LOTRNPCRideableEntity extends LOTRNPCEntity implements LOTRNPCMount {

    /** dataWatcher 17. */
    private static final EntityDataAccessor<Boolean> DATA_TAMED =
            SynchedEntityData.defineId(LOTRNPCRideableEntity.class, EntityDataSerializers.BOOLEAN);

    private @Nullable UUID tamingPlayer;
    private int npcTemper;

    protected LOTRNPCRideableEntity(EntityType<? extends LOTRNPCRideableEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TAMED, false);
    }

    public boolean isNPCTamed() {
        return this.entityData.get(DATA_TAMED);
    }

    public void setNPCTamed(boolean flag) {
        this.entityData.set(DATA_TAMED, flag);
    }

    public void tameNPC(Player player) {
        setNPCTamed(true);
        this.tamingPlayer = player.getUUID();
    }

    public @Nullable Player getTamingPlayer() {
        return this.tamingPlayer == null ? null : level().getPlayerByUUID(this.tamingPlayer);
    }

    public int getNPCTemper() {
        return this.npcTemper;
    }

    public int getMaxNPCTemper() {
        return 100;
    }

    public void increaseNPCTemper(int amount) {
        this.npcTemper = Mth.clamp(this.npcTemper + amount, 0, getMaxNPCTemper());
    }

    /** angerNPC: its hurt cry, higher. */
    public void angerNPC() {
        playSound(getHurtSound(damageSources().generic()), getSoundVolume(), getVoicePitch() * 1.5f);
    }

    /** getMountInventory: its saddle and armour, if it has them. */
    public @Nullable Container getMountInventory() {
        return null;
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return super.removeWhenFarAway(distSqr) && !isNPCTamed();
    }

    @Override
    public boolean canRenameNPC() {
        return isNPCTamed() || super.canRenameNPC();
    }

    // --- Being ridden -----------------------------------------------------------

    /** A player steers it only once it is tamed and saddled (LOTRMountFunctions.isMountControllable). */
    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        if (getFirstPassenger() instanceof Player player) {
            return isNPCTamed() && isMountSaddled() ? player : null;
        }
        return super.getControllingPassenger();
    }

    /** LOTRMountFunctions.move: facing where the rider looks. */
    @Override
    protected void tickRidden(Player controller, Vec3 riddenInput) {
        super.tickRidden(controller, riddenInput);
        Vec2 rot = new Vec2(controller.getXRot() * 0.5f, controller.getYRot());
        setRot(rot.y % 360.0f, rot.x % 360.0f);
        this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
    }

    @Override
    protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
        float strafe = controller.xxa * 0.5f;
        float forward = controller.zza;
        if (forward <= 0.0f) {
            forward *= 0.25f;
        }
        return new Vec3(strafe, 0.0, forward);
    }

    @Override
    protected float getRiddenSpeed(Player controller) {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    /**
     * LOTRMountFunctions.update: a half-heart back one tick in 900. (A mount
     * that is not an NPC took its rider's target there; this one is an NPC
     * with targets of its own.)
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel && this.random.nextInt(900) == 0 && isAlive()) {
            heal(1.0f);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("NPCTamed", isNPCTamed());
        if (this.tamingPlayer != null) {
            output.putString("NPCTamer", this.tamingPlayer.toString());
        }
        output.putInt("NPCTemper", this.npcTemper);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setNPCTamed(input.getBooleanOr("NPCTamed", false));
        this.tamingPlayer = input.getString("NPCTamer").map(UUID::fromString).orElse(null);
        this.npcTemper = input.getIntOr("NPCTemper", 0);
    }
}
