package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHaradSlaveEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderHaradSlave: a slave in one of the skins of his people and sex
 * ({@code mob/nearHarad/slave/<people>_<sex>}; only men are taken).
 */
public class LOTRHaradSlaveRenderer
        extends LOTRBipedRenderer<LOTRHaradSlaveEntity, LOTRNPCRenderState, LOTRHumanModel<LOTRNPCRenderState>> {

    public LOTRHaradSlaveRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()));
    }

    private LOTRHaradSlaveRenderer(EntityRendererProvider.Context context, LOTRHumanModel<LOTRNPCRenderState> model) {
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
    public void extractRenderState(LOTRHaradSlaveEntity slave, LOTRNPCRenderState state, float partialTick) {
        super.extractRenderState(slave, state, partialTick);
        String sex = slave.familyInfo.isMale() ? "male" : "female";
        String skinDir = slave.getSlaveType().skinDir;
        String portDir = "near_harad/slave/" + skinDir.replace("nearHarad", "near_harad") + "_" + sex;
        LOTRRandomSkins skins = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/slave/" + skinDir + "_" + sex, portDir);
        state.skin = skins.getRandomSkin(slave.getUUID());
    }

    @Override
    public Identifier getTextureLocation(LOTRNPCRenderState state) {
        return state.skin;
    }
}
