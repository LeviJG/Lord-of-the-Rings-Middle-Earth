package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTREntityAngmarWarg: a warg of Angmar.
 */
public class LOTRAngmarWargEntity extends LOTRWargEntity {

    public LOTRAngmarWargEntity(EntityType<? extends LOTRAngmarWargEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ANGMAR;
    }

    /** createWargRider: an Angmar orc or archer, half the time barding the warg in its people's armour. */
    @Override
    protected LOTRNPCEntity createWargRider(ServerLevel level) {
        if (this.random.nextBoolean()) {
            setWargArmor(new ItemStack(LOTRCombatItems.ANGMAR_WARG_ARMOR));
        }
        return (level.getRandom().nextBoolean() ? LOTREntities.ANGMAR_ORC_ARCHER : LOTREntities.ANGMAR_ORC).create(level, EntitySpawnReason.JOCKEY);
    }
}
