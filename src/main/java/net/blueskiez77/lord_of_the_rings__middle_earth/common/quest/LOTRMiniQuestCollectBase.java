package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRMiniQuestCollectBase: so many of something to bring the giver, taken from the player's pockets
 * (the held slot first) whenever they speak to them; rewarded by the number asked for.
 */
public abstract class LOTRMiniQuestCollectBase extends LOTRMiniQuest {

    public int collectTarget;
    public int amountGiven;

    @Override
    public float getAlignmentBonus() {
        return Math.max(this.collectTarget * this.rewardFactor, 1.0f);
    }

    @Override
    public int getCoinBonus() {
        return Math.round(getAlignmentBonus() * 2.0f);
    }

    @Override
    public float getCompletionFactor() {
        return (float) this.amountGiven / this.collectTarget;
    }

    @Override
    public Component getQuestProgress() {
        return Component.translatable("lotr.miniquest.collect.progress", this.amountGiven, this.collectTarget);
    }

    @Override
    public Component getQuestProgressShorthand() {
        return Component.translatable("lotr.miniquest.progressShort", this.amountGiven, this.collectTarget);
    }

    public abstract boolean isQuestItem(ItemStack stack);

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.collectTarget > 0;
    }

    @Override
    public void onInteract(Player player, LOTRNPCEntity npc) {
        int prevAmountGiven = this.amountGiven;
        Inventory inventory = player.getInventory();
        List<Integer> slotNumbers = new ArrayList<>();
        slotNumbers.add(inventory.getSelectedSlot());
        for (int slot = 0; slot < Inventory.INVENTORY_SIZE; ++slot) {
            if (!slotNumbers.contains(slot)) {
                slotNumbers.add(slot);
            }
        }
        for (int slot : slotNumbers) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && isQuestItem(stack)) {
                int amountRemaining = this.collectTarget - this.amountGiven;
                if (stack.getCount() >= amountRemaining) {
                    stack.shrink(amountRemaining);
                    inventory.setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
                    this.amountGiven += amountRemaining;
                } else {
                    this.amountGiven += stack.getCount();
                    inventory.setItem(slot, ItemStack.EMPTY);
                }
            }
            if (this.amountGiven >= this.collectTarget) {
                complete(player, npc);
                break;
            }
        }
        if (this.amountGiven > prevAmountGiven && !isCompleted()) {
            updateQuest();
        }
        if (!isCompleted()) {
            sendProgressSpeechbank(player, npc);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        this.collectTarget = nbt.getIntOr("Target", 0);
        this.amountGiven = nbt.getIntOr("Given", 0);
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        nbt.putInt("Target", this.collectTarget);
        nbt.putInt("Given", this.amountGiven);
    }
}
