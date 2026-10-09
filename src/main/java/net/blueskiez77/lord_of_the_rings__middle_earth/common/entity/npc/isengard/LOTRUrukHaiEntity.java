package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

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
 * LOTREntityUrukHai: a fighting Uruk of Isengard, man-sized and no weak orc,
 * in Uruk armour (bare-headed one time in ten) with a cleaver or pike (two
 * times in eight each), battleaxe, dagger (poisoned or not) or warhammer, and
 * a spear one time in six. It never skirmishes, speaks lower, and leaves Uruk
 * steel and things from an Uruk tent.
 *
 * <p>NOT ported yet: the killUrukHai and raidUrukCamp achievements (D7; the
 * latter asked for a player killing one near an Uruk NPC respawner).
 */
public class LOTRUrukHaiEntity extends LOTROrcEntity {

    public LOTRUrukHaiEntity(EntityType<? extends LOTRUrukHaiEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_URUK_HAI;
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTROrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 26.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    public boolean canOrcSkirmish() {
        return false;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    protected Item getOrcSteelDrop() {
        return LOTRMaterialItems.URUK_STEEL_INGOT;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75f;
    }

    @Override
    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.URUK_TENT, 1, 2 + looting);
        }
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ISENGARD;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "isengard/orc/hired";
            }
            return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f
                    ? "isengard/orc/friendly" : "isengard/orc/neutral";
        }
        return "isengard/orc/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(switch (this.random.nextInt(8)) {
            case 0, 1 -> LOTRCombatItems.URUK_CLEAVER;
            case 2, 3 -> LOTRCombatItems.URUK_PIKE;
            case 4 -> LOTRCombatItems.URUK_BATTLEAXE;
            case 5 -> LOTRCombatItems.URUK_DAGGER;
            case 6 -> LOTRCombatItems.POISONED_URUK_DAGGER;
            default -> LOTRCombatItems.URUK_WARHAMMER;
        }));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.URUK_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.URUK_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.URUK_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.URUK_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.URUK_HELMET));
        }
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.ISENGARD.createQuest(this);
    }
}
