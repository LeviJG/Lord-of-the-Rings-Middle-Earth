package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class LOTRBlockBone extends Block {
	public LOTRBlockBone() {
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(1.0f);
		setResistance(5.0f);
		setStepSound(Block.soundTypeStone);
	}
}
