package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTREventHandler.onBreakingSpeed: a tool's speed modifier multiplies the
 * finished digging speed, but only on blocks the tool is actually good for.
 * And feast mode.
 */
@Mixin(Player.class)
abstract class LOTRPlayerMixin {
    /**
     * LOTRReplacedMethods.Player.canEat: in feast mode ("Feast Mode", as the
     * server has it), food can be eaten at any hunger -- but not while riding.
     */
    @ModifyReturnValue(method = "canEat", at = @At("RETURN"))
    private boolean lotr$feastMode(boolean canEat) {
        Player player = (Player) (Object) this;
        return canEat || LOTRConfig.isFeastMode(player.level()) && player.getVehicle() == null;
    }

    @ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
    private float lotr$toolSpeed(float speed, @Local(argsOnly = true) BlockState state) {
        ItemStack tool = ((Player) (Object) this).getInventory().getSelectedItem();
        if (tool.getDestroySpeed(state) > 1.0f) {
            return speed * LOTRModifiers.toolSpeedFactor(tool);
        }
        return speed;
    }
}
