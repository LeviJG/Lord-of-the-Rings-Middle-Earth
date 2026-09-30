package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland.LOTRDunlendingBartenderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland.LOTRDunlendingBerserkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland.LOTRDunlendingEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderDunlendingBase and LOTRRenderDunlending: a Dunlending in one of
 * the skins for its sex (a male berserker in the berserkers' own). The plain
 * Dunlending and the bartender (LOTRRenderDunlending) wear, with no
 * chestplate on, one of Dunland's outfits -- the bartender his apron -- where
 * the warriors and their kin (LOTRRenderDunlendingBase) do not.
 *
 * <p>The bomb rider (one Dunlending in 10000) wears an orc bomb on his head,
 * and another hangs 3 blocks up, three quarters size, turned with him; the
 * original's doRender also nudged the whole of him half a block higher, and
 * so does this.
 */
public class LOTRDunlendingRenderer
        extends LOTRBipedRenderer<LOTRDunlendingEntity, LOTRDunlendingRenderer.State, LOTRHumanModel<LOTRDunlendingRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dunland/dunlending_male", "dunland/dunlending_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dunland/dunlending_female", "dunland/dunlending_female");
    private static final LOTRRandomSkins SKINS_BERSERKER = LOTRRandomSkins.loadSkinsList("lotr:mob/dunland/berserker", "dunland/berserker");
    private static final LOTRRandomSkins OUTFITS = LOTRRandomSkins.loadSkinsList("lotr:mob/dunland/outfit", "dunland/outfit");
    private static final Identifier APRON =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/dunland/bartender_apron.png");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
        public boolean wearingBomb;
        public final BlockModelRenderState bomb = new BlockModelRenderState();
    }

    private final boolean useOutfits;
    private final BlockModelResolver blockModelResolver;

    /** LOTRRenderDunlending: the plain Dunlending's and the bartender's. */
    public static LOTRDunlendingRenderer dunlending(EntityRendererProvider.Context context) {
        return new LOTRDunlendingRenderer(context, true);
    }

    /** LOTRRenderDunlendingBase: the warriors' and their kin's. */
    public static LOTRDunlendingRenderer warrior(EntityRendererProvider.Context context) {
        return new LOTRDunlendingRenderer(context, false);
    }

    private LOTRDunlendingRenderer(EntityRendererProvider.Context context, boolean useOutfits) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), useOutfits);
    }

    private LOTRDunlendingRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model, boolean useOutfits) {
        super(context, model, model, 0.5f);
        this.useOutfits = useOutfits;
        this.blockModelResolver = context.getBlockModelResolver();
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
    public void extractRenderState(LOTRDunlendingEntity dunlending, State state, float partialTick) {
        super.extractRenderState(dunlending, state, partialTick);
        LOTRRandomSkins skins = !dunlending.familyInfo.isMale() ? SKINS_FEMALE
                : dunlending instanceof LOTRDunlendingBerserkerEntity ? SKINS_BERSERKER : SKINS_MALE;
        state.skin = skins.getRandomSkin(dunlending.getUUID());
        state.outfit = null;
        if (this.useOutfits && dunlending.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            state.outfit = dunlending instanceof LOTRDunlendingBartenderEntity ? APRON : OUTFITS.getRandomSkin(dunlending.getUUID());
        }
        state.wearingBomb = dunlending.isWearingBomb();
        if (state.wearingBomb) {
            this.blockModelResolver.update(state.bomb, LOTRCombatBlocks.ORC_BOMB.defaultBlockState(), TntRenderer.BLOCK_DISPLAY_CONTEXT);
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.wearingBomb) {
            super.submit(state, poseStack, collector, camera);
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.5f, 0.0f);
        // renderBomb(entity, 0, 0, 0, partialTick, fuse 5, strength 0, 0.75, 1): centred 2.5 above.
        poseStack.pushPose();
        poseStack.translate(0.0f, 2.5f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.bodyRot));
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        TntMinecartRenderer.submitWhiteSolidBlock(state.bomb, poseStack, collector, state.lightCoords, false, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
        poseStack.popPose();
    }
}
