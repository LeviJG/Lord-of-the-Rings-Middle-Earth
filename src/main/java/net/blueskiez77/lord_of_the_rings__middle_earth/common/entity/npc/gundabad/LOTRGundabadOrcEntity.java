package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gundabad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
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
 * LOTREntityGundabadOrc: an orc of Gundabad, armed and armoured from
 * whatever it has scavenged -- any of 45 blades, axes, picks and polearms of
 * stone, iron, bronze and the orcs' own steel, one time in six a spear as
 * well, and a helmet (two times in three), chestplate, leggings and boots each
 * drawn from leather, bronze, fur, bone, Angmar, Mordor, Dol Guldur or
 * Gundabad Uruk gear. Now and then it leaves something from a Gundabad tent,
 * and one time in 4000 a block of dirt named "Such Wealth".
 *
 * <p>NOT ported yet: the killGundabadOrc achievement (D7).
 */
public class LOTRGundabadOrcEntity extends LOTROrcEntity {

    private static final Item[] WEAPONS = {
            Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE, Items.IRON_SWORD, Items.IRON_AXE,
            Items.IRON_PICKAXE, LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.POISONED_IRON_DAGGER,
            LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.BRONZE_SWORD, LOTRToolItems.BRONZE_AXE,
            LOTRToolItems.BRONZE_PICKAXE, LOTRCombatItems.BRONZE_DAGGER, LOTRCombatItems.POISONED_BRONZE_DAGGER,
            LOTRCombatItems.BRONZE_BATTLEAXE, LOTRCombatItems.ANGMAR_SWORD, LOTRToolItems.ANGMAR_AXE,
            LOTRToolItems.ANGMAR_PICKAXE, LOTRCombatItems.ANGMAR_DAGGER, LOTRCombatItems.POISONED_ANGMAR_DAGGER,
            LOTRCombatItems.ANGMAR_BATTLEAXE, LOTRCombatItems.ANGMAR_WARHAMMER, LOTRCombatItems.MORDOR_SCIMITAR,
            LOTRToolItems.MORDOR_AXE, LOTRToolItems.MORDOR_PICKAXE, LOTRCombatItems.MORDOR_DAGGER,
            LOTRCombatItems.POISONED_MORDOR_DAGGER, LOTRCombatItems.MORDOR_BATTLEAXE,
            LOTRCombatItems.MORDOR_WARHAMMER, LOTRCombatItems.MORDOR_WARSCYTHE, LOTRCombatItems.DOL_GULDUR_SWORD,
            LOTRToolItems.DOL_GULDUR_AXE, LOTRToolItems.DOL_GULDUR_PICKAXE, LOTRCombatItems.DOL_GULDUR_DAGGER,
            LOTRCombatItems.POISONED_DOL_GULDUR_DAGGER, LOTRCombatItems.DOL_GULDUR_BATTLEAXE,
            LOTRCombatItems.DOL_GULDUR_WARHAMMER, LOTRCombatItems.GUNDABAD_URUK_CLEAVER,
            LOTRCombatItems.GUNDABAD_URUK_WARAXE, LOTRCombatItems.GUNDABAD_URUK_BLUDGEON,
            LOTRCombatItems.GUNDABAD_URUK_PIKE, LOTRCombatItems.GUNDABAD_URUK_DAGGER,
            LOTRCombatItems.POISONED_GUNDABAD_URUK_DAGGER, LOTRCombatItems.ANGMAR_POLEAXE,
            LOTRCombatItems.DOL_GULDUR_SPIKE};
    private static final Item[] SPEARS = {
            LOTRCombatItems.IRON_SPEAR, LOTRCombatItems.BRONZE_SPEAR, LOTRCombatItems.STONE_SPEAR,
            LOTRCombatItems.ANGMAR_SPEAR, LOTRCombatItems.MORDOR_SPEAR, LOTRCombatItems.DOL_GULDUR_SPEAR,
            LOTRCombatItems.GUNDABAD_URUK_SPEAR};
    private static final Item[] HELMETS = {
            Items.LEATHER_HELMET, LOTRCombatItems.BRONZE_HELMET, LOTRCombatItems.FUR_HAT,
            LOTRCombatItems.BONE_HELMET, LOTRCombatItems.ANGMAR_HELMET, LOTRCombatItems.MORDOR_HELMET,
            LOTRCombatItems.DOL_GULDUR_HELMET};
    private static final Item[] BODIES = {
            Items.LEATHER_CHESTPLATE, LOTRCombatItems.BRONZE_CHESTPLATE, LOTRCombatItems.FUR_TUNIC,
            LOTRCombatItems.BONE_CHESTPLATE, LOTRCombatItems.ANGMAR_CHESTPLATE, LOTRCombatItems.MORDOR_CHESTPLATE,
            LOTRCombatItems.DOL_GULDUR_CHESTPLATE, LOTRCombatItems.GUNDABAD_URUK_CHESTPLATE};
    private static final Item[] LEGS = {
            Items.LEATHER_LEGGINGS, LOTRCombatItems.BRONZE_LEGGINGS, LOTRCombatItems.FUR_LEGGINGS,
            LOTRCombatItems.BONE_LEGGINGS, LOTRCombatItems.ANGMAR_LEGGINGS, LOTRCombatItems.MORDOR_LEGGINGS,
            LOTRCombatItems.DOL_GULDUR_LEGGINGS, LOTRCombatItems.GUNDABAD_URUK_LEGGINGS};
    private static final Item[] BOOTS = {
            Items.LEATHER_BOOTS, LOTRCombatItems.BRONZE_BOOTS, LOTRCombatItems.FUR_BOOTS,
            LOTRCombatItems.BONE_BOOTS, LOTRCombatItems.ANGMAR_BOOTS, LOTRCombatItems.MORDOR_BOOTS,
            LOTRCombatItems.DOL_GULDUR_BOOTS, LOTRCombatItems.GUNDABAD_URUK_BOOTS};

    public LOTRGundabadOrcEntity(EntityType<? extends LOTRGundabadOrcEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.GUNDABAD;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public String getOrcSkirmishSpeech() {
        return "gundabad/orc/skirmish";
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "gundabad/orc/hired";
            }
            return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f
                    ? "gundabad/orc/friendly" : "gundabad/orc/neutral";
        }
        return "gundabad/orc/hostile";
    }

    @Override
    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.GUNDABAD_TENT, 1, 2 + looting);
        }
        if (this.random.nextInt(4000) == 0) {
            ItemStack dirt = new ItemStack(Items.DIRT);
            dirt.set(DataComponents.CUSTOM_NAME, Component.literal("Such Wealth"));
            spawnAtLocation(level, dirt);
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

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.GUNDABAD.createQuest(this);
    }
}
