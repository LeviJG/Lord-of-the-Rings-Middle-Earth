package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** The hired-unit screens' packets. */
public final class LOTRHiredPayloads {

    private LOTRHiredPayloads() {
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, path);
    }

    /** LOTRPacketHiredGui: the unit's state for its player's screen, and whether to open it. */
    public record HiredGui(int entityId, boolean openGui, boolean isActive, boolean canMove,
                           boolean teleportAutomatically, int mobKills, int xp, float alignmentRequired,
                           int pledgeType, boolean inCombat, boolean guardMode, int guardRange)
            implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<HiredGui> TYPE = new CustomPacketPayload.Type<>(id("hired_gui"));

        public static final StreamCodec<RegistryFriendlyByteBuf, HiredGui> STREAM_CODEC = StreamCodec.of(
                (buf, p) -> {
                    buf.writeVarInt(p.entityId);
                    buf.writeBoolean(p.openGui);
                    buf.writeBoolean(p.isActive);
                    buf.writeBoolean(p.canMove);
                    buf.writeBoolean(p.teleportAutomatically);
                    buf.writeInt(p.mobKills);
                    buf.writeInt(p.xp);
                    buf.writeFloat(p.alignmentRequired);
                    buf.writeByte(p.pledgeType);
                    buf.writeBoolean(p.inCombat);
                    buf.writeBoolean(p.guardMode);
                    buf.writeInt(p.guardRange);
                },
                buf -> new HiredGui(buf.readVarInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                        buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readByte(),
                        buf.readBoolean(), buf.readBoolean(), buf.readInt()));

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketHiredUnitInteract: 0 talk, 1 command. */
    public record Interact(int entityId, int action) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Interact> TYPE = new CustomPacketPayload.Type<>(id("hired_interact"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Interact> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Interact::entityId, ByteBufCodecs.VAR_INT, Interact::action, Interact::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketHiredUnitCommand: a button on a page of the unit's screen; -1 when it closes. */
    public record Command(int entityId, int page, int action, int value) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Command> TYPE = new CustomPacketPayload.Type<>(id("hired_command"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Command> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Command::entityId, ByteBufCodecs.VAR_INT, Command::page,
                ByteBufCodecs.VAR_INT, Command::action, ByteBufCodecs.VAR_INT, Command::value, Command::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketHiredUnitDismiss: 0 dismiss. */
    public record Dismiss(int entityId, int action) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Dismiss> TYPE = new CustomPacketPayload.Type<>(id("hired_dismiss"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Dismiss> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Dismiss::entityId, ByteBufCodecs.VAR_INT, Dismiss::action, Dismiss::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** openGui 33 (sendClientsideGUI): the held squadron item's naming screen. */
    public record OpenSquadronItem() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OpenSquadronItem> TYPE =
                new CustomPacketPayload.Type<>(id("open_squadron_item"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenSquadronItem> STREAM_CODEC =
                StreamCodec.unit(new OpenSquadronItem());

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketItemSquadron: the held squadron item's new company (empty for none). */
    public record ItemSquadron(String squadron) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ItemSquadron> TYPE =
                new CustomPacketPayload.Type<>(id("item_squadron"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ItemSquadron> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ItemSquadron::squadron, ItemSquadron::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketLocationFX SWORD_COMMAND: where a command sword's order fell. */
    public record SwordCommandFX(double x, double y, double z) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SwordCommandFX> TYPE = new CustomPacketPayload.Type<>(id("sword_command_fx"));

        public static final StreamCodec<RegistryFriendlyByteBuf, SwordCommandFX> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.DOUBLE, SwordCommandFX::x, ByteBufCodecs.DOUBLE, SwordCommandFX::y, ByteBufCodecs.DOUBLE,
                SwordCommandFX::z, SwordCommandFX::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketNPCSquadron: the company the unit is in (empty for none). */
    public record Squadron(int entityId, String squadron) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<Squadron> TYPE = new CustomPacketPayload.Type<>(id("npc_squadron"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Squadron> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Squadron::entityId, ByteBufCodecs.STRING_UTF8, Squadron::squadron, Squadron::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
