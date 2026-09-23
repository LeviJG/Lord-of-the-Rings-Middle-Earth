package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * LOTRTickHandlerClient's drunkenness: while nausea is active the view drifts.
 *
 * <p>Transcribed from the client tick: drunkenness is the nausea's remaining
 * seconds, capped at 100. Each tick the yaw turns by a twentieth of that in the
 * current direction and the pitch bobs by a twentieth of it along
 * cos(ticksExisted / 10); one tick in a hundred the drift reverses. So a strong
 * drink pulls the crosshair round hard, and it eases off as the effect runs out.
 *
 * <p>The pitch is clamped to +-90, which 1.7.10 did later in setAngles.
 */
public final class LOTRDrunkCamera {
    private static int drunkennessDirection = 1;

    private LOTRDrunkCamera() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTRDrunkCamera::tick);
    }

    private static void tick(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.isPaused()
                || !(minecraft.getCameraEntity() instanceof LivingEntity viewer)) {
            return;
        }
        MobEffectInstance nausea = viewer.getEffect(MobEffects.NAUSEA);
        if (nausea == null) {
            return;
        }
        float drunkenness = nausea.isInfiniteDuration() ? 100.0f
                : Math.min(nausea.getDuration() / 20.0f, 100.0f);
        viewer.setYRot(viewer.getYRot() + drunkennessDirection * drunkenness / 20.0f);
        viewer.setXRot(Mth.clamp(viewer.getXRot()
                + Mth.cos(viewer.tickCount / 10.0f) * drunkenness / 20.0f, -90.0f, 90.0f));
        if (minecraft.level.getRandom().nextInt(100) == 0) {
            drunkennessDirection *= -1;
        }
    }
}
