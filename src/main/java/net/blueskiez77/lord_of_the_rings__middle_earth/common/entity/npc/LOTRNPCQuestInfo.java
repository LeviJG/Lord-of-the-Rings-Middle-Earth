package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuests;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityQuestInfo: the mini-quest an NPC has to offer -- now and then one of its people's, held
 * for a day unless someone is reading it -- or one meant for a single player; the players on its
 * quests (who keep it from despawning, and are told where it has gone); and, to each player watching,
 * whether it has something for them.
 *
 * <p>NOT ported yet: the bounty help -- where a bounty's target was last seen, told to a player
 * hunting them -- which needs players' last biome and waypoint (D10, D13).
 */
public class LOTRNPCQuestInfo {

    public static final int MAX_OFFER_TIME = 24000;

    private final LOTRNPCEntity theNPC;
    private @Nullable LOTRMiniQuest miniquestOffer;
    private int offerTime;
    private int offerChance = 20000;
    private float minAlignment;
    private final Map<UUID, LOTRMiniQuest> playerSpecificOffers = new HashMap<>();
    private final Collection<Player> openOfferPlayers = new ArrayList<>();
    private final Map<UUID, Boolean> playerPacketCache = new HashMap<>();
    private final Collection<UUID> activeQuestPlayers = new ArrayList<>();
    public boolean clientIsOffering;
    public int clientOfferColor;

    public LOTRNPCQuestInfo(LOTRNPCEntity npc) {
        this.theNPC = npc;
    }

    // ---------------------------------------------------------------- packets

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> payloadType(String name) {
        return new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
    }

    /** LOTRPacketMiniquestOffer: the offer screen, with the quest. */
    public record OfferPayload(int npcId, CompoundTag quest) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OfferPayload> TYPE = payloadType("miniquest_offer");
        public static final StreamCodec<RegistryFriendlyByteBuf, OfferPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OfferPayload::npcId, ByteBufCodecs.COMPOUND_TAG, OfferPayload::quest, OfferPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketMiniquestOfferClose: the offer taken or turned down. */
    public record OfferResponsePayload(int npcId, boolean accept) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OfferResponsePayload> TYPE = payloadType("miniquest_offer_response");
        public static final StreamCodec<RegistryFriendlyByteBuf, OfferResponsePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OfferResponsePayload::npcId, ByteBufCodecs.BOOL, OfferResponsePayload::accept,
                OfferResponsePayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketNPCIsOfferingQuest: whether the NPC has something for this player, and its colour. */
    public record OfferingPayload(int npcId, boolean offering, int color) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OfferingPayload> TYPE = payloadType("npc_offering_quest");
        public static final StreamCodec<RegistryFriendlyByteBuf, OfferingPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OfferingPayload::npcId, ByteBufCodecs.BOOL, OfferingPayload::offering,
                ByteBufCodecs.INT, OfferingPayload::color, OfferingPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void initNetworking() {
        PayloadTypeRegistry.clientboundPlay().register(OfferPayload.TYPE, OfferPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OfferingPayload.TYPE, OfferingPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(OfferResponsePayload.TYPE, OfferResponsePayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OfferResponsePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (player.level().getEntity(payload.npcId()) instanceof LOTRNPCEntity npc) {
                npc.questInfo.receiveOfferResponse(player, payload.accept());
            }
        });
    }

    // ---------------------------------------------------------------- offers

    public void addActiveQuestPlayer(Player player) {
        this.activeQuestPlayers.add(player.getUUID());
    }

    public void removeActiveQuestPlayer(Player player) {
        this.activeQuestPlayers.remove(player.getUUID());
    }

    public boolean anyActiveQuestPlayers() {
        return !this.activeQuestPlayers.isEmpty();
    }

    public boolean anyOpenOfferPlayers() {
        return !this.openOfferPlayers.isEmpty();
    }

    public boolean canGenerateQuests() {
        if (!LOTRConfig.allowMiniquests || this.theNPC.isBaby() || this.theNPC.isDrunkard()) {
            return false;
        }
        return !this.theNPC.isTrader() && !this.theNPC.isTraderEscort && !this.theNPC.hiredNPCInfo.isActive;
    }

    public boolean canOfferQuestsTo(Player player) {
        if (canGenerateQuests() && this.theNPC.isFriendlyAndAligned(player) && this.theNPC.getTarget() == null) {
            return LOTRPlayerAlignments.getAlignment(player, this.theNPC.getFaction()) >= this.minAlignment;
        }
        return false;
    }

    public void clearMiniQuestOffer() {
        setMiniQuestOffer(null, 0);
    }

