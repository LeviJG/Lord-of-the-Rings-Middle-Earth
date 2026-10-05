package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRHaradTurbanItem;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBanditHarad: a bandit of the south, with any bronze, iron,
 * Umbaric or Haradric dagger (poisoned or not), bare-headed or one time in
 * four in a turban of a robe's colour, one time in three set with a gold
 * ornament.
 */
public class LOTRBanditHaradEntity extends LOTRBanditEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.BRONZE_DAGGER, LOTRCombatItems.IRON_DAGGER,
            LOTRCombatItems.UMBARIC_DAGGER, LOTRCombatItems.POISONED_UMBARIC_DAGGER, LOTRCombatItems.HARADRIC_DAGGER,
            LOTRCombatItems.POISONED_HARADRIC_DAGGER};
    private static final int[] ROBE_COLOURS = {3354412, 5984843, 5968655, 3619908, 9007463, 3228720};

    public LOTRBanditHaradEntity(EntityType<? extends LOTRBanditHaradEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon());
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        if (this.random.nextInt(4) == 0) {
            ItemStack turban = LOTRNearHaradrimBaseEntity.turban(ROBE_COLOURS[this.random.nextInt(ROBE_COLOURS.length)]);
            setItemSlot(EquipmentSlot.HEAD, LOTRHaradTurbanItem.setHasOrnament(turban, this.random.nextInt(3) == 0));
        }
        return data;
    }
}
