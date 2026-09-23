package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRRenderSignCarved's colour picking: carved letters are drawn in a colour
 * that stands out against the block they are cut into.
 *
 * <p>The original averaged the block's face icon straight out of the texture
 * atlas, then flipped its brightness by 0.6 and halved its saturation. Sprites
 * no longer expose their pixels, so this reads the texture file behind the
 * block's particle sprite instead, first animation frame only, and caches the
 * result per texture.
 */
public final class LOTRSignColors {
    private static final Map<Identifier, Integer> CONTRAST = new HashMap<>();

    private LOTRSignColors() {
    }

    /** getContrastingColor, as opaque RGB. */
    public static int contrastColor(BlockState onBlock) {
        BlockState state = onBlock.isAir() ? Blocks.STONE.defaultBlockState() : onBlock;
        Minecraft minecraft = Minecraft.getInstance();
        SpriteContents contents = minecraft.getModelManager().getBlockStateModelSet()
                .getParticleMaterial(state).sprite().contents();
        return CONTRAST.computeIfAbsent(contents.name(),
                name -> 0xFF000000 | calculateContrast(averageColor(minecraft, name, contents.width(), contents.height())));
    }

    private static int averageColor(Minecraft minecraft, Identifier sprite, int width, int height) {
        Identifier file = Identifier.fromNamespaceAndPath(sprite.getNamespace(), "textures/" + sprite.getPath() + ".png");
        try (InputStream in = minecraft.getResourceManager().open(file); NativeImage image = NativeImage.read(in)) {
            int w = Math.min(width, image.getWidth());
            int h = Math.min(height, image.getHeight());
            long r = 0;
            long g = 0;
            long b = 0;
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    int argb = image.getPixel(x, y);
                    r += ARGB.red(argb);
                    g += ARGB.green(argb);
                    b += ARGB.blue(argb);
                }
            }
            int count = Math.max(1, w * h);
            return (int) (r / count) << 16 | (int) (g / count) << 8 | (int) (b / count);
        } catch (IOException e) {
            return 0x7F7F7F;
        }
    }

    /** calculateContrast: brightness moved 0.6 away, saturation halved. */
    private static int calculateContrast(int rgb) {
        float[] hsb = Color.RGBtoHSB(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF, null);
        float brightness = hsb[2] > 0.6f ? hsb[2] - 0.6f : hsb[2] + 0.4f;
        return Color.HSBtoRGB(hsb[0], hsb[1] * 0.5f, Mth.clamp(brightness, 0.0f, 1.0f)) & 0xFFFFFF;
    }
}
