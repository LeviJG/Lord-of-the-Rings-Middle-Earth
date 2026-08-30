package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.ctm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGateBlock;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.SpriteSourceRegistry;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
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

        // Gates resolve the same way but bake a different model: they are thin,
        // oriented, and swap to a base-less sprite set when open. Registering a
        // resolver replaces the blockstate JSON entirely, which is why gates
        // have none -- the same as the border cubes above.
        for (LOTRGateBlock gate : LOTRGateBorders.all()) {
            context.registerBlockStateResolver(gate, resolverContext -> {
                BlockStateModel.UnbakedRoot root = new UnbakedGate(gate).asRoot();

                for (BlockState state : resolverContext.block().getStateDefinition().getPossibleStates()) {
                    resolverContext.setModel(state, root);
                }
            });
        }
    }

    private record UnbakedGate(LOTRGateBlock gate) implements BlockStateModel.Unbaked, ModelDebugName {
        @Override
        public void resolveDependencies(Resolver resolver) {
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            Identifier root = LOTRGateBorders.root(gate);
            Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> withBase = new HashMap<>();
            Map<Set<LOTRConnectedBorder.Piece>, Material.Baked> withoutBase = new HashMap<>();

            for (Set<LOTRConnectedBorder.Piece> pieces : LOTRConnectedBorder.allCombinations()) {
                withBase.put(pieces, baker.materials().get(
                        new Material(LOTRConnectedBorderSpriteSource.spriteFor(root, pieces, true)), this));
                withoutBase.put(pieces, baker.materials().get(
                        new Material(LOTRConnectedBorderSpriteSource.spriteFor(root, pieces, false)), this));
            }

            // ONLY the gates with no connected art have a flat sprite. The
            // other twenty never draw one -- LOTRGateModel reaches for it only
            // when hasConnectedTextures() is false -- and resolving it anyway
            // asked the baker for lotr:block/<gate>, which does not exist, so
            // every connected gate logged a "Missing textures in model" warning
            // at load.
            Material.Baked flat = gate.hasConnectedTextures() ? null
                    : baker.materials().get(new Material(LOTRGateBorders.flatTexture(gate)), this);

            return new LOTRGateModel(gate, withBase, withoutBase, flat);
        }

        @Override
        public String debugName() {
            return "lotr gate: " + LOTRGateBorders.name(gate);
        }
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