package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGreyWandererTracker;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRMiniQuestWelcome: the Grey Wanderer's welcome to Middle-earth, in fifteen stages -- the red
 * book given and read, the map, the factions and alignment, then the road ahead -- each told in a
 * line of his. While a player is on it he stays; if he has moved on, it fails.
 *
 * <p>Not ported (user): the three pouches given at the thirteenth stage. NOT ported yet: the map
 * (D13), whose opening the fifth stage waits on -- until then the quest goes no further than that
 * stage -- and the doGreyQuest achievement (D7).
 */
public class LOTRMiniQuestWelcome extends LOTRMiniQuest {

    public static final String SPEECHBANK = "char/gandalf/quest";
    public static final int STAGE_GET_ITEMS = 1;
    public static final int STAGE_READ_BOOK = 2;
    public static final int STAGE_OPEN_MAP = 5;
    public static final int STAGE_CYCLE_ALIGNMENT = 8;
    public static final int STAGE_CYCLE_REGIONS = 9;
    public static final int STAGE_OPEN_FACTIONS = 11;
    public static final int STAGE_COMPLETE = 15;
    public static final int NUM_STAGES = 15;

    public int stage;
    public boolean movedOn;

    public LOTRMiniQuestWelcome() {
    }

    public LOTRMiniQuestWelcome(LOTRNPCEntity gandalf) {
        setNPCInfo(gandalf);
        this.speechBankStart = "";
        this.speechBankProgress = "";
        this.speechBankComplete = "";
        this.speechBankTooMany = "";
        this.quoteStart = LOTRSpeech.getSpeechBank(SPEECHBANK).getSpeechAtLine(2);
        this.quoteComplete = LOTRSpeech.getSpeechBank(SPEECHBANK).getSpeechAtLine(12);
    }

    /** forceMenu_Map_Factions: whether the LOTR menu key should open the map, or the factions, for this stage. */
    public static boolean[] forceMenuMapFactions(Iterable<LOTRMiniQuest> activeQuests) {
        boolean[] flags = {false, false};
        for (LOTRMiniQuest quest : activeQuests) {
            if (quest instanceof LOTRMiniQuestWelcome qw) {
                if (qw.stage == STAGE_OPEN_MAP) {
                    flags[0] = true;
                    break;
                }
                if (qw.stage == STAGE_OPEN_FACTIONS) {
                    flags[1] = true;
                    break;
                }
            }
        }
        return flags;
    }

    @Override
    public boolean canPlayerAccept(Player player) {
        return !LOTRMiniQuests.forPlayer(player.getUUID()).hasAnyGWQuest();
    }

    @Override
    public boolean canRewardVariousExtraItems() {
        return false;
    }

    @Override
    public void complete(Player player, LOTRNPCEntity npc) {
        super.complete(player, npc);
        updateGreyWanderer();
    }

    @Override
    public float getAlignmentBonus() {
        return 0.0f;
    }

    @Override
    public int getCoinBonus() {
        return 0;
    }

    @Override
    public float getCompletionFactor() {
        return this.stage / 15.0f;
    }

    @Override
    public Component getFactionSubtitle() {
        return Component.empty();
    }

    @Override
    public String getObjectiveInSpeech() {
        return "OBJECTIVE_SPEECH";
    }

    @Override
    public String getProgressedObjectiveInSpeech() {
        return "OBJECTIVE_SPEECH_PROGRESSED";
    }

    @Override
    public Component getQuestFailure() {
        if (this.movedOn) {
            return Component.translatable("lotr.gui.redBook.mq.diary.movedOn", this.entityName);
        }
        return super.getQuestFailure();
    }

    @Override
    public Component getQuestFailureShorthand() {
        if (this.movedOn) {
            return Component.translatable("lotr.gui.redBook.mq.movedOn");
        }
        return super.getQuestFailureShorthand();
    }

    @Override
    public ItemStack getQuestIcon() {
        return new ItemStack(LOTRMiscItems.RED_BOOK);
    }

