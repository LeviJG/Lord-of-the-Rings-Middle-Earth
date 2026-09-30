package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRTauredainEntity;

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
 * LOTRRenderTauredain: a Taurethrim in one of the skins for its sex, and one
 * in three with no chestplate on in one of the Taurethrim outfits.
 * LOTRRenderTauredainShaman always puts a shaman with no chestplate on in the
 * shaman's outfit.
 */
public class LOTRTauredainRenderer
        extends LOTRBipedRenderer<LOTRTauredainEntity, LOTRTauredainRenderer.State, LOTRHumanModel<LOTRTauredainRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/tauredain/tauredain_male", "tauredain/tauredain_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/tauredain/tauredain_female", "tauredain/tauredain_female");
    private static final LOTRRandomSkins OUTFITS = LOTRRandomSkins.loadSkinsList("lotr:mob/tauredain/outfit", "tauredain/outfit");
    private static final LOTRRandomSkins SHAMAN_OUTFITS =
            LOTRRandomSkins.loadSkinsList("lotr:mob/tauredain/shaman_outfit", "tauredain/shaman_outfit");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
    }

    private final boolean shaman;

    public LOTRTauredainRenderer(EntityRendererProvider.Context context) {
        this(context, false);
    }

    /** LOTRRenderTauredainShaman. */
    public static LOTRTauredainRenderer shaman(EntityRendererProvider.Context context) {
        return new LOTRTauredainRenderer(context, true);
    }

    private LOTRTauredainRenderer(EntityRendererProvider.Context context, boolean shaman) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), shaman);
    }

    private LOTRTauredainRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model, boolean shaman) {
        super(context, model, model, 0.5f);
        this.shaman = shaman;
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
    public void extractRenderState(LOTRTauredainEntity man, State state, float partialTick) {
        super.extractRenderState(man, state, partialTick);
        state.skin = (man.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(man.getUUID());
        boolean chestEmpty = man.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        if (this.shaman && chestEmpty) {
            state.outfit = SHAMAN_OUTFITS.getRandomSkin(man.getUUID());
        } else {
            state.outfit = chestEmpty && LOTRRandomSkins.nextInt(man.getUUID(), 3) == 0
                    ? OUTFITS.getRandomSkin(man.getUUID()) : null;
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
