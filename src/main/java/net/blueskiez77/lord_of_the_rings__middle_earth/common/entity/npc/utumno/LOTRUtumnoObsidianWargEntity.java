package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** LOTREntityUtumnoObsidianWarg: an obsidian warg. */
public class LOTRUtumnoObsidianWargEntity extends LOTRUtumnoWargEntity {

    public LOTRUtumnoObsidianWargEntity(EntityType<? extends LOTRUtumnoObsidianWargEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected LOTRWargType randomType() {
        return LOTRWargType.OBSIDIAN;
    }
}
