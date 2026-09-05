package lotr.common.item;

import lotr.common.LOTRCreativeTabs;

public class LOTRItemRingil extends LOTRItemSword implements LOTRStoryItem {
	public LOTRItemRingil() {
		super(LOTRMaterial.HIGH_ELVEN);
		setMaxDamage(1500);
		lotrWeaponDamage = 9.0f;
		setIsElvenBlade();
		setCreativeTab(LOTRCreativeTabs.tabStory);
	}
}
