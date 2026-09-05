package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;

public class LOTRBlockSand extends BlockFalling {
	public LOTRBlockSand() {
		super(Material.sand);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(0.5f);
		setStepSound(soundTypeSand);
	}
}
