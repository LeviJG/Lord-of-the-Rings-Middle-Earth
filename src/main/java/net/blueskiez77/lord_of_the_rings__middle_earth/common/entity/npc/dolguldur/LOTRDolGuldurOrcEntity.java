package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dolguldur;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRMirkwoodSpiderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDolGuldurOrc: an orc of Dol Guldur in Dol Guldur armour
 * (bare-headed one time in five), with a battleaxe, dagger (poisoned or not),
 * sword, warhammer, pickaxe, axe or spike; one in six carries a spear as well,
 * falling back on the other. Sent out mounted (by a respawner), it rides a
 * Mirkwood spider -- a banner bearer excepted. Now and then it leaves
 * something from a Dol Guldur tent.
 *
 * <p>NOT ported yet: the killDolGuldurOrc achievement (D7).
 */
public class LOTRDolGuldurOrcEntity extends LOTROrcEntity {

    public LOTRDolGuldurOrcEntity(EntityType<? extends LOTRDolGuldurOrcEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTROrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DOL_GULDUR;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public String getOrcSkirmishSpeech() {
        return "dolGuldur/orc/skirmish";
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "dolGuldur/orc/hired";
            }
            return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f
                    ? "dolGuldur/orc/friendly" : "dolGuldur/orc/neutral";
        }
        return "dolGuldur/orc/hostile";
    }

    @Override
    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.DOL_GULDUR_TENT, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.DOL_GULDUR_BATTLEAXE, LOTRCombatItems.DOL_GULDUR_DAGGER,
                LOTRCombatItems.POISONED_DOL_GULDUR_DAGGER, LOTRCombatItems.DOL_GULDUR_SWORD,
                LOTRCombatItems.DOL_GULDUR_WARHAMMER, LOTRToolItems.DOL_GULDUR_PICKAXE, LOTRToolItems.DOL_GULDUR_AXE,
                LOTRCombatItems.DOL_GULDUR_SPIKE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.DOL_GULDUR_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DOL_GULDUR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DOL_GULDUR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DOL_GULDUR_CHESTPLATE));
        if (this.random.nextInt(5) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.DOL_GULDUR_HELMET));
        }
        // After whatever mount the NPC base gave it -- the original too left
        // any such horse standing riderless.
        if (this.spawnRidingHorse && !(this instanceof LOTRBannerBearer)) {
            ServerLevel server = level.getLevel();
            LOTRMirkwoodSpiderEntity spider = LOTREntities.MIRKWOOD_SPIDER.create(server, EntitySpawnReason.JOCKEY);
            if (spider != null) {
                spider.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
                if (server.noCollision(spider)) {
                    spider.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, null);
                    spider.isNPCPersistent = this.isNPCPersistent;
                    server.addFreshEntity(spider);
                    startRiding(spider, true, false);
                }
            }
        }
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.DOL_GULDUR.createQuest(this);
    }
}
