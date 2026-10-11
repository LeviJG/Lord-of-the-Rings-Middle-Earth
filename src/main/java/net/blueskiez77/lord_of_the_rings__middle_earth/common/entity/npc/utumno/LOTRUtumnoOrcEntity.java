package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityUtumnoOrc: an orc of Utumno, man-sized and no weak orc, 24 strong, in Utumno armour (its
 * helmet nine times in ten) with an Utumno sword, battleaxe, dagger (poisoned or not) or warhammer, a
 * spear one time in six. It does not skirmish, speaks deeper, and one in five bears an Elvish name.
 */
public class LOTRUtumnoOrcEntity extends LOTROrcEntity {

    public LOTRUtumnoOrcEntity(EntityType<? extends LOTRUtumnoOrcEntity> type, Level level) {
        super(type, level);
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTROrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public boolean canOrcSkirmish() {
        return false;
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, true);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_UTUMNO_ORC;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.65f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return "utumno/orc/hostile";
    }

    @Override
    public void setupNPCName() {
        if (this.random.nextInt(5) == 0) {
            this.familyInfo.setName(LOTRNames.getSindarinOrQuenyaName(this.random, this.random.nextBoolean()));
        } else {
            this.familyInfo.setName(LOTRNames.getOrcName(this.random));
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item weapon = switch (this.random.nextInt(6)) {
            case 0, 1 -> LOTRCombatItems.UTUMNO_SWORD;
            case 2 -> LOTRCombatItems.UTUMNO_BATTLEAXE;
            case 3 -> LOTRCombatItems.UTUMNO_DAGGER;
            case 4 -> LOTRCombatItems.POISONED_UTUMNO_DAGGER;
            default -> LOTRCombatItems.UTUMNO_WARHAMMER;
        };
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapon));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UTUMNO_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.UTUMNO_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.UTUMNO_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.UTUMNO_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.UTUMNO_HELMET));
        }
        return data;
    }
}
