package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRScrapTraderEntity;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

/**
 * LOTRTickHandlerClient.scrapTraderMisbehaveTick: one tick in 50000 that a
 * player, with no screen open, looks the oddment collector in the eye, the
 * world goes black for twenty seconds (the lightmap,
 * LOTRLightmapRenderStateExtractorMixin) while he towers over it, ringed by
 * tall copies of himself (LOTRScrapTraderRenderer).
 */
public final class LOTRScrapTraderMisbehaviour {

    private static int misbehaveTick;
    private static int traderId = -1;

    private LOTRScrapTraderMisbehaviour() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTRScrapTraderMisbehaviour::tick);
    }

    public static boolean isMisbehaving() {
        return misbehaveTick > 0;
    }

    /** The one he is doing it as, which is drawn wherever the camera looks (ignoreFrustumCheck). */
    public static boolean isMisbehaving(Entity trader) {
        return misbehaveTick > 0 && trader.getId() == traderId;
    }

    private static void tick(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.isPaused()) {
            return;
        }
        if (misbehaveTick > 0) {
            if (--misbehaveTick <= 0) {
                traderId = -1;
            }
        } else if (minecraft.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof LOTRScrapTraderEntity trader
                && minecraft.gui.screen() == null && minecraft.level.getRandom().nextInt(50000) == 0) {
            misbehaveTick = 400;
            traderId = trader.getId();
        }
    }
}
