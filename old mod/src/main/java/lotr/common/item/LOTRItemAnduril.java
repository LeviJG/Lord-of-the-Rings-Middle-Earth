package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.Item;

public class LOTRItemAnduril extends LOTRItemSword implements LOTRStoryItem {
	public LOTRItemAnduril() {
		super(Item.ToolMaterial.IRON);
		setMaxDamage(1500);
		lotrWeaponDamage = 9.0f;
		setCreativeTab(LOTRCreativeTabs.tabStory);
	}
}
