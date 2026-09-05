package lotr.common.entity.animal;

import net.minecraft.world.World;

public class LOTREntityJungleScorpion extends LOTREntityScorpion {
	public LOTREntityJungleScorpion(World world) {
		super(world);
	}

	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && (spawningFromSpawner || posY < 60.0 || rand.nextInt(100) == 0);
	}
}
