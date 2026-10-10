package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionBounties;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMiniQuestBounty: hunt down a player who has slain many of the giver's faction in its land. The
 * target is told a bounty is set on them; slaying them costs them standing (down to +100) with their
 * pledged or best faction and is told to all; if they slay the hunter instead, the hunter loses
 * standing with the bounty's faction and the target gains with theirs. A bounty another claims first
 * fails. Every fifth bounty brought in earns a headhunter's trophy.
 */
public class LOTRMiniQuestBounty extends LOTRMiniQuest {

    public @Nullable UUID targetID;
    public @Nullable String targetName;
    public boolean killed;
    public float alignmentBonus;
    public int coinBonus;
    public boolean bountyClaimedByOther;
    public boolean killedByBounty;

    @Override
    public boolean canPlayerAccept(Player player) {
        if (super.canPlayerAccept(player) && !this.targetID.equals(player.getUUID())
                && LOTRPlayerAlignments.getAlignment(player, this.entityFaction) >= 100.0f) {
            for (LOTRMiniQuest quest : LOTRMiniQuests.forPlayer(player.getUUID()).getActiveMiniQuests()) {
                if (quest instanceof LOTRMiniQuestBounty bounty && bounty.targetID.equals(this.targetID)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void complete(Player player, LOTRNPCEntity npc) {
        LOTRMiniQuests.PlayerQuests pd = this.playerData;
        pd.addCompletedBountyQuest();
        int bComplete = pd.getCompletedBountyQuests();
        if (bComplete > 0 && bComplete % 5 == 0) {
            this.rewardItemTable.add(new ItemStack(LOTRMaterialItems.HEADHUNTERS_TROPHY));
        }
        super.complete(player, npc);
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.DO_MINIQUEST_HUNTER);
        if (bComplete > 0 && bComplete % 5 == 0) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.DO_MINIQUEST_HUNTER5);
        }
    }

    @Override
    public float getAlignmentBonus() {
        return this.alignmentBonus;
    }

    @Override
    public int getCoinBonus() {
        return this.coinBonus;
    }

    @Override
    public float getCompletionFactor() {
        return this.killed ? 1.0f : 0.0f;
    }

    public float getKilledAlignmentPenalty() {
        return -this.alignmentBonus * 2.0f;
    }

    @Override
    public String getObjectiveInSpeech() {
        return this.targetName;
    }

    /** getPledgeOrHighestAlignmentFaction: the pledge, or one of the factions they stand best with above min. */
    public static @Nullable LOTRFaction getPledgeOrHighestAlignmentFaction(Player player, float min) {
        LOTRFaction pledge = LOTRPlayerAlignments.get(player).pledgeFaction();
        if (pledge != null) {
            return pledge;
        }
        List<LOTRFaction> highestFactions = new ArrayList<>();
        float highestAlignment = min;
        for (LOTRFaction f : LOTRFaction.getPlayableAlignmentFactions()) {
            float alignment = LOTRPlayerAlignments.getAlignment(player, f);
            if (alignment <= min) {
                continue;
            }
            if (alignment > highestAlignment) {
                highestFactions.clear();
                highestFactions.add(f);
                highestAlignment = alignment;
            } else if (alignment == highestAlignment) {
                highestFactions.add(f);
            }
        }
        return highestFactions.isEmpty() ? null : highestFactions.get(player.getRandom().nextInt(highestFactions.size()));
    }

    @Override
    public String getProgressedObjectiveInSpeech() {
        return this.targetName;
    }

    @Override
    public Component getQuestFailure() {
        if (this.killedByBounty) {
            return Component.translatable("lotr.miniquest.bounty.killedBy", this.targetName);
        }
        if (this.bountyClaimedByOther) {
            return Component.translatable("lotr.miniquest.bounty.claimed", this.targetName);
        }
        return super.getQuestFailure();
    }

    @Override
    public Component getQuestFailureShorthand() {
        if (this.killedByBounty) {
            return Component.translatable("lotr.miniquest.bounty.killedBy.short");
        }
        if (this.bountyClaimedByOther) {
            return Component.translatable("lotr.miniquest.bounty.claimed.short");
        }
        return super.getQuestFailureShorthand();
    }

    @Override
    public ItemStack getQuestIcon() {
        return new ItemStack(Items.IRON_SWORD);
    }

    @Override
    public Component getQuestObjective() {
        return Component.translatable("lotr.miniquest.bounty", this.targetName);
    }

    @Override
    public Component getQuestProgress() {
        return Component.translatable(this.killed ? "lotr.miniquest.bounty.progress.slain" : "lotr.miniquest.bounty.progress.notSlain");
    }

    @Override
    public Component getQuestProgressShorthand() {
        return Component.translatable("lotr.miniquest.progressShort", this.killed ? 1 : 0, 1);
    }

    @Override
    public boolean isFailed() {
        return super.isFailed() || this.bountyClaimedByOther || this.killedByBounty;
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.targetID != null;
    }

    @Override
    public void onInteract(Player player, LOTRNPCEntity npc) {
        if (this.killed) {
            complete(player, npc);
        } else {
            sendProgressSpeechbank(player, npc);
        }
    }

    @Override
    public void onKill(Player player, LivingEntity entity) {
        if (this.killed || isFailed() || !(entity instanceof Player slainPlayer) || !entity.getUUID().equals(this.targetID)) {
            return;
        }
        this.killed = true;
        LOTRFactionBounties.forFaction(this.entityFaction).forPlayer(slainPlayer).recordBountyKilled();
        updateQuest();
        LOTRFaction highestFaction = getPledgeOrHighestAlignmentFaction(slainPlayer, 100.0f);
        if (highestFaction != null) {
            float curAlignment = LOTRPlayerAlignments.getAlignment(slainPlayer, highestFaction);
            float alignmentLoss = getKilledAlignmentPenalty();
            if (curAlignment + alignmentLoss < 100.0f) {
                alignmentLoss = -(curAlignment - 100.0f);
            }
            LOTRPlayerAlignments.addAlignment(slainPlayer, new LOTRAlignmentValues.AlignmentBonus(alignmentLoss,
                    "lotr.alignment.bountyKilled"), highestFaction, player);
            slainPlayer.sendSystemMessage(Component.translatable("chat.lotr.bountyKilled1", player.getName(),
                    this.entityFaction.factionName()).withStyle(ChatFormatting.YELLOW));
            slainPlayer.sendSystemMessage(Component.translatable("chat.lotr.bountyKilled2",
                    highestFaction.factionName()).withStyle(ChatFormatting.YELLOW));
        }
        Component announce = Component.translatable("chat.lotr.bountyKill", player.getName(), slainPlayer.getName(),
                this.entityFaction.factionName()).withStyle(ChatFormatting.YELLOW);
        if (player instanceof ServerPlayer serverPlayer) {
            for (ServerPlayer other : serverPlayer.level().getServer().getPlayerList().getPlayers()) {
                if (other != slainPlayer) {
                    other.sendSystemMessage(announce);
                }
            }
        }
    }

    @Override
    public void onKilledByPlayer(Player player, Player killer) {
        if (this.killed || isFailed() || !killer.getUUID().equals(this.targetID)) {
            return;
        }
        LOTRFaction killerHighestFaction = getPledgeOrHighestAlignmentFaction(killer, 0.0f);
        if (killerHighestFaction != null) {
            LOTRPlayerAlignments.addAlignment(killer, new LOTRAlignmentValues.AlignmentBonus(this.alignmentBonus,
                    "lotr.alignment.killedHunter"), killerHighestFaction, player);
        }
        float curAlignment = LOTRPlayerAlignments.getAlignment(player, this.entityFaction);
        if (curAlignment > 100.0f) {
            float alignmentLoss = getKilledAlignmentPenalty();
            if (curAlignment + alignmentLoss < 100.0f) {
                alignmentLoss = -(curAlignment - 100.0f);
            }
            LOTRPlayerAlignments.addAlignment(player, new LOTRAlignmentValues.AlignmentBonus(alignmentLoss,
                    "lotr.alignment.killedByBounty"), this.entityFaction, killer);
            player.sendSystemMessage(Component.translatable("chat.lotr.killedByBounty1", killer.getName())
                    .withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.translatable("chat.lotr.killedByBounty2", this.entityFaction.factionName())
                    .withStyle(ChatFormatting.YELLOW));
        }
        this.killedByBounty = true;
        updateQuest();
        LOTRPlayerAchievements.addAchievement(killer, LOTRAchievement.KILL_HUNTING_PLAYER);
        Component announce = Component.translatable("chat.lotr.killedByBounty", player.getName(), killer.getName())
                .withStyle(ChatFormatting.YELLOW);
        if (player instanceof ServerPlayer serverPlayer) {
            for (ServerPlayer other : serverPlayer.level().getServer().getPlayerList().getPlayers()) {
                if (other != player) {
                    other.sendSystemMessage(announce);
                }
            }
        }
    }

    @Override
    public void onPlayerTick(Player player) {
        if (isActive() && !this.killed && !this.bountyClaimedByOther
                && LOTRFactionBounties.forFaction(this.entityFaction).forPlayer(this.targetID).recentlyBountyKilled()) {
            this.bountyClaimedByOther = true;
            updateQuest();
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        nbt.getString("TargetID").ifPresent(s -> this.targetID = UUID.fromString(s));
        nbt.getString("TargetName").ifPresent(s -> this.targetName = s);
        this.killed = nbt.getBooleanOr("Killed", false);
        this.alignmentBonus = nbt.contains("Alignment") ? nbt.getIntOr("Alignment", 0) : nbt.getFloatOr("AlignF", 0.0f);
        this.coinBonus = nbt.getIntOr("Coins", 0);
        this.bountyClaimedByOther = nbt.getBooleanOr("BountyClaimed", false);
        this.killedByBounty = nbt.getBooleanOr("KilledBy", false);
    }

    @Override
    public boolean shouldRandomiseCoinReward() {
        return false;
    }

    /** start: and the target is told, when next they are about, that a bounty is set on them. */
    @Override
    public void start(Player player, LOTRNPCEntity npc) {
        super.start(player, npc);
        LOTRMiniQuests.forPlayer(this.targetID).placeBountyFor(npc.getFaction());
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        if (this.targetID != null) {
            nbt.putString("TargetID", this.targetID.toString());
        }
        if (this.targetName != null) {
            nbt.putString("TargetName", this.targetName);
        }
        nbt.putBoolean("Killed", this.killed);
        nbt.putFloat("AlignF", this.alignmentBonus);
        nbt.putInt("Coins", this.coinBonus);
        nbt.putBoolean("BountyClaimed", this.bountyClaimedByOther);
        nbt.putBoolean("KilledBy", this.killedByBounty);
    }

    /** BountyHelp: what a giver's people may tell of a target's whereabouts. */
    public enum BountyHelp {
        BIOME("biome"), WAYPOINT("wp");

        public final String speechName;

        BountyHelp(String name) {
            this.speechName = name;
        }

        public static BountyHelp getRandomHelpType(RandomSource random) {
            return values()[random.nextInt(values().length)];
        }
    }

    public static class QFBounty extends QuestFactoryBase<LOTRMiniQuestBounty> {

        public QFBounty(String name) {
            super(name);
        }

        @Override
        protected LOTRMiniQuestBounty newQuest() {
            return new LOTRMiniQuestBounty();
        }

        @Override
        public Class<? super LOTRMiniQuestBounty> getQuestClass() {
            return LOTRMiniQuestBounty.class;
        }

        /** A target from those with 25 recent kills in the faction's land: worth their kills (1 to 50), and ten coins each. */
        @Override
        public @Nullable LOTRMiniQuestBounty createQuest(LOTRNPCEntity npc, RandomSource rand) {
            if (!LOTRConfig.allowBountyQuests) {
                return null;
            }
            List<LOTRFactionBounties.PlayerData> players = LOTRFactionBounties.forFaction(npc.getFaction()).findBountyTargets(25);
            if (players.isEmpty()) {
                return null;
            }
            LOTRMiniQuestBounty quest = super.createQuest(npc, rand);
            LOTRFactionBounties.PlayerData targetData = players.get(rand.nextInt(players.size()));
            int alignment = Mth.clamp(targetData.getNumKills(), 1, 50);
            int coins = Mth.clamp((int) (targetData.getNumKills() * 10.0f * Mth.randomBetween(rand, 0.75f, 1.25f)), 1, 1000);
            quest.targetID = targetData.playerID;
            String username = targetData.findUsername();
            quest.targetName = username.isBlank() ? quest.targetID.toString() : username;
            quest.alignmentBonus = alignment;
            quest.coinBonus = coins;
            return quest;
        }
    }
}
