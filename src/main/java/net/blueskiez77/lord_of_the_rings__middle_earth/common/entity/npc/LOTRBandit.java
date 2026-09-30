package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryNPC;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * IBandit: an NPC that robs players (LOTREntityAIBanditSteal) into a small
 * inventory of its own and then runs (LOTREntityAIBanditFlee), dropping its
 * loot when slain.
 */
public interface LOTRBandit {

    boolean canTargetPlayerForTheft(Player player);

    LOTRNPCEntity getBanditAsNPC();

    LOTRInventoryNPC getBanditInventory();

    int getMaxThefts();

    Component getTheftChatMsg(Player player);

    String getTheftSpeechBank(Player player);

    /** Helper.canStealFromPlayerInv: anything in the main inventory outside the selected slot. */
    static boolean canStealFromPlayerInv(Player player) {
        Inventory inv = player.getInventory();
        for (int slot = 0; slot < Inventory.INVENTORY_SIZE; ++slot) {
            if (slot != inv.getSelectedSlot() && !inv.getItem(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /** Helper.createInv: "BanditInventory", a slot per theft. */
    static LOTRInventoryNPC createInv(LOTRNPCEntity npc, int maxThefts) {
        return new LOTRInventoryNPC("BanditInventory", npc, maxThefts);
    }
}
