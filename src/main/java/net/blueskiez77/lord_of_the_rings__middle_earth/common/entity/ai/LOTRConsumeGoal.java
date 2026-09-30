package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIConsumeBase: now and then -- four times as often when hurt --
 * a grown NPC with no one to fight puts its held item away, holds something
 * to eat, drink or smoke for a while, and then takes it.
 */
public abstract class LOTRConsumeGoal extends net.minecraft.world.entity.ai.goal.Goal {

    protected final LOTRNPCEntity npc;
    protected final RandomSource random;
    protected final @Nullable LOTRFoods foodPool;
    private final int chanceToConsume;
    private int consumeTick;

    protected LOTRConsumeGoal(LOTRNPCEntity npc, @Nullable LOTRFoods foods, int chance) {
        this.npc = npc;
        this.random = npc.getRandom();
        this.foodPool = foods;
        this.chanceToConsume = chance;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    protected abstract void consume();

    protected abstract ItemStack createConsumable();

    protected abstract void updateConsumeTick(int tick);

    protected int getConsumeTime() {
        return 32;
    }

    protected boolean shouldConsume() {
        boolean needsHeal = this.npc.getHealth() < this.npc.getMaxHealth();
        return needsHeal && this.random.nextInt(this.chanceToConsume / 4) == 0
                || this.random.nextInt(this.chanceToConsume) == 0;
    }

    @Override
    public boolean canUse() {
        if (this.npc.isBaby() || this.npc.getTarget() != null || this.npc.npcItemsInv.getIsEating()) {
            return false;
        }
        return shouldConsume();
    }

    @Override
    public boolean canContinueToUse() {
        return this.consumeTick > 0 && !this.npc.getMainHandItem().isEmpty() && this.npc.getTarget() == null;
    }

    @Override
    public void start() {
        this.npc.npcItemsInv.setEatingBackup(this.npc.getMainHandItem().copy());
        this.npc.npcItemsInv.setIsEating(true);
        this.npc.setItemSlot(EquipmentSlot.MAINHAND, createConsumable());
        this.consumeTick = getConsumeTime();
    }

    @Override
    public void stop() {
        this.npc.setItemSlot(EquipmentSlot.MAINHAND, this.npc.npcItemsInv.getEatingBackup());
        this.npc.npcItemsInv.setEatingBackup(ItemStack.EMPTY);
        this.npc.npcItemsInv.setIsEating(false);
        this.npc.refreshCurrentAttackMode();
        this.consumeTick = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        --this.consumeTick;
        updateConsumeTick(this.consumeTick);
        if (this.consumeTick == 0) {
            consume();
        }
    }
}
