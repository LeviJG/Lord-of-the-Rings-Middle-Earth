package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRWeaponRackModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRWeaponRackBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRWeaponRackBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderWeaponRack. The rack draws nothing in the chunk mesh -- the block's
 * render shape is INVISIBLE -- so this draws the stand and whatever is on it.
 *
 * <p>The original's matrix was {@code translate(x+0.5, y+1.5, z+0.5)}, a yaw
 * from the facing bits, an extra {@code translate(0, 0.375, -0.5)} for the wall
 * form, then {@code scale(-1, -1, 1)}: the usual flip that takes an entity model
 * built y-down from a rotation point at 24 into block space. It is reproduced
 * here so the model's own numbers need no adjustment.
 */
public class LOTRWeaponRackRenderer
        implements BlockEntityRenderer<LOTRWeaponRackBlockEntity, LOTRWeaponRackRenderState> {

    /** Was lotr:item/weaponRack.png, which is not a legal texture path now. */
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/weapon_rack.png");

    private final LOTRWeaponRackModel model;
    private final ItemModelResolver items;

    public LOTRWeaponRackRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LOTRWeaponRackModel(LOTRWeaponRackModel.createLayer().bakeRoot());
        this.items = context.itemModelResolver();
    }

    @Override
    public LOTRWeaponRackRenderState createRenderState() {
        return new LOTRWeaponRackRenderState();
    }

    @Override
    public void extractRenderState(LOTRWeaponRackBlockEntity rack, LOTRWeaponRackRenderState state,
            float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(rack, state, crumbling);

        BlockState blockState = rack.getBlockState();
        state.onWall = blockState.hasProperty(LOTRWeaponRackBlock.ON_WALL)
                && blockState.getValue(LOTRWeaponRackBlock.ON_WALL);
        Direction facing = blockState.hasProperty(LOTRWeaponRackBlock.FACING)
                ? blockState.getValue(LOTRWeaponRackBlock.FACING)
                : Direction.NORTH;
        state.yaw = yawOf(facing);

        state.weapon.clear();
        if (!rack.getWeapon().isEmpty()) {
            // NONE: no display transform at all. See the note on the weapon
            // matrix in submit -- the original's chain already cancels the pose
            // its own renderer applied, so anything applied here would be a
            // second, uncancelled pose on top.
            items.updateForTopItem(state.weapon, rack.getWeapon(),
                    ItemDisplayContext.NONE, rack.getLevel(), null, 0);
        }
    }

    /**
     * The original mapped its two direction bits to 0/270/180/90 degrees, in
     * the order south, west, north, east. That is the NEGATIVE of the facing's
     * own yRot, not the yRot itself -- the rack is drawn through a scale(-1,
     * -1, 1), so its yaw is mirrored too. Using yRot directly put east and west
     * racks a half turn out, which with the half-block push towards the wall
     * left them hanging off the opposite side of their block.
     */
    private static float yawOf(Direction facing) {
        return -facing.toYRot();
    }

    @Override
    public void submit(LOTRWeaponRackRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        int light = state.lightCoords;
        boolean onWall = state.onWall;
        float yaw = state.yaw;

        // entityCutout rather than entitySolid: renderTileEntityAt began with
        // glDisable(GL_CULL_FACE), so the rack is drawn from both sides. With a
        // culling render type its back faces vanish and you can see through it.
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(TEXTURE), (pose, consumer) -> {
            // ModelPart.render wants a PoseStack and custom geometry hands over
            // a single Pose; a local stack seeded with it is the cheapest way
            // across, as the troll totem does.
            PoseStack local = new PoseStack();
            local.last().set(pose);
            applyRackTransform(local, yaw, onWall);
            if (onWall) {
                model.renderWall(local, consumer, light, OverlayTexture.NO_OVERLAY);
            } else {
                model.renderFloor(local, consumer, light, OverlayTexture.NO_OVERLAY);
            }
        });

        if (state.weapon.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        applyRackTransform(poseStack, yaw, onWall);

        // Undo the entity-model flip. applyRackTransform ends in
        // scale(-1, -1, 1) because LOTRModelWeaponRack is built y-down from a
        // rotation point at 24; the weapon wants none of that. Doing it again
        // leaves a world-aligned frame with a POSITIVE determinant, which also
        // fixes the winding -- with the flip in place every face of the item
        // was inside-out, which is what made it look wrong from behind.
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        // Down onto the hooks.
        //
        // Not the original's matrix. That chain was built to cancel the pose
        // 1.7.10's ItemRenderer applied for ItemRenderType.EQUIPPED, and with
        // that pose gone there is nothing left for it to cancel -- transcribing
        // its numbers just moved the weapon somewhere arbitrary. These offsets
        // come from measuring the model instead, working the boxes of
        // LOTRModelWeaponRack through the same matrix the rack itself is drawn
        // with. In sixteenths of a block, with the rack facing south:
        //
        //   floor: hook shelves at x 2..4 and 12..14, top face y 10, with the
        //          prongs at y 10..11 either side of a cradle at z 7.5..8.5.
        //          So a weapon lies along x through (0.5, 10/16, 0.5).
        //   wall:  the base lies flat, the holder is pulled down, and the lip
        //          that stops the weapon falling is the prong at y 4.5..5.5,
        //          z 4..5. So it rests at about (0.5, 5.75/16, 4.5/16).
        //
        // The frame origin is not the block corner: applyRackTransform starts
        // at (0.5, 1.5, 0.5), and the wall form adds (0, 0.375, -0.5) on top,
        // so these are the offsets FROM those origins.
        if (onWall) {
            poseStack.translate(0.0F, 0.4F - 1.875F, 0.28F - 0.0F);
        } else {
            poseStack.translate(0.0F, 0.65F - 1.5F, 0.0F);
        }

        // A sword sprite runs corner to corner, handle at the bottom left, so
        // rolling it -45 degrees lays the blade flat along the rack.
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));

        // 0.625 of a block, the original's weaponScale -- and the distance
        // between the two hook centres (x 3/16 and 13/16), so a blade spans
        // hook to hook. Applied LAST, so the offsets above stay in block units.
        poseStack.scale(0.625F, 0.625F, 0.625F);

        state.weapon.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private static void applyRackTransform(PoseStack poseStack, float yaw, boolean onWall) {
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        if (onWall) {
            poseStack.translate(0.0F, 0.375F, -0.5F);
        }
        poseStack.scale(-1.0F, -1.0F, 1.0F);
    }
}
