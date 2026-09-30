package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmokingPipeItem;

import net.minecraft.world.item.ItemStack;

/** LOTREntityAIGandalfSmoke: the hobbits' pipe, with the magic smoke. */
public class LOTRGandalfSmokeGoal extends LOTRHobbitSmokeGoal {

    public LOTRGandalfSmokeGoal(LOTRNPCEntity npc, int chance) {
        super(npc, chance);
    }

    @Override
    protected ItemStack createConsumable() {
        return LOTRSmokingPipeItem.of(LOTRSmokingPipeItem.MAGIC_COLOR);
    }
}
