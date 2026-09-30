package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeManEntity;

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
 * LOTRRenderBreeMan: a man in one of the skins for its sex. One woman in four
 * with nothing on her head wears a headscarf or hood (headwear_female).
 *
 * <p>LOTRRenderBreeTrader: a trader's outfit ({@code bree/<outfit>.png}) worn
 * when it has no chestplate on.
 *
 * <p>LOTRRenderBreeRuffian: a ruffian's own skins, and one in three with
 * nothing on his head wears a hood.
 */
public class LOTRBreeManRenderer
        extends LOTRBipedRenderer<LOTRBreeManEntity, LOTRBreeManRenderer.State, LOTRHumanModel<LOTRBreeManRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/bree/bree_male", "bree/bree_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/bree/bree_female", "bree/bree_female");
    private static final LOTRRandomSkins HEADWEAR_FEMALE =
            LOTRRandomSkins.loadSkinsList("lotr:mob/bree/headwear_female", "bree/headwear_female");
    private static final LOTRRandomSkins SKINS_RUFFIAN = LOTRRandomSkins.loadSkinsList("lotr:mob/bree/ruffian", "bree/ruffian");
    private static final LOTRRandomSkins HOODS_RUFFIAN =
            LOTRRandomSkins.loadSkinsList("lotr:mob/bree/ruffian_hood", "bree/ruffian_hood");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier headwear;
        public boolean chestEmpty;
    }

    private final @Nullable Identifier traderOutfit;
    private boolean ruffian;

    public LOTRBreeManRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    public static LOTRBreeManRenderer ruffian(EntityRendererProvider.Context context) {
        LOTRBreeManRenderer renderer = new LOTRBreeManRenderer(context);
        renderer.ruffian = true;
        return renderer;
    }

    public static EntityRendererProvider<LOTRBreeManEntity> trader(String outfit) {
        return context -> new LOTRBreeManRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/bree/" + outfit + ".png"));
    }

    private LOTRBreeManRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTRBreeManRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model,
                                @Nullable Identifier traderOutfit) {
        super(context, model, model, 0.5f);
        this.traderOutfit = traderOutfit;
        // func_82421_b: LOTRModelBiped(1.0) outside, (0.5) for leggings -- a plain biped's armour.
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
                // Pass 0: the headwear; pass 1: the trader's outfit.
                if (state.headwear != null) {
                    coloredCutoutModelCopyLayerRender(outfitModel, state.headwear, poseStack, collector, light, state, -1, 1);
                }
                if (LOTRBreeManRenderer.this.traderOutfit != null && state.chestEmpty) {
                    coloredCutoutModelCopyLayerRender(outfitModel, LOTRBreeManRenderer.this.traderOutfit, poseStack,
                            collector, light, state, -1, 2);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRBreeManEntity man, State state, float partialTick) {
        super.extractRenderState(man, state, partialTick);
        boolean male = man.familyInfo.isMale();
        boolean bareHead = man.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
        if (this.ruffian) {
            state.skin = SKINS_RUFFIAN.getRandomSkin(man.getUUID());
            state.headwear = bareHead && LOTRRandomSkins.nextInt(man.getUUID(), 3) == 0
                    ? HOODS_RUFFIAN.getRandomSkin(man.getUUID()) : null;
        } else {
            state.skin = (male ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(man.getUUID());
            state.headwear = !male && bareHead && LOTRRandomSkins.nextInt(man.getUUID(), 4) == 0
                    ? HEADWEAR_FEMALE.getRandomSkin(man.getUUID()) : null;
        }
        state.chestEmpty = man.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
