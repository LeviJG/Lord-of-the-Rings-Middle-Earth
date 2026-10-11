package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityUtumnoTroll: a troll of Utumno, half as big again, hitting for 7 and charging at 2.0;
 * no name, no speech, no tickling. Should the sun ever find one it does not become a stone troll but
 * shatters into stone.
 */
public class LOTRUtumnoTrollEntity extends LOTRTrollEntity {

    private static final byte EVENT_STONE = 15;

    public LOTRUtumnoTrollEntity(EntityType<? extends LOTRUtumnoTrollEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRTrollEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 7.0);
    }

    @Override
    public boolean canTrollBeTickled(Player player) {
        return false;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 5 + this.random.nextInt(6);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_UTUMNO_TROLL;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return null;
    }

    @Override
    protected Goal getTrollAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 2.0, false);
    }

    @Override
    public float getTrollScale() {
        return 1.5f;
    }

    @Override
    public boolean hasTrollName() {
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == EVENT_STONE) {
            for (int l = 0; l < 64; ++l) {
                level().addParticle(LOTRParticles.LARGE_STONE, getX() + this.random.nextGaussian() * getBbWidth() * 0.5,
                        getY() + this.random.nextDouble() * getBbHeight(), getZ() + this.random.nextGaussian() * getBbWidth() * 0.5,
                        0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void onTrollDeathBySun() {
        playSound(LOTRSounds.TROLL_TRANSFORM, getSoundVolume(), getVoicePitch());
        level().broadcastEntityEvent(this, EVENT_STONE);
        discard();
    }
}
