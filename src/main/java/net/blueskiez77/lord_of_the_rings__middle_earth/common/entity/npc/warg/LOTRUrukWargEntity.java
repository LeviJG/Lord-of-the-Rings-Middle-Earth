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
 * LOTREntityUrukWarg: a warg of Isengard.
 */
public class LOTRUrukWargEntity extends LOTRWargEntity {

    public LOTRUrukWargEntity(EntityType<? extends LOTRUrukWargEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ISENGARD;
    }

    /** createWargRider: an Isengard snaga or archer, half the time barding the warg in Isengard's armour. */
    @Override
    protected LOTRNPCEntity createWargRider(ServerLevel level) {
        if (this.random.nextBoolean()) {
            setWargArmor(new ItemStack(LOTRCombatItems.ISENGARD_WARG_ARMOR));
        }
        return (level.getRandom().nextBoolean() ? LOTREntities.ISENGARD_SNAGA_ARCHER : LOTREntities.ISENGARD_SNAGA)
                .create(level, EntitySpawnReason.JOCKEY);
    }
}
