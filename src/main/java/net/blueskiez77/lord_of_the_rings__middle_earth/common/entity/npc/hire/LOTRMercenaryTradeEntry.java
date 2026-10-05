package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

/** LOTRMercenaryTradeEntry: the mercenary himself, at his own price, while no one else has him. */
public class LOTRMercenaryTradeEntry extends LOTRUnitTradeEntry {

    private final LOTRMercenary theMerc;

    @SuppressWarnings("unchecked")
    private LOTRMercenaryTradeEntry(LOTRMercenary merc) {
        super(() -> (EntityType<? extends LOTRNPCEntity>) ((LOTRNPCEntity) merc).getType(), merc.getMercBaseCost(),
                merc.getMercAlignmentRequired());
        this.theMerc = merc;
    }

    public static LOTRMercenaryTradeEntry createFor(LOTRMercenary merc) {
        return new LOTRMercenaryTradeEntry(merc);
    }

    @Override
    protected LOTRNPCEntity getOrCreateHiredNPC(ServerLevel level) {
        return (LOTRNPCEntity) this.theMerc;
    }

    @Override
    public boolean hasRequiredCostAndAlignment(Player player, LOTRHireableBase trader) {
        if (((LOTRNPCEntity) this.theMerc).hiredNPCInfo.isActive) {
            return false;
        }
        return super.hasRequiredCostAndAlignment(player, trader);
    }
}
