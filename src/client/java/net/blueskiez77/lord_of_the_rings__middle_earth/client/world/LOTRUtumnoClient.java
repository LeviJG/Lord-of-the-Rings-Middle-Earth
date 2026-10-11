package net.blueskiez77.lord_of_the_rings__middle_earth.client.world;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRTickHandlerClient's side of Utumno: rising in the return portal's light, the world turns over
 * (the camera rolled round to upside down, five degrees a tick, and back again after), and the player
 * is held to the light's middle and never let sink; the flames of a servant's death running into an
 * altar; the smoke of water boiling away.
 */
public final class LOTRUtumnoClient {

    private static float utumnoCamRoll;
    private static boolean inUtumnoReturnPortal;
    private static int utumnoReturnX;
    private static int utumnoReturnZ;
    private static double lastUtumnoReturnY = -1.0;

    private LOTRUtumnoClient() {
    }

    public static void init() {
        LOTRUtumno.clientInReturnPortal = player -> {
            if (player == Minecraft.getInstance().player) {
                inUtumnoReturnPortal = true;
            }
        };
        ClientPlayNetworking.registerGlobalReceiver(LOTRUtumno.ReturnPayload.TYPE, (payload, context) -> context.client().execute(() -> {
            inUtumnoReturnPortal = true;
            utumnoReturnX = Mth.floor(payload.posX());
            utumnoReturnZ = Mth.floor(payload.posZ());
        }));
        ClientPlayNetworking.registerGlobalReceiver(LOTRUtumno.KillPayload.TYPE, (payload, context) -> context.client().execute(() ->
                killParticles(payload.entityID(), payload.pos())));
        ClientPlayNetworking.registerGlobalReceiver(LOTRUtumno.EvaporatePayload.TYPE, (payload, context) -> context.client().execute(() -> {
            ClientLevel level = context.client().level;
            if (level == null) {
                return;
            }
            RandomSource rand = level.getRandom();
            BlockPos pos = payload.pos();
            for (int l = 0; l < 8; ++l) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(),
                        pos.getZ() + rand.nextFloat(), 0.0, 0.0, 0.0);
            }
        }));
        ClientTickEvents.END_CLIENT_TICK.register(LOTRUtumnoClient::tick);
    }

    /** camRoll, in degrees. */
    public static float getCameraRoll() {
        return utumnoCamRoll;
    }

    private static void tick(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null || mc.isPaused()) {
            return;
        }
        if (LOTRUtumno.isUtumno(mc.level)) {
            if (inUtumnoReturnPortal) {
                if (utumnoCamRoll < 180.0f) {
                    utumnoCamRoll = Math.min(utumnoCamRoll + 5.0f, 180.0f);
                }
            } else if (utumnoCamRoll > 0.0f) {
                utumnoCamRoll = Math.max(utumnoCamRoll - 5.0f, 0.0f);
            }
        } else {
            utumnoCamRoll = 0.0f;
        }
        if (inUtumnoReturnPortal) {
            player.setPos(utumnoReturnX + 0.5, player.getY(), utumnoReturnZ + 0.5);
            if (lastUtumnoReturnY >= 0.0 && player.getY() < lastUtumnoReturnY) {
                player.setPos(player.getX(), lastUtumnoReturnY, player.getZ());
            }
            lastUtumnoReturnY = player.getY();
        } else {
            lastUtumnoReturnY = -1.0;
        }
        inUtumnoReturnPortal = false;
    }

    /** LOTRPacketUtumnoKill: flames rising off the altar, and flames drawn from the dead into it. */
    private static void killParticles(int entityID, BlockPos pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(entityID);
        if (entity == null) {
            return;
        }
        RandomSource rand = level.getRandom();
        int i1 = pos.getX();
        int j1 = pos.getY();
        int k1 = pos.getZ();
        BlockState block = level.getBlockState(pos);
        VoxelShape shape = block.getShape(level, pos);
        double blockTop = shape.isEmpty() ? 1.0 : shape.max(net.minecraft.core.Direction.Axis.Y);
        for (int l = 0; l < 8; ++l) {
            double d = i1 + 0.25 + rand.nextDouble() * 0.5;
            double d1 = j1 + 0.1;
            double d2 = k1 + 0.25 + rand.nextDouble() * 0.5;
            double d4 = 0.1 + rand.nextDouble() * 0.1;
            level.addParticle(LOTRParticles.UTUMNO_KILL, d, d1, d2, 0.0, d4, 0.0);
        }
        for (int l = 0; l < 12; ++l) {
            double d = entity.getX() + rand.nextGaussian() * 0.8;
            double d1 = entity.getBoundingBox().minY + entity.getBbHeight() * 0.7 + rand.nextGaussian() * 0.2;
            double d2 = entity.getZ() + rand.nextGaussian() * 0.8;
            double d3 = i1 + 0.5 - d;
            double d4 = j1 + blockTop - d1;
            double d5 = k1 + 0.5 - d2;
            double v = 0.05;
            level.addParticle(LOTRParticles.UTUMNO_KILL, d, d1, d2, d3 * v, d4 * v, d5 * v);
        }
    }
}
