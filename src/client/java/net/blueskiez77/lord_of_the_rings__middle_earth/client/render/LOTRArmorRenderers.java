package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGaladhrimHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGemsbokHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGondolinHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRRohirricMarshalHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTaurethrimChieftainHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGondorHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRLindonHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGondorWingedHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMorgulHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMorwaithChieftainHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSwanHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRUrukHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRArmorModels: the helmets that are not the standard armour biped.
 *
 * <p>1.7.10 kept a map from item to a custom ModelBiped and rendered that
 * instead of the default for those items. 26.2 has no such hook of its own --
 * the humanoid equipment layer draws one fixed armour model for every helmet --
 * so this uses Fabric's ArmorRenderer, which is the equivalent seam: register a
 * renderer against an item and it takes over that item's armour rendering.
 *
 * <p>Twelve so far. The original has twenty-two of these
 * (Arnor, the swan helmet, the winged Dol Amroth one, the Gundabad and black
 * Uruk ones, and so on) and they all want the same treatment: a transcribed
 * model, a texture, and one more register() call below.
 */
public final class LOTRArmorRenderers {

    private LOTRArmorRenderers() {
    }

    public static void init() {
        LOTRGondorHelmetModel gondor =
                new LOTRGondorHelmetModel(LOTRGondorHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.GONDOR_HELMET, "gondor_helmet",
                (poseStack, consumer, light) ->
                        gondor.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGaladhrimHelmetModel galadhrim =
                new LOTRGaladhrimHelmetModel(LOTRGaladhrimHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.GALADHRIM_HELMET, "galadhrim_helmet",
                (poseStack, consumer, light) ->
                        galadhrim.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRUrukHelmetModel uruk =
                new LOTRUrukHelmetModel(LOTRUrukHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.URUK_HELMET, "uruk_helmet",
                (poseStack, consumer, light) ->
                        uruk.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGondorWingedHelmetModel winged = new LOTRGondorWingedHelmetModel(
                LOTRGondorWingedHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.GONDOR_WINGED_HELMET, "gondor_winged_helmet",
                (poseStack, consumer, light) ->
                        winged.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRMorgulHelmetModel morgul =
                new LOTRMorgulHelmetModel(LOTRMorgulHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.MORGUL_HELMET, "morgul_helmet",
                (poseStack, consumer, light) ->
                        morgul.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        // These two wear the SET's sheet rather than one of their own -- the
        // original gives them no extraName, so getArmorName returned gemsbok_1
        // and high_elven_1, the same layer the chestplate uses. The horns and
        // the crest are painted in the corner of it that no standard armour
        // model samples.
        LOTRGemsbokHelmetModel gemsbok =
                new LOTRGemsbokHelmetModel(LOTRGemsbokHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.GEMSBOK_HIDE_HELMET, "gemsbok_hide",
                (poseStack, consumer, light) ->
                        gemsbok.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRLindonHelmetModel lindon =
                new LOTRLindonHelmetModel(LOTRLindonHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.LINDON_HELMET, "lindon",
                (poseStack, consumer, light) ->
                        lindon.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRSwanHelmetModel swan =
                new LOTRSwanHelmetModel(LOTRSwanHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.DOL_AMROTH_HELMET, "dol_amroth_winged_helmet",
                (poseStack, consumer, light) ->
                        swan.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRMorwaithChieftainHelmetModel lion = new LOTRMorwaithChieftainHelmetModel(
                LOTRMorwaithChieftainHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.MORWAITH_CHIEFTAIN_HELMET, "morwaith_chieftain_helmet",
                (poseStack, consumer, light) ->
                        lion.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGondolinHelmetModel gondolin =
                new LOTRGondolinHelmetModel(LOTRGondolinHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.GONDOLIN_HELMET, "gondolin_helmet",
                (poseStack, consumer, light) ->
                        gondolin.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRTaurethrimChieftainHelmetModel taurethrim = new LOTRTaurethrimChieftainHelmetModel(
                LOTRTaurethrimChieftainHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.TAURETHRIM_CHIEFTAIN_HELMET, "taurethrim_chieftain_helmet",
                (poseStack, consumer, light) ->
                        taurethrim.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRRohirricMarshalHelmetModel marshal = new LOTRRohirricMarshalHelmetModel(
                LOTRRohirricMarshalHelmetModel.createLayer().bakeRoot());
        register(LOTRItems.ROHIRRIC_MARSHAL_HELMET, "rohirric_marshal_helmet",
                (poseStack, consumer, light) ->
                        marshal.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));
    }

    /** What a helmet model draws, once its head transform is in place. */
    private interface HeadModel {
        void render(PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer consumer, int light);
    }

    /** One line per helmet: the item, its worn texture, and the model to draw. */
    private static void register(net.minecraft.world.item.Item helmet, String texture, HeadModel model) {
        Identifier path = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "textures/entity/equipment/humanoid/" + texture + ".png");
        ArmorRenderer.register((poseStack, collector, stack, state, slot, light, contextModel) ->
                renderHead(poseStack, collector, light, contextModel, model, path), helmet);
    }

    /**
     * Draw a head-slot model where the wearer's head is.
     *
     * <p>The armour model's own head part carries the pose -- the wearer looking
     * around, a mob's head tilt -- so its transform is applied first and the
     * helmet is drawn inside it. Cutout with no culling, as armour layers are:
     * the crest fins are one block thick and would lose faces otherwise.
     */
    private static void renderHead(PoseStack poseStack,
            net.minecraft.client.renderer.SubmitNodeCollector collector, int light,
            HumanoidModel<HumanoidRenderState> contextModel, HeadModel model,
            Identifier texture) {
        poseStack.pushPose();
        ModelPart head = contextModel.head;
        head.translateAndRotate(poseStack);
        collector.submitCustomGeometry(poseStack, RenderTypes.armorCutoutNoCull(texture),
                (pose, consumer) -> {
                    // ModelPart.render wants a PoseStack and custom geometry
                    // hands over a single Pose; a local stack seeded with it is
                    // the cheapest way across, as the port's other models do.
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    model.render(local, consumer, light);
                });
        poseStack.popPose();
    }
}
