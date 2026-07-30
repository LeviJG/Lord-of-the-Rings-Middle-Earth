package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.slf4j.Logger;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;

/**
 * Generates the composited border sprites for one connected-border family, at
 * atlas-stitch time.
 *
 * This is how the 1.7.10 mod worked: LOTRConnectedTextures#createConnectedIcons
 * alpha-composited the base and its border pieces into one image per neighbour
 * combination and registered each as its own icon, so the world renderer only
 * ever drew a single quad per face. Doing the same here replaces the earlier
 * approach of layering up to nine coplanar quads per face, which needed a depth
 * fudge to avoid z-fighting.
 *
 * 47 sprites are produced per family -- the number of distinct piece sets the
 * border rules can yield out of 256 neighbour configurations, and the same count
 * the connected-texture format uses. Each is named
 * &lt;base&gt;_ctm_&lt;mask&gt;, where mask is the bitmask from
 * LOTRConnectedBorder#keyOf.
 *
 * Nothing is written to disk: these live in the stitched atlas for the session
 * and are rebuilt on every resource reload, so a resource pack that retextures
 * the thirteen source pieces gets all 47 combinations regenerated for free.
 *
 * Declared in assets/lotr/atlases/blocks.json as
 * {"type": "lotr:connected_border", "base": "&lt;texture base name&gt;"}.
 */
public class LOTRConnectedBorderSpriteSource implements SpriteSource {

    public static final Identifier ID =
            Identifier.fromNamespaceAndPath("lotr", "connected_border");

    public static final MapCodec<LOTRConnectedBorderSpriteSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("base").forGetter(source -> source.baseTexture)
            ).apply(instance, LOTRConnectedBorderSpriteSource::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    /** Sprite id infix separating the family name from the combination mask. */
    public static final String CTM_INFIX = "_ctm_";

    private final Identifier baseTexture;

    public LOTRConnectedBorderSpriteSource(Identifier baseTexture) {
        this.baseTexture = baseTexture;
    }

    /** Sprite id of the composited tile for one piece set. */
    public static Identifier spriteFor(Identifier baseTexture, Set<LOTRConnectedBorder.Piece> pieces) {
        return Identifier.fromNamespaceAndPath(baseTexture.getNamespace(),
                baseTexture.getPath() + CTM_INFIX + LOTRConnectedBorder.keyOf(pieces));
    }

    @Override
    public void run(ResourceManager resourceManager, Output output) {
        Optional<Resource> base = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(source(BASE)));

        if (base.isEmpty()) {
            LOGGER.warn("LOTR connected border: missing base texture for {}", baseTexture);
            return;
        }

        // Load the twelve overlays up front; a family with a missing piece is a
        // packaging error worth naming rather than silently rendering wrong.
        Map<LOTRConnectedBorder.Piece, Resource> overlays =
                new EnumMap<>(LOTRConnectedBorder.Piece.class);

        for (LOTRConnectedBorder.Piece piece : LOTRConnectedBorder.Piece.values()) {
            Identifier file = TEXTURE_ID_CONVERTER.idToFile(source(piece.textureSuffix()));
            Optional<Resource> resource = resourceManager.getResource(file);

            if (resource.isEmpty()) {
                LOGGER.warn("LOTR connected border: missing piece {} for {}", file, baseTexture);
                return;
            }

            overlays.put(piece, resource.get());
        }

        int count = 0;

        for (Set<LOTRConnectedBorder.Piece> pieces : LOTRConnectedBorder.allCombinations()) {
            Identifier spriteId = spriteFor(baseTexture, pieces);
            output.add(spriteId, new Composited(spriteId, base.get(), overlays, pieces));
            count++;
        }

        LOGGER.info("LOTR ctm border: generated {} sprites for {} (e.g. {})",
                count, baseTexture, spriteFor(baseTexture, LOTRConnectedBorder.piecesFor(
                        false, false, false, false, false, false, false, false)));
    }

    private static final String BASE = LOTRConnectedBorder.BASE_SUFFIX;

    private Identifier source(String suffix) {
        return Identifier.fromNamespaceAndPath(baseTexture.getNamespace(),
                baseTexture.getPath() + "_" + suffix);
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }

    /** One composited tile: the base with a given set of overlays blended on. */
    private record Composited(Identifier spriteId, Resource base,
                              Map<LOTRConnectedBorder.Piece, Resource> overlays,
                              Set<LOTRConnectedBorder.Piece> pieces) implements SpriteSource.DiscardableLoader {

        @Override
        public SpriteContents get(SpriteResourceLoader spriteResourceLoader) {
            NativeImage image = read(base);

            if (image == null) {
                return null;
            }

            List<NativeImage> loaded = new ArrayList<>();

            try {
                for (LOTRConnectedBorder.Piece piece : LOTRConnectedBorder.Piece.values()) {
                    if (!pieces.contains(piece)) {
                        continue;
                    }

                    NativeImage overlay = read(overlays.get(piece));

                    if (overlay == null) {
                        return null;
                    }

                    loaded.add(overlay);
                    blend(overlay, image);
                }
            } finally {
                loaded.forEach(NativeImage::close);
            }

            // A default animation section, never Optional.empty(): the atlas
            // expects one, and passing empty leaves the sprite unusable, which
            // surfaces only as the missing-texture checkerboard.
            AnimationMetadataSection animation = new AnimationMetadataSection(
                    Optional.empty(), Optional.empty(), Optional.empty(), 1, false);

            FrameSize size = new FrameSize(image.getWidth(), image.getHeight());
            return new SpriteContents(spriteId, size, image, Optional.of(animation), List.of(), Optional.empty());
        }

        private static NativeImage read(Resource resource) {
            try (InputStream stream = resource.open()) {
                return NativeImage.read(stream);
            } catch (IOException e) {
                LOGGER.error("LOTR connected border: unable to read source texture", e);
                return null;
            }
        }

        /** Alpha-blend every pixel of src over dst, in place. */
        private static void blend(NativeImage src, NativeImage dst) {
            int width = Math.min(src.getWidth(), dst.getWidth());
            int height = Math.min(src.getHeight(), dst.getHeight());

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    dst.setPixel(x, y, ARGB.alphaBlend(dst.getPixel(x, y), src.getPixel(x, y)));
                }
            }
        }
    }
}