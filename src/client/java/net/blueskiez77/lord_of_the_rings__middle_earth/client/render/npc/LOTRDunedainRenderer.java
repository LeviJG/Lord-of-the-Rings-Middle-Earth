package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRDunedainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerIthilienEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EquipmentSlot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderDunedain: a Dúnadan in one of the skins for their sex.
 *
 * <p>LOTRRenderDunedainTrader: the blacksmith wears his outfit
 * ({@code ranger/outfit_blacksmith.png}) whenever he has no chestplate on.
 *
 * <p>LOTRRenderRanger: an Ithilien ranger wears one of Gondor's ranger skins;
 * and a ranger in hiding is drawn at a sixth of his opacity. The original
 * faded his armour, held items and cape with him; the port's armour and held
 * items cannot be faded, so while he hides they are not drawn at all.
 */
public class LOTRDunedainRenderer
        extends LOTRBipedRenderer<LOTRDunedainEntity, LOTRDunedainRenderer.State, LOTRHumanModel<LOTRDunedainRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/ranger/ranger_male", "ranger/ranger_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/ranger/ranger_female", "ranger/ranger_female");
    private static final LOTRRandomSkins SKINS_ITHILIEN = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/ranger", "gondor/ranger");
    /** doRangerInvisibility: glColor4f(1, 1, 1, 0.15). */
    private static final float SNEAK_ALPHA = 0.15f;

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
        public boolean rangerSneaking;
    }

    private final @Nullable Identifier traderOutfit;

    public LOTRDunedainRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    public static EntityRendererProvider<LOTRDunedainEntity> trader(String outfit) {
        return context -> new LOTRDunedainRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/ranger/" + outfit + ".png"));
    }

    private LOTRDunedainRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTRDunedainRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model,
                                 @Nullable Identifier traderOutfit) {
        super(context, model, model, 0.5f);
        this.traderOutfit = traderOutfit;
        addLayer(new HumanoidArmorLayer<State, LOTRHumanModel<State>, HumanoidModel<State>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<State>) new LOTRBipedModel<State>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (!state.rangerSneaking) {
                    super.submit(poseStack, collector, light, state, yRot, xRot);
                }
            }
        });
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
    public void extractRenderState(LOTRDunedainEntity dunedain, State state, float partialTick) {
        super.extractRenderState(dunedain, state, partialTick);
        if (dunedain instanceof LOTRRangerIthilienEntity) {
            state.skin = SKINS_ITHILIEN.getRandomSkin(dunedain.getUUID());
        } else {
            state.skin = (dunedain.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(dunedain.getUUID());
        }
        state.outfit = this.traderOutfit != null && dunedain.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                ? this.traderOutfit : null;
        state.rangerSneaking = dunedain instanceof LOTRRangerEntity ranger && ranger.isRangerSneaking();
        if (state.rangerSneaking) {
            state.rightHandItemState.clear();
            state.leftHandItemState.clear();
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected int getModelTint(State state) {
        return state.rangerSneaking ? ARGB.white(SNEAK_ALPHA) : -1;
    }

    @Override
    protected @Nullable RenderType getRenderType(State state, boolean visible, boolean translucent, boolean glowing) {
        if (state.rangerSneaking && visible) {
            return RenderTypes.entityTranslucent(getTextureLocation(state));
        }
        return super.getRenderType(state, visible, translucent, glowing);
    }
}
