package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDwarfWarrior: a dwarf in dwarven armour, bare-headed one time in
 * ten, with a sword, battleaxe, warhammer, mattock or pike; one in six carries
 * a spear as well, falling back on his other weapon.
 *
 * <p>NOT ported yet: the Durin's Folk shield (LOTRShields.ALIGNMENT_DWARF,
 * D7), and throwing the spear (spears keep vanilla's mechanics, user).
 */
public class LOTRDwarfWarriorEntity extends LOTRDwarfEntity {

    public LOTRDwarfWarriorEntity(EntityType<? extends LOTRDwarfWarriorEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(switch (this.random.nextInt(7)) {
            case 0 -> LOTRCombatItems.DWARVEN_SWORD;
            case 1, 2 -> LOTRCombatItems.DWARVEN_BATTLEAXE;
            case 3, 4 -> LOTRCombatItems.DWARVEN_WARHAMMER;
            case 5 -> LOTRToolItems.DWARVEN_MATTOCK;
            default -> LOTRCombatItems.DWARVEN_PIKE;
        }));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.DWARVEN_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DWARVEN_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DWARVEN_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DWARVEN_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.DWARVEN_HELMET));
        }
        return data;
    }
}
