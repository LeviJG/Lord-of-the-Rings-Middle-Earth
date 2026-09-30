package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTREntityHighElfBase: the High Elves, of Lindon and of Rivendell alike.
 * Slain by a player they may leave a drink of miruvor.
 *
 * <p>NOT ported yet: their natural spawn check (above y 62 on grass, with
 * the biomes).
 */
public abstract class LOTRHighElfBaseEntity extends LOTRElfEntity {

    protected LOTRHighElfBaseEntity(EntityType<? extends LOTRHighElfBaseEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HIGH_ELF;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    /**
     * dropElfItems: to a player, now and then a light-to-strong miruvor in
     * one of the elves' vessels (1 in 20, less with looting).
     */
    @Override
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropElfItems(level, killedByPlayer, looting);
        if (killedByPlayer && this.random.nextInt(Math.max(20 - looting * 4, 1)) == 0) {
            ItemStack elfDrink = LOTRDrinkItem.stack(LOTRFoodItems.MIRUVOR, 1 + this.random.nextInt(3));
            LOTRVessel[] vessels = LOTRFoods.ELF_DRINK.getDrinkVessels();
            elfDrink.set(LOTRDataComponents.VESSEL, vessels[this.random.nextInt(vessels.length)]);
            spawnAtLocation(level, elfDrink, 0.0f);
        }
    }
}
