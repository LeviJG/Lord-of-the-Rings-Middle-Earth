package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import java.util.HashSet;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * TEMPORARY (W0a diagnosis): logs where stone, the mod's blocks and ores are turned into air, with
 * the call stack, the first time each distinct stack is seen. Remove once the hollow underground is
 * found.
 */
@Mixin(LevelChunkSection.class)
abstract class LOTRDebugStoneRemovalMixin {
    private static final Set<String> LOTR_DEBUG_SEEN = new HashSet<>();
    private static long lotrDebugCount;

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At("RETURN"))
    private void lotr$debugStoneRemoval(int x, int y, int z, BlockState state, boolean useLocks, CallbackInfoReturnable<BlockState> cir) {
        BlockState old = cir.getReturnValue();
        if (!state.isAir() || old == null || old.isAir()) {
            return;
        }
        boolean target = old.is(Blocks.STONE) || old.is(Blocks.COAL_ORE) || old.is(Blocks.IRON_ORE)
                || BuiltInRegistries.BLOCK.getKey(old.getBlock()).getNamespace().equals(LOTRMod.NAMESPACE)
                && BuiltInRegistries.BLOCK.getKey(old.getBlock()).getPath().contains("rock");
        if (!target) {
            return;
        }
        synchronized (LOTR_DEBUG_SEEN) {
            ++lotrDebugCount;
            StackTraceElement[] stack = new Throwable().getStackTrace();
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < stack.length && i < 40; ++i) {
                sb.append("\n    at ").append(stack[i]);
            }
            String key = sb.toString();
            if (LOTR_DEBUG_SEEN.size() < 15 && LOTR_DEBUG_SEEN.add(key)) {
                LOTRMod.LOGGER.warn("[LOTR W0a] {} -> air (removal #{}, thread {}){}", old, lotrDebugCount, Thread.currentThread().getName(), key);
            } else if (lotrDebugCount % 100000 == 0) {
                LOTRMod.LOGGER.warn("[LOTR W0a] {} stone-like blocks turned to air so far", lotrDebugCount);
            }
        }
    }
}
