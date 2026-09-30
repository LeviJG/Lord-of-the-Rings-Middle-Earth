package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrownRockEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * LOTRRenderThrownRock: a block of stone -- or, for a totem's rock that
 * brings a troll, a troll totem's head -- drawn as the block item is, a block
 * across, facing the way it flies and tumbling over.
 */
public class LOTRThrownRockRenderer extends EntityRenderer<LOTRThrownRockEntity, LOTRThrownRockRenderState> {

    private static final ItemStack STONE = new ItemStack(Items.STONE);

    private final ItemModelResolver items;

    public LOTRThrownRockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public LOTRThrownRockRenderState createRenderState() {
        return new LOTRThrownRockRenderState();
    }

    @Override
    public void extractRenderState(LOTRThrownRockEntity rock, LOTRThrownRockRenderState state, float partialTick) {
        super.extractRenderState(rock, state, partialTick);
        state.yaw = rock.getYRot(partialTick);
        state.pitch = rock.getXRot(partialTick);
        ItemStack block = rock.getSpawnsTroll() ? new ItemStack(LOTRUtilityBlocks.TROLL_TOTEM_HEAD) : STONE;
        this.items.updateForNonLiving(state.block, block, ItemDisplayContext.NONE, rock);
    }

    @Override
    public void submit(LOTRThrownRockRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.pitch));
        state.block.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
