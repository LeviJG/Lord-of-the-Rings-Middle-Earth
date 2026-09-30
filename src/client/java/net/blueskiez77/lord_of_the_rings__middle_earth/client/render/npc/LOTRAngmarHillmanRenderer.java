package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar.LOTRAngmarHillmanEntity;

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
 * LOTRRenderAngmarHillman: a hillman of Rhudaur in one of the skins for its
 * sex. The plain hillman (not the warriors) wears one of the hillmen's
 * outfits when he has no chestplate on.
 */
public class LOTRAngmarHillmanRenderer
        extends LOTRBipedRenderer<LOTRAngmarHillmanEntity, LOTRAngmarHillmanRenderer.State, LOTRHumanModel<LOTRAngmarHillmanRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/hillman/hillman_male", "hillman/hillman_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/hillman/hillman_female", "hillman/hillman_female");
    private static final LOTRRandomSkins OUTFITS = LOTRRandomSkins.loadSkinsList("lotr:mob/hillman/outfit", "hillman/outfit");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
    }

    private final boolean useOutfits;

    /** LOTRRenderAngmarHillman(true), the plain hillman's. */
    public static LOTRAngmarHillmanRenderer hillman(EntityRendererProvider.Context context) {
        return new LOTRAngmarHillmanRenderer(context, true);
    }

    /** LOTRRenderAngmarHillman(false), the warriors' and their kin's. */
    public static LOTRAngmarHillmanRenderer warrior(EntityRendererProvider.Context context) {
        return new LOTRAngmarHillmanRenderer(context, false);
    }

    private LOTRAngmarHillmanRenderer(EntityRendererProvider.Context context, boolean useOutfits) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), useOutfits);
    }

    private LOTRAngmarHillmanRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model, boolean useOutfits) {
        super(context, model, model, 0.5f);
        this.useOutfits = useOutfits;
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
    public void extractRenderState(LOTRAngmarHillmanEntity hillman, State state, float partialTick) {
        super.extractRenderState(hillman, state, partialTick);
        state.skin = (hillman.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(hillman.getUUID());
        state.outfit = this.useOutfits && hillman.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                ? OUTFITS.getRandomSkin(hillman.getUUID()) : null;
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
