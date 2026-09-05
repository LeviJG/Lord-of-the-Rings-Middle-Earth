package lotr.common.block;

import net.minecraft.block.Block;

public class LOTRBlockBirdCageWood extends LOTRBlockBirdCage {
	public LOTRBlockBirdCageWood() {
		setStepSound(Block.soundTypeWood);
		setCageTypes("wood");
	}
}
