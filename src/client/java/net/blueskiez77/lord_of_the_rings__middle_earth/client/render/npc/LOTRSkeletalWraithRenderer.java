package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSkeletalWraithModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRSkeletalWraithEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderSkeleton: a skeletal wraith in the skeleton's own skin, arms out
 * before it, in whatever armour it fell in. It was a vanilla RenderBiped, so
 * it is drawn at full size, not a player's fifteen sixteenths.
 */
public class LOTRSkeletalWraithRenderer extends LOTRBipedRenderer<LOTRSkeletalWraithEntity, LOTRNPCRenderState,
        LOTRSkeletalWraithModel<LOTRNPCRenderState>> {

    private static final Identifier SKIN = Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    public LOTRSkeletalWraithRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRSkeletalWraithModel<>(LOTRSkeletalWraithModel.createBodyLayer().bakeRoot()));
    }

    private LOTRSkeletalWraithRenderer(EntityRendererProvider.Context context,
                                       LOTRSkeletalWraithModel<LOTRNPCRenderState> model) {
        super(context, model, model, 0.5f);
        // func_82421_b: LOTRModelSkeleton(1.0) and (0.5), which keep the biped's full limbs.
        addLayer(new HumanoidArmorLayer<LOTRNPCRenderState, LOTRSkeletalWraithModel<LOTRNPCRenderState>,
                HumanoidModel<LOTRNPCRenderState>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<LOTRNPCRenderState>) new LOTRSkeletalWraithModel<LOTRNPCRenderState>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
    }

    @Override
    public LOTRNPCRenderState createRenderState() {
        return new LOTRNPCRenderState();
    }

    @Override
    public void extractRenderState(LOTRSkeletalWraithEntity wraith, LOTRNPCRenderState state, float partialTick) {
        super.extractRenderState(wraith, state, partialTick);
        state.skin = SKIN;
    }

    @Override
    public Identifier getTextureLocation(LOTRNPCRenderState state) {
        return SKIN;
    }

    @Override
    protected void scale(LOTRNPCRenderState state, PoseStack poseStack) {
    }
}
