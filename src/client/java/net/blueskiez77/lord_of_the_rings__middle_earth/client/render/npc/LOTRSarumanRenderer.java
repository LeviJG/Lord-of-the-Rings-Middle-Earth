package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import java.awt.Color;
import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRSarumanEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderSaruman: Saruman in his skin, tinted round the whole spectrum
 * every 360 ticks, and every three seconds taking to twitching -- tipped and
 * shifted at random each frame -- or leaving off.
 */
public class LOTRSarumanRenderer
        extends LOTRBipedRenderer<LOTRSarumanEntity, LOTRSarumanRenderer.State, LOTRHumanModel<LOTRSarumanRenderer.State>> {

    private static final Identifier SKIN =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/char/saruman.png");

    public static class State extends LOTRNPCRenderState {
        public int tint = -1;
    }

    private final Random rand = new Random();
    private boolean twitch;

    public LOTRSarumanRenderer(EntityRendererProvider.Context context) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()));
    }

    private LOTRSarumanRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model) {
        super(context, model, model, 0.5f);
        addLayer(new HumanoidArmorLayer<State, LOTRHumanModel<State>, HumanoidModel<State>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<State>) new LOTRBipedModel<State>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRSarumanEntity saruman, State state, float partialTick) {
        super.extractRenderState(saruman, state, partialTick);
        state.skin = SKIN;
        float hue = saruman.tickCount % 360 / 360.0f;
        state.tint = 0xFF000000 | Color.HSBtoRGB(hue, 1.0f, 1.0f);
        if (saruman.tickCount % 60 == 0) {
            this.twitch = !this.twitch;
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected int getModelTint(State state) {
        return state.tint;
    }

    @Override
    protected void scale(State state, PoseStack poseStack) {
        super.scale(state, poseStack);
        if (this.twitch) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(this.rand.nextFloat() * 40.0f));
            poseStack.mulPose(Axis.YP.rotationDegrees(this.rand.nextFloat() * 40.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(this.rand.nextFloat() * 40.0f));
            poseStack.translate(this.rand.nextFloat() * 0.5f, this.rand.nextFloat() * 0.5f, this.rand.nextFloat() * 0.5f);
        }
    }
}
