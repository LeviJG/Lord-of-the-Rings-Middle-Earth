package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDate;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLore;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredTask;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBonusMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMiniQuest: a task an NPC sets a player -- given on a day, by someone of a faction, for a reward
 * in alignment, coins and sometimes an item, a lore book, a smith's scroll or (from a dwarf) a book of
 * true silver -- which the NPC's death, or another's hand, may fail. Saved by the original's keys.
 *
 * One time in ten the rewards come in a pouch, as many as fit.
 *
 * <p>NOT ported yet: the biome it was given in (with the biomes, D10).
 */
public abstract class LOTRMiniQuest {

    public static final Map<String, Supplier<? extends LOTRMiniQuest>> NAME_TO_QUEST = new HashMap<>();
    public static final Map<Class<? extends LOTRMiniQuest>, String> QUEST_TO_NAME = new HashMap<>();
    public static final int MAX_MINIQUESTS_PER_FACTION = 5;
    public static final double RENDER_HEAD_DISTANCE = 12.0;

    static {
        registerQuestType("Collect", LOTRMiniQuestCollect.class, LOTRMiniQuestCollect::new);
        registerQuestType("KillFaction", LOTRMiniQuestKillFaction.class, LOTRMiniQuestKillFaction::new);
        registerQuestType("KillEntity", LOTRMiniQuestKillEntity.class, LOTRMiniQuestKillEntity::new);
        registerQuestType("Bounty", LOTRMiniQuestBounty.class, LOTRMiniQuestBounty::new);
        registerQuestType("Welcome", LOTRMiniQuestWelcome.class, LOTRMiniQuestWelcome::new);
        registerQuestType("Pickpocket", LOTRMiniQuestPickpocket.class, LOTRMiniQuestPickpocket::new);
    }

    public LOTRMiniQuestFactory questGroup;
    /** The player's quests this belongs to; none while it is only an offer. */
    public LOTRMiniQuests.@Nullable PlayerQuests playerData;
    public UUID questUUID = UUID.randomUUID();
    public UUID entityUUID;
    public String entityName = "";
    public String entityNameFull = "";
    public LOTRFaction entityFaction;
    public int questColor;
    public int dateGiven;
    public float rewardFactor = 1.0f;
    public boolean willHire;
    public float hiringAlignment;
    public final List<ItemStack> rewardItemTable = new ArrayList<>();
    public boolean completed;
    public int dateCompleted;
    public int coinsRewarded;
    public float alignmentRewarded;
    public boolean wasHired;
    public final List<ItemStack> itemsRewarded = new ArrayList<>();
    public boolean entityDead;
    public @Nullable BlockPos lastLocation;
    public @Nullable String lastLocationDimension;
    public String speechBankStart = "";
    public String speechBankProgress = "";
    public String speechBankComplete = "";
    public String speechBankTooMany = "";
    public String quoteStart = "";
    public String quoteComplete = "";
    public final List<String> quotesStages = new ArrayList<>();

    private static <Q extends LOTRMiniQuest> void registerQuestType(String name, Class<Q> type, Supplier<Q> factory) {
        NAME_TO_QUEST.put(name, factory);
        QUEST_TO_NAME.put(type, name);
    }

