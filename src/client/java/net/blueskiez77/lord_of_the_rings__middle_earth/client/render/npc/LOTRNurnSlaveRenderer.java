package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRNurnSlaveEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderNurnSlave: a slave in one of the skins of their sex
 * ({@code mob/nurn/slave_<sex>}).
 */
public class LOTRNurnSlaveRenderer
        extends LOTRBipedRenderer<LOTRNurnSlaveEntity, LOTRNPCRenderState, LOTRHumanModel<LOTRNPCRenderState>> {

    public LOTRNurnSlaveRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()));
    }

    private LOTRNurnSlaveRenderer(EntityRendererProvider.Context context, LOTRHumanModel<LOTRNPCRenderState> model) {
        super(context, model, model, 0.5f);
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
    public void extractRenderState(LOTRNurnSlaveEntity slave, LOTRNPCRenderState state, float partialTick) {
        super.extractRenderState(slave, state, partialTick);
        String sex = slave.familyInfo.isMale() ? "male" : "female";
        LOTRRandomSkins skins = LOTRRandomSkins.loadSkinsList("lotr:mob/nurn/slave_" + sex, "nurn/slave_" + sex);
        state.skin = skins.getRandomSkin(slave.getUUID());
    }

    @Override
    public Identifier getTextureLocation(LOTRNPCRenderState state) {
        return state.skin;
    }
}
