package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.enchant.LOTREnchantment;
import net.minecraft.item.Item;

public class LOTRItemEnchantment extends Item {
	public LOTREnchantment theEnchant;

	public LOTRItemEnchantment(LOTREnchantment ench) {
		setCreativeTab(LOTRCreativeTabs.tabMaterials);
		theEnchant = ench;
	}
}
