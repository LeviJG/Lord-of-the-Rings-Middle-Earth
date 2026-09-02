package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTREntTrunkModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTrollModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBossTrophyEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrophyType;

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
 * LOTRRenderBossTrophy, transcribed.
 *
 * <p>The shared frame is {@code translate(x, y, z)}, {@code scale(-1, -1, 1)}
 * and then a yaw -- {@code 180 + facing * 90} for a hanging trophy, or
 * {@code 180 - rotationYaw} for one on the floor. Note there is no {@code +1.5}
 * on the Y here, unlike the stone troll: a trophy's model sits at its own feet.
 *
 * <p>What is drawn then depends on the trophy. The chieftain is the troll's
 * head, drawn twice and tipped apart, since a Hill-troll chieftain has two of
 * them. The Mallorn Ent is the ent's trunk at six-tenths scale, with the arms
 * hidden and the cut end capped off.
 */
public class LOTRBossTrophyRenderer
        extends EntityRenderer<LOTRBossTrophyEntity, LOTRBossTrophyRenderState> {

    private static final Map<LOTRTrophyType, Identifier> TEXTURES =
            new EnumMap<>(LOTRTrophyType.class);

    static {
        for (LOTRTrophyType type : LOTRTrophyType.values()) {
            TEXTURES.put(type, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                    "textures/entity/trophy/" + type.getSerializedName() + ".png"));
        }
    }

    /** The original's modelscale, kept only where it is arithmetic on offsets. */
    private static final float MODEL_SCALE = 0.0625f;

    /** The ent trunk is drawn at six-tenths. */
    private static final float ENT_SCALE = 0.6f;

    private final LOTRTrollModel troll;
    private final LOTREntTrunkModel entTrunk;

    public LOTRBossTrophyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.troll = new LOTRTrollModel(
                LOTRTrollModel.createLayer(0.0f).bakeRoot(), LOTRTrollModel.Piece.ALL);
        this.entTrunk = new LOTREntTrunkModel(LOTREntTrunkModel.createLayer().bakeRoot());
        this.shadowRadius = 0.5f;
    }

    @Override
    public LOTRBossTrophyRenderState createRenderState() {
        return new LOTRBossTrophyRenderState();
    }

    @Override
    public void extractRenderState(LOTRBossTrophyEntity trophy, LOTRBossTrophyRenderState state,
            float partialTick) {
        super.extractRenderState(trophy, state, partialTick);
        state.type = trophy.getTrophyType();
        state.hanging = trophy.isTrophyHanging();
        state.rotation = state.hanging
                ? 180.0f + trophy.getTrophyFacing().get2DDataValue() * 90.0f
                : 180.0f - trophy.getYRot();
    }

    @Override
    public void submit(LOTRBossTrophyRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        int light = state.lightCoords;
        Identifier texture = TEXTURES.get(state.type);
        boolean hanging = state.hanging;
        LOTRTrophyType type = state.type;

        poseStack.pushPose();
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));

        // entityCutout, not entitySolid: doRender began with a
        // glDisable(GL_CULL_FACE).
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture),
                (pose, consumer) -> {
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    if (type == LOTRTrophyType.MOUNTAIN_TROLL_CHIEFTAIN) {
                        this.troll.setTrophyPose();
                        local.translate(0.0f, -0.05f, 0.1f);

                        local.pushPose();
                        local.translate(-0.25f, 0.0f, 0.0f);
                        local.mulPose(Axis.ZP.rotationDegrees(-10.0f));
                        local.mulPose(Axis.YP.rotationDegrees(15.0f));
                        this.troll.renderHead(local, consumer, light, OverlayTexture.NO_OVERLAY);
                        local.popPose();

                        local.pushPose();
                        local.translate(0.25f, 0.0f, 0.0f);
                        local.mulPose(Axis.ZP.rotationDegrees(10.0f));
                        local.mulPose(Axis.YP.rotationDegrees(-15.0f));
                        this.troll.renderHead(local, consumer, light, OverlayTexture.NO_OVERLAY);
                        local.popPose();
                    } else {
                        // 34 model units up, measured before the scale is
                        // applied, which is what seats a trunk whose own boxes
                        // run from y -48 to 0.
                        local.translate(0.0f, 34.0f * MODEL_SCALE * ENT_SCALE, 0.0f);
                        if (hanging) {
                            // Pushed off the wall. The original divides by the
                            // scale here rather than multiplying, which is odd
                            // but is what it does, so it is kept.
                            local.translate(0.0f, 0.0f, 3.0f * MODEL_SCALE / ENT_SCALE);
                        }
                        local.scale(ENT_SCALE, ENT_SCALE, ENT_SCALE);
                        this.entTrunk.render(local, consumer, light, OverlayTexture.NO_OVERLAY);
                    }
                });

        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }
}
