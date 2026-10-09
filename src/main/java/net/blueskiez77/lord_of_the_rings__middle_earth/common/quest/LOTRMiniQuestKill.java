package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/** LOTRMiniQuestKill: so many of some foe to slay, told to the giver once done. */
public abstract class LOTRMiniQuestKill extends LOTRMiniQuest {

    public int killTarget;
    public int killCount;

    @Override
    public float getAlignmentBonus() {
        return this.killTarget * this.rewardFactor;
    }

    @Override
    public int getCoinBonus() {
        return Math.round(this.killTarget * 2.0f * this.rewardFactor);
    }

    @Override
    public float getCompletionFactor() {
        return (float) this.killCount / this.killTarget;
    }

    public abstract Component getKillTargetName();

    @Override
    public String getObjectiveInSpeech() {
        return this.killTarget + " " + getKillTargetName().getString();
    }

    @Override
    public String getProgressedObjectiveInSpeech() {
        return this.killTarget - this.killCount + " " + getKillTargetName().getString();
    }

    @Override
    public ItemStack getQuestIcon() {
        return new ItemStack(Items.IRON_SWORD);
    }

    @Override
    public Component getQuestObjective() {
        return Component.translatable("lotr.miniquest.kill", this.killTarget, getKillTargetName());
    }

    @Override
    public Component getQuestProgress() {
        return Component.translatable("lotr.miniquest.kill.progress", this.killCount, this.killTarget);
    }

    @Override
    public Component getQuestProgressShorthand() {
        return Component.translatable("lotr.miniquest.progressShort", this.killCount, this.killTarget);
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.killTarget > 0;
    }

    @Override
    public void onInteract(Player player, LOTRNPCEntity npc) {
        if (this.killCount >= this.killTarget) {
            complete(player, npc);
        } else {
            sendProgressSpeechbank(player, npc);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        this.killTarget = nbt.getIntOr("Target", 0);
        this.killCount = nbt.getIntOr("Count", 0);
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        nbt.putInt("Target", this.killTarget);
        nbt.putInt("Count", this.killCount);
    }

    public abstract static class QFKill<Q extends LOTRMiniQuestKill> extends QuestFactoryBase<Q> {
        public int minTarget;
        public int maxTarget;

        protected QFKill(String name) {
            super(name);
        }

        @Override
        public Class<? super Q> getQuestClass() {
            return LOTRMiniQuestKill.class;
        }

        @Override
        public @Nullable Q createQuest(LOTRNPCEntity npc, RandomSource rand) {
            Q quest = super.createQuest(npc, rand);
            quest.killTarget = Mth.randomBetweenInclusive(rand, this.minTarget, this.maxTarget);
            return quest;
        }

        public QFKill<Q> setKillTarget(int min, int max) {
            this.minTarget = min;
            this.maxTarget = max;
            return this;
        }
    }
}
