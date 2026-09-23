package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.AxeItem;

/** LOTRItemAxe: vanilla's axe -- it strips logs and scrapes copper -- carrying LOTR modifiers. */
public class LOTRAxeItem extends AxeItem implements LOTRModifiable {

    public LOTRAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity holder, EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }
}
