package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditFleeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditStealGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBandit;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryNPC;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRuffianSpy: a ruffian who picks pockets. He robs the player he
 * last turned on, or one reeling with nausea, of one theft, then runs; he
 * fights only when set upon. Slain, he gives up what he took.
 *
 * <p>NOT ported yet: his bounty help (a player may pay him off with coins,
 * gold, silver, a gem or a ring, to hear where a bounty's target was last seen
 * -- with the biomes and waypoints, D10/D13).
 */
public class LOTRRuffianSpyEntity extends LOTRBreeRuffianEntity implements LOTRBandit {

    private final LOTRInventoryNPC ruffianInventory = LOTRBandit.createInv(this, getMaxThefts());
    private @Nullable Player playerToRob;

    public LOTRRuffianSpyEntity(EntityType<? extends LOTRRuffianSpyEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected int addBreeAttackAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRBanditStealGoal(this, 1.6));
        prio++;
        this.goalSelector.addGoal(prio, new LOTRAttackOnCollideGoal(this, 1.4, false));
        prio++;
        this.goalSelector.addGoal(prio, new LOTRBanditFleeGoal(this, 1.4));
        return prio;
    }

    @Override
    public boolean canTargetPlayerForTheft(Player player) {
        return player == this.playerToRob || canRuffianTarget(player);
    }

    @Override
    public LOTRNPCEntity getBanditAsNPC() {
        return this;
    }

    @Override
    public LOTRInventoryNPC getBanditInventory() {
        return this.ruffianInventory;
    }

    @Override
    public int getMaxThefts() {
        return 1;
    }

    @Override
    public Component getTheftChatMsg(Player player) {
        return Component.translatable("chat.lotr.ruffianSteal");
    }

    @Override
    public String getTheftSpeechBank(Player player) {
        return "bree/ruffian/hostile";
    }

    /** setAttackTarget: a player (not in creative) he turns on is the one he will rob. */
    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        if (target instanceof Player player && !player.isCreative()) {
            this.playerToRob = player;
        }
        super.setTarget(target, speak);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide()) {
            this.ruffianInventory.dropAllItems();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.ruffianInventory.save(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.ruffianInventory.load(input);
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.RUFFIAN_SPY.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_RUFFIAN_SPY;
    }
}
