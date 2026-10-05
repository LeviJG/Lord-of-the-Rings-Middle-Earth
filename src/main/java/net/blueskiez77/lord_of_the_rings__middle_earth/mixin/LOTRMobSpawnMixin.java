package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTREventHandler.onEntitySpawnAttempt: no monster spawns -- naturally or
 * from a spawner, the spawns Forge's CheckSpawn asked about -- in land any
 * banner protects.
 */
@Mixin(Mob.class)
abstract class LOTRMobSpawnMixin {

    @ModifyReturnValue(method = "checkSpawnRules", at = @At("RETURN"))
    private boolean lotr$noMonstersInBannerLand(boolean canSpawn, LevelAccessor level, EntitySpawnReason reason) {
        Mob mob = (Mob) (Object) this;
        if (canSpawn && mob instanceof Monster && level instanceof Level fullLevel
                && (reason == EntitySpawnReason.NATURAL || reason == EntitySpawnReason.SPAWNER)
                && LOTRBannerProtection.isProtected(fullLevel, mob, LOTRBannerProtection.anyBanner(), false)) {
            return false;
        }
        return canSpawn;
    }
}
