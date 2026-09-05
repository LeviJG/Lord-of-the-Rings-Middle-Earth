package lotr.common.block;

import net.minecraft.world.World;

public class LOTRBlockReedDry extends LOTRBlockReed {
	@Override
	public boolean canReedGrow(World world, int i, int j, int k) {
		return false;
	}
}
