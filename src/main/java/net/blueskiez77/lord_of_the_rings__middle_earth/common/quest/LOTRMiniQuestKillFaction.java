package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/** LOTRMiniQuestKillFaction: so many of a faction's people. */
public class LOTRMiniQuestKillFaction extends LOTRMiniQuestKill {

    public @Nullable LOTRFaction killFaction;

    @Override
    public Component getKillTargetName() {
        return this.killFaction.factionEntityName();
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.killFaction != null;
    }

    @Override
    public void onKill(Player player, LivingEntity entity) {
        if (this.killCount < this.killTarget && LOTRNearestAttackableTargetGoal.factionOf(entity) == this.killFaction) {
            ++this.killCount;
            updateQuest();
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        this.killFaction = LOTRFaction.forName(nbt.getStringOr("KillFaction", ""));
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        nbt.putString("KillFaction", this.killFaction.codeName());
    }

    public static class QFKillFaction extends QFKill<LOTRMiniQuestKillFaction> {
        public LOTRFaction killFaction;

        public QFKillFaction(String name) {
            super(name);
        }

        @Override
        protected LOTRMiniQuestKillFaction newQuest() {
            return new LOTRMiniQuestKillFaction();
        }

        @Override
        public @Nullable LOTRMiniQuestKillFaction createQuest(LOTRNPCEntity npc, RandomSource rand) {
            LOTRMiniQuestKillFaction quest = super.createQuest(npc, rand);
            quest.killFaction = this.killFaction;
            return quest;
        }

        public QFKillFaction setKillFaction(LOTRFaction faction, int min, int max) {
            this.killFaction = faction;
            setKillTarget(min, max);
            return this;
        }
    }
}
