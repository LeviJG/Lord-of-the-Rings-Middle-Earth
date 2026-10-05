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
 * <p>Drunkenness is the nausea's remaining seconds, capped at 100. Each tick
 * the yaw turns by a twentieth of that in the current direction and the pitch
 * bobs by a twentieth of it along cos(ticksExisted / 10); one tick in a
 * hundred the drift reverses. So a strong drink pulls the crosshair round
 * hard, and it eases off as the effect runs out.
 *
 * <p>1.7.10's camera interpolated the player's rotation between ticks, so the
 * per-tick turn looked smooth. The modern local player's camera reads its
 * rotation unsmoothed every frame, so the same per-tick step would jerk the
 * view twenty times a second; the drift is instead applied every frame (where
 * the mouse turns the player), scaled by the ticks that frame covers. The
 * direction still reverses on the tick.
 */
public final class LOTRDrunkCamera {
    private static int drunkennessDirection = 1;

    private LOTRDrunkCamera() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTRDrunkCamera::tick);
    }

    private static void tick(Minecraft minecraft) {
        if (minecraft.level != null && !minecraft.isPaused() && drunkenness(minecraft) > 0.0f
                && minecraft.level.getRandom().nextInt(100) == 0) {
            drunkennessDirection *= -1;
        }
    }

    /** Called each frame, before the mouse turns the player. */
    public static void frame(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.isPaused()
                || !(minecraft.getCameraEntity() instanceof LivingEntity viewer)) {
            return;
        }
        float drunkenness = drunkenness(minecraft);
        if (drunkenness <= 0.0f) {
            return;
        }
        float ticks = minecraft.getDeltaTracker().getGameTimeDeltaTicks();
        float time = viewer.tickCount + minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float yDelta = drunkennessDirection * drunkenness / 20.0f * ticks;
        float xDelta = Mth.cos(time / 10.0f) * drunkenness / 20.0f * ticks;
        // As Entity.turn: the old rotation moves too, so nothing interpolating between them jumps back.
        viewer.setYRot(viewer.getYRot() + yDelta);
        viewer.setXRot(Mth.clamp(viewer.getXRot() + xDelta, -90.0f, 90.0f));
        viewer.yRotO += yDelta;
        viewer.xRotO = Mth.clamp(viewer.xRotO + xDelta, -90.0f, 90.0f);
    }

    private static float drunkenness(Minecraft minecraft) {
        if (!(minecraft.getCameraEntity() instanceof LivingEntity viewer)) {
            return 0.0f;
        }
        MobEffectInstance nausea = viewer.getEffect(MobEffects.NAUSEA);
        if (nausea == null) {
            return 0.0f;
        }
        return nausea.isInfiniteDuration() ? 100.0f : Math.min(nausea.getDuration() / 20.0f, 100.0f);
    }
}
