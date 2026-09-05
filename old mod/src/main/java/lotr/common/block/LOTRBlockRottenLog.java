package lotr.common.block;

import net.minecraft.block.Block;

public class LOTRBlockRottenLog extends LOTRBlockWoodBase {
	public LOTRBlockRottenLog() {
		setWoodNames("rotten");
	}

	public static boolean isRottenWood(Block block) {
		return block instanceof LOTRBlockRottenLog;
	}
}
