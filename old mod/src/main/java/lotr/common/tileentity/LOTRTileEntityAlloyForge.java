package lotr.common.tileentity;

import lotr.common.LOTRMod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTRTileEntityAlloyForge extends LOTRTileEntityAlloyForgeBase {
	@Override
	public ItemStack getAlloySmeltingResult(ItemStack itemstack, ItemStack alloyItem) {
		if (isIron(itemstack) && isGoldNugget(alloyItem)) {
			return new ItemStack(LOTRMod.gildedIron);
		}
		return super.getAlloySmeltingResult(itemstack, alloyItem);
	}

	@Override
	public String getForgeName() {
		return StatCollector.translateToLocal("container.lotr.alloyForge");
	}

	@Override
	public ItemStack getSmeltingResult(ItemStack itemstack) {
		return super.getSmeltingResult(itemstack);
	}
}
