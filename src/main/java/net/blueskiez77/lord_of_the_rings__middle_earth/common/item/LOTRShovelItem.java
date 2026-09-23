package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.ShovelItem;

/** LOTRItemShovel: vanilla's shovel -- it flattens paths and douses campfires -- carrying LOTR modifiers. */
public class LOTRShovelItem extends ShovelItem implements LOTRModifiable {

    public LOTRShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity holder, EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }
}