    public void setMiniQuestOffer(@Nullable LOTRMiniQuest quest, int time) {
        this.miniquestOffer = quest;
        this.offerTime = time;
    }

    public void setPlayerSpecificOffer(Player player, LOTRMiniQuest quest) {
        this.playerSpecificOffers.put(player.getUUID(), quest);
    }

    public void setOfferChance(int i) {
        this.offerChance = i;
    }

    public void setMinAlignment(float f) {
        this.minAlignment = f;
    }

    private @Nullable LOTRMiniQuest generateRandomMiniQuest() {
        for (int l = 0; l < 8; ++l) {
            LOTRMiniQuest quest = this.theNPC.createMiniQuest();
            if (quest == null) {
                continue;
            }
            if (quest.isValidQuest()) {
                return quest;
            }
            LOTRMod.LOGGER.error("Created an invalid LOTR miniquest {}", quest.speechBankStart);
        }
        return null;
    }

    public @Nullable LOTRMiniQuest getOfferFor(Player player) {
        return getOfferFor(player, null);
    }

    /** getOfferFor: one meant for this player, else the NPC's own; the flag says which. */
    private @Nullable LOTRMiniQuest getOfferFor(Player player, boolean @Nullable [] isSpecific) {
        LOTRMiniQuest specific = this.playerSpecificOffers.get(player.getUUID());
        if (isSpecific != null) {
            isSpecific[0] = specific != null;
        }
        return specific != null ? specific : this.miniquestOffer;
    }

    /** interact: a quest elsewhere that wants this NPC, then this NPC's quest in hand, then its offer. */
    public boolean interact(Player player) {
        LOTRMiniQuests.PlayerQuests playerData = LOTRMiniQuests.forPlayer(player.getUUID());
        List<LOTRMiniQuest> thisNPCQuests = playerData.getMiniQuestsForEntity(this.theNPC, true);
        if (thisNPCQuests.isEmpty()) {
            for (LOTRMiniQuest quest : playerData.getActiveMiniQuests()) {
                if (!quest.entityUUID.equals(this.theNPC.getUUID()) && quest.onInteractOther(player, this.theNPC)) {
                    return true;
                }
            }
        }
        if (canOfferQuestsTo(player)) {
            if (!thisNPCQuests.isEmpty()) {
                LOTRMiniQuest activeQuest = thisNPCQuests.getFirst();
                activeQuest.onInteract(player, this.theNPC);
                if (activeQuest.isCompleted()) {
                    removeActiveQuestPlayer(player);
                } else {
                    playerData.setTrackingMiniQuest(activeQuest);
                }
                return true;
            }
            LOTRMiniQuest offer = getOfferFor(player, null);
            if (offer != null && offer.isValidQuest() && offer.canPlayerAccept(player)) {
                if (playerData.getMiniQuestsForFaction(this.theNPC.getFaction(), true).size() < LOTRMiniQuest.MAX_MINIQUESTS_PER_FACTION) {
                    sendMiniquestOffer(player, offer);
                } else {
                    this.theNPC.sendSpeechBank(player, offer.speechBankTooMany, offer);
                }
                return true;
            }
        }
        return false;
    }

    /** onDeath: the quests it set fail. */
    public void onDeath() {
        for (UUID player : this.activeQuestPlayers) {
            for (LOTRMiniQuest quest : LOTRMiniQuests.forPlayer(player).getMiniQuestsForEntity(this.theNPC, true)) {
                if (quest.isActive()) {
                    quest.setEntityDead();
                }
            }
        }
    }

    public void tick() {
        if (this.miniquestOffer == null) {
            if (canGenerateQuests() && this.theNPC.getRandom().nextInt(this.offerChance) == 0) {
                this.miniquestOffer = generateRandomMiniQuest();
                if (this.miniquestOffer != null) {
                    this.offerTime = MAX_OFFER_TIME;
                }
            }
        } else if (!this.miniquestOffer.isValidQuest() || !canGenerateQuests()) {
            clearMiniQuestOffer();
        } else if (!anyOpenOfferPlayers()) {
            if (this.offerTime > 0) {
                --this.offerTime;
            } else {
                clearMiniQuestOffer();
            }
        }
        if (this.theNPC.tickCount % 10 == 0) {
            pruneActiveQuestPlayers();
            sendDataToAllWatchers();
        }
    }

