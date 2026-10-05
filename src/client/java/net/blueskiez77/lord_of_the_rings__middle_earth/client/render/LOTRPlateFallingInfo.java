package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.Map;
import java.util.WeakHashMap;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlateFallingInfo: for a worn plate and its food, each layer's height
 * lagging behind the wearer's -- held up after a jump and then falling, each
 * layer a tick later than the one below -- so the pile topples down onto the
 * head. Kept on the client for every living thing, as the original did.
 */
public final class LOTRPlateFallingInfo {

    private static final Map<Entity, LOTRPlateFallingInfo> INFO = new WeakHashMap<>();

    private final Entity theEntity;
    private final float[] posXTicksAgo = new float[65];
    private final boolean[] isFalling = new boolean[65];
    private final float[] fallerPos = new float[65];
    private final float[] prevFallerPos = new float[65];
    private final float[] fallerSpeed = new float[65];

    private LOTRPlateFallingInfo(Entity entity) {
        this.theEntity = entity;
    }

    public static @Nullable LOTRPlateFallingInfo get(Entity entity) {
        return INFO.get(entity);
    }

    /** onLivingUpdate, client side: every living thing, every tick. */
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null || client.isPaused()) {
                return;
            }
            for (Entity entity : client.level.entitiesForRendering()) {
                if (entity instanceof LivingEntity) {
                    INFO.computeIfAbsent(entity, LOTRPlateFallingInfo::new).update();
                }
            }
        });
    }

    public float getFoodOffsetY(int food, float f) {
        return getOffsetY(food - 1, f);
    }

    public float getPlateOffsetY(float f) {
        return getOffsetY(0, f);
    }

    private float getOffsetY(int index, float f) {
        index = Mth.clamp(index, 0, this.fallerPos.length - 1);
        float pos = this.prevFallerPos[index] + (this.fallerPos[index] - this.prevFallerPos[index]) * f;
        float offset = pos - (float) Mth.lerp(f, this.theEntity.yo, this.theEntity.getY());
        return Math.max(offset, 0.0f);
    }

    private void update() {
        float curPos = (float) this.theEntity.getY();
        if (!this.theEntity.onGround() && this.theEntity.getDeltaMovement().y > 0.0) {
            for (int l = 0; l < this.posXTicksAgo.length; ++l) {
                this.posXTicksAgo[l] = Math.max(this.posXTicksAgo[l], curPos);
            }
        }
        for (int l = this.posXTicksAgo.length - 1; l > 0; --l) {
            this.posXTicksAgo[l] = this.posXTicksAgo[l - 1];
        }
        this.posXTicksAgo[0] = curPos;
        for (int l = 0; l < this.fallerPos.length; ++l) {
            this.prevFallerPos[l] = this.fallerPos[l];
            float pos = this.fallerPos[l];
            float speed = this.fallerSpeed[l];
            boolean fall = this.isFalling[l];
            if (!fall && pos > this.posXTicksAgo[l]) {
                fall = true;
            }
            this.isFalling[l] = fall;
            if (fall) {
                speed += 0.08f;
                pos -= speed;
                speed *= 0.98f;
            } else {
                speed = 0.0f;
            }
            if (pos < curPos) {
                pos = curPos;
                speed = 0.0f;
                this.isFalling[l] = false;
            }
            this.fallerPos[l] = pos;
            this.fallerSpeed[l] = speed;
        }
    }
}
