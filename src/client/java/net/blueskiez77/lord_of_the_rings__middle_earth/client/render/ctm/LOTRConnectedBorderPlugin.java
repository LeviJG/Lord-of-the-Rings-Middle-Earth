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
            BlockStateModel.UnbakedRoot root = new Unbaked(type).asRoot();

            for (BlockState state : resolverContext.block().getStateDefinition().getPossibleStates()) {
                resolverContext.setModel(state, root);
            }
        }));
    }

    private record Unbaked(LOTRConnectedBorderType type) implements BlockStateModel.Unbaked, ModelDebugName {
        @Override
        public void resolveDependencies(Resolver resolver) {
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
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