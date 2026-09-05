package lotr.client.fx;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityUtumnoKillFX extends EntityFlameFX {
	public double paramMotionX;
	public double paramMotionY;
	public double paramMotionZ;

	public LOTREntityUtumnoKillFX(World world, double d, double d1, double d2, double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		paramMotionX = motionX = d3;
		paramMotionY = motionY = d4;
		paramMotionZ = motionZ = d5;
		setParticleTextureIndex(144 + rand.nextInt(3));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		motionX = paramMotionX;
		motionY = paramMotionY;
		motionZ = paramMotionZ;
		Block block = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
		if (block == LOTRMod.utumnoReturnPortalBase) {
			setDead();
		}
	}
}
