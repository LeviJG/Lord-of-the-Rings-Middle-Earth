package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRMercenary;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMoredainMercenary: a Moredain who fights for Near Harad and hires
 * himself out, for 20 coins, to anyone Near Harad does not dislike. Two times
 * in three an Umbaric weapon, else Haradric bronze, and one in five a spear of
 * the same kind as well; Gulf, Harnennor, Umbar or coast armour (1 in 8, else
 * 1 in 5, else 1 in 3, else the coast's), and a turban of one of six colours
 * nine times in ten. He never rides out.
 *
 * <p>NOT ported yet: the mercenary's screens (D16).
 */
public class LOTRMoredainMercenaryEntity extends LOTRMoredainEntity implements LOTRMercenary {

    private static final Item[] WEAPONS_IRON = {LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_SCIMITAR,
            LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_DAGGER, LOTRCombatItems.POISONED_UMBARIC_DAGGER,
            LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_MACE,
            LOTRCombatItems.UMBARIC_PIKE};
    private static final Item[] WEAPONS_BRONZE = {LOTRCombatItems.HARADRIC_SWORD, LOTRCombatItems.HARADRIC_SWORD,
            LOTRCombatItems.HARADRIC_SWORD, LOTRCombatItems.HARADRIC_DAGGER, LOTRCombatItems.POISONED_HARADRIC_DAGGER,
            LOTRCombatItems.HARADRIC_PIKE};
    private static final int[] TURBAN_COLOURS = {10487808, 5976610, 14864579, 10852752, 11498561, 12361037};

    public LOTRMoredainMercenaryEntity(EntityType<? extends LOTRMoredainMercenaryEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_MOREDAIN;
        this.spawnRidingHorse = false;
    }

    @Override
    protected Goal createHaradrimAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.7, true);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.NEAR_HARAD;
    }

    @Override
    public LOTRFaction getHiringFaction() {
        return LOTRFaction.NEAR_HARAD;
    }

    @Override
    public float getMercAlignmentRequired() {
        return 0.0f;
    }

    @Override
    public int getMercBaseCost() {
        return 20;
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
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
            return this.hiredNPCInfo.getHiringPlayer() == player ? "nearHarad/mercenary/hired" : "nearHarad/mercenary/friendly";
        }
        return "nearHarad/mercenary/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (this.random.nextInt(3) == 0) {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS_BRONZE[this.random.nextInt(WEAPONS_BRONZE.length)]));
            if (this.random.nextInt(5) == 0) {
                this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
                this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.HARADRIC_SPEAR));
            }
        } else {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS_IRON[this.random.nextInt(WEAPONS_IRON.length)]));
            if (this.random.nextInt(5) == 0) {
                this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
                this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UMBARIC_SPEAR));
            }
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        if (this.random.nextInt(8) == 0) {
            wear(LOTRCombatItems.GULFEN_BOOTS, LOTRCombatItems.GULFEN_LEGGINGS, LOTRCombatItems.GULFEN_CHESTPLATE);
        } else if (this.random.nextInt(5) == 0) {
            wear(LOTRCombatItems.HARNENNOR_BOOTS, LOTRCombatItems.HARNENNOR_LEGGINGS, LOTRCombatItems.HARNENNOR_CHESTPLATE);
        } else if (this.random.nextInt(3) == 0) {
            wear(LOTRCombatItems.UMBARIC_BOOTS, LOTRCombatItems.UMBARIC_LEGGINGS, LOTRCombatItems.UMBARIC_CHESTPLATE);
        } else {
            wear(LOTRCombatItems.COAST_SOUTHRON_BOOTS, LOTRCombatItems.COAST_SOUTHRON_LEGGINGS,
                    LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE);
        }
        if (this.random.nextInt(10) == 0) {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        } else {
            setItemSlot(EquipmentSlot.HEAD,
                    LOTRNearHaradrimBaseEntity.turban(TURBAN_COLOURS[this.random.nextInt(TURBAN_COLOURS.length)]));
        }
        return data;
    }

    private void wear(Item boots, Item legs, Item body) {
        setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(legs));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(body));
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.HIRE_MOREDAIN_MERCENARY);
    }
}
