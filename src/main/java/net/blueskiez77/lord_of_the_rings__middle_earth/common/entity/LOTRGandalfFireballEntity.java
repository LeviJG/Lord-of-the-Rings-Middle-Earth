package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGandalfFireball: flies flat -- no gravity -- until it hits
 * something or ten seconds pass, then bursts for ten damage on whatever it
 * struck and as much again on anything within six blocks, falling off by half
 * a heart a block.
 *
 * <p>isEntityVulnerable spared friends of the High Elves: the port keeps the
 * player half of that (anyone whose High-elven alignment is negative), and
 * since it has no NPCs, everything else living is fair game.
 */
public class LOTRGandalfFireballEntity extends ThrowableItemProjectile {
    private static final int MAX_AGE = 200;
    private static final double BLAST_RANGE = 6.0;
    private static final float DAMAGE = 10.0f;

    private int fireballAge;

    public LOTRGandalfFireballEntity(EntityType<? extends LOTRGandalfFireballEntity> type, Level level) {
        super(type, level);
    }

    public LOTRGandalfFireballEntity(EntityType<? extends LOTRGandalfFireballEntity> type, LivingEntity thrower,
            Level level, ItemStack stack) {
        super(type, thrower, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRItems.GANDALF_FIREBALL;
    }

    /** getGravityVelocity returned 0. */
    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel server && !isRemoved() && ++fireballAge >= MAX_AGE) {
            explode(server, null);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        if (level() instanceof ServerLevel server) {
            explode(server, null);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (level() instanceof ServerLevel server && isVulnerable(hit.getEntity())) {
            explode(server, hit.getEntity());
        }
    }

    private boolean isVulnerable(Entity entity) {
        if (entity == getOwner() || !(entity instanceof LivingEntity)) {
            return false;
        }
        if (entity instanceof Player player) {
            return LOTRPlayerAlignments.getAlignment(player, LOTRFaction.HIGH_ELF) < 0.0f;
        }
        return true;
    }

    private void explode(ServerLevel server, @Nullable Entity struck) {
        server.playSound(null, getX(), getY(), getZ(), LOTRSounds.ITEM_GANDALF_FIREBALL, SoundSource.NEUTRAL,
                4.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        // LOTREntityGandalfFireballExplodeFX: one firework-style flash, tinted
        // (0.33, 1, 1) -- vanilla's flash particle, which it extended.
        server.sendParticles(ColorParticleOption.create(ParticleTypes.FLASH, 0.33f, 1.0f, 1.0f),
                getX(), getY(), getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        DamageSource source = getOwner() instanceof LivingEntity owner
                ? damageSources().mobAttack(owner)
                : damageSources().magic();
        if (struck != null) {
            struck.hurtServer(server, source, DAMAGE);
        }
        for (LivingEntity entity : server.getEntitiesOfClass(LivingEntity.class,
                getBoundingBox().inflate(BLAST_RANGE))) {
            if (entity == struck || !isVulnerable(entity)) {
                continue;
            }
            float damage = DAMAGE - distanceTo(entity) * 0.5f;
            if (damage > 0.0f) {
                entity.hurtServer(server, source, damage);
            }
        }
        discard();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("FireballAge", fireballAge);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        fireballAge = input.getIntOr("FireballAge", 0);
    }
}
