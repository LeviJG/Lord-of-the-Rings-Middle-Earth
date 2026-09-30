package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTermiteModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRTermiteEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

/** LOTRRenderTermite: shadow 0.2, the model at a quarter of its size. */
public class LOTRTermiteRenderer extends MobRenderer<LOTRTermiteEntity, LivingEntityRenderState, LOTRTermiteModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/termite.png");

    public LOTRTermiteRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRTermiteModel(LOTRTermiteModel.createBodyLayer().bakeRoot()), 0.2f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(0.25f, 0.25f, 0.25f);
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
