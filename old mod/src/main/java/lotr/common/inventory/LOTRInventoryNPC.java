package lotr.common.inventory;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.item.ItemStack;

public class LOTRInventoryNPC extends LOTREntityInventory {
	public LOTREntityNPC theNPC;

	public LOTRInventoryNPC(String s, LOTREntityNPC npc, int i) {
		super(s, npc, i);
		theNPC = npc;
	}

	@Override
	public void dropItem(ItemStack itemstack) {
		theNPC.npcDropItem(itemstack, 0.0f, false, true);
	}
}
