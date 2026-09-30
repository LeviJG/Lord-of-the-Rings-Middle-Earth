package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGemsbokModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRGemsbokEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRWhiteOryxEntity;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderGemsbok, shadow 0.5, and LOTRRenderWhiteOryx, which extended it
 * with random skins and preRenderCallback's 0.9 scale.
 */
@SuppressWarnings("deprecation")
public class LOTRGemsbokRenderer extends AgeableMobRenderer<LOTRGemsbokEntity, LOTRSkinnedRenderState, LOTRGemsbokModel> {

    /** Was lotr:mob/gemsbok.png. */
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/gemsbok.png");
    private static final LOTRRandomSkins ORYX_SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/whiteOryx", "white_oryx");

    private final boolean oryx;

    private LOTRGemsbokRenderer(EntityRendererProvider.Context context, boolean oryx) {
        super(context,
                new LOTRGemsbokModel(LOTRGemsbokModel.createBodyLayer().bakeRoot()),
                new LOTRGemsbokModel(LOTRGemsbokModel.createBabyLayer().bakeRoot()),
                0.5f);
        this.oryx = oryx;
    }

    public static LOTRGemsbokRenderer gemsbok(EntityRendererProvider.Context context) {
        return new LOTRGemsbokRenderer(context, false);
    }

    public static LOTRGemsbokRenderer whiteOryx(EntityRendererProvider.Context context) {
        return new LOTRGemsbokRenderer(context, true);
    }

    @Override
    public LOTRSkinnedRenderState createRenderState() {
        return new LOTRSkinnedRenderState();
    }

    @Override
    public void extractRenderState(LOTRGemsbokEntity entity, LOTRSkinnedRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.skin = this.oryx ? ORYX_SKINS.getRandomSkin(entity.getUUID()) : TEXTURE;
    }

    @Override
    protected void scale(LOTRSkinnedRenderState state, PoseStack poseStack) {
        if (this.oryx) {
            float scale = LOTRWhiteOryxEntity.ORYX_SCALE;
            poseStack.scale(scale, scale, scale);
        }
    }

    @Override
    public Identifier getTextureLocation(LOTRSkinnedRenderState state) {
        return state.skin;
    }
}
