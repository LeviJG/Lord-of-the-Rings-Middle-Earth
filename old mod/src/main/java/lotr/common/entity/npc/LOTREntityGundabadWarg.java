package lotr.common.entity.npc;

import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LOTREntityGundabadWarg extends LOTREntityWarg {
	public LOTREntityGundabadWarg(World world) {
		super(world);
	}

	@Override
	public LOTREntityNPC createWargRider() {
		return worldObj.rand.nextBoolean() ? new LOTREntityGundabadOrcArcher(worldObj) : new LOTREntityGundabadOrc(worldObj);
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.GUNDABAD;
	}

	@Override
	public EntityAIBase getWargAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.75, false);
	}
}
