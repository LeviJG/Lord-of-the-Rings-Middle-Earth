package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRHalfTrollModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll.LOTRHalfTrollEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.EnumMap;
import java.util.Map;

/**
 * LOTRRenderHalfTroll: a half-troll in one of the half-troll skins, its
 * armour drawn on its own shape. The mod's armour takes the half-troll's
 * textures, {@code entity/half_troll/<armour>_1.png} (_2 for leggings), as
 * LOTRItemArmor.getArmorTexture chose {@code mob/halfTroll/} for a half-troll
 * wearer -- only half-troll armour has them; anything else of the mod's shows
 * the missing texture, as it did. Vanilla armour keeps its own textures.
 * LOTRRenderHalfTrollScavenger puts the scavenger's outfit on in the helmet's
 * pass, in place of any helmet.
 */
public class LOTRHalfTrollRenderer extends LOTRBipedRenderer<LOTRHalfTrollEntity, LOTRHalfTrollModel.State,
        LOTRHalfTrollModel<LOTRHalfTrollModel.State>> {

    private static final LOTRRandomSkins SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/halfTroll/halfTroll", "half_troll/half_troll");
    private static final Identifier SCAVENGER_OUTFIT = texture("scavenger");

    private final boolean scavenger;

    public LOTRHalfTrollRenderer(EntityRendererProvider.Context context) {
        this(context, false);
    }

    /** LOTRRenderHalfTrollScavenger. */
    public static LOTRHalfTrollRenderer scavenger(EntityRendererProvider.Context context) {
        return new LOTRHalfTrollRenderer(context, true);
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/half_troll/" + name + ".png");
    }

    private LOTRHalfTrollRenderer(EntityRendererProvider.Context context, boolean scavenger) {
        this(context, new LOTRHalfTrollModel<>(LOTRHalfTrollModel.createBodyLayer().bakeRoot()), scavenger);
    }

    private LOTRHalfTrollRenderer(EntityRendererProvider.Context context, LOTRHalfTrollModel<LOTRHalfTrollModel.State> model,
                                  boolean scavenger) {
        super(context, model, model, 0.5f);
        this.scavenger = scavenger;
        Map<EquipmentSlot, LOTRHalfTrollModel<LOTRHalfTrollModel.State>> armor = new EnumMap<>(EquipmentSlot.class);
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            armor.put(slot, new LOTRHalfTrollModel<>(LOTRHalfTrollModel.createArmorLayer(slot).bakeRoot(), slot));
        }
        EquipmentLayerRenderer equipment = context.getEquipmentRenderer();
        LOTRHalfTrollModel<LOTRHalfTrollModel.State> outfit =
                new LOTRHalfTrollModel<>(LOTRHalfTrollModel.createOutfitLayer().bakeRoot());
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTRHalfTrollModel.State state,
                               float yRot, float xRot) {
                if (!LOTRHalfTrollRenderer.this.scavenger) {
                    submitArmor(poseStack, collector, light, state, state.headEquipment, EquipmentSlot.HEAD);
                } else {
                    coloredCutoutModelCopyLayerRender(outfit, SCAVENGER_OUTFIT, poseStack, collector, light, state, -1, 1);
                }
                submitArmor(poseStack, collector, light, state, state.chestEquipment, EquipmentSlot.CHEST);
                submitArmor(poseStack, collector, light, state, state.legsEquipment, EquipmentSlot.LEGS);
                submitArmor(poseStack, collector, light, state, state.feetEquipment, EquipmentSlot.FEET);
            }

            private void submitArmor(PoseStack poseStack, SubmitNodeCollector collector, int light,
                                     LOTRHalfTrollModel.State state, ItemStack stack, EquipmentSlot slot) {
                Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
                if (equippable == null || !HumanoidArmorLayer.shouldRender(stack, slot) || equippable.assetId().isEmpty()) {
                    return;
                }
                LOTRHalfTrollModel<LOTRHalfTrollModel.State> armorModel = armor.get(slot);
                Identifier asset = equippable.assetId().get().identifier();
                if (asset.getNamespace().equals(LOTRMod.NAMESPACE)) {
                    Identifier texture = texture(asset.getPath() + (slot == EquipmentSlot.LEGS ? "_2" : "_1"));
                    collector.order(1).submitModel(armorModel, state, poseStack, RenderTypes.armorCutoutNoCull(texture),
                            light, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
                    if (stack.hasFoil()) {
                        collector.order(2).submitModel(armorModel, state, poseStack, RenderTypes.armorEntityGlint(),
                                light, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
                    }
                } else {
                    equipment.renderLayers(slot == EquipmentSlot.LEGS
                                    ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID,
                            equippable.assetId().get(), armorModel, state, stack, poseStack, collector, light,
                            state.outlineColor);
                }
            }
        });
    }

    @Override
    public LOTRHalfTrollModel.State createRenderState() {
        return new LOTRHalfTrollModel.State();
    }

    @Override
    public void extractRenderState(LOTRHalfTrollEntity halfTroll, LOTRHalfTrollModel.State state, float partialTick) {
        super.extractRenderState(halfTroll, state, partialTick);
        state.skin = SKINS.getRandomSkin(halfTroll.getUUID());
        state.mohawk = halfTroll.hasMohawk();
        state.horns = halfTroll.hasHorns();
        state.fullHorns = halfTroll.hasFullHorns();
    }

    @Override
    public Identifier getTextureLocation(LOTRHalfTrollModel.State state) {
        return state.skin;
    }
}
