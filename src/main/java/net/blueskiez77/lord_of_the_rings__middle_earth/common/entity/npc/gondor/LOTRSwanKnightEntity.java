package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySwanKnight: a knight of Dol Amroth in its full armour, seeking
 * out Gondor's enemies, one in four on a barded horse. He fights with a
 * Dol Amroth sword, or one time in four a longspear; mounted, one in three
 * couches a lance.
 *
 * <p>NOT ported yet: the Dol Amroth shield (LOTRShields.ALIGNMENT_DOL_AMROTH,
 * D7) and the killSwanKnight achievement.
 */
public class LOTRSwanKnightEntity extends LOTRDolAmrothSoldierEntity {

    public LOTRSwanKnightEntity(EntityType<? extends LOTRSwanKnightEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(4) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRGondorManEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(LOTRNPCAttributes.HORSE_ATTACK_SPEED, 2.0)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
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
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(4) == 0
                ? LOTRCombatItems.DOL_AMROTH_LONGSPEAR : LOTRCombatItems.DOL_AMROTH_SWORD));
        this.npcItemsInv.setMeleeWeaponMounted(this.random.nextInt(3) == 0
                ? new ItemStack(LOTRCombatItems.DOL_AMROTH_LANCE) : this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItemMounted(this.npcItemsInv.getMeleeWeaponMounted().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DOL_AMROTH_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DOL_AMROTH_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DOL_AMROTH_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.DOL_AMROTH_HELMET));
        return data;
    }
}
