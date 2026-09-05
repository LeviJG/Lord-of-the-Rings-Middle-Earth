package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.World;

public class LOTREntityMordorSpider extends LOTREntitySpiderBase {
	public LOTREntityMordorSpider(World world) {
		super(world);
	}

	@Override
	public float getAlignmentBonus() {
		return 1.0f;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.MORDOR;
	}

	@Override
	public LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killMordorSpider;
	}

	@Override
	public int getRandomSpiderScale() {
		return 1 + rand.nextInt(3);
	}

	@Override
	public int getRandomSpiderType() {
		return VENOM_POISON;
	}

	@Override
	public IEntityLivingData initCreatureForHire(IEntityLivingData data) {
		return super.onSpawnWithEgg(data);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		if (!worldObj.isRemote && rand.nextInt(3) == 0) {
			LOTREntityMordorOrc rider = rand.nextBoolean() ? new LOTREntityMordorOrcArcher(worldObj) : new LOTREntityMordorOrc(worldObj);
			rider.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0f);
			((LOTREntityNPC) rider).onSpawnWithEgg(null);
			rider.isNPCPersistent = isNPCPersistent;
			worldObj.spawnEntityInWorld(rider);
			rider.mountEntity(this);
		}
		return data;
	}
}
