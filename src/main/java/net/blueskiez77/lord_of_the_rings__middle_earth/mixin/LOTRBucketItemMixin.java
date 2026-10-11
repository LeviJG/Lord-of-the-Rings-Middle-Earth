package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** LOTREventHandler.onFillBucket: water emptied on Utumno's fire level boils away, the bucket emptied. */
@Mixin(BucketItem.class)
abstract class LOTRBucketItemMixin {
    @Shadow
    @Final
    protected Fluid content;

    @Inject(method = "emptyContents", at = @At("HEAD"), cancellable = true)
    private void lotr$utumnoFire(LivingEntity user, Level level, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        if (this.content == Fluids.WATER && LOTRUtumno.evaporatesBucket(level, pos)) {
            cir.setReturnValue(true);
        }
    }
}
