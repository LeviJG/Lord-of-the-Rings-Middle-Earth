package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeManEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** LOTREntityAIBreeEat: as any Bree-lander eats, except that one of them only ever eats carrots. */
public class LOTRBreeEatGoal extends LOTREatGoal {

    public LOTRBreeEatGoal(LOTRNPCEntity npc, LOTRFoods foods, int chance) {
        super(npc, foods, chance);
    }

    @Override
    protected ItemStack createConsumable() {
        if (LOTRBreeManEntity.CARROT_EATER_NAME.equals(this.npc.getNPCName())) {
            return new ItemStack(Items.CARROT);
        }
        return super.createConsumable();
    }
}
