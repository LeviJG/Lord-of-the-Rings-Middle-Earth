package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBearModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRBearEntity;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/** LOTRRenderBear: shadow 0.5, the skin of its colour, drawn at 1.2 scale. */
@SuppressWarnings("deprecation")
public class LOTRBearRenderer extends AgeableMobRenderer<LOTRBearEntity, LOTRSkinnedRenderState, LOTRBearModel> {

    /** scaleBearModel. */
    public static final float SCALE = 1.2f;

    public LOTRBearRenderer(EntityRendererProvider.Context context) {
        super(context,
                new LOTRBearModel(LOTRBearModel.createBodyLayer().bakeRoot()),
                new LOTRBearModel(LOTRBearModel.createBabyLayer().bakeRoot()),
                0.5f);
    }

    /** getBearSkin. */
    public static Identifier getBearSkin(LOTRBearEntity.BearType type) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/bear/" + type.textureName() + ".png");
    }

    @Override
    public LOTRSkinnedRenderState createRenderState() {
        return new LOTRSkinnedRenderState();
    }

    @Override
    public void extractRenderState(LOTRBearEntity bear, LOTRSkinnedRenderState state, float partialTick) {
        super.extractRenderState(bear, state, partialTick);
        state.skin = getBearSkin(bear.getBearType());
    }

    @Override
    protected void scale(LOTRSkinnedRenderState state, PoseStack poseStack) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }

    @Override
    public Identifier getTextureLocation(LOTRSkinnedRenderState state) {
        return state.skin;
    }
}
