package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale.LOTRDaleLevymanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale.LOTRDaleManEntity;

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
 * LOTRRenderDaleMan: a man or woman of Dale in one of the skins for their sex
 * -- a soldier's for the levymen and soldiers.
 *
 * <p>LOTRRenderDaleTrader: the blacksmith and baker wear their aprons
 * ({@code dale/<outfit>.png}) whenever they have no chestplate on.
 */
public class LOTRDaleManRenderer
        extends LOTRBipedRenderer<LOTRDaleManEntity, LOTRDaleManRenderer.State, LOTRHumanModel<LOTRDaleManRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dale/dale_male", "dale/dale_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dale/dale_female", "dale/dale_female");
    private static final LOTRRandomSkins SKINS_SOLDIER = LOTRRandomSkins.loadSkinsList("lotr:mob/dale/dale_soldier", "dale/dale_soldier");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
    }

    private final @Nullable Identifier traderOutfit;

    public LOTRDaleManRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    public static EntityRendererProvider<LOTRDaleManEntity> trader(String outfit) {
        return context -> new LOTRDaleManRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/dale/" + outfit + ".png"));
    }

    private LOTRDaleManRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTRDaleManRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model,
                                 @Nullable Identifier traderOutfit) {
        super(context, model, model, 0.5f);
        this.traderOutfit = traderOutfit;
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
                    coloredCutoutModelCopyLayerRender(outfitModel, state.outfit, poseStack, collector, light, state, -1, 1);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRDaleManEntity man, State state, float partialTick) {
        super.extractRenderState(man, state, partialTick);
        if (man.familyInfo.isMale()) {
            state.skin = (man instanceof LOTRDaleLevymanEntity ? SKINS_SOLDIER : SKINS_MALE).getRandomSkin(man.getUUID());
        } else {
            state.skin = SKINS_FEMALE.getRandomSkin(man.getUUID());
        }
        state.outfit = this.traderOutfit != null && man.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                ? this.traderOutfit : null;
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }


}
