package lotr.client.fx;

import net.minecraft.world.World;

public class LOTREntityPickpocketFailFX extends LOTREntityPickpocketFX {
	public LOTREntityPickpocketFailFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		setParticleTextureIndex(176 + rand.nextInt(6));
		particleGravity = 0.6f;
		bounciness = 0.5f;
	}

	@Override
	public void updatePickpocketIcon() {
	}
}
