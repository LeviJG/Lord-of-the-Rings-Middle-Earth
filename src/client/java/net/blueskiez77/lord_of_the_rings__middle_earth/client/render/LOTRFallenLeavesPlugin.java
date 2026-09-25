package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFallenLeavesBlock;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;

import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Hands every fallen-leaves block its LOTRFallenLeavesModel, drawn from its
 * leaf's texture, and gives the six vanilla ones their leaf's tint -- the
 * original's colorMultiplier asked the leaf block. The mod's leaves are not
 * tinted (LOTRBlockLeavesBase.colorMultiplier returned white).
 */
public final class LOTRFallenLeavesPlugin implements ModelLoadingPlugin {

    private LOTRFallenLeavesPlugin() {
    }

    public static void init() {
        ModelLoadingPlugin.register(new LOTRFallenLeavesPlugin());
        for (Block block : LOTRBlocks.ALL_FALLEN_LEAVES) {
            Block leaves = ((LOTRFallenLeavesBlock) block).leaves();
            if (leaves == Blocks.SPRUCE_LEAVES) {
                BlockColorRegistry.register(List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_EVERGREEN)), block);
            } else if (leaves == Blocks.BIRCH_LEAVES) {
                BlockColorRegistry.register(List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_BIRCH)), block);
            } else if (isVanilla(leaves)) {
                BlockColorRegistry.register(List.of(BlockTintSources.foliage()), block);
            }
        }
    }

    static boolean isVanilla(Block leaves) {
        return BuiltInRegistries.BLOCK.getKey(leaves).getNamespace().equals("minecraft");
    }

    @Override
    public void initialize(Context context) {
        for (Block block : LOTRBlocks.ALL_FALLEN_LEAVES) {
            Block leaves = ((LOTRFallenLeavesBlock) block).leaves();
            context.registerBlockStateResolver(block, resolverContext -> {
                BlockStateModel.UnbakedRoot root = new Unbaked(leaves).asRoot();
                for (BlockState state : resolverContext.block().getStateDefinition().getPossibleStates()) {
                    resolverContext.setModel(state, root);
                }
            });
        }
    }

    private record Unbaked(Block leaves) implements BlockStateModel.Unbaked, ModelDebugName {
        @Override
        public void resolveDependencies(Resolver resolver) {
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(this.leaves);
            Identifier texture = Identifier.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
            return new LOTRFallenLeavesModel(baker.materials().get(new Material(texture), this), isVanilla(this.leaves));
        }

        @Override
        public String debugName() {
            return "lotr fallen leaves: " + BuiltInRegistries.BLOCK.getKey(this.leaves);
        }
    }
}
