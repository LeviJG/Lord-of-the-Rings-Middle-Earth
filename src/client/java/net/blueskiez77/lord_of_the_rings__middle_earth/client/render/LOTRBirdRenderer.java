package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBirdModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRBirdEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCrebainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRGorcrowEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRSeagullEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * LOTRRenderBird: shadow 0.2, random skins per kind (and the gorcrow's,
 * crebain's and seagull's own), those three scaled up, and the stolen item
 * held in the beak (renderEquippedItems). The item is placed at the head in
 * 26.2's item display terms, which only approximate the original's
 * renderItem offsets.
 */
public class LOTRBirdRenderer extends MobRenderer<LOTRBirdEntity, LOTRBirdRenderState, LOTRBirdModel> {

    private static final Map<String, LOTRRandomSkins> SKINS = new HashMap<>();

    public LOTRBirdRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRBirdModel(LOTRBirdModel.createBodyLayer().bakeRoot()), 0.2f);
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRBirdRenderState state, float yRot, float xRot) {
                if (state.stolenItem.isEmpty()) {
                    return;
                }
                poseStack.pushPose();
                getParentModel().body.translateAndRotate(poseStack);
                getParentModel().head.translateAndRotate(poseStack);
                poseStack.translate(0.0f, 0.0f, -0.2f);
                poseStack.scale(0.25f, 0.25f, 0.25f);
                state.stolenItem.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
                poseStack.popPose();
            }
        });
    }

    /** getBirdSkins, keyed by the original's "lotr:mob/bird/&lt;dir&gt;". */
    private static LOTRRandomSkins skins(String dir) {
        return SKINS.computeIfAbsent(dir, d -> LOTRRandomSkins.loadSkinsList("lotr:mob/bird/" + d,
                "bird/" + (d.equals("farHarad") ? "far_harad" : d)));
    }

    @Override
    public LOTRBirdRenderState createRenderState() {
        return new LOTRBirdRenderState();
    }

    @Override
    public void extractRenderState(LOTRBirdEntity bird, LOTRBirdRenderState state, float partialTick) {
        super.extractRenderState(bird, state, partialTick);
        state.skin = skins(bird.getBirdTextureDir()).getRandomSkin(bird.getUUID());
        state.still = bird.isBirdStill();
        state.flapping = bird.flapTime > 0;
        state.wingTime = state.still && state.flapping ? bird.flapTime - partialTick : state.ageInTicks;
        state.birdScale = bird instanceof LOTRCrebainEntity ? LOTRCrebainEntity.SCALE
                : bird instanceof LOTRGorcrowEntity ? LOTRGorcrowEntity.SCALE
                : bird instanceof LOTRSeagullEntity ? LOTRSeagullEntity.SCALE : 1.0f;
        this.itemModelResolver.updateForLiving(state.stolenItem, bird.getStolenItem(), ItemDisplayContext.GROUND, bird);
    }

    @Override
    protected void scale(LOTRBirdRenderState state, PoseStack poseStack) {
        poseStack.scale(state.birdScale, state.birdScale, state.birdScale);
    }

    @Override
    public Identifier getTextureLocation(LOTRBirdRenderState state) {
        return state.skin;
    }
}
