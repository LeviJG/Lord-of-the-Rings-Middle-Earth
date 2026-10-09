package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRPlayerShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderPlayer.preRenderSpecials and LOTRRenderBiped.renderNPCShield: the shield a player has
 * chosen, or an NPC bears, worn on the back -- or on the left arm, turned out, while holding a
 * weapon or tool (but not drawing a bow). An NPC with something in its left hand wears it on its back,
 * and one wearing a cape as well wears none. A player carrying a vanilla shield bears the design on
 * that instead (LOTRShieldItemDesign), so none is worn. A player one can see though they are
 * invisible wears it faintly.
 */
public class LOTRShieldLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {

    public LOTRShieldLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, S state, float yRot, float xRot) {
        Entity entity = Minecraft.getInstance().level == null ? null
                : Minecraft.getInstance().level.getEntity(((LOTROverheadHolder) state).lotr$getEntityId());
        if (!(entity instanceof LivingEntity living)) {
            return;
        }
        LOTRShields shield = shieldOf(living);
        if (shield == null) {
            return;
        }
        if (living instanceof Player && (living.getMainHandItem().is(Items.SHIELD) || living.getOffhandItem().is(Items.SHIELD))) {
            return;
        }
        boolean heldLeft = living instanceof LOTRNPCEntity && !living.getOffhandItem().isEmpty();
        if (heldLeft && ((LOTRNPCEntity) living).npcCape != null) {
            return;
        }
        int alpha = 255;
        if (state.isInvisible) {
            if (state.isInvisibleToPlayer) {
                return;
            }
            alpha = 38;
        }
        ItemStack held = living.getMainHandItem();
        ItemStack inUse = living.isUsingItem() ? living.getUseItem() : ItemStack.EMPTY;
        boolean holdingWeapon = (held.has(DataComponents.TOOL) || held.has(DataComponents.WEAPON))
                && (inUse.isEmpty() || inUse.getUseAnimation() != ItemUseAnimation.BOW);
        Equippable chest = living.getItemBySlot(EquipmentSlot.CHEST).get(DataComponents.EQUIPPABLE);
        boolean wearingChestplate = chest != null && chest.slot() == EquipmentSlot.CHEST;
        LOTRShieldRenderer.submit(shield, poseStack, collector, light, getParentModel().body, getParentModel().leftArm,
                !holdingWeapon || heldLeft, wearingChestplate, alpha);
    }

    public static @Nullable LOTRShields shieldOf(LivingEntity entity) {
        if (entity instanceof Player player) {
            return LOTRPlayerShields.getShield(player);
        }
        return entity instanceof LOTRNPCEntity npc ? npc.npcShield : null;
    }
}
