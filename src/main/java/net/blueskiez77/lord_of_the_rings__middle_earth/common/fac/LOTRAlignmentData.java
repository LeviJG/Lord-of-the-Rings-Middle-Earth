package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Immutable payload for the per-player alignment Data Attachment.
 * This is the modern replacement for the alignment portion of the 1.7.10
 * LOTRPlayerData god-object. It holds only the alignment-relevant state:
 *   - alignments: per-faction alignment value (keyed by faction, default 0)
 *   - pledgeFaction: the faction the player has pledged to (nullable)
 *   - hideAlignment: HUD toggle
 * Fabric Data Attachments strongly encourage immutable types, so this is a
 * record; all "mutations" return a new instance (see withAlignment etc.),
 * and the accessor layer (LOTRPlayerAlignments) swaps the attachment value.
 * Persistence and networking are handled by the two codecs below, so no
 * manual NBT read/write or packet class is needed (unlike the original).
 * The map is (de)serialized keyed by faction code name, matching the old
 * "AlignmentMap"/"AlignF" concept, so the data model stays faithful.
 * NOTE: the broader pledge system (cooldowns, broken pledges, enemy limits)
 * is intentionally NOT modeled yet — only the pledgeFaction reference the
 * alignment math needs. Expand this record when the pledge slice is ported.
 */
public record LOTRAlignmentData(Map<LOTRFaction, Float> alignments,
                                LOTRFaction pledgeFaction,
                                boolean hideAlignment) {

    public static final LOTRAlignmentData EMPTY = new LOTRAlignmentData(Map.of(), null, false);

    /** Persistence codec (used by .persistent() on the attachment). */
    public static final Codec<LOTRAlignmentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.unboundedMap(LOTRFaction.CODEC, Codec.FLOAT)
                            .optionalFieldOf("alignments", Map.of())
                            .forGetter(LOTRAlignmentData::alignments),
                    LOTRFaction.CODEC
                            .optionalFieldOf("pledgeFaction")
                            .forGetter(data -> java.util.Optional.ofNullable(data.pledgeFaction())),
                    Codec.BOOL
                            .optionalFieldOf("hideAlignment", false)
                            .forGetter(LOTRAlignmentData::hideAlignment))
            .apply(instance, (alignments, pledge, hide) ->
                    new LOTRAlignmentData(alignments, pledge.orElse(null), hide)));

    /** Sync codec (used by .syncWith() on the attachment). */
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRAlignmentData> STREAM_CODEC =
            StreamCodec.of(LOTRAlignmentData::encode, LOTRAlignmentData::decode);

    private static void encode(RegistryFriendlyByteBuf buf, LOTRAlignmentData data) {
        buf.writeVarInt(data.alignments.size());
        for (Map.Entry<LOTRFaction, Float> entry : data.alignments.entrySet()) {
            buf.writeUtf(entry.getKey().codeName());
            buf.writeFloat(entry.getValue());
        }
        buf.writeBoolean(data.pledgeFaction != null);
        if (data.pledgeFaction != null) {
            buf.writeUtf(data.pledgeFaction.codeName());
        }
        buf.writeBoolean(data.hideAlignment);
    }

    private static LOTRAlignmentData decode(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<LOTRFaction, Float> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            LOTRFaction fac = LOTRFaction.forName(buf.readUtf());
            float value = buf.readFloat();
            if (fac != null) {
                map.put(fac, value);
            }
        }
        LOTRFaction pledge = buf.readBoolean() ? LOTRFaction.forName(buf.readUtf()) : null;
        boolean hide = buf.readBoolean();
        return new LOTRAlignmentData(map, pledge, hide);
    }

    public float getAlignment(LOTRFaction faction) {
        if (faction.hasFixedAlignment) {
            return faction.fixedAlignment;
        }
        return alignments.getOrDefault(faction, 0.0f);
    }

    public boolean isPledgedTo(LOTRFaction faction) {
        return pledgeFaction == faction;
    }

    public LOTRAlignmentData withAlignment(LOTRFaction faction, float value) {
        Map<LOTRFaction, Float> newMap = new HashMap<>(alignments);
        newMap.put(faction, value);
        return new LOTRAlignmentData(newMap, pledgeFaction, hideAlignment);
    }

    public LOTRAlignmentData withPledgeFaction(LOTRFaction faction) {
        return new LOTRAlignmentData(alignments, faction, hideAlignment);
    }

    public LOTRAlignmentData withHideAlignment(boolean hide) {
        return new LOTRAlignmentData(alignments, pledgeFaction, hide);
    }
}
