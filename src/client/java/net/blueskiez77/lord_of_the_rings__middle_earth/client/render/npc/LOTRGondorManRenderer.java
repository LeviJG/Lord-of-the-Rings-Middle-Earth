package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorSoldierEntity;

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
 * LOTRRenderGondorMan: a man in one of the skins for his sex -- a soldier's
 * for the soldiers. One in four with no chestplate wears one of the common
 * outfits, and one woman in four with nothing on her head a headscarf.
 *
 * <p>LOTRRenderGondorTrader: a trader wears its own outfit
 * ({@code gondor/<outfit>.png}) instead, whenever it has no chestplate on.
 *
 * <p>LOTRRenderGondorRenegade: a renegade with nothing on his head always
 * wears his hood.
 *
 * <p>LOTRRenderSwanKnight: Dol Amroth's men wear the swan knights' skins,
 * whatever their sex, and nothing more.
 */
public class LOTRGondorManRenderer
        extends LOTRBipedRenderer<LOTRGondorManEntity, LOTRGondorManRenderer.State, LOTRHumanModel<LOTRGondorManRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/gondor_male", "gondor/gondor_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/gondor_female", "gondor/gondor_female");
    private static final LOTRRandomSkins SKINS_SOLDIER = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/gondorSoldier", "gondor/gondor_soldier");
    private static final LOTRRandomSkins OUTFITS = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/outfit", "gondor/outfit");
    private static final LOTRRandomSkins HEADWEAR_FEMALE =
            LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/headwear_female", "gondor/headwear_female");
    private static final LOTRRandomSkins HOODS_RENEGADE =
            LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/renegade_hood", "near_harad/renegade_hood");
    private static final LOTRRandomSkins SKINS_SWAN_KNIGHT =
            LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/swanKnight", "gondor/swan_knight");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier headwear;
        public @Nullable Identifier outfit;
    }

    private final @Nullable Identifier traderOutfit;
    private boolean renegade;
    private boolean swanKnight;

    public LOTRGondorManRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    public static EntityRendererProvider<LOTRGondorManEntity> trader(String outfit) {
        return context -> new LOTRGondorManRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/gondor/" + outfit + ".png"));
    }

    public static LOTRGondorManRenderer renegade(EntityRendererProvider.Context context) {
        LOTRGondorManRenderer renderer = new LOTRGondorManRenderer(context);
        renderer.renegade = true;
        return renderer;
    }

    public static LOTRGondorManRenderer swanKnight(EntityRendererProvider.Context context) {
        LOTRGondorManRenderer renderer = new LOTRGondorManRenderer(context);
        renderer.swanKnight = true;
        return renderer;
    }

    private LOTRGondorManRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTRGondorManRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model,
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
                // Pass 0: the headwear or hood; pass 1: the outfit.
                if (state.headwear != null) {
                    coloredCutoutModelCopyLayerRender(outfitModel, state.headwear, poseStack, collector, light, state, -1, 1);
                }
                if (state.outfit != null) {
                    coloredCutoutModelCopyLayerRender(outfitModel, state.outfit, poseStack, collector, light, state, -1, 2);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRGondorManEntity man, State state, float partialTick) {
        super.extractRenderState(man, state, partialTick);
        if (this.swanKnight) {
            state.skin = SKINS_SWAN_KNIGHT.getRandomSkin(man.getUUID());
            state.headwear = null;
            state.outfit = null;
            return;
        }
        boolean male = man.familyInfo.isMale();
        if (male) {
            state.skin = (man instanceof LOTRGondorSoldierEntity ? SKINS_SOLDIER : SKINS_MALE).getRandomSkin(man.getUUID());
        } else {
            state.skin = SKINS_FEMALE.getRandomSkin(man.getUUID());
        }
        boolean bareHead = man.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
        boolean noChest = man.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        int roll = LOTRRandomSkins.nextInt(man.getUUID(), 4);
        if (this.renegade && bareHead) {
            state.headwear = HOODS_RENEGADE.getRandomSkin(man.getUUID());
        } else {
            state.headwear = bareHead && !male && roll == 0 ? HEADWEAR_FEMALE.getRandomSkin(man.getUUID()) : null;
        }
        if (this.traderOutfit != null && noChest) {
            state.outfit = this.traderOutfit;
        } else {
            state.outfit = noChest && roll == 0 ? OUTFITS.getRandomSkin(man.getUUID()) : null;
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
