package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.npc.LOTREntitySpiderBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRBlockQuagmire extends Block {
	public LOTRBlockQuagmire() {
		super(Material.ground);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		if (entity instanceof LOTREntitySpiderBase) {
			((LOTREntitySpiderBase) entity).setInQuag();
		} else {
			entity.setInWeb();
		}
	}
}
