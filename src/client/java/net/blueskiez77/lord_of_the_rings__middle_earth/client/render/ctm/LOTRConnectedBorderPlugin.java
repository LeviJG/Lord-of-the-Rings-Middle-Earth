package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.SpriteSourceRegistry;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Installs the connected-border world model for every family registered in
 * LOTRConnectedBorderTypes.
 *
 * The families themselves live in LOTRConnectedBorderTypes; this class only
 * installs them.
 *
 * 26.2 approach: rather than wrapping an already-baked model (BakedModel and the
 * after-bake wrapping path are both gone), each block gets a BlockStateResolver
 * that assigns every one of its states our own unbaked model. Baking that model
 * is where the thirteen sprites are resolved, via ModelBaker#materials -- the
 * only place a Material.Baked can be obtained.
 */
public final class LOTRConnectedBorderPlugin implements ModelLoadingPlugin {

    private LOTRConnectedBorderPlugin() {
    }

    public static void init() {
        SpriteSourceRegistry.register(
                LOTRConnectedBorderSpriteSource.ID, LOTRConnectedBorderSpriteSource.CODEC);
        ModelLoadingPlugin.register(new LOTRConnectedBorderPlugin());
    }

    @Override
    public void initialize(Context context) {
        LOTRConnectedBorderTypes.all().forEach((block, type) -> context.registerBlockStateResolver(block, resolverContext -> {
            // asRoot() lifts our Unbaked into the UnbakedRoot that setModel
            // expects. The border ignores block state entirely, so every state
            // of the block gets the same model.
            BlockStateModel.UnbakedRoot root = new Unbaked(type).asRoot();

            for (BlockState state : resolverContext.block().getStateDefinition().getPossibleStates()) {
                resolverContext.setModel(state, root);
            }
        }));
    }

    /**
     * Unbaked form of the connected-border model. Its whole job is to turn the
     * type's thirteen texture ids into baked materials at the one moment a
     * ModelBaker is available.
     */
    private record Unbaked(LOTRConnectedBorderType type) implements BlockStateModel.Unbaked, ModelDebugName {

        @Override
        public void resolveDependencies(Resolver resolver) {
            // No parent or child models to pull in: the geometry is generated in
            // code and the sprites come off the atlas, which the
            // atlases/blocks.json directory source already stitches.
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            // One material per composited combination. The sprite source has
            // already generated these onto the atlas; here we just look each
            // one up.
            Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> composited = new HashMap<>();

            for (Set<LOTRConnectedBorder.Piece> pieces : LOTRConnectedBorder.allCombinations()) {
                Material material = new Material(
                        LOTRConnectedBorderSpriteSource.spriteFor(type.root(), pieces));
                composited.put(pieces, baker.materials().get(material, this));
            }

            return new LOTRConnectedBorderModel(type, composited);
        }

        @Override
        public String debugName() {
            return "lotr connected border: " + type.textureBaseName();
        }
    }
}