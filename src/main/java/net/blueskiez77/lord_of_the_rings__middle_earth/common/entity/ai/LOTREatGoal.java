package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

/** LOTREntityAIEat: something from the people's food list, healing by its nutrition. */
public class LOTREatGoal extends LOTRConsumeGoal {

    public LOTREatGoal(LOTRNPCEntity npc, LOTRFoods foods, int chance) {
        super(npc, foods, chance);
    }

    @Override
    protected void consume() {
        FoodProperties food = this.npc.getMainHandItem().get(DataComponents.FOOD);
        if (food != null) {
            this.npc.heal(food.nutrition());
        }
    }

    @Override
    protected ItemStack createConsumable() {
        return this.foodPool.getRandomFood(this.random);
    }

    @Override
    protected void updateConsumeTick(int tick) {
        if (tick % 4 == 0) {
            this.npc.spawnFoodParticles();
            this.npc.playSound(SoundEvents.GENERIC_EAT.value(), 0.5f + 0.5f * this.random.nextInt(2),
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
    }
}
