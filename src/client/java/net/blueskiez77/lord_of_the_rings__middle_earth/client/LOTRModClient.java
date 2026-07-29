package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.connected.LOTRConnectedBorderPlugin;

import net.fabricmc.api.ClientModInitializer;

/**
 * Client entrypoint. Owns HUD, keybinds, screens.
 *
 * NOTE on leaves render layer: in 26.1 Minecraft automatically assigns the
 * ChunkSectionLayer per quad from the sprite's properties, so blocks with
 * transparent pixels (our leaves) should get cutout rendering for free.
 * If leaves ever render opaque/black, register them explicitly:
 *
 *   import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
 *   for (Block leaves : LOTRBlocks.ALL_LEAVES) {
 *       BlockRenderLayerMap.putBlock(leaves, BlockRenderLayer.CUTOUT_MIPPED);
 *   }
 *
 * (Note the package moved in 1.21.6: blockrenderlayer.v1 -> client.rendering.v1)
 */
public class LOTRModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LOTRMod.LOGGER.info("LOTR client initializing...");

        //LOTRKeyBindings.register();

        // Connected-border blocks (mithril block, and the dwarven bricks /
        // cobblebrick / daub / plates / gates once they are ported).
        //
        // No render-layer registration here any more: BlockRenderLayerMap was
        // removed in 26.2, because Minecraft now derives each quad's
        // ChunkSectionLayer from the properties of the sprite assigned to it.
        // The border overlays have transparent pixels, so they get cutout
        // treatment automatically.
        LOTRConnectedBorderPlugin.init();
    }
}