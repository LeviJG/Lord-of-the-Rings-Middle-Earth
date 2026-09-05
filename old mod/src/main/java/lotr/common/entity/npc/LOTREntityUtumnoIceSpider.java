package lotr.common.entity.npc;

import lotr.common.LOTRDamage;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class LOTREntityUtumnoIceSpider extends LOTREntitySpiderBase {
	public LOTREntityUtumnoIceSpider(World world) {
		super(world);
		isImmuneToFrost = true;
		isChilly = true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			if (entity instanceof EntityPlayerMP) {
				LOTRDamage.doFrostDamage((EntityPlayerMP) entity);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canRideSpider() {
		return false;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.UTUMNO;
	}

	@Override
	public int getRandomSpiderScale() {
		return rand.nextInt(4);
	}

	@Override
	public int getRandomSpiderType() {
		return VENOM_SLOWNESS;
	}
}
