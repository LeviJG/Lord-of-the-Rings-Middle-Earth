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
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
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
                    Identifier.CODEC.fieldOf("base").forGetter(source -> source.baseTexture),
                    Codec.BOOL.optionalFieldOf("no_base", false).forGetter(source -> source.alsoWithoutBase),
                    Identifier.CODEC.optionalFieldOf("slice_from")
                            .forGetter(source -> Optional.ofNullable(source.sliceFrom))
            ).apply(instance, LOTRConnectedBorderSpriteSource::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String CTM_INFIX = "_ctm_";

    // LOTRConnectedTextures.createConnectedIcons took an includeNoBase flag and,
    // when it was set, built a SECOND copy of every combination with the base
    // layer left out. Only the gates ever asked for it: an open gate drew its
    // frame pieces over nothing at all, which is what makes an opened gate a
    // see-through hole with a rim rather than a solid panel you can walk
    // through. These sprites are that second copy.
    public static final String CTM_NO_BASE_INFIX = "_ctm_nobase_";

    private final Identifier baseTexture;

    private final boolean alsoWithoutBase;

    /**
     * LOTRConnectedTextures.registerNonConnectedGateIcons. The three portcullis
     * gates ship a single flat sprite and no hand-drawn border art; the original
     * cut the twelve pieces out of that sprite at load time -- a 3/16 strip down
     * each side, the four corner squares, and the inner corners reusing the
     * corner squares -- over a fully transparent base. Set this to the flat
     * sprite's id to do the same instead of reading thirteen files.
     */
    private final @Nullable Identifier sliceFrom;

    public LOTRConnectedBorderSpriteSource(Identifier baseTexture) {
        this(baseTexture, false, Optional.empty());
    }

    public LOTRConnectedBorderSpriteSource(Identifier baseTexture, boolean alsoWithoutBase,
                                           Optional<Identifier> sliceFrom) {
        this.baseTexture = baseTexture;
        this.alsoWithoutBase = alsoWithoutBase;
        this.sliceFrom = sliceFrom.orElse(null);
    }

    public static Identifier spriteFor(Identifier baseTexture, Set<LOTRConnectedBorder.Piece> pieces) {
        return spriteFor(baseTexture, pieces, true);
    }

    public static Identifier spriteFor(Identifier baseTexture, Set<LOTRConnectedBorder.Piece> pieces,
                                       boolean withBase) {
        return Identifier.fromNamespaceAndPath(baseTexture.getNamespace(),
                baseTexture.getPath() + (withBase ? CTM_INFIX : CTM_NO_BASE_INFIX)
                        + LOTRConnectedBorder.keyOf(pieces));
    }

    @Override
    public void run(ResourceManager resourceManager, Output output) {
        Layers layers = sliceFrom == null
                ? readPieceFiles(resourceManager)
                : readFlatToSlice(resourceManager);

        if (layers == null) {
            return;
        }

        int count = 0;

        for (Set<LOTRConnectedBorder.Piece> pieces : LOTRConnectedBorder.allCombinations()) {
            Identifier spriteId = spriteFor(baseTexture, pieces, true);
            output.add(spriteId, new Composited(spriteId, layers, pieces, true));
            count++;

            if (alsoWithoutBase) {
                Identifier bareId = spriteFor(baseTexture, pieces, false);
                output.add(bareId, new Composited(bareId, layers, pieces, false));
                count++;
            }
        }

        LOGGER.info("LOTR ctm border: generated {} sprites for {} (e.g. {})",
                count, baseTexture, spriteFor(baseTexture, LOTRConnectedBorder.piecesFor(
                        false, false, false, false, false, false, false, false)));
    }

    /**
     * Reads the source images for one family. Called on the atlas loading
     * thread, once per sprite, and hands back images the caller owns and must
     * close.
     */
    private interface Layers {
        /** The bottom layer, or {@code null} if the read failed. */
        @Nullable NativeImage base();

        /** One border piece at full sprite size, or {@code null} if the read failed. */
        @Nullable NativeImage piece(LOTRConnectedBorder.Piece piece);
    }

    private @Nullable Layers readPieceFiles(ResourceManager resourceManager) {
        Optional<Resource> base = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(source(BASE)));

        if (base.isEmpty()) {
            LOGGER.warn("LOTR connected border: missing base texture for {}", baseTexture);
            return null;
        }

        Map<LOTRConnectedBorder.Piece, Resource> overlays =
                new EnumMap<>(LOTRConnectedBorder.Piece.class);

        for (LOTRConnectedBorder.Piece piece : LOTRConnectedBorder.Piece.values()) {
            Identifier file = TEXTURE_ID_CONVERTER.idToFile(source(piece.textureSuffix()));
            Optional<Resource> resource = resourceManager.getResource(file);

            if (resource.isEmpty()) {
                LOGGER.warn("LOTR connected border: missing piece {} for {}", file, baseTexture);
                return null;
            }

            overlays.put(piece, resource.get());
        }

        Resource baseResource = base.get();

        return new Layers() {
            @Override
            public NativeImage base() {
                return read(baseResource);
            }

            @Override
            public NativeImage piece(LOTRConnectedBorder.Piece piece) {
                return read(overlays.get(piece));
            }
        };
    }

    private @Nullable Layers readFlatToSlice(ResourceManager resourceManager) {
        Identifier file = TEXTURE_ID_CONVERTER.idToFile(sliceFrom);
        Optional<Resource> flat = resourceManager.getResource(file);

        if (flat.isEmpty()) {
            LOGGER.warn("LOTR connected border: missing slice source {} for {}", file, baseTexture);
            return null;
        }

        Resource flatResource = flat.get();

        return new Layers() {
            @Override
            public @Nullable NativeImage base() {
                NativeImage flatImage = read(flatResource);

                if (flatImage == null) {
                    return null;
                }

                // The base is transparent; the flat sprite is read only to fix
                // the sprite's dimensions, exactly as the original's empty
                // BufferedImage did.
                NativeImage blank = new NativeImage(flatImage.getWidth(), flatImage.getHeight(), true);
                flatImage.close();
                return blank;
            }

            @Override
            public @Nullable NativeImage piece(LOTRConnectedBorder.Piece piece) {
                NativeImage flatImage = read(flatResource);

                if (flatImage == null) {
                    return null;
                }

                try {
                    return slice(flatImage, piece);
                } finally {
                    flatImage.close();
                }
            }
        };
    }

    /** LOTRConnectedTextures.getSubImageIcon, driven by the same 3/16 strip widths. */
    private static NativeImage slice(NativeImage flat, LOTRConnectedBorder.Piece piece) {
        int width = flat.getWidth();
        int height = flat.getHeight();
        int stripW = Math.max(Math.round(width / 16.0f * 3.0f), 1);
        int stripH = Math.max(Math.round(height / 16.0f * 3.0f), 1);

        // The region kept. Inner corners reuse the outer corner squares, as
        // registerNonConnectedGateIcons did.
        record Region(int x, int y, int w, int h) {
        }

        Region region = switch (piece) {
            case EDGE_LEFT -> new Region(0, 0, stripW, height);
            case EDGE_RIGHT -> new Region(width - stripW, 0, stripW, height);
            case EDGE_TOP -> new Region(0, 0, width, stripH);
            case EDGE_BOTTOM -> new Region(0, height - stripH, width, stripH);
            case CORNER_TOP_LEFT, INNER_TOP_LEFT -> new Region(0, 0, stripW, stripH);
            case CORNER_TOP_RIGHT, INNER_TOP_RIGHT -> new Region(width - stripW, 0, stripW, stripH);
            case CORNER_BOTTOM_LEFT, INNER_BOTTOM_LEFT -> new Region(0, height - stripH, stripW, stripH);
            case CORNER_BOTTOM_RIGHT, INNER_BOTTOM_RIGHT ->
                    new Region(width - stripW, height - stripH, stripW, stripH);
        };

        NativeImage cut = new NativeImage(width, height, true);

        for (int dy = 0; dy < region.h(); dy++) {
            for (int dx = 0; dx < region.w(); dx++) {
                int px = region.x() + dx;
                int py = region.y() + dy;
                cut.setPixel(px, py, flat.getPixel(px, py));
            }
        }

        return cut;
    }

    private static @Nullable NativeImage read(Resource resource) {
        try (InputStream stream = resource.open()) {
            return NativeImage.read(stream);
        } catch (IOException e) {
            LOGGER.error("LOTR connected border: unable to read source texture", e);
            return null;
        }
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

    private record Composited(Identifier spriteId, Layers layers,
                              Set<LOTRConnectedBorder.Piece> pieces,
                              boolean withBase) implements SpriteSource.DiscardableLoader {
        @Override
        public SpriteContents get(SpriteResourceLoader spriteResourceLoader) {
            NativeImage image = layers.base();

            if (image == null) {
                return null;
            }

            if (!withBase) {
                // The base is still read, because it is what defines the
                // sprite's dimensions; its pixels are simply dropped.
                NativeImage blank = new NativeImage(image.getWidth(), image.getHeight(), true);
                image.close();
                image = blank;
            }

            List<NativeImage> loaded = new ArrayList<>();
            boolean handedOff = false;

            try {
                for (LOTRConnectedBorder.Piece piece : LOTRConnectedBorder.Piece.values()) {
                    if (!pieces.contains(piece)) {
                        continue;
                    }

                    NativeImage overlay = layers.piece(piece);

                    if (overlay == null) {
                        return null;
                    }

                    loaded.add(overlay);
                    blend(overlay, image);
                }

                FrameSize size = new FrameSize(image.getWidth(), image.getHeight());
                // No AnimationMetadataSection: these composites are a single
                // frame. Passing one made every one of them a ticking animated
                // sprite, uploading to the atlas 20 times a second forever.
                SpriteContents contents =
                        new SpriteContents(spriteId, size, image, Optional.empty(), List.of(), Optional.empty());
                handedOff = true;
                return contents;
            } finally {
                loaded.forEach(NativeImage::close);
                // SpriteContents takes ownership of image on success. On any
                // early return it does not, and the buffer is off-heap, so it
                // would never be reclaimed by GC.
                if (!handedOff) {
                    image.close();
                }
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