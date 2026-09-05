package lotr.common.item;

import lotr.common.LOTRCreativeTabs;

public class LOTRItemGlamdring extends LOTRItemSword implements LOTRStoryItem {
	public LOTRItemGlamdring() {
		super(LOTRMaterial.GONDOLIN);
		setCreativeTab(LOTRCreativeTabs.tabStory);
		setIsElvenBlade();
	}
}