    /** pruneActiveQuestPlayers: those with none of its quests left go; the rest learn where it is. */
    private void pruneActiveQuestPlayers() {
        if (this.activeQuestPlayers.isEmpty()) {
            return;
        }
        Collection<UUID> removes = new HashSet<>();
        for (UUID player : this.activeQuestPlayers) {
            List<LOTRMiniQuest> playerQuests = LOTRMiniQuests.forPlayer(player).getMiniQuestsForEntity(this.theNPC, true);
            if (playerQuests.isEmpty()) {
                removes.add(player);
                continue;
            }
            for (LOTRMiniQuest quest : playerQuests) {
                quest.updateLocation(this.theNPC);
            }
        }
        this.activeQuestPlayers.removeAll(removes);
    }

    public void receiveOfferResponse(Player player, boolean accept) {
        this.openOfferPlayers.remove(player);
        if (!accept) {
            return;
        }
        boolean[] container = new boolean[1];
        LOTRMiniQuest quest = getOfferFor(player, container);
        if (quest != null && quest.isValidQuest() && canOfferQuestsTo(player)) {
            quest.playerData = LOTRMiniQuests.forPlayer(player.getUUID());
            quest.start(player, this.theNPC);
            if (container[0]) {
                this.playerSpecificOffers.remove(player.getUUID());
            } else {
                clearMiniQuestOffer();
            }
        }
    }

    /** sendData: told only when it changes. */
    public void sendData(ServerPlayer player) {
        LOTRMiniQuest questOffer = getOfferFor(player, null);
        boolean isOffering = questOffer != null && canOfferQuestsTo(player);
        int color = questOffer != null ? questOffer.getQuestColor() : 0;
        Boolean prevOffering = this.playerPacketCache.put(player.getUUID(), isOffering);
        if (isOffering != (prevOffering != null && prevOffering)) {
            ServerPlayNetworking.send(player, new OfferingPayload(this.theNPC.getId(), isOffering, color));
        }
    }

    private void sendDataToAllWatchers() {
        for (ServerPlayer player : PlayerLookup.tracking(this.theNPC)) {
            sendData(player);
        }
    }

    /** onPlayerStartTracking: told afresh, as a player newly in sight has not been told. */
    public void onStartSeenBy(ServerPlayer player) {
        this.playerPacketCache.remove(player.getUUID());
        sendData(player);
    }

    private void sendMiniquestOffer(Player player, LOTRMiniQuest quest) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new OfferPayload(this.theNPC.getId(), quest.save(registries())));
            this.openOfferPlayers.add(player);
        }
    }

    private HolderLookup.Provider registries() {
        return this.theNPC.level().registryAccess();
    }

    // ---------------------------------------------------------------- saving

    public void save(ValueOutput output) {
        if (this.miniquestOffer != null) {
            output.store("MQOffer", CompoundTag.CODEC, this.miniquestOffer.save(registries()));
        }
        output.putInt("MQOfferTime", this.offerTime);
        ValueOutput.TypedOutputList<CompoundTag> specific = output.list("MQSpecificOffers", CompoundTag.CODEC);
        this.playerSpecificOffers.forEach((playerID, offer) -> {
            CompoundTag offerData = offer.save(registries());
            offerData.putString("OfferPlayerID", playerID.toString());
            specific.add(offerData);
        });
        ValueOutput.TypedOutputList<String> active = output.list("ActiveQuestPlayers", com.mojang.serialization.Codec.STRING);
        this.activeQuestPlayers.forEach(id -> active.add(id.toString()));
    }

    public void load(ValueInput input) {
        this.miniquestOffer = input.read("MQOffer", CompoundTag.CODEC)
                .map(tag -> LOTRMiniQuest.loadQuest(tag, registries(), null)).orElse(null);
        this.offerTime = input.getIntOr("MQOfferTime", 0);
        this.playerSpecificOffers.clear();
        for (CompoundTag offerData : input.listOrEmpty("MQSpecificOffers", CompoundTag.CODEC)) {
            try {
                UUID playerID = UUID.fromString(offerData.getStringOr("OfferPlayerID", ""));
                LOTRMiniQuest offer = LOTRMiniQuest.loadQuest(offerData, registries(), null);
                if (offer != null && offer.isValidQuest()) {
                    this.playerSpecificOffers.put(playerID, offer);
                }
            } catch (IllegalArgumentException e) {
                LOTRMod.LOGGER.warn("Error loading NPC player-specific miniquest offer", e);
            }
        }
        this.activeQuestPlayers.clear();
        for (String s : input.listOrEmpty("ActiveQuestPlayers", com.mojang.serialization.Codec.STRING)) {
            try {
                this.activeQuestPlayers.add(UUID.fromString(s));
            } catch (IllegalArgumentException ignored) {
            }
        }
        input.getString("NPCMiniQuestPlayer").ifPresent(s -> this.activeQuestPlayers.add(UUID.fromString(s)));
    }
}
