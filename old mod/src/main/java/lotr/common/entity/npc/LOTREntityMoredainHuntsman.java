package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityMoredainHuntsman extends LOTREntityMoredainVillageTrader {
	public LOTREntityMoredainHuntsman(World world) {
		super(world);
	}

	@Override
	public LOTRTradeEntries getBuyPool() {
		return LOTRTradeEntries.MOREDAIN_HUNTSMAN_BUY;
	}

	@Override
	public LOTRTradeEntries getSellPool() {
		return LOTRTradeEntries.MOREDAIN_HUNTSMAN_SELL;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearMoredain));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		npcItemsInv.setSpearBackup(null);
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsMoredain));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsMoredain));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyMoredain));
		return data;
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}
}
