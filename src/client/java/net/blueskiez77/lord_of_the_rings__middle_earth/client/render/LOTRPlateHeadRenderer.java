package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.math.Axis;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.BlockItem;

/**
 * LOTRModelHeadPlate: a plate worn in the helmet slot sits flat on top of the
 * head. It follows the head's yaw but not its pitch, as the original did.
 *
 * <p>NOT ported: the food the original piled on a worn plate from the wearer's
 * held stack, and LOTRPlateFallingInfo, which let that pile lag behind a jump.
 */
public final class LOTRPlateHeadRenderer {
    private LOTRPlateHeadRenderer() {
    }

    public static void init() {
        ArmorRenderer.register((poseStack, collector, stack, state, slot, light, model) -> {
            if (!(stack.getItem() instanceof BlockItem plate)) {
                return;
            }
            ModelPart head = model.head;
            if (!head.visible) {
                return;
            }
            poseStack.pushPose();
            poseStack.translate(head.x / 16.0f, head.y / 16.0f, head.z / 16.0f);
            poseStack.mulPose(Axis.YP.rotation(head.yRot));
            // The model is y-down from the neck: the top of the head is half a
            // block up. Flip back to y-up there and centre the plate on it.
            poseStack.translate(0.0f, -0.5f, 0.0f);
            poseStack.scale(1.0f, -1.0f, 1.0f);
            poseStack.translate(-0.5f, 0.0f, -0.5f);
            LOTRPlateGeometry.submit(poseStack, collector, plate.getBlock(), light);
            poseStack.popPose();
        }, LOTRBlocks.FINE_PLATE.asItem(), LOTRBlocks.WOODEN_PLATE.asItem(), LOTRBlocks.STONEWARE_PLATE.asItem());
    }
}
