package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohanManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohanShieldmaidenEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohirrimWarriorEntity;

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
 * LOTRRenderRohirrim: a man in one of the skins for his sex -- a warrior's
 * for the riders, a shieldmaiden's for her.
 *
 * <p>LOTRRenderRohanTrader: a trader's outfit ({@code rohan/<outfit>.png})
 * worn when it has no chestplate on.
 */
public class LOTRRohirrimRenderer
        extends LOTRBipedRenderer<LOTRRohanManEntity, LOTRRohirrimRenderer.State, LOTRHumanModel<LOTRRohirrimRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/rohan/rohan_male", "rohan/rohan_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/rohan/rohan_female", "rohan/rohan_female");
    private static final LOTRRandomSkins SKINS_SOLDIER = LOTRRandomSkins.loadSkinsList("lotr:mob/rohan/warrior", "rohan/warrior");
    private static final LOTRRandomSkins SKINS_SHIELDMAIDEN =
            LOTRRandomSkins.loadSkinsList("lotr:mob/rohan/shieldmaiden", "rohan/shieldmaiden");

    public static class State extends LOTRNPCRenderState {
        public boolean chestEmpty;
    }

    private final @Nullable Identifier traderOutfit;

    public LOTRRohirrimRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    public static EntityRendererProvider<LOTRRohanManEntity> trader(String outfit) {
        return context -> new LOTRRohirrimRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/rohan/" + outfit + ".png"));
    }

    private LOTRRohirrimRenderer(EntityRendererProvider.Context context, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), traderOutfit);
    }

    private LOTRRohirrimRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model,
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
                if (LOTRRohirrimRenderer.this.traderOutfit != null && state.chestEmpty) {
                    coloredCutoutModelCopyLayerRender(outfitModel, LOTRRohirrimRenderer.this.traderOutfit, poseStack,
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
    public void extractRenderState(LOTRRohanManEntity rohirrim, State state, float partialTick) {
        super.extractRenderState(rohirrim, state, partialTick);
        LOTRRandomSkins skins;
        if (rohirrim.familyInfo.isMale()) {
            skins = rohirrim instanceof LOTRRohirrimWarriorEntity ? SKINS_SOLDIER : SKINS_MALE;
        } else {
            skins = rohirrim instanceof LOTRRohanShieldmaidenEntity ? SKINS_SHIELDMAIDEN : SKINS_FEMALE;
        }
        state.skin = skins.getRandomSkin(rohirrim.getUUID());
        state.chestEmpty = rohirrim.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
