package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.fac.LOTRFaction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUrukWarg extends LOTREntityWarg {
	public LOTREntityUrukWarg(World world) {
		super(world);
	}

	@Override
	public LOTREntityNPC createWargRider() {
		if (rand.nextBoolean()) {
			setWargArmor(new ItemStack(LOTRMod.wargArmorUruk));
		}
		return worldObj.rand.nextBoolean() ? new LOTREntityIsengardSnagaArcher(worldObj) : new LOTREntityIsengardSnaga(worldObj);
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.ISENGARD;
	}
}
