package lotr.common.block;

import net.minecraft.creativetab.CreativeTabs;

public class LOTRBlockRedBrick extends LOTRBlockBrickBase {
	public LOTRBlockRedBrick() {
		setBrickNames("mossy", "cracked");
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(2.0f);
		setResistance(10.0f);
	}
}
