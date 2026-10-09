package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMiniQuestCollect: so many of one item -- any drink of the kind asked for, whatever it is in;
 * stolen goods never.
 */
public class LOTRMiniQuestCollect extends LOTRMiniQuestCollectBase {

    public @Nullable ItemStack collectItem;

    @Override
    public String getObjectiveInSpeech() {
        return this.collectTarget + " " + this.collectItem.getHoverName().getString();
    }

    @Override
    public String getProgressedObjectiveInSpeech() {
        return this.collectTarget - this.amountGiven + " " + this.collectItem.getHoverName().getString();
    }

    @Override
    public ItemStack getQuestIcon() {
        return this.collectItem;
    }

    @Override
    public Component getQuestObjective() {
        return Component.translatable("lotr.miniquest.collect", this.collectTarget, this.collectItem.getHoverName());
    }

    /** The same item; a drink by its kind alone (isItemFullDrink, getEquivalentDrink). */
    @Override
    public boolean isQuestItem(ItemStack stack) {
        if (IPickpocketable.Helper.isPickpocketed(stack)) {
            return false;
        }
        if (LOTRVessel.isFullDrink(this.collectItem)) {
            return LOTRVessel.equivalentDrink(this.collectItem).getItem() == LOTRVessel.equivalentDrink(stack).getItem();
        }
        return stack.getItem() == this.collectItem.getItem();
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.collectItem != null && !this.collectItem.isEmpty();
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        if (nbt.get("Item") != null) {
            this.collectItem = loadItem(nbt.get("Item"), registries);
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        if (this.collectItem != null) {
            nbt.put("Item", saveItem(this.collectItem, registries));
        }
    }

    public static class QFCollect extends QuestFactoryBase<LOTRMiniQuestCollect> {
        public Supplier<ItemStack> collectItem;
        public int minTarget;
        public int maxTarget;

        public QFCollect(String name) {
            super(name);
        }

        @Override
        protected LOTRMiniQuestCollect newQuest() {
            return new LOTRMiniQuestCollect();
        }

        @Override
        public Class<? super LOTRMiniQuestCollect> getQuestClass() {
            return LOTRMiniQuestCollect.class;
        }

        @Override
        public @Nullable LOTRMiniQuestCollect createQuest(LOTRNPCEntity npc, RandomSource rand) {
            LOTRMiniQuestCollect quest = super.createQuest(npc, rand);
            quest.collectItem = this.collectItem.get();
            quest.collectTarget = Mth.randomBetweenInclusive(rand, this.minTarget, this.maxTarget);
            return quest;
        }

        public QFCollect setCollectItem(Supplier<ItemStack> stack, int min, int max) {
            this.collectItem = stack;
            this.minTarget = min;
            this.maxTarget = max;
            return this;
        }
    }
}
