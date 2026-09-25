package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jspecify.annotations.Nullable;

/**
 * The pledge timers LOTRPlayerData kept beside the pledge itself: the kill
 * cooldown that a second pledge-faction kill within a day turns into a broken
 * pledge, and the cooldown after a broken pledge before a new one may be made.
 * Both count down every tick, so this is mutable and changed in place; the
 * client hears about the break cooldown through LOTRBrokenPledgePayload, on
 * the original's schedule, rather than through attachment sync.
 */
public final class LOTRPledgeCooldowns {

    public static final Codec<LOTRPledgeCooldowns> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.optionalFieldOf("PledgeKillCD", 0).forGetter(c -> c.killCooldown),
                    Codec.INT.optionalFieldOf("PledgeBreakCD", 0).forGetter(c -> c.breakCooldown),
                    Codec.INT.optionalFieldOf("PledgeBreakCDStart", 0).forGetter(c -> c.breakCooldownStart),
                    LOTRFaction.CODEC.optionalFieldOf("BrokenPledgeFac")
                            .forGetter(c -> Optional.ofNullable(c.brokenPledgeFaction)))
            .apply(instance, (kill, cd, start, broken) ->
                    new LOTRPledgeCooldowns(kill, cd, start, broken.orElse(null))));

    public int killCooldown;
    public int breakCooldown;
    public int breakCooldownStart;
    public @Nullable LOTRFaction brokenPledgeFaction;

    public LOTRPledgeCooldowns() {
    }

    private LOTRPledgeCooldowns(int killCooldown, int breakCooldown, int breakCooldownStart,
            @Nullable LOTRFaction brokenPledgeFaction) {
        this.killCooldown = killCooldown;
        this.breakCooldown = breakCooldown;
        this.breakCooldownStart = breakCooldownStart;
        this.brokenPledgeFaction = brokenPledgeFaction;
    }
}
