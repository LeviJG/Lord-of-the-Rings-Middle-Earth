package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDaleLevyman: Dale's militia, always men, seeking out their
 * people's enemies, armed with whatever came to hand, in dyed leather and a
 * Dale gambeson, one in three with a helmet of some sort.
 */
public class LOTRDaleLevymanEntity extends LOTRDaleManEntity {

    private static final int[] LEATHER_DYES = {7034184, 5650986, 7039851, 5331051, 2305612, 2698291, 1973790};

    public LOTRDaleLevymanEntity(EntityType<? extends LOTRDaleLevymanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createDaleAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, true);
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
            return this.hiredNPCInfo.getHiringPlayer() == player ? "dale/soldier/hired" : "dale/soldier/friendly";
        }
        return "dale/soldier/hostile";
    }

    protected ItemStack dyeLeather(Item leather) {
        ItemStack stack = new ItemStack(leather);
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(LEATHER_DYES[this.random.nextInt(LEATHER_DYES.length)]));
        return stack;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.DALE_SWORD, LOTRCombatItems.DALE_BATTLEAXE, LOTRCombatItems.DALE_PITCHFORK,
                Items.IRON_SWORD, Items.IRON_AXE, LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.IRON_PIKE,
                LOTRCombatItems.BRONZE_SWORD, LOTRToolItems.BRONZE_AXE, LOTRCombatItems.BRONZE_BATTLEAXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, dyeLeather(Items.LEATHER_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, dyeLeather(Items.LEATHER_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DALE_GAMBESON));
        if (this.random.nextInt(3) == 0) {
            setItemSlot(EquipmentSlot.HEAD, switch (this.random.nextInt(3)) {
                case 0 -> new ItemStack(LOTRCombatItems.DALE_HELMET);
                case 1 -> new ItemStack(Items.IRON_HELMET);
                default -> dyeLeather(Items.LEATHER_HELMET);
            });
        } else {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        }
        return data;
    }
}
