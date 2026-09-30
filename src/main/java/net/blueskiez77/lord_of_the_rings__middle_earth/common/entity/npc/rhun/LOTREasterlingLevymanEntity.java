package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityEasterlingLevyman: a levied man of Rhûn with whatever weapon came
 * to hand (one in five also a spear), leather or bronze armour piece by
 * piece and no helmet; half the time a kaftan of one of six colours over his
 * chest, and half of those its leggings too. He seeks out Rhûn's enemies.
 */
public class LOTREasterlingLevymanEntity extends LOTREasterlingEntity {

    private static final Item[] LEVY_WEAPONS = {LOTRCombatItems.RHUNIC_DAGGER, LOTRCombatItems.POISONED_RHUNIC_DAGGER,
            LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.BRONZE_DAGGER, LOTRCombatItems.RHUNIC_SWORD,
            LOTRCombatItems.RHUNIC_BATTLEAXE, Items.IRON_SWORD, LOTRCombatItems.BRONZE_SWORD,
            LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.BRONZE_BATTLEAXE, LOTRCombatItems.RHUNIC_SPEAR,
            LOTRCombatItems.IRON_SPEAR, LOTRCombatItems.BRONZE_SPEAR};
    private static final Item[] LEVY_SPEARS = {LOTRCombatItems.RHUNIC_SPEAR, LOTRCombatItems.IRON_SPEAR,
            LOTRCombatItems.BRONZE_SPEAR};
    private static final Item[] LEVY_BODIES = {Items.LEATHER_CHESTPLATE, LOTRCombatItems.BRONZE_CHESTPLATE};
    private static final Item[] LEVY_LEGS = {Items.LEATHER_LEGGINGS, LOTRCombatItems.BRONZE_LEGGINGS};
    private static final Item[] LEVY_BOOTS = {Items.LEATHER_BOOTS, LOTRCombatItems.BRONZE_BOOTS};
    private static final int[] KAFTAN_COLOURS = {14823729, 11862016, 5512477, 14196753, 11374145, 7366222};

    public LOTREasterlingLevymanEntity(EntityType<? extends LOTREasterlingLevymanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "rhun/warrior/hired" : "rhun/warrior/friendly";
        }
        return "rhun/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LEVY_WEAPONS[this.random.nextInt(LEVY_WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LEVY_SPEARS[this.random.nextInt(LEVY_SPEARS.length)]));
        }
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LEVY_BOOTS[this.random.nextInt(LEVY_BOOTS.length)]));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LEVY_LEGS[this.random.nextInt(LEVY_LEGS.length)]));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LEVY_BODIES[this.random.nextInt(LEVY_BODIES.length)]));
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        if (this.random.nextBoolean()) {
            int kaftanColour = KAFTAN_COLOURS[this.random.nextInt(KAFTAN_COLOURS.length)];
            setItemSlot(EquipmentSlot.CHEST, kaftan(LOTRMiscItems.KAFTAN, kaftanColour));
            if (this.random.nextBoolean()) {
                setItemSlot(EquipmentSlot.LEGS, kaftan(LOTRMiscItems.KAFTAN_LEGGINGS, kaftanColour));
            }
        }
        return data;
    }
}
