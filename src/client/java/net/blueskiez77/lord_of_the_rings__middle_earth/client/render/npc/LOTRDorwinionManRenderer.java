package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion.LOTRDorwinionManEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderDorwinionMan: a Dorwinion man in one of the skins for its sex,
 * and one in two with no chestplate on in one of Dorwinion's outfits.
 */
public class LOTRDorwinionManRenderer
        extends LOTRBipedRenderer<LOTRDorwinionManEntity, LOTRDorwinionManRenderer.State, LOTRHumanModel<LOTRDorwinionManRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dorwinion/dorwinion_male", "dorwinion/dorwinion_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dorwinion/dorwinion_female", "dorwinion/dorwinion_female");
    private static final LOTRRandomSkins OUTFITS = LOTRRandomSkins.loadSkinsList("lotr:mob/dorwinion/outfit", "dorwinion/outfit");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
    }

    public LOTRDorwinionManRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()));
    }

    private LOTRDorwinionManRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model) {
        super(context, model, model, 0.5f);
        addLayer(new HumanoidArmorLayer<State, LOTRHumanModel<State>, HumanoidModel<State>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<State>) new LOTRBipedModel<State>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
        LOTRHumanModel<State> outfitModel = new LOTRHumanModel<>(LOTRHumanModel.createOutfitLayer().bakeRoot());
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (state.outfit != null) {
                    coloredCutoutModelCopyLayerRender(outfitModel, state.outfit, poseStack, collector, light, state, -1, 2);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRDorwinionManEntity man, State state, float partialTick) {
        super.extractRenderState(man, state, partialTick);
        state.skin = (man.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(man.getUUID());
        state.outfit = man.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                && LOTRRandomSkins.nextInt(man.getUUID(), 2) == 0 ? OUTFITS.getRandomSkin(man.getUUID()) : null;
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
