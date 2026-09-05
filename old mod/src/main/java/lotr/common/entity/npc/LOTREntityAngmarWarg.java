package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.fac.LOTRFaction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityAngmarWarg extends LOTREntityWarg {
	public LOTREntityAngmarWarg(World world) {
		super(world);
	}

	@Override
	public LOTREntityNPC createWargRider() {
		if (rand.nextBoolean()) {
			setWargArmor(new ItemStack(LOTRMod.wargArmorAngmar));
		}
		return worldObj.rand.nextBoolean() ? new LOTREntityAngmarOrcArcher(worldObj) : new LOTREntityAngmarOrc(worldObj);
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.ANGMAR;
	}
}
