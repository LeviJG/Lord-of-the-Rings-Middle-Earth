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

public class LOTRConnectedBorderSpriteSource implements SpriteSource {
    public static final Identifier ID =
            Identifier.fromNamespaceAndPath("lotr", "connected_border");

    public static final MapCodec<LOTRConnectedBorderSpriteSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("base").forGetter(source -> source.baseTexture)
            ).apply(instance, LOTRConnectedBorderSpriteSource::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String CTM_INFIX = "_ctm_";

    private final Identifier baseTexture;

    public LOTRConnectedBorderSpriteSource(Identifier baseTexture) {
        this.baseTexture = baseTexture;
    }

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