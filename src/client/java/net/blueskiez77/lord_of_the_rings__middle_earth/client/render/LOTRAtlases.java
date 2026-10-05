package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.resources.Identifier;

/**
 * The block and item atlases' texture locations. 26.2 deprecates
 * TextureAtlas.LOCATION_BLOCKS and LOCATION_ITEMS without a replacement
 * (vanilla's own atlas list still registers them under these names), so the
 * renderers that draw atlas sprites directly name them here.
 */
public final class LOTRAtlases {

    public static final Identifier BLOCKS = Identifier.withDefaultNamespace("textures/atlas/blocks.png");
    public static final Identifier ITEMS = Identifier.withDefaultNamespace("textures/atlas/items.png");

    private LOTRAtlases() {
    }
}
