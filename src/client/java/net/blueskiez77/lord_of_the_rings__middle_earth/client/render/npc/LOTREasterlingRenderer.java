package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun.LOTREasterlingEntity;

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
 * LOTRRenderEasterling: an Easterling in one of the skins for its sex.
 * LOTRRenderEasterlingTrader adds a trader's outfit
 * ({@code mob/rhun/<outfit>.png}) when it has no chestplate on.
 */
public class LOTREasterlingRenderer
        extends LOTRBipedRenderer<LOTREasterlingEntity, LOTREasterlingRenderer.State, LOTRHumanModel<LOTREasterlingRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/rhun/easterling_male", "rhun/easterling_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/rhun/easterling_female", "rhun/easterling_female");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
    }

    private final @Nullable Identifier traderOutfit;

    public LOTREasterlingRenderer(EntityRendererProvider.Context context) {
        this(context, (Identifier) null);
    }

    /** LOTRRenderEasterlingTrader: an outfit with no chestplate on. */
    public static EntityRendererProvider<LOTREasterlingEntity> trader(String outfit) {
        return context -> new LOTREasterlingRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/rhun/" + outfit + ".png"));
    }

    private LOTREasterlingRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTREasterlingRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model,
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
    public void extractRenderState(LOTREasterlingEntity easterling, State state, float partialTick) {
        super.extractRenderState(easterling, state, partialTick);
        state.skin = (easterling.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(easterling.getUUID());
        state.outfit = this.traderOutfit != null && easterling.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                ? this.traderOutfit : null;
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
