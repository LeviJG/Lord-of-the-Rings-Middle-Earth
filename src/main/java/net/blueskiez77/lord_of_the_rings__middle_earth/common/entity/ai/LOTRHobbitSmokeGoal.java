package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRSmokeRingEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmokingPipeItem;

import net.minecraft.world.item.ItemStack;

/** LOTREntityAIHobbitSmoke: a pipe, then a smoke ring, a puff and a heart back. */
public class LOTRHobbitSmokeGoal extends LOTRConsumeGoal {

    public LOTRHobbitSmokeGoal(LOTRNPCEntity npc, int chance) {
        super(npc, null, chance);
    }

    @Override
    protected void consume() {
        ItemStack stack = this.npc.getMainHandItem();
        int colour = stack.getItem() instanceof LOTRSmokingPipeItem ? LOTRSmokingPipeItem.getSmokeColor(stack) : 0;
        LOTRSmokeRingEntity ring = new LOTRSmokeRingEntity(this.npc.level(), this.npc).setSmokeColour(colour);
        ring.shootFromRotation(this.npc, this.npc.getXRot(), this.npc.getYRot(), 0.0f, LOTRSmokeRingEntity.SPEED, 1.0f);
        this.npc.level().addFreshEntity(ring);
        this.npc.playSound(LOTRSounds.ITEM_PUFF, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        this.npc.heal(2.0f);
    }

    @Override
    protected ItemStack createConsumable() {
        return new ItemStack(LOTRMiscItems.SMOKING_PIPE);
    }

    @Override
    protected void updateConsumeTick(int tick) {
    }
}
