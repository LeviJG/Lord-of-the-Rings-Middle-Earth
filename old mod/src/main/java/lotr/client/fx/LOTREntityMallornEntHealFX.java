package lotr.client.fx;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.world.World;

public class LOTREntityMallornEntHealFX extends EntityDiggingFX {
	public LOTREntityMallornEntHealFX(World world, double d, double d1, double d2, double d3, double d4, double d5, Block block, int meta, int color) {
		super(world, d, d1, d2, d3, d4, d5, block, meta);
		particleRed *= (color >> 16 & 0xFF) / 255.0f;
		particleGreen *= (color >> 8 & 0xFF) / 255.0f;
		particleBlue *= (color & 0xFF) / 255.0f;
		particleScale *= 2.0f;
		particleMaxAge = 30;
		motionX = d3;
		motionY = d4;
		motionZ = d5;
		particleGravity = 0.0f;
		renderDistanceWeight = 10.0;
		noClip = true;
	}
}
