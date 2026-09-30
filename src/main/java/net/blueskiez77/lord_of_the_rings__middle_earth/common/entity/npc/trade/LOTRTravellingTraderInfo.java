package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTRTravellingTraderInfo: a travelling trader's visit. Once started (by
 * the travelling trader spawner, D12), it lasts a day: every player is
 * told of the arrival, the trader and its escort of two to four keep within
 * sixteen blocks of where they came, two minutes before the end the trader
 * says it is leaving, and at the end everyone is told it has gone and it and
 * its escort vanish. Killed during a visit, its death is told to everyone
 * and its escort goes. Until a visit starts it never leaves.
 */
public class LOTRTravellingTraderInfo {

    private final LOTRNPCEntity theEntity;
    private final LOTRTravellingTrader theTrader;
    public int timeUntilDespawn = -1;
    private final List<UUID> escortUUIDs = new ArrayList<>();

    public LOTRTravellingTraderInfo(LOTRTravellingTrader trader) {
        this.theEntity = (LOTRNPCEntity) trader;
        this.theTrader = trader;
    }

    public void onDeath() {
        if (this.theEntity.level() instanceof ServerLevel level && this.timeUntilDespawn >= 0) {
            level.getServer().getPlayerList().broadcastSystemMessage(
                    this.theEntity.getCombatTracker().getDeathMessage(), false);
            removeEscorts(level);
        }
    }

    public void tick() {
        if (!(this.theEntity.level() instanceof ServerLevel level)) {
            return;
        }
        if (this.timeUntilDespawn > 0) {
            --this.timeUntilDespawn;
        }
        if (this.timeUntilDespawn == 2400) {
            for (ServerPlayer player : level.players()) {
                LOTRSpeech.sendSpeechAndChatMessage(player, this.theEntity, this.theTrader.getDepartureSpeech());
            }
        }
        if (this.timeUntilDespawn == 0) {
            Component name = this.theEntity.getName().copy().withStyle(ChatFormatting.YELLOW);
            for (ServerPlayer player : level.players()) {
                player.sendSystemMessage(Component.translatable("lotr.travellingTrader.depart", name));
            }
            this.theEntity.discard();
            removeEscorts(level);
        }
    }

    private void removeEscorts(ServerLevel level) {
        for (UUID uuid : this.escortUUIDs) {
            Entity escort = level.getEntity(uuid);
            if (escort != null) {
                escort.discard();
            }
        }
    }

    /** startVisiting: a day's stay, announced, with a fresh escort of two to four. */
    public void startVisiting(Player player) {
        if (!(this.theEntity.level() instanceof ServerLevel level)) {
            return;
        }
        this.timeUntilDespawn = 24000;
        Component name = this.theEntity.getName().copy().withStyle(ChatFormatting.YELLOW);
        if (level.getServer().getPlayerList().getPlayerCount() <= 1) {
            level.getServer().getPlayerList().broadcastSystemMessage(
                    Component.translatable("lotr.travellingTrader.arrive", name), false);
        } else {
            Component message = Component.translatable("lotr.travellingTrader.arriveMP", name, player.getName());
            for (ServerPlayer other : level.players()) {
                other.sendSystemMessage(message);
            }
        }
        BlockPos home = BlockPos.containing(this.theEntity.getX(), this.theEntity.getBoundingBox().minY,
                this.theEntity.getZ());
        this.theEntity.setHomeTo(home, 16);
        int escorts = 2 + level.getRandom().nextInt(3);
        for (int l = 0; l < escorts; ++l) {
            LOTRNPCEntity escort = this.theTrader.createTravellingEscort(level);
            if (escort == null) {
                continue;
            }
            escort.snapTo(this.theEntity.getX(), this.theEntity.getY(), this.theEntity.getZ(),
                    this.theEntity.getYRot(), this.theEntity.getXRot());
            escort.isNPCPersistent = true;
            escort.spawnRidingHorse = false;
            escort.finalizeSpawn(level, level.getCurrentDifficultyAt(home), EntitySpawnReason.EVENT, null);
            level.addFreshEntity(escort);
            escort.setHomeTo(home, 16);
            escort.isTraderEscort = true;
            this.escortUUIDs.add(escort.getUUID());
        }
    }

    public void save(ValueOutput output) {
        output.putInt("DespawnTime", this.timeUntilDespawn);
        ValueOutput.TypedOutputList<UUID> list = output.list("Escorts", UUIDUtil.CODEC);
        this.escortUUIDs.forEach(list::add);
    }

    public void load(ValueInput input) {
        this.timeUntilDespawn = input.getIntOr("DespawnTime", -1);
        this.escortUUIDs.clear();
        input.listOrEmpty("Escorts", UUIDUtil.CODEC).forEach(this.escortUUIDs::add);
    }
}
