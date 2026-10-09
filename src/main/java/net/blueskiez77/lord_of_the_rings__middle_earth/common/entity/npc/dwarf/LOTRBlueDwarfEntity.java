package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBlueDwarf: the dwarves of the Blue Mountains, who marry only
 * their own. They eat without cram, fight back with a blue dwarven dagger,
 * and leave blue dwarven steel and the Blue Mountains' goods.
 *
 * <p>NOT ported yet: the killBlueDwarf and marryBlueDwarf achievements (D7).
 */
public class LOTRBlueDwarfEntity extends LOTRDwarfEntity {

    public LOTRBlueDwarfEntity(EntityType<? extends LOTRBlueDwarfEntity> type, Level level) {
        super(type, level);
        this.familyInfo.marriageEntityClass = LOTRBlueDwarfEntity.class;
    }

    @Override
    public LOTRFoods getDwarfFoods() {
        return LOTRFoods.BLUE_DWARF;
    }

    @Override
    public Item getDwarfSteelDrop() {
        return LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT;
    }

    @Override
    public LOTRChestContents.Pool getLarderDrops() {
        return LOTRChestContents.BLUE_DWARF_HOUSE_LARDER;
    }

    @Override
    public LOTRChestContents.Pool getGenericDrops() {
        return LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.BLUE_MOUNTAINS;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "blueDwarf/dwarf/hired";
            }
            return isBaby() ? "blueDwarf/child/friendly" : "blueDwarf/dwarf/friendly";
        }
        return isBaby() ? "blueDwarf/child/hostile" : "blueDwarf/dwarf/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BLUE_DWARVEN_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.BLUE_MOUNTAINS.createQuest(this);
    }
}
