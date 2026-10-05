package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBonusMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTREntityAlignmentBonus: the floating notice of an alignment change, made
 * only on the client where it was earned. It lasts four seconds, up to fifteen
 * for a big change, and is never saved.
 */
public class LOTRAlignmentBonusEntity extends Entity {

    public int particleAge;
    public int particleMaxAge = 80;
    public String name = "";
    public LOTRFaction mainFaction = LOTRFaction.UNALIGNED;
    public float prevMainAlignment;
    public LOTRAlignmentBonusMap factionBonusMap = new LOTRAlignmentBonusMap();
    public boolean isKill;
    public boolean isHiredKill;
    public float conquestBonus;

    public LOTRAlignmentBonusEntity(EntityType<? extends LOTRAlignmentBonusEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public LOTRAlignmentBonusEntity setup(double x, double y, double z, String name, LOTRFaction faction, float prev,
            Map<LOTRFaction, Float> bonusMap, boolean kill, boolean hiredKill, float conqBonus) {
        setPos(x, y, z);
        this.name = name;
        this.mainFaction = faction;
        this.prevMainAlignment = prev;
        this.factionBonusMap = new LOTRAlignmentBonusMap();
        this.factionBonusMap.putAll(bonusMap);
        this.isKill = kill;
        this.isHiredKill = hiredKill;
        this.conquestBonus = conqBonus;
        calcMaxAge();
        return this;
    }

    private void calcMaxAge() {
        float highestBonus = 0.0f;
        for (LOTRFaction fac : this.factionBonusMap.getChangedFactions()) {
            highestBonus = Math.max(highestBonus, Math.abs(this.factionBonusMap.get(fac)));
        }
        highestBonus = Math.max(highestBonus, Math.abs(this.conquestBonus));
        this.particleMaxAge = 80 + (int) (Math.min(1.0f, highestBonus / 50.0f) * 220.0f);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.particleAge;
        if (this.particleAge >= this.particleMaxAge) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }
}