    /** loadQuestFromNBT: the quest the tag holds, if it is a whole one. */
    public static @Nullable LOTRMiniQuest loadQuest(CompoundTag nbt, HolderLookup.Provider registries,
                                                   LOTRMiniQuests.@Nullable PlayerQuests playerData) {
        String type = nbt.getStringOr("QuestType", "");
        Supplier<? extends LOTRMiniQuest> factory = NAME_TO_QUEST.get(type);
        if (factory == null) {
            LOTRMod.LOGGER.error("Could not instantiate miniquest of type {}", type);
            return null;
        }
        LOTRMiniQuest quest = factory.get();
        quest.playerData = playerData;
        try {
            quest.readFromNBT(nbt, registries);
        } catch (RuntimeException e) {
            LOTRMod.LOGGER.error("Could not load a LOTR miniquest", e);
            return null;
        }
        if (quest.isValidQuest()) {
            return quest;
        }
        LOTRMod.LOGGER.error("Loaded an invalid LOTR miniquest {}", quest.speechBankStart);
        return null;
    }

    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        writeToNBT(nbt, registries);
        return nbt;
    }

    protected static Tag saveItem(ItemStack stack, HolderLookup.Provider registries) {
        return ItemStack.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registries), stack).getOrThrow();
    }

    protected static @Nullable ItemStack loadItem(Tag tag, HolderLookup.Provider registries) {
        return ItemStack.CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registries), tag).result().orElse(null);
    }

    public boolean anyRewardsGiven() {
        return this.alignmentRewarded > 0.0f || this.coinsRewarded > 0 || !this.itemsRewarded.isEmpty();
    }

    public boolean canPlayerAccept(Player player) {
        return true;
    }

    public boolean canRewardVariousExtraItems() {
        return true;
    }

    /** complete: the rewards, the hiring, the speech, and the quest set among the completed. */
    public void complete(Player player, LOTRNPCEntity npc) {
        this.completed = true;
        this.dateCompleted = LOTRDate.ShireReckoning.currentDay;
        RandomSource rand = npc.getRandom();
        List<ItemStack> dropItems = new ArrayList<>();
        float alignment = getAlignmentBonus();
        if (alignment != 0.0f) {
            alignment *= Mth.randomBetween(rand, 0.75f, 1.25f);
            alignment = Math.max(alignment, 1.0f);
            LOTRAlignmentValues.AlignmentBonus bonus = LOTRAlignmentValues.createMiniquestBonus(alignment);
            LOTRFaction rewardFaction = getAlignmentRewardFaction();
            if (!this.questGroup.isNoAlignRewardForEnemy() || LOTRPlayerAlignments.getAlignment(player, rewardFaction) >= 0.0f) {
                LOTRAlignmentBonusMap alignmentMap = LOTRPlayerAlignments.addAlignment(player, bonus, rewardFaction, npc);
                this.alignmentRewarded = alignmentMap.get(rewardFaction);
            }
        }
        int coins = getCoinBonus();
        if (coins != 0) {
            if (shouldRandomiseCoinReward()) {
                coins = Math.round(coins * Mth.randomBetween(rand, 0.75f, 1.25f));
                if (rand.nextInt(12) == 0) {
                    coins *= Mth.randomBetweenInclusive(rand, 2, 5);
                }
            }
            this.coinsRewarded = coins = Math.max(coins, 1);
            int coinsRemain = coins;
            int[] values = {1, 10, 100};
            for (int l = LOTRCoins.TYPES - 1; l >= 0; --l) {
                int coinValue = values[l];
                if (coinsRemain < coinValue) {
                    continue;
                }
                int numCoins = coinsRemain / coinValue;
                coinsRemain -= numCoins * coinValue;
                while (numCoins > 64) {
                    numCoins -= 64;
                    dropItems.add(new ItemStack(LOTRCoins.coin(l), 64));
                }
                dropItems.add(new ItemStack(LOTRCoins.coin(l), numCoins));
            }
        }
        if (!this.rewardItemTable.isEmpty()) {
            ItemStack item = this.rewardItemTable.get(rand.nextInt(this.rewardItemTable.size()));
            dropItems.add(item.copy());
            this.itemsRewarded.add(item.copy());
        }
        if (canRewardVariousExtraItems()) {
            LOTRLore lore;
            if (rand.nextInt(10) == 0 && this.questGroup != null && !this.questGroup.getLoreCategories().isEmpty()
                    && (lore = LOTRLore.getMultiRandomLore(this.questGroup.getLoreCategories(), rand, true)) != null) {
                ItemStack loreBook = lore.createLoreBook(rand);
                dropItems.add(loreBook.copy());
                this.itemsRewarded.add(loreBook.copy());
            }
            if (rand.nextInt(15) == 0) {
                ItemStack modItem = LOTRModifiers.randomTemplate(rand);
                dropItems.add(modItem.copy());
                this.itemsRewarded.add(modItem.copy());
            }
            if (npc instanceof LOTRDwarfEntity && rand.nextInt(10) == 0) {
                ItemStack mithrilBook = new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER);
                dropItems.add(mithrilBook.copy());
                this.itemsRewarded.add(mithrilBook.copy());
            }
        }
        if (npc.level() instanceof ServerLevel level && !dropItems.isEmpty()) {
            if (canRewardVariousExtraItems() && rand.nextInt(10) == 0) {
                ItemStack pouch = npc.createNPCPouchDrop();
                LOTRPouchItem.fillPouchFromListAndRetainUnfilled(pouch, dropItems);
                npc.spawnAtLocation(level, pouch);
                // The red book lists a plain pouch: the original took the copy's tags off.
                this.itemsRewarded.add(new ItemStack(pouch.getItem()));
            }
            for (ItemStack drop : dropItems) {
                npc.spawnAtLocation(level, drop);
            }
        }
        if (this.willHire) {
            @SuppressWarnings("unchecked")
            EntityType<? extends LOTRNPCEntity> type = (EntityType<? extends LOTRNPCEntity>) npc.getType();
            LOTRUnitTradeEntry tradeEntry = new LOTRUnitTradeEntry(() -> type, 0, this.hiringAlignment);
            tradeEntry.task = LOTRHiredTask.WARRIOR;
            npc.hiredNPCInfo.hireUnit(player, false, this.entityFaction, tradeEntry, null,
                    npc.getVehicle() instanceof net.minecraft.world.entity.Mob mount ? mount : null);
            this.wasHired = true;
        }
        updateQuest();
        if (this.playerData != null) {
            this.playerData.completeMiniQuest(this);
        }
        sendCompletedSpeech(player, npc);
        LOTRAchievement achievement;
        if (this.questGroup != null && (achievement = this.questGroup.getAchievement()) != null) {
            LOTRPlayerAchievements.addAchievement(player, achievement);
        }
    }

    public abstract float getAlignmentBonus();

    public LOTRFaction getAlignmentRewardFaction() {
        return this.questGroup.checkAlignmentRewardFaction(this.entityFaction);
    }

    public abstract int getCoinBonus();

    public abstract float getCompletionFactor();

    public Component getFactionSubtitle() {
        return this.entityFaction.isPlayableAlignmentFaction() ? this.entityFaction.factionName() : Component.empty();
    }

    public abstract String getObjectiveInSpeech();

    public abstract String getProgressedObjectiveInSpeech();

    public int getQuestColor() {
        return this.questColor;
    }

    public Component getQuestFailure() {
        return Component.translatable("lotr.gui.redBook.mq.diary.dead", this.entityName);
    }

    public Component getQuestFailureShorthand() {
        return Component.translatable("lotr.gui.redBook.mq.dead");
    }

    public abstract ItemStack getQuestIcon();

    public abstract Component getQuestObjective();

    public abstract Component getQuestProgress();

    public abstract Component getQuestProgressShorthand();

    public void handleEvent(LOTRMiniQuestEvent event) {
    }

    public boolean isActive() {
        return !this.completed && !isFailed();
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean isFailed() {
        return this.entityDead;
    }

    public boolean isValidQuest() {
        return this.entityUUID != null && this.entityFaction != null;
    }

    public void onInteract(Player player, LOTRNPCEntity npc) {
    }

    public boolean onInteractOther(Player player, LOTRNPCEntity npc) {
        return false;
    }

    public void onKill(Player player, LivingEntity entity) {
    }

    public void onKilledByPlayer(Player player, Player killer) {
    }

    public void onPlayerTick(Player player) {
    }

    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.getString("QuestGroup").map(LOTRMiniQuestFactory::forName).ifPresent(f -> this.questGroup = f);
        nbt.getString("QuestUUID").ifPresent(s -> this.questUUID = UUID.fromString(s));
        this.entityUUID = nbt.contains("UUIDMost") && nbt.contains("UUIDLeast")
                ? new UUID(nbt.getLongOr("UUIDMost", 0L), nbt.getLongOr("UUIDLeast", 0L))
                : UUID.fromString(nbt.getStringOr("EntityUUID", ""));
        this.entityName = nbt.getStringOr("Owner", "");
        this.entityNameFull = nbt.getStringOr("OwnerFull", this.entityName);
        this.entityFaction = LOTRFaction.forName(nbt.getStringOr("Faction", ""));
        this.questColor = nbt.contains("Color") ? nbt.getIntOr("Color", 0)
                : this.entityFaction == null ? 0 : this.entityFaction.getFactionColor();
        this.dateGiven = nbt.getIntOr("DateGiven", 0);
        this.rewardFactor = nbt.contains("RewardFactor") ? nbt.getFloatOr("RewardFactor", 1.0f) : 1.0f;
        this.willHire = nbt.getBooleanOr("WillHire", false);
        this.hiringAlignment = nbt.contains("HiringAlignment") ? nbt.getIntOr("HiringAlignment", 0)
                : nbt.getFloatOr("HiringAlignF", 0.0f);
        this.rewardItemTable.clear();
        for (Tag tag : nbt.getListOrEmpty("RewardItemTable")) {
            ItemStack item = loadItem(tag, registries);
            if (item != null && !item.isEmpty()) {
                this.rewardItemTable.add(item);
            }
        }
        this.completed = nbt.getBooleanOr("Completed", false);
        this.dateCompleted = nbt.getIntOr("DateCompleted", 0);
        this.coinsRewarded = nbt.getShortOr("CoinReward", (short) 0);
        this.alignmentRewarded = nbt.contains("AlignmentReward") ? nbt.getShortOr("AlignmentReward", (short) 0)
                : nbt.getFloatOr("AlignRewardF", 0.0f);
        this.wasHired = nbt.getBooleanOr("WasHired", false);
        this.itemsRewarded.clear();
        for (Tag tag : nbt.getListOrEmpty("ItemRewards")) {
            ItemStack item = loadItem(tag, registries);
            if (item != null && !item.isEmpty()) {
                this.itemsRewarded.add(item);
            }
        }
        this.entityDead = nbt.getBooleanOr("OwnerDead", false);
        if (nbt.contains("Dimension")) {
            this.lastLocation = new BlockPos(nbt.getIntOr("XPos", 0), nbt.getIntOr("YPos", 0), nbt.getIntOr("ZPos", 0));
            this.lastLocationDimension = nbt.getStringOr("Dimension", "");
        }
        this.speechBankStart = nbt.getStringOr("SpeechStart", "");
        this.speechBankProgress = nbt.getStringOr("SpeechProgress", "");
        this.speechBankComplete = nbt.getStringOr("SpeechComplete", "");
        this.speechBankTooMany = nbt.getStringOr("SpeechTooMany", "");
        this.quoteStart = nbt.getStringOr("QuoteStart", "");
        this.quoteComplete = nbt.getStringOr("QuoteComplete", "");
        this.quotesStages.clear();
        for (Tag tag : nbt.getListOrEmpty("QuotesStages")) {
            tag.asString().ifPresent(this.quotesStages::add);
        }
        // An old quest with no group: the group named in its speech bank's path.
        if (this.questGroup == null && this.speechBankStart != null) {
            int i1 = this.speechBankStart.indexOf('/');
            int i2 = this.speechBankStart.indexOf('/', i1 + 1);
            if (i1 >= 0 && i2 >= 0) {
                LOTRMiniQuestFactory factory = LOTRMiniQuestFactory.forName(this.speechBankStart.substring(i1 + 1, i2));
                if (factory != null) {
                    this.questGroup = factory;
                }
            }
        }
    }

    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putString("QuestType", QUEST_TO_NAME.get(getClass()));
        if (this.questGroup != null) {
            nbt.putString("QuestGroup", this.questGroup.getBaseName());
        }
        nbt.putString("QuestUUID", this.questUUID.toString());
        nbt.putString("EntityUUID", this.entityUUID.toString());
        nbt.putString("Owner", this.entityName);
        nbt.putString("OwnerFull", this.entityNameFull);
        nbt.putString("Faction", this.entityFaction.codeName());
        nbt.putInt("Color", this.questColor);
        nbt.putInt("DateGiven", this.dateGiven);
        nbt.putFloat("RewardFactor", this.rewardFactor);
        nbt.putBoolean("WillHire", this.willHire);
        nbt.putFloat("HiringAlignF", this.hiringAlignment);
        if (!this.rewardItemTable.isEmpty()) {
            ListTag items = new ListTag();
            this.rewardItemTable.forEach(item -> items.add(saveItem(item, registries)));
            nbt.put("RewardItemTable", items);
        }
        nbt.putBoolean("Completed", this.completed);
        nbt.putInt("DateCompleted", this.dateCompleted);
        nbt.putShort("CoinReward", (short) this.coinsRewarded);
        nbt.putFloat("AlignRewardF", this.alignmentRewarded);
        nbt.putBoolean("WasHired", this.wasHired);
        if (!this.itemsRewarded.isEmpty()) {
            ListTag items = new ListTag();
            this.itemsRewarded.forEach(item -> items.add(saveItem(item, registries)));
            nbt.put("ItemRewards", items);
        }
        nbt.putBoolean("OwnerDead", this.entityDead);
        if (this.lastLocation != null) {
            nbt.putInt("XPos", this.lastLocation.getX());
            nbt.putInt("YPos", this.lastLocation.getY());
            nbt.putInt("ZPos", this.lastLocation.getZ());
            nbt.putString("Dimension", this.lastLocationDimension == null ? "" : this.lastLocationDimension);
        }
        nbt.putString("SpeechStart", this.speechBankStart);
        nbt.putString("SpeechProgress", this.speechBankProgress);
        nbt.putString("SpeechComplete", this.speechBankComplete);
        nbt.putString("SpeechTooMany", this.speechBankTooMany);
        nbt.putString("QuoteStart", this.quoteStart);
        nbt.putString("QuoteComplete", this.quoteComplete);
        if (!this.quotesStages.isEmpty()) {
            ListTag stages = new ListTag();
            this.quotesStages.forEach(s -> stages.add(StringTag.valueOf(s)));
            nbt.put("QuotesStages", stages);
        }
    }

    public void sendCompletedSpeech(Player player, LOTRNPCEntity npc) {
        sendQuoteSpeech(player, npc, this.quoteComplete);
    }

    public void sendProgressSpeechbank(Player player, LOTRNPCEntity npc) {
        npc.sendSpeechBank(player, this.speechBankProgress, this);
    }

    public void sendQuoteSpeech(Player player, LOTRNPCEntity npc, String quote) {
        LOTRSpeech.sendSpeech(player, npc, LOTRSpeech.formatSpeech(quote, player, null, getObjectiveInSpeech()));
        npc.markNPCSpoken();
    }

    public void setEntityDead() {
        this.entityDead = true;
        updateQuest();
    }

    public void setNPCInfo(LOTRNPCEntity npc) {
        this.entityUUID = npc.getUUID();
        this.entityName = npc.getNPCName();
        this.entityNameFull = npc.getName().getString();
        this.entityFaction = npc.getFaction();
        this.questColor = npc.getMiniquestColor();
    }

    public boolean shouldRandomiseCoinReward() {
        return true;
    }

    /** start: taken by the player -- dated, among their quests, the one they now follow. */
    public void start(Player player, LOTRNPCEntity npc) {
        setNPCInfo(npc);
        this.dateGiven = LOTRDate.ShireReckoning.currentDay;
        this.playerData.addMiniQuest(this);
        npc.questInfo.addActiveQuestPlayer(player);
        this.playerData.setTrackingMiniQuest(this);
    }

    /** updateLocation: where its giver now is, sent on when it has gone more than sixteen blocks. */
    public void updateLocation(LOTRNPCEntity npc) {
        BlockPos coords = npc.blockPosition();
        BlockPos prevCoords = this.lastLocation;
        this.lastLocation = coords;
        this.lastLocationDimension = npc.level().dimension().identifier().toString();
        if (prevCoords == null || coords.distSqr(prevCoords) > 256.0) {
            updateQuest();
        }
    }

    public void updateQuest() {
        if (this.playerData != null) {
            this.playerData.updateMiniQuest(this);
        }
    }

    /**
     * QuestFactoryBase: how a quest of one kind is made for a group -- its name (and so its speech
     * banks, miniquest/&lt;group&gt;/&lt;name&gt;_start and the rest), reward factor, and whether
     * finishing it hires its giver, or gives one of a set of items.
     */
    public abstract static class QuestFactoryBase<Q extends LOTRMiniQuest> {
        public LOTRMiniQuestFactory questFactoryGroup;
        public final String questName;
        public float rewardFactor = 1.0f;
        public boolean willHire;
        public float hiringAlignment;
        public @Nullable List<Supplier<ItemStack>> rewardItems;

        protected QuestFactoryBase(String name) {
            this.questName = name;
        }

        protected abstract Q newQuest();

        public abstract Class<? super Q> getQuestClass();

        public @Nullable Q createQuest(LOTRNPCEntity npc, RandomSource rand) {
            Q quest = newQuest();
            quest.questGroup = this.questFactoryGroup;
            String pathName = "miniquest/" + this.questFactoryGroup.getBaseName() + "/";
            String pathNameBaseSpeech = "miniquest/" + this.questFactoryGroup.getBaseSpeechGroup().getBaseName() + "/";
            String questPathName = pathName + this.questName + "_";
            quest.speechBankStart = questPathName + "start";
            quest.speechBankProgress = questPathName + "progress";
            quest.speechBankComplete = questPathName + "complete";
            quest.speechBankTooMany = pathNameBaseSpeech + "_tooMany";
            quest.quoteStart = LOTRSpeech.getSpeechBank(quest.speechBankStart).getRandomSpeech();
            quest.quoteComplete = LOTRSpeech.getSpeechBank(quest.speechBankComplete).getRandomSpeech();
            quest.setNPCInfo(npc);
            quest.rewardFactor = this.rewardFactor;
            quest.willHire = this.willHire;
            quest.hiringAlignment = this.hiringAlignment;
            if (this.rewardItems != null) {
                this.rewardItems.forEach(item -> quest.rewardItemTable.add(item.get()));
            }
            return quest;
        }

        public QuestFactoryBase<Q> setHiring(float f) {
            this.willHire = true;
            this.hiringAlignment = f;
            return this;
        }

        public QuestFactoryBase<Q> setRewardFactor(float f) {
            this.rewardFactor = f;
            return this;
        }

        public QuestFactoryBase<Q> setRewardItems(List<Supplier<ItemStack>> items) {
            this.rewardItems = items;
            return this;
        }
    }

    /** SorterAlphabetical: the active first, then by faction, then by giver's name. */
    public static final Comparator<LOTRMiniQuest> SORTER_ALPHABETICAL = (q1, q2) -> {
        if (!q2.isActive() && q1.isActive()) {
            return 1;
        }
        if (!q1.isActive() && q2.isActive()) {
            return -1;
        }
        if (q1.entityFaction == q2.entityFaction) {
            return q1.entityName.compareTo(q2.entityName);
        }
        return Integer.compare(q1.entityFaction.ordinal(), q2.entityFaction.ordinal());
    };
}
