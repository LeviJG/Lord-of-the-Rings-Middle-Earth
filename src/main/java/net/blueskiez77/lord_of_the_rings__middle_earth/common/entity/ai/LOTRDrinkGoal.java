package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * LOTREntityAIDrink: a drink from the people's list, brewed ones at a random
 * strength from light to strong. An alcoholic one may, one time in three, make
 * the NPC drunk for 30-1500 seconds -- but only near a friendly bartender.
 * Drunkards drink longer and more often.
 */
public class LOTRDrinkGoal extends LOTRConsumeGoal {

    private static final double BARTENDER_RANGE = 12.0;

    public LOTRDrinkGoal(LOTRNPCEntity npc, LOTRFoods foods, int chance) {
        super(npc, foods, chance);
    }

    @Override
    protected void consume() {
        ItemStack stack = this.npc.getMainHandItem();
        if (stack.getItem() instanceof LOTRDrinkItem drink) {
            drink.applyToNPC(this.npc, stack);
            if (drink.alcoholicity() > 0.0f && this.npc.canGetDrunk() && !this.npc.isDrunkard()
                    && this.random.nextInt(3) == 0) {
                LOTRFaction faction = this.npc.getFaction();
                boolean bartenderNearby = !this.npc.level().getEntitiesOfClass(Entity.class,
                        this.npc.getBoundingBox().inflate(BARTENDER_RANGE),
                        e -> e instanceof LOTRBartender && e.isAlive()
                                && !LOTRNearestAttackableTargetGoal.factionOf(e).isBadRelation(faction)).isEmpty();
                if (bartenderNearby) {
                    int drunkTime = Mth.nextInt(this.random, 30, 1500);
                    this.npc.familyInfo.setDrunkTime(drunkTime * 20);
                }
            }
        }
    }

    @Override
    protected ItemStack createConsumable() {
        ItemStack drink = this.foodPool.getRandomFood(this.random);
        if (drink.getItem() instanceof LOTRDrinkItem item && item.isBrewable()) {
            drink.set(LOTRDataComponents.DRINK_STRENGTH, 1 + this.random.nextInt(3));
        }
        return drink;
    }

    @Override
    protected int getConsumeTime() {
        int time = super.getConsumeTime();
        if (this.npc.isDrunkard()) {
            time *= 1 + this.random.nextInt(4);
        }
        return time;
    }

    @Override
    protected boolean shouldConsume() {
        if (this.npc.isDrunkard() && this.random.nextInt(100) == 0) {
            return true;
        }
        return super.shouldConsume();
    }

    @Override
    protected void updateConsumeTick(int tick) {
        if (tick % 4 == 0) {
            this.npc.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5f, this.random.nextFloat() * 0.1f + 0.9f);
        }
    }
}
