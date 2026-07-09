package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

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

        LOTRAlignmentHud.register();
        LOTRKeyBindings.register();
    }
}