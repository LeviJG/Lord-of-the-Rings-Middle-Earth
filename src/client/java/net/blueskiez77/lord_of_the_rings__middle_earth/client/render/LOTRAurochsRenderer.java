package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRAurochsModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRAurochsEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRKineArawEntity;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderAurochs, shadow 0.5, and LOTRRenderKineAraw, which extended it
 * with its own skins and preRenderCallback's KINE_SCALE.
 */
@SuppressWarnings("deprecation")
public class LOTRAurochsRenderer extends AgeableMobRenderer<LOTRAurochsEntity, LOTRAurochsRenderState, LOTRAurochsModel> {

    private static final LOTRRandomSkins AUROCHS_SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/aurochs", "aurochs");
    private static final LOTRRandomSkins KINE_SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/kineAraw", "kine_araw");

    private final boolean kine;

    private LOTRAurochsRenderer(EntityRendererProvider.Context context, boolean kine) {
        super(context,
                new LOTRAurochsModel(LOTRAurochsModel.createBodyLayer().bakeRoot()),
                new LOTRAurochsModel(LOTRAurochsModel.createBabyLayer().bakeRoot()),
                0.5f);
        this.kine = kine;
    }

    public static LOTRAurochsRenderer aurochs(EntityRendererProvider.Context context) {
        return new LOTRAurochsRenderer(context, false);
    }

    public static LOTRAurochsRenderer kineAraw(EntityRendererProvider.Context context) {
        return new LOTRAurochsRenderer(context, true);
    }

    @Override
    public LOTRAurochsRenderState createRenderState() {
        return new LOTRAurochsRenderState();
    }

    @Override
    public void extractRenderState(LOTRAurochsEntity entity, LOTRAurochsRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.enraged = entity.isAurochsEnraged();
        state.skin = (this.kine ? KINE_SKINS : AUROCHS_SKINS).getRandomSkin(entity.getUUID());
    }

    @Override
    protected void scale(LOTRAurochsRenderState state, PoseStack poseStack) {
        if (this.kine) {
            float scale = LOTRKineArawEntity.KINE_SCALE;
            poseStack.scale(scale, scale, scale);
        }
    }

    @Override
    public Identifier getTextureLocation(LOTRAurochsRenderState state) {
        return state.skin;
    }
}
