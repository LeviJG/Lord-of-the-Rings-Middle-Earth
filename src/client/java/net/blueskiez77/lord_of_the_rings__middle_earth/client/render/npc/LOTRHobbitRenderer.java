package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHobbitModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderHobbit: three quarters of a man's size, in one of the skins for
 * its sex and age, and a married hobbit's ring drawn on an outfit layer.
 */
public class LOTRHobbitRenderer
        extends LOTRBipedRenderer<LOTRHobbitEntity, LOTRHobbitRenderer.State, LOTRHobbitModel<LOTRHobbitRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/hobbit/hobbit_male", "hobbit/hobbit_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/hobbit/hobbit_female", "hobbit/hobbit_female");
    private static final LOTRRandomSkins SKINS_MALE_CHILD = LOTRRandomSkins.loadSkinsList("lotr:mob/hobbit/child_male", "hobbit/child_male");
    private static final LOTRRandomSkins SKINS_FEMALE_CHILD = LOTRRandomSkins.loadSkinsList("lotr:mob/hobbit/child_female", "hobbit/child_female");
    private static final Identifier RING_TEXTURE =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/hobbit/ring.png");

    public static class State extends LOTRNPCRenderState {
        public boolean wearingRing;
        public boolean chestEmpty;
    }

    private final @Nullable Identifier traderOutfit;

    public LOTRHobbitRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    /**
     * LOTRRenderHobbitTrader: a trader's outfit ({@code mob/hobbit/<outfit>.png})
     * worn in the pass that would otherwise draw the ring, when it has no
     * chestplate on.
     */
    public static EntityRendererProvider<LOTRHobbitEntity> trader(String outfit) {
        return context -> new LOTRHobbitRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/hobbit/" + outfit + ".png"));
    }

    private LOTRHobbitRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHobbitModel<>(LOTRHobbitModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTRHobbitRenderer(EntityRendererProvider.Context context, LOTRHobbitModel<State> model,
                               @Nullable Identifier traderOutfit) {
        super(context, model, model, 0.5f);
        this.traderOutfit = traderOutfit;
        LOTRHobbitModel<State> traderModel = new LOTRHobbitModel<>(LOTRHobbitModel.createOutfitLayer().bakeRoot());
        addLayer(new HumanoidArmorLayer<State, LOTRHobbitModel<State>, HumanoidModel<State>>(this,
                LOTRHobbitModel.createArmorLayers().map(layer -> (HumanoidModel<State>) new LOTRHobbitModel<State>(layer.bakeRoot())),
                context.getEquipmentRenderer()));
        LOTRHobbitModel<State> outfit = new LOTRHobbitModel<>(LOTRHobbitModel.createOutfitLayer().bakeRoot()) {
            @Override
            public void setupAnim(State state) {
                super.setupAnim(state);
                this.rightArm.visible = false;
            }
        };
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (traderOutfit != null && state.chestEmpty) {
                    coloredCutoutModelCopyLayerRender(traderModel, traderOutfit, poseStack, collector, light, state, -1, 1);
                } else if (state.wearingRing) {
                    coloredCutoutModelCopyLayerRender(outfit, RING_TEXTURE, poseStack, collector, light, state, -1, 1);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRHobbitEntity hobbit, State state, float partialTick) {
        super.extractRenderState(hobbit, state, partialTick);
        boolean child = hobbit.isBaby();
        LOTRRandomSkins skins = hobbit.familyInfo.isMale()
                ? child ? SKINS_MALE_CHILD : SKINS_MALE
                : child ? SKINS_FEMALE_CHILD : SKINS_FEMALE;
        state.skin = skins.getRandomSkin(hobbit.getUUID());
        state.chestEmpty = hobbit.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        // shouldRenderPass 1: the people's ring on the head slot.
        state.wearingRing = hobbit.getClass() == hobbit.familyInfo.marriageEntityClass
                && hobbit.familyInfo.marriageRing != null
                && hobbit.getItemBySlot(EquipmentSlot.HEAD).is(hobbit.familyInfo.marriageRing);
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected void scale(State state, PoseStack poseStack) {
        super.scale(state, poseStack);
        poseStack.scale(0.75f, 0.75f, 0.75f);
    }
}
