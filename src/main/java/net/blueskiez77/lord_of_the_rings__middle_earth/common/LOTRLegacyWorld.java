package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The 1.7.10 world queries the original's logic was written against, worked
 * out from what the game still keeps. Minecraft's own methods for these are
 * deprecated -- they stay only for such legacy uses. Brightness and the chunk
 * check are computed here from undeprecated parts. Material solidity and
 * liquidity have no undeprecated source at all (the backing flags are
 * deprecated too), so they are read through vanilla's legacy accessors in this
 * one place, which keeps the rest of the mod free of deprecated calls.
 */
@SuppressWarnings("deprecation")
public final class LOTRLegacyWorld {

    private LOTRLegacyWorld() {
    }

    /** Material.isSolid(). */
    public static boolean isSolid(BlockState state) {
        return state.isSolid();
    }

    /** Material.isLiquid(). */
    public static boolean isLiquid(BlockState state) {
        return state.liquid();
    }

    /** Material.blocksMovement(): solid, except cobwebs and bamboo shoots. */
    public static boolean blocksMotion(BlockState state) {
        return state.blocksMotion();
    }

    /** getLightBrightness: the light here on 1.7.10's curve, lifted by the dimension's ambient light. */
    public static float brightness(LevelReader level, BlockPos pos) {
        float v = level.getMaxLocalRawBrightness(pos) / 15.0f;
        float curved = v / (4.0f - 3.0f * v);
        return Mth.lerp(level.dimensionType().ambientLight(), curved, 1.0f);
    }

    /** Entity.getBrightness: the light at its eyes, or none if its chunk is not loaded. */
    public static float brightness(Entity entity) {
        Level level = entity.level();
        if (!level.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(entity.getBlockX()),
                SectionPos.blockToSectionCoord(entity.getBlockZ()))) {
            return 0.0f;
        }
        return brightness(level, BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ()));
    }

    /** checkChunksExist: every chunk across this stretch of blocks is loaded. */
    public static boolean hasChunksAt(Level level, int minX, int minZ, int maxX, int maxZ) {
        for (int cx = SectionPos.blockToSectionCoord(minX); cx <= SectionPos.blockToSectionCoord(maxX); ++cx) {
            for (int cz = SectionPos.blockToSectionCoord(minZ); cz <= SectionPos.blockToSectionCoord(maxZ); ++cz) {
                if (!level.getChunkSource().hasChunk(cx, cz)) {
                    return false;
                }
            }
        }
        return true;
    }
}
