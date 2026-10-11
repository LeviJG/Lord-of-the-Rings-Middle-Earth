package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRPortals;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRTeleporter;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityPortal: the Ring portal, the great ring of fire burnt out of a gold ring in the overworld
 * (LOTRPortals) and standing over Middle-earth's spawn point. It grows to its full size over six
 * seconds, turning all the while; whoever touches it is taken between the overworld and
 * Middle-earth at once, and once grown it takes any creature that wanders into it as well.
 * Nothing harms it; a creative player's blow breaks it.
 */
public class LOTRPortalEntity extends Entity {

    public static final int MAX_SCALE = 120;

    private static final EntityDataAccessor<Integer> DATA_SCALE =
            SynchedEntityData.defineId(LOTRPortalEntity.class, EntityDataSerializers.INT);

    public float prevPortalRotation;
    public float portalRotation;

    public LOTRPortalEntity(EntityType<? extends LOTRPortalEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SCALE, 0);
    }

    public int getScale() {
        return this.entityData.get(DATA_SCALE);
    }

    public void setScale(int i) {
        this.entityData.set(DATA_SCALE, i);
    }

    public float getPortalRotation(float f) {
        return this.prevPortalRotation + (this.portalRotation - this.prevPortalRotation) * f;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(LOTRMiscItems.GOLD_RING);
    }

    /** isEntityInvulnerable: nothing hurts it. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    /** hitByEntity: a creative player's blow shatters it, ringing like glass. */
    @Override
    public boolean skipAttackInteraction(Entity source) {
        if (source instanceof Player player) {
            if (player.getAbilities().instabuild && this.level() instanceof ServerLevel level) {
                SoundType sound = SoundType.GLASS;
                level.playSound(null, this.getX(), this.getY(), this.getZ(), sound.getBreakSound(), SoundSource.NEUTRAL,
                        (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
                level.broadcastEntityEvent(this, (byte) 16);
                this.discard();
            }
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 16) {
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, LOTRMiscItems.GOLD_RING);
            for (int l = 0; l < 16; ++l) {
                this.level().addParticle(particle, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** onUpdate, which never called the base entity's: it neither falls nor burns. */
    @Override
    public void tick() {
        this.prevPortalRotation = this.portalRotation;
        this.portalRotation += 4.0f;
        while (this.portalRotation - this.prevPortalRotation < -180.0f) {
            this.prevPortalRotation -= 360.0f;
        }
        while (this.portalRotation - this.prevPortalRotation >= 180.0f) {
            this.prevPortalRotation += 360.0f;
        }
        Level level = this.level();
        if (level instanceof ServerLevel && level.dimension() != Level.OVERWORLD && level.dimension() != LOTRWorldGen.MIDDLE_EARTH) {
            this.discard();
            return;
        }
        if (level instanceof ServerLevel && getScale() < MAX_SCALE) {
            setScale(getScale() + 1);
        }
        AABB box = this.getBoundingBox();
        if (getScale() >= MAX_SCALE) {
            for (Player player : level.getEntitiesOfClass(Player.class, box.inflate(8.0))) {
                if (box.intersects(player.getBoundingBox()) && !player.isPassenger() && !player.isVehicle()) {
                    LOTRPortals.setInPortal(player, null);
                }
            }
            List<Entity> entities = level.getEntities(this, box.inflate(8.0));
            for (Entity entity : entities) {
                if (entity instanceof Player || !box.intersects(entity.getBoundingBox()) || entity.isPassenger()
                        || entity.isVehicle() || entity.isOnPortalCooldown()) {
                    continue;
                }
                transferEntity(entity);
            }
            if (this.random.nextInt(50) == 0) {
                level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PORTAL_AMBIENT, SoundSource.NEUTRAL,
                        0.5f, this.random.nextFloat() * 0.4f + 0.8f, false);
            }
            for (int i = 0; i < 2; ++i) {
                double d = this.getX() - 3.0 + this.random.nextFloat() * 6.0f;
                double d1 = this.getY() - 0.75 + this.random.nextFloat() * 3.0f;
                double d2 = this.getZ() - 3.0 + this.random.nextFloat() * 6.0f;
                double d3 = (this.getX() - d) / 8.0;
                double d4 = (this.getY() + 1.5 - d1) / 8.0;
                double d5 = (this.getZ() - d2) / 8.0;
                double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                double d7 = 1.0 - d6;
                double d8 = 0.0;
                double d9 = 0.0;
                double d10 = 0.0;
                if (d7 > 0.0) {
                    d7 *= d7;
                    d8 += d3 / d6 * d7 * 0.2;
                    d9 += d4 / d6 * d7 * 0.2;
                    d10 += d5 / d6 * d7 * 0.2;
                }
                level.addParticle(ParticleTypes.FLAME, d, d1, d2, d8, d9, d10);
            }
        }
        for (Entity entity : level.getEntities(this, box.inflate(0.25))) {
            if (entity instanceof ServerPlayer player && !player.isPassenger() && !player.isVehicle()) {
                onPlayerCollision(player);
            }
        }
    }

    /** transferEntity: a creature taken through, from the overworld to Middle-earth or back. */
    public void transferEntity(Entity entity) {
        if (this.level() instanceof ServerLevel level) {
            ServerLevel target = level.getServer().getLevel(level.dimension() == Level.OVERWORLD ? LOTRWorldGen.MIDDLE_EARTH : Level.OVERWORLD);
            if (target != null) {
                LOTRPortals.transferEntity(entity, LOTRTeleporter.transition(target, entity, true));
            }
        }
    }

    /** onEntityCollision: a player touching it is taken across at once, unless just arrived. */
    private void onPlayerCollision(ServerPlayer player) {
        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown(10);
            return;
        }
        player.setPortalCooldown(10);
        ServerLevel target = player.level().getServer().getLevel(
                player.level().dimension() != LOTRWorldGen.MIDDLE_EARTH ? LOTRWorldGen.MIDDLE_EARTH : Level.OVERWORLD);
        if (target != null) {
            player.teleport(LOTRTeleporter.transition(target, player, true));
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setScale(input.getIntOr("Scale", 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("Scale", getScale());
    }
}
