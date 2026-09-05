package lotr.common.entity.animal;

import lotr.common.item.LOTRItemLionRug;
import net.minecraft.world.World;

public class LOTREntityLioness extends LOTREntityLionBase {
	public LOTREntityLioness(World world) {
		super(world);
	}

	@Override
	public LOTRItemLionRug.LionRugType getLionRugType() {
		return LOTRItemLionRug.LionRugType.LIONESS;
	}

	@Override
	public boolean isMale() {
		return false;
	}
}
