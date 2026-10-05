package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRWizardHatModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGandalfEntity;

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
 * LOTRRenderGandalf: the Grey Wanderer in his skin, his pointed hat when he
 * wears no helmet and his cloak when he wears no chestplate -- all three
 * Father Christmas's at Christmas -- and his name always over his head within
 * 64 blocks, three quarters of a block higher than usual, unless he is
 * speaking.
 *
 * <p>NOT ported yet: his cape (and the Christmas one), with the NPC capes.
 */
public class LOTRGandalfRenderer
        extends LOTRBipedRenderer<LOTRGandalfEntity, LOTRGandalfRenderer.State, LOTRHumanModel<LOTRGandalfRenderer.State>> {

    private static final Identifier SKIN = texture("gandalf");
    private static final Identifier HAT = texture("gandalf_hat");
    private static final Identifier CLOAK = texture("gandalf_cloak");
    private static final Identifier SKIN_SANTA = texture("santa");
    private static final Identifier HAT_SANTA = texture("santa_hat");
    private static final Identifier CLOAK_SANTA = texture("santa_cloak");

    public static class State extends LOTRNPCRenderState {
        public boolean christmas;
        public boolean headEmpty;
        public boolean chestEmpty;
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/char/" + name + ".png");
    }

    public LOTRGandalfRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()));
    }

    private LOTRGandalfRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model) {
        super(context, model, model, 0.5f);
        addLayer(new HumanoidArmorLayer<State, LOTRHumanModel<State>, HumanoidModel<State>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<State>) new LOTRBipedModel<State>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
        LOTRWizardHatModel<State> hatModel = new LOTRWizardHatModel<>(LOTRWizardHatModel.createLayer().bakeRoot());
        LOTRHumanModel<State> cloakModel = new LOTRHumanModel<>(LOTRHumanModel.createOutfitLayer().bakeRoot());
        // shouldRenderPass 0 and 1: the hat without a helmet, the cloak without a chestplate.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (state.headEmpty) {
                    coloredCutoutModelCopyLayerRender(hatModel, state.christmas ? HAT_SANTA : HAT,
                            poseStack, collector, light, state, -1, 1);
                }
                if (state.chestEmpty) {
                    coloredCutoutModelCopyLayerRender(cloakModel, state.christmas ? CLOAK_SANTA : CLOAK,
                            poseStack, collector, light, state, -1, 2);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    protected boolean shouldShowName(LOTRGandalfEntity gandalf, double distanceSq) {
        return true;
    }

    /** getCapeToRender: Father Christmas's red cape at Christmas. */
    @Override
    protected @Nullable Identifier getCapeToRender(LOTRGandalfEntity gandalf) {
        return LOTRMod.isChristmas() ? LOTRCapes.GANDALF_SANTA : gandalf.npcCape;
    }

    @Override
    public void extractRenderState(LOTRGandalfEntity gandalf, State state, float partialTick) {
        super.extractRenderState(gandalf, state, partialTick);
        state.christmas = LOTRMod.isChristmas();
        state.skin = state.christmas ? SKIN_SANTA : SKIN;
        state.headEmpty = gandalf.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
        state.chestEmpty = gandalf.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        if (state.speech != null) {
            state.nameTag = null;
        } else if (state.nameTagAttachment != null) {
            state.nameTagAttachment = state.nameTagAttachment.add(0.0, 0.75, 0.0);
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
