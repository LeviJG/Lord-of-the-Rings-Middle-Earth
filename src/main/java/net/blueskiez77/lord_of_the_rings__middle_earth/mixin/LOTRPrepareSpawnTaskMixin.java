package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import java.util.concurrent.CompletableFuture;

import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldTypeMiddleEarth;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.config.PrepareSpawnTask;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * A player new to a world of the Middle-earth type is made in Middle-earth itself, near its
 * spawn point, rather than in the overworld and moved on (LOTRWorldTypeMiddleEarth).
 */
@Mixin(PrepareSpawnTask.class)
public abstract class LOTRPrepareSpawnTaskMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @ModifyVariable(method = "start", at = @At("STORE"))
    private ServerLevel lotr$spawnLevel(ServerLevel level, @Local ServerPlayer.SavedPosition saved) {
        ServerLevel middleEarth = LOTRWorldTypeMiddleEarth.newPlayerLevel(this.server, saved.dimension().isEmpty());
        return middleEarth != null ? middleEarth : level;
    }

    @ModifyVariable(method = "start", at = @At("STORE"))
    private CompletableFuture<Vec3> lotr$spawnPosition(CompletableFuture<Vec3> position, @Local ServerPlayer.SavedPosition saved) {
        ServerLevel middleEarth = LOTRWorldTypeMiddleEarth.newPlayerLevel(this.server, saved.dimension().isEmpty());
        return middleEarth != null ? LOTRWorldTypeMiddleEarth.newPlayerSpawn(middleEarth) : position;
    }
}
