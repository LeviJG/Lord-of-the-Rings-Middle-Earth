package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.awt.Color;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySouthronTrader: a bazaar trader of the coast, trading with anyone
 * Near Harad does not dislike, in a turban of a random rich colour.
 *
 * <p>Not here: the turban's gold ornament one time in three (tracked with the
 * turban ornament). NOT ported yet: the tradeBazaarTrader achievement (D7).
 */
public abstract class LOTRSouthronTraderEntity extends LOTRNearHaradrimEntity implements LOTRTradeable {

    protected LOTRSouthronTraderEntity(EntityType<? extends LOTRSouthronTraderEntity> type, Level level) {
        super(type, level);
    }

    /** createTraderTurban: any hue, saturation 0.6-0.8, brightness 0.5-0.75. */
    public static ItemStack createTraderTurban(RandomSource random) {
        random.nextInt(3); // setHasOrnament's roll
        float h = random.nextFloat() * 360.0f;
        float s = Mth.randomBetween(random, 0.6f, 0.8f);
        float b = Mth.randomBetween(random, 0.5f, 0.75f);
        return turban(Color.HSBtoRGB(h, s, b) & 0xFFFFFF);
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "nearHarad/coast/bazaarTrader/friendly" : "nearHarad/coast/bazaarTrader/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.HEAD, createTraderTurban(this.random));
        return data;
    }
}
