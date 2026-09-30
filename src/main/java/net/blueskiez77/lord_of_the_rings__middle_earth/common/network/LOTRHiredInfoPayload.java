package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import java.util.Optional;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNPCInfo;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** LOTRPacketHiredInfo: a unit's hiring player, task, squadron and level, for those who see it. */
public record LOTRHiredInfoPayload(int entityId, Optional<UUID> hiringPlayer, int task, String squadron, int xpLevel)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LOTRHiredInfoPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "hired_info"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRHiredInfoPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LOTRHiredInfoPayload::entityId,
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), LOTRHiredInfoPayload::hiringPlayer,
            ByteBufCodecs.VAR_INT, LOTRHiredInfoPayload::task,
            ByteBufCodecs.STRING_UTF8, LOTRHiredInfoPayload::squadron,
            ByteBufCodecs.VAR_INT, LOTRHiredInfoPayload::xpLevel,
            LOTRHiredInfoPayload::new);

    public static LOTRHiredInfoPayload of(LOTRNPCEntity npc) {
        LOTRHiredNPCInfo info = npc.hiredNPCInfo;
        String squadron = info.getSquadron();
        return new LOTRHiredInfoPayload(npc.getId(), Optional.ofNullable(info.getHiringPlayerUUID()),
                info.getTask().ordinal(), squadron == null ? "" : squadron, info.xpLevel);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
