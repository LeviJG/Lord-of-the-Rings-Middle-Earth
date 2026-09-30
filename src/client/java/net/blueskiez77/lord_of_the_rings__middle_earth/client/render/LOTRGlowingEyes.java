package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;

/**
 * LOTRGlowingEyes and LOTRTextures.getEyesTexture: a creature's eyes drawn a
 * second time at full brightness, added on top. The eyes texture is made
 * once per skin, in memory, as the original made it: a transparent sheet
 * the skin's size holding only the eye pixels, copied as they are.
 */
public final class LOTRGlowingEyes {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Identifier, Identifier> EYES_TEXTURES = new HashMap<>();

    private LOTRGlowingEyes() {
    }

    /** getEyesTexture: the eye pixels at each {x, y}, {@code eyeWidth} by {@code eyeHeight}. */
    public static Identifier getEyesTexture(Identifier skin, int[][] eyesCoords, int eyeWidth, int eyeHeight) {
        // Keyed by the eyes' own name, not the skin's: a spider has two sets.
        Identifier key = Identifier.fromNamespaceAndPath(skin.getNamespace(),
                skin.getPath() + "_eyes_" + eyeWidth + "_" + eyeHeight);
        return EYES_TEXTURES.computeIfAbsent(key, eyes -> {
            Minecraft mc = Minecraft.getInstance();
            try (InputStream in = mc.getResourceManager().open(skin); NativeImage skinImage = NativeImage.read(in)) {
                NativeImage eyesImage = new NativeImage(skinImage.getWidth(), skinImage.getHeight(), true);
                for (int[] eye : eyesCoords) {
                    for (int i = eye[0]; i < eye[0] + eyeWidth; ++i) {
                        for (int j = eye[1]; j < eye[1] + eyeHeight; ++j) {
                            eyesImage.setPixel(i, j, skinImage.getPixel(i, j));
                        }
                    }
                }
                mc.getTextureManager().register(eyes, new DynamicTexture(eyes::toString, eyesImage));
                return eyes;
            } catch (IOException e) {
                LOGGER.error("Failed to generate eyes skin", e);
                return MissingTextureAtlasSprite.getLocation();
            }
        });
    }
}
