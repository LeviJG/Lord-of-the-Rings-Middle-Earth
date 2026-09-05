package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class LOTRBlockKebab extends Block {
	public LOTRBlockKebab() {
		super(Material.sand);
		setCreativeTab(LOTRCreativeTabs.tabFood);
		setHardness(0.5f);
		setStepSound(Block.soundTypeWood);
	}
}
