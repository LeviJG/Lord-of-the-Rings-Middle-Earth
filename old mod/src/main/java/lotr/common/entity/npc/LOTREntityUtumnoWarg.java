package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LOTREntityUtumnoWarg extends LOTREntityWarg {
	public LOTREntityUtumnoWarg(World world) {
		super(world);
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
	}

	@Override
	public boolean canWargBeRidden() {
		return false;
	}

	@Override
	public LOTREntityNPC createWargRider() {
		return worldObj.rand.nextBoolean() ? new LOTREntityUtumnoOrcArcher(worldObj) : new LOTREntityUtumnoOrc(worldObj);
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.UTUMNO;
	}

	@Override
	public LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killUtumnoWarg;
	}

	@Override
	public EntityAIBase getWargAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.7, true);
	}
}
