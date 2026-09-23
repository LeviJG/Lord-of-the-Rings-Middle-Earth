package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.AxeItem;

/** LOTRItemAxe: vanilla's axe -- it strips logs and scrapes copper -- carrying LOTR modifiers. */
public class LOTRAxeItem extends AxeItem {

    public LOTRAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }

}
