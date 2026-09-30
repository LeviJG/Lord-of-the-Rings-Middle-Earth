package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityLamedonHillman: a hillman of Lamedon, charging even what he
 * cannot see, with an axe, battleaxe or pike, in a jacket and boots and no
 * leggings; one in three wears a leather hat, grey, dark or black half the
 * time, with a feather half the time.
 *
 * <p>NOT ported yet: nothing beyond the levyman's.
 */
public class LOTRLamedonHillmanEntity extends LOTRGondorLevymanEntity {

    public LOTRLamedonHillmanEntity(EntityType<? extends LOTRLamedonHillmanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, true);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {Items.IRON_AXE, LOTRCombatItems.IRON_BATTLEAXE, LOTRCombatItems.IRON_PIKE,
                LOTRToolItems.BRONZE_AXE, LOTRCombatItems.BRONZE_BATTLEAXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.LAMEDON_JACKET));
        if (this.random.nextInt(3) == 0) {
            int[] hatColours = {6316128, 2437173, 0};
            int[] featherColours = {16777215, 10526880, 5658198, 2179924, 798013};
            ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
            if (this.random.nextBoolean()) {
                hat.set(DataComponents.DYED_COLOR, new DyedItemColor(hatColours[this.random.nextInt(hatColours.length)]));
            }
            if (this.random.nextBoolean()) {
                hat.set(LOTRDataComponents.HAT_FEATHER, featherColours[this.random.nextInt(featherColours.length)]);
            }
            setItemSlot(EquipmentSlot.HEAD, hat);
        } else {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        }
        return data;
    }
}
