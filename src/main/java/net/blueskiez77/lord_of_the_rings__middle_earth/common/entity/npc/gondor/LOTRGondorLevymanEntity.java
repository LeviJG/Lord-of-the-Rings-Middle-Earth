package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

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
 * LOTREntityGondorLevyman: Gondor's militia, always men, seeking out their
 * people's enemies -- chasing even what they cannot see. Armed with whatever
 * came to hand, in dyed leather and a gambeson, one in three with a helmet of
 * some sort.
 */
public class LOTRGondorLevymanEntity extends LOTRGondorManEntity {

    private static final int[] LEATHER_DYES = {10855845, 8026746, 5526612, 3684408, 8350297, 10388590, 4799795,
            5330539, 4211801, 2632504};

    public LOTRGondorLevymanEntity(EntityType<? extends LOTRGondorLevymanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createGondorAttackAI() {
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
            return this.hiredNPCInfo.getHiringPlayer() == player ? "gondor/soldier/hired" : "gondor/soldier/friendly";
        }
        return "gondor/soldier/hostile";
    }

    /** dyeLeather: one of the militia's colours. */
    protected ItemStack dyeLeather(Item leather) {
        ItemStack stack = new ItemStack(leather);
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(LEATHER_DYES[this.random.nextInt(LEATHER_DYES.length)]));
        return stack;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.GONDOR_SWORD, LOTRCombatItems.GONDOR_WARHAMMER, LOTRCombatItems.GONDOR_PIKE,
                Items.IRON_SWORD, Items.IRON_AXE, LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.IRON_PIKE,
                LOTRCombatItems.BRONZE_SWORD, LOTRToolItems.BRONZE_AXE, LOTRCombatItems.BRONZE_BATTLEAXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, dyeLeather(Items.LEATHER_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, dyeLeather(Items.LEATHER_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GONDOR_GAMBESON));
        if (this.random.nextInt(3) == 0) {
            setItemSlot(EquipmentSlot.HEAD, switch (this.random.nextInt(3)) {
                case 0 -> new ItemStack(LOTRCombatItems.GONDOR_HELMET);
                case 1 -> new ItemStack(Items.IRON_HELMET);
                default -> dyeLeather(Items.LEATHER_HELMET);
            });
        } else {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        }
        return data;
    }
}
