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
 * LOTREntityMordorWarg: a warg of Mordor, always black.
 */
public class LOTRMordorWargEntity extends LOTRWargEntity {

    public LOTRMordorWargEntity(EntityType<? extends LOTRMordorWargEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.MORDOR;
    }

    /** entityInit: always black. */
    @Override
    protected LOTRWargType randomType() {
        return LOTRWargType.BLACK;
    }

    /** createWargRider: a Mordor orc or archer, half the time barding the warg in its people's armour. */
    @Override
    protected LOTRNPCEntity createWargRider(ServerLevel level) {
        if (this.random.nextBoolean()) {
            setWargArmor(new ItemStack(LOTRCombatItems.MORDOR_WARG_ARMOR));
        }
        return (level.getRandom().nextBoolean() ? LOTREntities.MORDOR_ORC_ARCHER : LOTREntities.MORDOR_ORC).create(level, EntitySpawnReason.JOCKEY);
    }
}
