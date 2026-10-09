package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGondorSoldier: a soldier of Gondor in full Gondorian armour,
 * one in six on a barded horse. He fights with a warhammer, or now and then
 * a sword or pike; mounted, one in three couches a lance; and one in five
 * carries a spear as well, with his other weapon as backup.
 *
 * <p>NOT ported yet: throwing the spear (spears keep vanilla's mechanics,
 * user).
 */
public class LOTRGondorSoldierEntity extends LOTRGondorLevymanEntity {

    public LOTRGondorSoldierEntity(EntityType<? extends LOTRGondorSoldierEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_GONDOR;
        this.spawnRidingHorse = this.random.nextInt(6) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRGondorManEntity.createAttributes()
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75);
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.45, false);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND,
                    mounted ? this.npcItemsInv.getIdleItemMounted() : this.npcItemsInv.getIdleItem());
        } else {
            setItemSlot(EquipmentSlot.MAINHAND,
                    mounted ? this.npcItemsInv.getMeleeWeaponMounted() : this.npcItemsInv.getMeleeWeapon());
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        int i = this.random.nextInt(6);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(i < 4 ? LOTRCombatItems.GONDOR_WARHAMMER
                : i == 4 ? LOTRCombatItems.GONDOR_SWORD : LOTRCombatItems.GONDOR_PIKE));
        this.npcItemsInv.setMeleeWeaponMounted(this.random.nextInt(3) == 0
                ? new ItemStack(LOTRCombatItems.GONDOR_LANCE) : this.npcItemsInv.getMeleeWeapon().copy());
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GONDOR_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItemMounted(this.npcItemsInv.getMeleeWeaponMounted().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GONDOR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GONDOR_HELMET));
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        if (this.random.nextInt(8) == 0) {
            return LOTRMiniQuestFactory.GONDOR_KILL_RENEGADE.createQuest(this);
        }
        return super.createMiniQuest();
    }
}