    @Override
    public Component getQuestObjective() {
        return switch (this.stage) {
            case 2 -> Component.translatable("lotr.miniquest.welcome.book");
            case 5 -> Component.translatable("lotr.miniquest.welcome.map", Component.keybind("key.lotr.menu"));
            case 8 -> Component.translatable("lotr.miniquest.welcome.align", Component.keybind("key.lotr.alignmentCycleLeft"),
                    Component.keybind("key.lotr.alignmentCycleRight"));
            case 9 -> Component.translatable("lotr.miniquest.welcome.alignRegions", Component.keybind("key.lotr.alignmentGroupPrev"),
                    Component.keybind("key.lotr.alignmentGroupNext"));
            case 11 -> Component.translatable("lotr.miniquest.welcome.factions", Component.keybind("key.lotr.menu"));
            default -> Component.translatable("lotr.miniquest.welcome.speak");
        };
    }

    @Override
    public Component getQuestProgress() {
        return getQuestProgressShorthand();
    }

    @Override
    public Component getQuestProgressShorthand() {
        return Component.translatable("lotr.miniquest.progressShort", this.stage, 15);
    }

    @Override
    public void handleEvent(LOTRMiniQuestEvent event) {
        int next = switch (event) {
            case OPEN_RED_BOOK -> this.stage == 2 ? 3 : -1;
            case VIEW_MAP -> this.stage == 5 ? 6 : -1;
            case CYCLE_ALIGNMENT -> this.stage == 8 ? 9 : -1;
            case CYCLE_ALIGNMENT_REGION -> this.stage == 9 ? 10 : -1;
            case VIEW_FACTIONS -> this.stage == 11 ? 12 : -1;
        };
        if (next > 0) {
            this.stage = next;
            updateQuest();
            updateGreyWanderer();
        }
    }

    @Override
    public boolean isFailed() {
        return super.isFailed() || this.movedOn;
    }

    private String line(int i) {
        return LOTRSpeech.getSpeechBank(SPEECHBANK).getSpeechAtLine(i);
    }

    /** Each stage's line; a new stage's is written in the red book too. */
    private void advance(Player player, LOTRNPCEntity npc, int lineIndex, int nextStage) {
        String line = line(lineIndex);
        sendQuoteSpeech(player, npc, line);
        this.quotesStages.add(line);
        this.stage = nextStage;
        updateQuest();
    }

    @Override
    public void onInteract(Player player, LOTRNPCEntity npc) {
        updateGreyWanderer();
        switch (this.stage) {
            case 1 -> {
                if (npc.level() instanceof ServerLevel level) {
                    npc.spawnAtLocation(level, new ItemStack(LOTRMiscItems.RED_BOOK));
                }
                advance(player, npc, 4, 2);
            }
            case 2 -> sendQuoteSpeech(player, npc, line(4));
            case 3 -> advance(player, npc, 5, 4);
            case 4 -> advance(player, npc, 6, 5);
            case 5 -> sendQuoteSpeech(player, npc, line(6));
            case 6 -> advance(player, npc, 7, 7);
            case 7 -> advance(player, npc, 8, 8);
            case 8, 9 -> sendQuoteSpeech(player, npc, line(8));
            case 10 -> advance(player, npc, 9, 11);
            case 11 -> sendQuoteSpeech(player, npc, line(9));
            case 12 -> advance(player, npc, 10, 13);
            case 13 -> advance(player, npc, 11, 14);
            case 14 -> {
                this.stage = 15;
                updateQuest();
                complete(player, npc);
            }
            default -> {
            }
        }
    }

    @Override
    public void onPlayerTick(Player player) {
        if (!LOTRGreyWandererTracker.isWandererActive(this.entityUUID)) {
            this.movedOn = true;
            updateQuest();
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        this.stage = nbt.getByteOr("WStage", (byte) 0);
        this.movedOn = nbt.getBooleanOr("WMovedOn", false);
    }

    @Override
    public void start(Player player, LOTRNPCEntity npc) {
        super.start(player, npc);
        String line = line(3);
        sendQuoteSpeech(player, npc, line);
        this.quotesStages.add(line);
        this.stage = 1;
        updateQuest();
        updateGreyWanderer();
    }

    public void updateGreyWanderer() {
        LOTRGreyWandererTracker.setWandererActive(this.entityUUID);
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        nbt.putByte("WStage", (byte) this.stage);
        nbt.putBoolean("WMovedOn", this.movedOn);
    }
}
