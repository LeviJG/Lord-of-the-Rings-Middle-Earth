package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPlateBlockEntity;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.math.Axis;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.BlockItem;

/**
 * LOTRModelHeadPlate: a plate worn in the helmet slot sits flat on top of the
 * head. It follows the head's yaw but not its pitch, as the original did.
 * The food the wearer holds -- all but one of the stack -- is piled on it;
 * plate and pile lag behind a jump and then fall back (LOTRPlateFallingInfo).
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
            Entity wearer = Minecraft.getInstance().level == null ? null
                    : Minecraft.getInstance().level.getEntity(((LOTROverheadHolder) state).lotr$getEntityId());
            float tick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
            LOTRPlateFallingInfo fallingInfo = wearer == null ? null : LOTRPlateFallingInfo.get(wearer);
            float fallOffset = fallingInfo == null ? 0.0f : fallingInfo.getPlateOffsetY(tick);
            poseStack.translate(0.0f, fallOffset * 0.5f, 0.0f);
            poseStack.translate(-0.5f, 0.0f, -0.5f);
            LOTRPlateGeometry.submit(poseStack, collector, plate.getBlock(), light);
            if (wearer instanceof LivingEntity living && LOTRPlateBlockEntity.isValidFoodItem(living.getMainHandItem())) {
                ItemStack held = living.getMainHandItem();
                int count = held.getCount() - 1;
                if (count > 0) {
                    ItemStackRenderState food = new ItemStackRenderState();
                    Minecraft.getInstance().getItemModelResolver().updateForTopItem(food, held, ItemDisplayContext.NONE,
                            living.level(), null, 0);
                    float[] offsets = new float[count];
                    for (int l = 0; l < count; ++l) {
                        offsets[l] = fallingInfo == null ? 0.0f : fallingInfo.getFoodOffsetY(l, tick);
                    }
                    LOTRPlateRenderer.submitFood(poseStack, collector, food, count, LOTRPlateRenderer.rotations(0, 0, 0, count),
                            offsets, light);
                }
            }
            poseStack.popPose();
        }, LOTRFoodBlocks.FINE_PLATE.asItem(), LOTRFoodBlocks.WOODEN_PLATE.asItem(), LOTRFoodBlocks.STONEWARE_PLATE.asItem());
    }
}
