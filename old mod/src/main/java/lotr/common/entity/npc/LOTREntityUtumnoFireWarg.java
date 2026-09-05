package lotr.common.entity.npc;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTREntityUtumnoFireWarg extends LOTREntityUtumnoWarg {
	public LOTREntityUtumnoFireWarg(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		boolean flag = super.attackEntityAsMob(entity);
		if (!worldObj.isRemote && flag) {
			entity.setFire(4);
		}
		return flag;
	}

	@Override
	public void entityInit() {
		super.entityInit();
		setWargType(LOTREntityWarg.WargType.FIRE);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		String s = rand.nextInt(3) > 0 ? "flame" : "smoke";
		worldObj.spawnParticle(s, posX + (rand.nextDouble() - 0.5) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5) * width, 0.0, 0.0, 0.0);
	}
}
