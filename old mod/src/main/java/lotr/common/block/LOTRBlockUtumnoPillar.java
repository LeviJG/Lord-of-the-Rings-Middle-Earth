package lotr.common.block;

import lotr.common.world.LOTRWorldProviderUtumno;
import net.minecraft.block.Block;

public class LOTRBlockUtumnoPillar extends LOTRBlockPillarBase implements LOTRWorldProviderUtumno.UtumnoBlock {
	public LOTRBlockUtumnoPillar() {
		setPillarNames("fire", "ice", "obsidian");
		setHardness(1.5f);
		setResistance(Float.MAX_VALUE);
		setStepSound(Block.soundTypeStone);
	}
}
