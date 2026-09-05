package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTROrcBombBlock;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityOrcBomb: a lit orc bomb, tumbling until its fuse runs out.
 *
 * <p>Extends {@link PrimedTnt} for the same reason the original did: the block
 * state it carries is what gets drawn, so vanilla's TNT renderer draws an orc
 * bomb with no renderer of our own, flashing and swelling included.
 *
 * <p>Almost everything else is overridden, because the original's bomb is not
 * vanilla TNT. Its fuse is {@code 40 + strength * 20} ticks rather than a flat
 * 80; its blast is {@code (strength + 1) * 4} rather than 4; and its physics are
 * its own -- 0.04 of gravity a tick, 0.98 drag, and on the ground a 0.7 skid
 * with a 0.5 bounce, where vanilla TNT simply stops.
 *
 * <p>NOT ported: droppedByHiredUnit and droppedTargetingPlayer, which asked
 * LOTRMod.canGrief whether a bomb an NPC dropped should break terrain. There are
 * no NPCs in the port, so every bomb here is one a player set off, and every
 * bomb breaks terrain -- which is what droppedByPlayer meant.
 */
public class LOTROrcBombEntity extends PrimedTnt {

    public LOTROrcBombEntity(EntityType<? extends LOTROrcBombEntity> type, Level level) {
        super(type, level);
    }

    public LOTROrcBombEntity(EntityType<? extends LOTROrcBombEntity> type, Level level,
            double x, double y, double z, @Nullable LivingEntity igniter, BlockState bombState) {
        this(type, level);
        setPos(x, y, z);
        // The horizontal scatter vanilla gives primed TNT, so a bomb does not
        // sit dead still on the spot it was lit.
        double angle = level.getRandom().nextDouble() * (float) (Math.PI * 2);
        setDeltaMovement(-Math.sin(angle) * 0.02, 0.2, -Math.cos(angle) * 0.02);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        setBlockState(bombState);
        // Whoever lit it, so its kills are credited to them. PrimedTnt keeps
        // this in a private field with no setter -- its only entry point is a
        // constructor that hardcodes EntityType.TNT and is therefore useless to
        // a subclass -- so the field is opened up in lotr.accesswidener.
        if (igniter != null) {
            this.owner = EntityReference.of(igniter);
        }
        setFuse(fuseFor(bombState));
    }

    /** LOTRBlockOrcBomb.getBombStrengthLevel, read off the block instead of metadata. */
    public int getStrengthLevel() {
        return getBlockState().getBlock() instanceof LOTROrcBombBlock bomb ? bomb.getStrengthLevel() : 0;
    }

    private static int fuseFor(BlockState state) {
        int strength = state.getBlock() instanceof LOTROrcBombBlock bomb ? bomb.getStrengthLevel() : 0;
        return 40 + strength * 20;
    }

    /** setFuseFromExplosion: a bomb caught in a blast burns a fraction of its fuse. */
    public void setFuseFromExplosion() {
        int fuse = getFuse();
        setFuse(this.random.nextInt(fuse / 4) + fuse / 8);
    }

    /** LOTRBlockOrcBomb.isFireBomb, read off the block the entity carries. */
    public boolean isFireBomb() {
        return getBlockState().getBlock() instanceof LOTROrcBombBlock bomb && bomb.isFireBomb();
    }

    private void explodeOrcBomb() {
        // (strength + 1) * 4: four, eight, twelve, and the fire kegs leave the
        // blast burning behind them.
        float power = (getStrengthLevel() + 1) * 4.0f;
        // The overload WITH an explicit damage source, which is what vanilla's
        // PrimedTnt.explode uses. The short overload builds a source naming only
        // the bomb, so setting the owner bought nothing: kills read as the
        // bomb's. damageSources().explosion(this, getOwner()) is what credits
        // them to whoever lit it.
        this.level().explode(this, this.damageSources().explosion(this, this.getOwner()), null,
                this.getX(), this.getY(), this.getZ(), power, isFireBomb(),
                Level.ExplosionInteraction.TNT);
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        // The original's own figures, not Entity's gravity: 0.04 down a tick,
        // 0.98 drag on all three axes, and on the ground a 0.7 skid with the
        // usual half-height bounce.
        this.setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.04, 0.0));
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 motion = this.getDeltaMovement().scale(0.98);
        if (this.onGround()) {
            motion = new Vec3(motion.x * 0.7, motion.y * -0.5, motion.z * 0.7);
        }
        this.setDeltaMovement(motion);

        // The fuse lives in PrimedTnt's synched data and nowhere else, and it
        // counts down on BOTH sides, exactly as vanilla's does. The renderer
        // reads it for the swell and the white flash, so a private counter
        // pushed into setFuse would stamp client-side garbage over the synced
        // value. Decrement the synched fuse; the server's copy corrects drift.
        int fuse = getFuse() - 1;
        setFuse(fuse);
        if (fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide()) {
                explodeOrcBomb();
            }
        } else if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.SMOKE,
                    this.getX(), this.getY() + 0.7, this.getZ(), 0.0, 0.0, 0.0);
        }
    }

}
