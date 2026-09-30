package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHumanModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRCorsairEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfHaradWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRGulfHaradrimEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedhrimEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNomadEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRUmbarWarriorEntity;

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
 * LOTRRenderNearHaradrim: a Southron in one of the skins for its sex -- a
 * warrior of the coast or of Umbar in the warriors' own, a Harnennor or Gulf
 * warrior in the Harnedor warriors', the other Harnedhrim, the Gulfings and
 * the Corsairs in the Harnedor skins, and the nomads in their own; a
 * Harnedhrim or Gulfing with no chestplate on wears one of Harnedor's outfits
 * half the time, and a nomad with nothing on its head one of the nomads' hats
 * half the time. LOTRRenderNearHaradrimWarlord wears the warlord's one skin, and LOTRRenderHaradrimTrader a trader's outfit
 * ({@code mob/nearHarad/<outfit>.png}) when it has no chestplate on.
 */
public class LOTRNearHaradrimRenderer extends LOTRBipedRenderer<LOTRNearHaradrimBaseEntity,
        LOTRNearHaradrimRenderer.State, LOTRHumanModel<LOTRNearHaradrimRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/haradrim_male", "near_harad/haradrim_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/haradrim_female", "near_harad/haradrim_female");
    private static final LOTRRandomSkins WARRIOR_SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/warrior", "near_harad/warrior");
    private static final LOTRRandomSkins HARNEDOR_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/harnedor_male", "near_harad/harnedor_male");
    private static final LOTRRandomSkins HARNEDOR_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/harnedor_female", "near_harad/harnedor_female");
    private static final LOTRRandomSkins HARNEDOR_WARRIOR_SKINS =
            LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/harnedorWarrior", "near_harad/harnedor_warrior");
    private static final LOTRRandomSkins HARNEDOR_OUTFITS =
            LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/harnedor_outfit", "near_harad/harnedor_outfit");
    private static final LOTRRandomSkins NOMAD_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/nomad_male", "near_harad/nomad_male");
    private static final LOTRRandomSkins NOMAD_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/nomad_female", "near_harad/nomad_female");
    private static final LOTRRandomSkins NOMAD_HATS = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/nomad_hat", "near_harad/nomad_hat");
    private static final Identifier WARLORD_SKIN = texture("warlord");

    public static class State extends LOTRNPCRenderState {
        public @Nullable Identifier outfit;
        public @Nullable Identifier hat;
    }

    private final boolean warlord;
    private final @Nullable Identifier traderOutfit;

    public LOTRNearHaradrimRenderer(EntityRendererProvider.Context context) {
        this(context, false, null);
    }

    /** LOTRRenderNearHaradrimWarlord. */
    public static LOTRNearHaradrimRenderer warlord(EntityRendererProvider.Context context) {
        return new LOTRNearHaradrimRenderer(context, true, null);
    }

    /** LOTRRenderNearHaradTrader and LOTRRenderHaradrimTrader: an outfit with no chestplate on. */
    public static EntityRendererProvider<LOTRNearHaradrimBaseEntity> trader(String outfit) {
        return context -> new LOTRNearHaradrimRenderer(context, false, texture(outfit));
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/near_harad/" + name + ".png");
    }

    private LOTRNearHaradrimRenderer(EntityRendererProvider.Context context, boolean warlord, @Nullable Identifier traderOutfit) {
        this(context, new LOTRHumanModel<>(LOTRHumanModel.createBodyLayer().bakeRoot()), warlord, traderOutfit);
    }

    private LOTRNearHaradrimRenderer(EntityRendererProvider.Context context, LOTRHumanModel<State> model, boolean warlord,
                                     @Nullable Identifier traderOutfit) {
        super(context, model, model, 0.5f);
        this.warlord = warlord;
        this.traderOutfit = traderOutfit;
        addLayer(new HumanoidArmorLayer<State, LOTRHumanModel<State>, HumanoidModel<State>>(this,
                HumanoidModel.createArmorMeshSet(new CubeDeformation(0.5f), new CubeDeformation(1.0f))
                        .map(mesh -> (HumanoidModel<State>) new LOTRBipedModel<State>(
                                LayerDefinition.create(mesh, 64, 32).bakeRoot())),
                context.getEquipmentRenderer()));
        LOTRHumanModel<State> outfitModel = new LOTRHumanModel<>(LOTRHumanModel.createOutfitLayer().bakeRoot());
        // shouldRenderPass 0: a nomad's hat, in the helmet's pass.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (state.hat != null) {
                    coloredCutoutModelCopyLayerRender(outfitModel, state.hat, poseStack, collector, light, state, -1, 1);
                }
            }
        });
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
    public void extractRenderState(LOTRNearHaradrimBaseEntity haradrim, State state, float partialTick) {
        super.extractRenderState(haradrim, state, partialTick);
        if (this.warlord) {
            state.skin = WARLORD_SKIN;
        } else if (haradrim instanceof LOTRHarnedorWarriorEntity || haradrim instanceof LOTRGulfHaradWarriorEntity) {
            state.skin = HARNEDOR_WARRIOR_SKINS.getRandomSkin(haradrim.getUUID());
        } else if (haradrim instanceof LOTRHarnedhrimEntity || haradrim instanceof LOTRGulfHaradrimEntity
                || haradrim instanceof LOTRCorsairEntity) {
            state.skin = (haradrim.familyInfo.isMale() ? HARNEDOR_MALE : HARNEDOR_FEMALE).getRandomSkin(haradrim.getUUID());
        } else if (haradrim instanceof LOTRNomadEntity) {
            state.skin = (haradrim.familyInfo.isMale() ? NOMAD_MALE : NOMAD_FEMALE).getRandomSkin(haradrim.getUUID());
        } else if (haradrim instanceof LOTRNearHaradrimWarriorEntity || haradrim instanceof LOTRUmbarWarriorEntity) {
            state.skin = WARRIOR_SKINS.getRandomSkin(haradrim.getUUID());
        } else {
            state.skin = (haradrim.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE).getRandomSkin(haradrim.getUUID());
        }
        state.hat = haradrim instanceof LOTRNomadEntity && haradrim.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && LOTRRandomSkins.nextInt(haradrim.getUUID(), 2) == 0
                ? NOMAD_HATS.getRandomSkin(haradrim.getUUID()) : null;
        boolean chestEmpty = haradrim.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        if (this.traderOutfit != null) {
            state.outfit = chestEmpty ? this.traderOutfit : null;
        } else if ((haradrim instanceof LOTRHarnedhrimEntity || haradrim instanceof LOTRGulfHaradrimEntity) && chestEmpty
                && LOTRRandomSkins.nextInt(haradrim.getUUID(), 2) == 0) {
            state.outfit = HARNEDOR_OUTFITS.getRandomSkin(haradrim.getUUID());
        } else {
            state.outfit = null;
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
