package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class LOTRBlockScorchedStone extends Block {
	public LOTRBlockScorchedStone() {
		super(Material.rock);
		setHardness(2.0f);
		setResistance(10.0f);
		setStepSound(Block.soundTypeStone);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
}
