package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBanditEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderBandit: a bandit in one of his skins -- the bandit's
 * ({@code mob/bandit/bandit}) or the Harad bandit's ({@code mob/bandit/harad}).
 */
public class LOTRBanditRenderer
        extends LOTRBipedRenderer<LOTRBanditEntity, LOTRNPCRenderState, LOTRHumanModel<LOTRNPCRenderState>> {

    private final LOTRRandomSkins skins;

    /** LOTRRenderBandit(s): the skins of {@code mob/bandit/<s>}. */
    public static EntityRendererProvider<LOTRBanditEntity> of(String kind) {
        return context -> new LOTRBanditRenderer(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()),
                LOTRRandomSkins.loadSkinsList("lotr:mob/bandit/" + kind, "bandit/" + kind));
    }

    private LOTRBanditRenderer(EntityRendererProvider.Context context, LOTRHumanModel<LOTRNPCRenderState> model,
                               LOTRRandomSkins skins) {
        super(context, model, model, 0.5f);
        this.skins = skins;
        addLayer(new HumanoidArmorLayer<LOTRNPCRenderState, LOTRHumanModel<LOTRNPCRenderState>, HumanoidModel<LOTRNPCRenderState>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<LOTRNPCRenderState>) new LOTRBipedModel<LOTRNPCRenderState>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
    }

    @Override
    public LOTRNPCRenderState createRenderState() {
        return new LOTRNPCRenderState();
    }

    @Override
    public void extractRenderState(LOTRBanditEntity bandit, LOTRNPCRenderState state, float partialTick) {
        super.extractRenderState(bandit, state, partialTick);
        state.skin = this.skins.getRandomSkin(bandit.getUUID());
    }

    @Override
    public Identifier getTextureLocation(LOTRNPCRenderState state) {
        return state.skin;
    }
}
