package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMechanisedRailBlock;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * The same as LOTROldMinecartBehaviorMixin, for the experimental minecart
 * physics: its brake and boost each take the rail's state, and see a
 * mechanised rail as a powered rail powered while the mechanism runs.
 */
@Mixin(NewMinecartBehavior.class)
abstract class LOTRNewMinecartBehaviorMixin {
    @ModifyVariable(method = {"calculateHaltTrackSpeed", "calculateBoostTrackSpeed"}, at = @At("HEAD"), argsOnly = true)
    private BlockState lotr$mechanisedRail(BlockState state) {
        return LOTRMechanisedRailBlock.asPoweredRail(state);
    }
}
