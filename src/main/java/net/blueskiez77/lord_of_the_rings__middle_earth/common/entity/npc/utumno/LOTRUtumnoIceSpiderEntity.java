package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRSpiderBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentHudPayloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * LOTREntityUtumnoIceSpider: a spider of Utumno's ice, any of four sizes, chilly and untroubled by
 * frost, whose venom slows and whose bite frosts a player's sight; never to be ridden.
 */
public class LOTRUtumnoIceSpiderEntity extends LOTRSpiderBaseEntity {

    public LOTRUtumnoIceSpiderEntity(EntityType<? extends LOTRUtumnoIceSpiderEntity> type, Level level) {
        super(type, level);
        this.isImmuneToFrost = true;
        this.isChilly = true;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            if (target instanceof ServerPlayer player) {
                ServerPlayNetworking.send(player, new LOTRAlignmentHudPayloads.EnvironmentOverlay(LOTRAlignmentHudPayloads.EnvironmentOverlay.FROST));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canRideSpider() {
        return false;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    protected int getRandomSpiderScale() {
        return this.random.nextInt(4);
    }

    @Override
    protected int getRandomSpiderType() {
        return VENOM_SLOWNESS;
    }
}
