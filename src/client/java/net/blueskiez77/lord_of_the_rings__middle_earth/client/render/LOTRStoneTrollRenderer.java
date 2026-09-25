package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTrollModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRStoneTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTrollStatueItem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderStoneTroll. Three passes over one model: the troll in stone, then
 * its shirt and trousers in the outfit texture, each inflated so it sits just
 * proud of the skin.
 *
 * <p>The original's matrix was {@code translate(x, y + 1.5, z)}, a
 * {@code scale(-1, -1, 1)} and then {@code rotate(180 - rotationYaw)} about Y:
 * the usual flip that takes a model built y-down from the floor into world
 * space. It is reproduced here so the model's own numbers need no adjustment.
 */
public class LOTRStoneTrollRenderer
        extends EntityRenderer<LOTRStoneTrollEntity, LOTRStoneTrollRenderState> {

    /** Was lotr:mob/troll/stone.png. */
    private static final Identifier STONE =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/troll/stone.png");

    /** LOTRRenderTroll.trollOutfits, the three outfit sheets. */
    private static final Identifier[] OUTFITS = new Identifier[LOTRTrollStatueItem.OUTFIT_COUNT];

    static {
        for (int i = 0; i < OUTFITS.length; i++) {
            OUTFITS[i] = Identifier.fromNamespaceAndPath(
                    LOTRMod.NAMESPACE, "textures/entity/troll/outfit_" + i + ".png");
        }
    }

    private final LOTRTrollModel model;
    private final LOTRTrollModel shirt;
    private final LOTRTrollModel trousers;

    public LOTRStoneTrollRenderer(EntityRendererProvider.Context context) {
        super(context);
        // Baked straight from the layer definition rather than through
        // EntityModelLayers, the way the port's block entity models are: three
        // separate bakes because the inflation differs, exactly as the original
        // built three LOTRModelTroll instances.
        this.model = new LOTRTrollModel(
                LOTRTrollModel.createLayer(0.0f).bakeRoot(), LOTRTrollModel.Piece.ALL);
        this.shirt = new LOTRTrollModel(
                LOTRTrollModel.createLayer(1.0f).bakeRoot(), LOTRTrollModel.Piece.SHIRT);
        this.trousers = new LOTRTrollModel(
                LOTRTrollModel.createLayer(0.75f).bakeRoot(), LOTRTrollModel.Piece.TROUSERS);
        // No shadow: LOTRRenderStoneTroll left Render's shadowSize at 0.
    }

    @Override
    public LOTRStoneTrollRenderState createRenderState() {
        return new LOTRStoneTrollRenderState();
    }

    @Override
    public void extractRenderState(LOTRStoneTrollEntity troll, LOTRStoneTrollRenderState state,
            float partialTick) {
        super.extractRenderState(troll, state, partialTick);
        state.yaw = troll.getYRot();
        state.outfit = troll.getTrollOutfit();
        state.twoHeads = troll.hasTwoHeads();
    }

    @Override
    public void submit(LOTRStoneTrollRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        int light = state.lightCoords;
        int outfit = state.outfit < 0 || state.outfit >= OUTFITS.length ? 0 : state.outfit;
        boolean twoHeads = state.twoHeads;

        poseStack.pushPose();
        poseStack.translate(0.0f, 1.5f, 0.0f);
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - state.yaw));
        // NO 0.0625f scale here, even though the original passed exactly that
        // to model.render. In 1.7.10 that argument was the scale ModelRenderer
        // multiplied its vertices by; ModelPart.Cube now divides its own
        // positions by 16 when it is built, so the sixteenth is already in the
        // geometry. Applying it again shrank the troll by another factor of 16
        // and left it hanging in the air, because the 1.5 offset below is in
        // blocks and no longer matched the model.

        this.model.setStatuePose();
        this.shirt.setStatuePose();
        this.trousers.setStatuePose();

        // entityCutout, not entitySolid: doRender began with a
        // glDisable(GL_CULL_FACE), so the troll is drawn from both sides. The
        // outfit layers in particular are open shells and lose faces otherwise.
        submitLayer(poseStack, collector, STONE, light, this.model, twoHeads);
        submitLayer(poseStack, collector, OUTFITS[outfit], light, this.shirt, twoHeads);
        submitLayer(poseStack, collector, OUTFITS[outfit], light, this.trousers, twoHeads);

        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }

    private static void submitLayer(PoseStack poseStack, SubmitNodeCollector collector,
            Identifier texture, int light, LOTRTrollModel model, boolean twoHeads) {
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture),
                (pose, consumer) -> {
                    // ModelPart.render wants a PoseStack and custom geometry
                    // hands over a single Pose; a local stack seeded with it is
                    // the cheapest way across, as the weapon rack does.
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    model.render(local, consumer, light, OverlayTexture.NO_OVERLAY, twoHeads);
                });
    }
}
