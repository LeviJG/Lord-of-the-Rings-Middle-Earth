package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLeafParticleOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGaladhrimTrader (the Elven Trader): a travelling trader of
 * Lothlórien who trades with those at +75 or better, with an escort of
 * Galadhrim elves, well protected (armour 10) and seeking no one out.
 *
 * <p>It dies in a burst of golden leaves. The original also sent the burst as
 * it departed, but only once its departure had already removed it, so it never
 * showed; that is kept.
 */
public class LOTRGaladhrimTraderEntity extends LOTRGaladhrimElfEntity implements LOTRTravellingTrader {

    private static final byte EVENT_LEAVES = 15;

    public LOTRGaladhrimTraderEntity(EntityType<? extends LOTRGaladhrimTraderEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.GALADHRIM_TRADER;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 1.6, false));
        addTargetTasks(false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.GALADHRIM_TRADER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.GALADHRIM_TRADER_SELL;
    }

    /** canTradeWith: +75 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 75.0f && isFriendly(player);
    }

    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return LOTREntities.GALADHRIM_ELF.create(level, EntitySpawnReason.EVENT);
    }

    @Override
    public String getDepartureSpeech() {
        return "galadhrim/trader/departure";
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    /** getExperiencePoints: 5 to 7. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 5 + this.random.nextInt(3);
    }

    /** getTotalArmorValue: 10, whatever it wears. */
    @Override
    public int getArmorValue() {
        return 10;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel level) {
            level.broadcastEntityEvent(this, EVENT_LEAVES);
        }
    }

    /** handleHealthUpdate(15): sixteen golden leaves scattering from it. */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_LEAVES) {
            for (int i = 0; i < 16; ++i) {
                double d = getX() + (this.random.nextDouble() - 0.5) * getBbWidth();
                double d1 = getY() + this.random.nextDouble() * getBbHeight();
                double d2 = getZ() + (this.random.nextDouble() - 0.5) * getBbWidth();
                level().addParticle(LOTRLeafParticleOptions.of(LOTRParticles.LEAF_GOLD, 30, 30), d, d1, d2,
                        -0.2 + this.random.nextFloat() * 0.4f, -0.2 + this.random.nextFloat() * 0.4f,
                        -0.2 + this.random.nextFloat() * 0.4f);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /** A travelling trader brought back by a respawner comes back only once. */
    @Override
    public boolean shouldTraderRespawn() {
        return false;
    }

    @Override
    public boolean shouldRenderNPCHair() {
        return false;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "galadhrim/trader/friendly" : "galadhrim/trader/neutral";
        }
        return "galadhrim/trader/hostile";
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_ELVEN_TRADER);
    }
}
