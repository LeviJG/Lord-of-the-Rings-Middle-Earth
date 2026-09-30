package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityIsengardSnaga: a lesser orc of Isengard, armed and armoured from
 * what it has scavenged -- any of 23 blades, axes, picks and pikes of stone,
 * iron, bronze and Uruk steel, one time in six a spear as well, and a helmet
 * (two times in three), chestplate, leggings and boots each of leather,
 * bronze, fur, bone or (but for the helmet) Uruk make. Now and then it leaves
 * something from an Uruk tent.
 *
 * <p>NOT ported yet: mini-quests (D14), the killIsengardSnaga achievement (D7).
 */
public class LOTRIsengardSnagaEntity extends LOTROrcEntity {

    private static final Item[] WEAPONS = {
            Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE, Items.IRON_SWORD, Items.IRON_AXE,
            Items.IRON_PICKAXE, LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.POISONED_IRON_DAGGER,
            LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.BRONZE_SWORD, LOTRToolItems.BRONZE_AXE,
            LOTRToolItems.BRONZE_PICKAXE, LOTRCombatItems.BRONZE_DAGGER, LOTRCombatItems.POISONED_BRONZE_DAGGER,
            LOTRCombatItems.BRONZE_BATTLEAXE, LOTRCombatItems.URUK_CLEAVER, LOTRToolItems.URUK_AXE,
            LOTRToolItems.URUK_PICKAXE, LOTRCombatItems.URUK_DAGGER, LOTRCombatItems.POISONED_URUK_DAGGER,
            LOTRCombatItems.URUK_BATTLEAXE, LOTRCombatItems.URUK_WARHAMMER, LOTRCombatItems.URUK_PIKE};
    private static final Item[] SPEARS = {
            LOTRCombatItems.IRON_SPEAR, LOTRCombatItems.BRONZE_SPEAR, LOTRCombatItems.STONE_SPEAR,
            LOTRCombatItems.URUK_SPEAR};
    private static final Item[] HELMETS = {
            Items.LEATHER_HELMET, LOTRCombatItems.BRONZE_HELMET, LOTRCombatItems.FUR_HAT,
            LOTRCombatItems.BONE_HELMET};
    private static final Item[] BODIES = {
            Items.LEATHER_CHESTPLATE, LOTRCombatItems.BRONZE_CHESTPLATE, LOTRCombatItems.FUR_TUNIC,
            LOTRCombatItems.BONE_CHESTPLATE, LOTRCombatItems.URUK_CHESTPLATE};
    private static final Item[] LEGS = {
            Items.LEATHER_LEGGINGS, LOTRCombatItems.BRONZE_LEGGINGS, LOTRCombatItems.FUR_LEGGINGS,
            LOTRCombatItems.BONE_LEGGINGS, LOTRCombatItems.URUK_LEGGINGS};
    private static final Item[] BOOTS = {
            Items.LEATHER_BOOTS, LOTRCombatItems.BRONZE_BOOTS, LOTRCombatItems.FUR_BOOTS,
            LOTRCombatItems.BONE_BOOTS, LOTRCombatItems.URUK_BOOTS};

    public LOTRIsengardSnagaEntity(EntityType<? extends LOTRIsengardSnagaEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ISENGARD;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public String getOrcSkirmishSpeech() {
        return "isengard/orc/skirmish";
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
    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.URUK_TENT, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(SPEARS[this.random.nextInt(SPEARS.length)]));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(BOOTS[this.random.nextInt(BOOTS.length)]));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LEGS[this.random.nextInt(LEGS.length)]));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(BODIES[this.random.nextInt(BODIES.length)]));
        if (this.random.nextInt(3) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(HELMETS[this.random.nextInt(HELMETS.length)]));
        }
        return data;
    }
}
