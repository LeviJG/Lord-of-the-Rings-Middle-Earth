package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRArnorHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBlackNumenoreanHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBlackUrukHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRDyedHeadModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSwanChestplateModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBodyArmorModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRDorwinionElvenHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTREasterlingHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGulfenChestplateModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHarnennorChestplateModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHarnennorHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSouthronChampionHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRUmbaricHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGaladhrimHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGemsbokHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGoldenTaurethrimHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGondolinHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGundabadUrukHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRRohirricMarshalHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTaurethrimChieftainHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGondorHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRLindonHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGondorWingedHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMorgulHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMorwaithChieftainHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSwanHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRUrukHelmetModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRArmorModels: the helmets that are not the standard armour biped.
 *
 * <p>1.7.10 kept a map from item to a custom ModelBiped and rendered that
 * instead of the default for those items. 26.2 has no such hook of its own --
 * the humanoid equipment layer draws one fixed armour model for every helmet --
 * so this uses Fabric's ArmorRenderer, which is the equivalent seam: register a
 * renderer against an item and it takes over that item's armour rendering.
 *
 * <p>Twenty-six so far, two of them chestplates. The original has twenty-two of these
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
        register(LOTRCombatItems.GONDOR_HELMET, "gondor_helmet",
                (poseStack, consumer, light) ->
                        gondor.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGaladhrimHelmetModel galadhrim =
                new LOTRGaladhrimHelmetModel(LOTRGaladhrimHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.GALADHRIM_HELMET, "galadhrim_helmet",
                (poseStack, consumer, light) ->
                        galadhrim.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRUrukHelmetModel uruk =
                new LOTRUrukHelmetModel(LOTRUrukHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.URUK_HELMET, "uruk_helmet",
                (poseStack, consumer, light) ->
                        uruk.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));
        // The berserker's helmet is the Uruk model on a sheet of its own --
        // LOTRArmorModels mapped both to a new LOTRModelUrukHelmet(1.0f).
        register(LOTRCombatItems.URUK_BERSERKER_HELMET, "uruk_berserker_helmet",
                (poseStack, consumer, light) ->
                        uruk.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGondorWingedHelmetModel winged = new LOTRGondorWingedHelmetModel(
                LOTRGondorWingedHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.GONDOR_WINGED_HELMET, "gondor_winged_helmet",
                (poseStack, consumer, light) ->
                        winged.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRMorgulHelmetModel morgul =
                new LOTRMorgulHelmetModel(LOTRMorgulHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.MORGUL_HELMET, "morgul_helmet",
                (poseStack, consumer, light) ->
                        morgul.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        // These two wear the SET's sheet rather than one of their own -- the
        // original gives them no extraName, so getArmorName returned gemsbok_1
        // and high_elven_1, the same layer the chestplate uses. The horns and
        // the crest are painted in the corner of it that no standard armour
        // model samples.
        LOTRGemsbokHelmetModel gemsbok =
                new LOTRGemsbokHelmetModel(LOTRGemsbokHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.GEMSBOK_HIDE_HELMET, "gemsbok_hide",
                (poseStack, consumer, light) ->
                        gemsbok.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRLindonHelmetModel lindon =
                new LOTRLindonHelmetModel(LOTRLindonHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.LINDON_HELMET, "lindon",
                (poseStack, consumer, light) ->
                        lindon.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRSwanHelmetModel swan =
                new LOTRSwanHelmetModel(LOTRSwanHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.DOL_AMROTH_HELMET, "dol_amroth_winged_helmet",
                (poseStack, consumer, light) ->
                        swan.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRMorwaithChieftainHelmetModel lion = new LOTRMorwaithChieftainHelmetModel(
                LOTRMorwaithChieftainHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.MORWAITH_CHIEFTAIN_HELMET, "morwaith_chieftain_helmet",
                (poseStack, consumer, light) ->
                        lion.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGondolinHelmetModel gondolin =
                new LOTRGondolinHelmetModel(LOTRGondolinHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.GONDOLIN_HELMET, "gondolin_helmet",
                (poseStack, consumer, light) ->
                        gondolin.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRTaurethrimChieftainHelmetModel taurethrim = new LOTRTaurethrimChieftainHelmetModel(
                LOTRTaurethrimChieftainHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.TAURETHRIM_CHIEFTAIN_HELMET, "taurethrim_chieftain_helmet",
                (poseStack, consumer, light) ->
                        taurethrim.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRRohirricMarshalHelmetModel marshal = new LOTRRohirricMarshalHelmetModel(
                LOTRRohirricMarshalHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.ROHIRRIC_MARSHAL_HELMET, "rohirric_marshal_helmet",
                (poseStack, consumer, light) ->
                        marshal.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGoldenTaurethrimHelmetModel golden = new LOTRGoldenTaurethrimHelmetModel(
                LOTRGoldenTaurethrimHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.GOLDEN_TAURETHRIM_HELMET, "golden_taurethrim_helmet",
                (poseStack, consumer, light) ->
                        golden.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRGundabadUrukHelmetModel gundabad = new LOTRGundabadUrukHelmetModel(
                LOTRGundabadUrukHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.GUNDABAD_URUK_HELMET, "gundabad_uruk_helmet",
                (poseStack, consumer, light) ->
                        gundabad.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRDorwinionElvenHelmetModel dorwinion = new LOTRDorwinionElvenHelmetModel(
                LOTRDorwinionElvenHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.DORWINION_ELVEN_HELMET, "dorwinion_elven_helmet",
                (poseStack, consumer, light) ->
                        dorwinion.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRSouthronChampionHelmetModel champion = new LOTRSouthronChampionHelmetModel(
                LOTRSouthronChampionHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.SOUTHRON_CHAMPION_HELMET, "coast_southron_champion_helmet",
                (poseStack, consumer, light) ->
                        champion.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRArnorHelmetModel arnor = new LOTRArnorHelmetModel(LOTRArnorHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.ARNOR_HELMET, "arnor_helmet",
                (poseStack, consumer, light) ->
                        arnor.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        // new LOTRModelEasterlingHelmet(1.0f, false) and (1.0f, true): the same
        // helmet, the warlord's with kine horns on a 64x64 sheet.
        LOTREasterlingHelmetModel goldenRhunic = new LOTREasterlingHelmetModel(
                LOTREasterlingHelmetModel.createLayer(false).bakeRoot());
        register(LOTRCombatItems.GOLDEN_RHUNIC_HELMET, "rhun_gold_helmet",
                (poseStack, consumer, light) ->
                        goldenRhunic.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));
        LOTREasterlingHelmetModel warlord = new LOTREasterlingHelmetModel(
                LOTREasterlingHelmetModel.createLayer(true).bakeRoot());
        register(LOTRCombatItems.RHUNIC_WARLORD_HELMET, "rhun_gold_warlord_helmet",
                (poseStack, consumer, light) ->
                        warlord.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        // helmetRivendell: LOTRModelHighElvenHelmet, which the port has as the
        // Lindon helmet's model, on the Rivendell set's own sheet (no extraName).
        LOTRLindonHelmetModel rivendell =
                new LOTRLindonHelmetModel(LOTRLindonHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.RIVENDELL_HELMET, "rivendell",
                (poseStack, consumer, light) ->
                        rivendell.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRUmbaricHelmetModel umbaric = new LOTRUmbaricHelmetModel(LOTRUmbaricHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.UMBARIC_HELMET, "umbaric_helmet",
                (poseStack, consumer, light) ->
                        umbaric.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRHarnennorHelmetModel harnennor = new LOTRHarnennorHelmetModel(
                LOTRHarnennorHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.HARNENNOR_HELMET, "harnedor_helmet",
                (poseStack, consumer, light) ->
                        harnennor.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRBlackNumenoreanHelmetModel numenorean = new LOTRBlackNumenoreanHelmetModel(
                LOTRBlackNumenoreanHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.BLACK_NUMENOREAN_HELMET, "black_numenorean_helmet",
                (poseStack, consumer, light) ->
                        numenorean.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        LOTRBlackUrukHelmetModel blackUruk = new LOTRBlackUrukHelmetModel(
                LOTRBlackUrukHelmetModel.createLayer().bakeRoot());
        register(LOTRCombatItems.BLACK_URUK_HELMET, "black_uruk",
                (poseStack, consumer, light) ->
                        blackUruk.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY));

        // The dyed head pieces, tinted whole as the original's glColor3f did,
        // in the colour each item had undyed: LOTRItemLeatherHat 0x684A36 (with
        // its feather, below), LOTRItemPartyHat and ROBES_WHITE 0xFFFFFF.
        registerLeatherHat(new LOTRDyedHeadModel(LOTRDyedHeadModel.createLeatherHat().bakeRoot()));
        registerDyed(LOTRMiscItems.PARTY_HAT, "party_hat",
                new LOTRDyedHeadModel(LOTRDyedHeadModel.createPartyHat().bakeRoot()), 0xFFFFFF);
        registerDyed(LOTRMiscItems.HARAD_TURBAN, "harad_turban",
                new LOTRDyedHeadModel(LOTRDyedHeadModel.createTurban().bakeRoot()), 0xFFFFFF);

        // bodyDolAmroth: LOTRModelSwanChestplate, on its "wingedBody" sheet.
        LOTRSwanChestplateModel swanBody = new LOTRSwanChestplateModel(
                LOTRSwanChestplateModel.createLayer().bakeRoot());
        Identifier swanTexture = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "textures/entity/equipment/humanoid/dol_amroth_winged_body.png");
        ArmorRenderer.register((poseStack, collector, stack, state, slot, light, contextModel) -> {
            swanBody.setupWings(state.ageInTicks, state.walkAnimationPos, state.walkAnimationSpeed);
            renderPart(poseStack, collector, light, contextModel.body, swanBody.body(), swanTexture);
            renderPart(poseStack, collector, light, contextModel.rightArm, swanBody.rightArm(), swanTexture);
            renderPart(poseStack, collector, light, contextModel.leftArm, swanBody.leftArm(), swanTexture);
        }, LOTRCombatItems.DOL_AMROTH_CHESTPLATE);

        // The two chestplates LOTRArmorModels gave models of their own.
        registerBody(LOTRCombatItems.GULFEN_CHESTPLATE, "gulf_harad_body",
                new LOTRGulfenChestplateModel(LOTRGulfenChestplateModel.createLayer().bakeRoot()));
        registerBody(LOTRCombatItems.HARNENNOR_CHESTPLATE, "harnedor_body",
                new LOTRHarnennorChestplateModel(LOTRHarnennorChestplateModel.createLayer().bakeRoot()));
    }

    /**
     * A chestplate model: its body and arms drawn inside the wearer's own body
     * and arm transforms, as ModelBiped drew bipedBody and the arms at their
     * rotation points. A hidden wearer part (a sleeve out of view) is skipped.
     */
    private static void registerBody(net.minecraft.world.item.Item chestplate, String texture,
            LOTRBodyArmorModel model) {
        Identifier path = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "textures/entity/equipment/humanoid/" + texture + ".png");
        ArmorRenderer.register((poseStack, collector, stack, state, slot, light, contextModel) -> {
            renderPart(poseStack, collector, light, contextModel.body, model.body(), path);
            renderPart(poseStack, collector, light, contextModel.rightArm, model.rightArm(), path);
            renderPart(poseStack, collector, light, contextModel.leftArm, model.leftArm(), path);
        }, chestplate);
    }

    private static void renderPart(PoseStack poseStack,
            net.minecraft.client.renderer.SubmitNodeCollector collector, int light,
            ModelPart wearer, ModelPart part, Identifier texture) {
        if (!wearer.visible) {
            return;
        }
        poseStack.pushPose();
        wearer.translateAndRotate(poseStack);
        collector.submitCustomGeometry(poseStack, RenderTypes.armorCutoutNoCull(texture),
                (pose, consumer) -> {
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    part.render(local, consumer, light, OverlayTexture.NO_OVERLAY);
                });
        poseStack.popPose();
    }

    /** A dyed head piece: the stack's dye, or the item's own undyed colour. */
    private static void registerDyed(net.minecraft.world.item.Item hat, String texture,
            LOTRDyedHeadModel model, int undyed) {
        Identifier path = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "textures/entity/equipment/humanoid/" + texture + ".png");
        ArmorRenderer.register((poseStack, collector, stack, state, slot, light, contextModel) -> {
            net.minecraft.world.item.component.DyedItemColor dyed =
                    stack.get(net.minecraft.core.component.DataComponents.DYED_COLOR);
            int color = 0xFF000000 | (dyed != null ? dyed.rgb() : undyed);
            renderHead(poseStack, collector, light, contextModel,
                    (local, consumer, l) -> model.render(local, consumer, l, OverlayTexture.NO_OVERLAY, color),
                    path);
        }, hat);
    }

    /**
     * LOTRModelLeatherHat: the dyed hat, and in its band -- if it has one -- a
     * feather drawn as the feather item in the feather's colour, placed where
     * the original put it after bipedHead.postRender.
     */
    private static void registerLeatherHat(LOTRDyedHeadModel model) {
        Identifier path = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "textures/entity/equipment/humanoid/leather_hat.png");
        ArmorRenderer.register((poseStack, collector, stack, state, slot, light, contextModel) -> {
            net.minecraft.world.item.component.DyedItemColor dyed =
                    stack.get(net.minecraft.core.component.DataComponents.DYED_COLOR);
            int color = 0xFF000000 | (dyed != null ? dyed.rgb() : LOTRLeatherHatItem.HAT_LEATHER);
            renderHead(poseStack, collector, light, contextModel,
                    (local, consumer, l) -> model.render(local, consumer, l, OverlayTexture.NO_OVERLAY, color),
                    path);

            Integer feather = stack.get(LOTRDataComponents.HAT_FEATHER);
            if (feather == null) {
                return;
            }
            poseStack.pushPose();
            contextModel.head.translateAndRotate(poseStack);
            poseStack.scale(0.375f, 0.375f, 0.375f);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(130.0f));
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(30.0f));
            poseStack.translate(0.25f, 1.5f, 0.75f);
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-45.0f));
            // 1.7.10 drew an item from its corner, one pixel deep behind it; a
            // 26.2 item model is centred on the origin.
            poseStack.translate(0.5f, 0.5f, -1.0f / 32.0f);
            ItemStack featherStack = new ItemStack(LOTRItems.FEATHER_DYED);
            featherStack.set(net.minecraft.core.component.DataComponents.DYED_COLOR,
                    new net.minecraft.world.item.component.DyedItemColor(feather));
            ItemStackRenderState item = new ItemStackRenderState();
            Minecraft.getInstance().getItemModelResolver().updateForTopItem(
                    item, featherStack, ItemDisplayContext.NONE, null, null, 0);
            item.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }, LOTRMiscItems.LEATHER_HAT);
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
