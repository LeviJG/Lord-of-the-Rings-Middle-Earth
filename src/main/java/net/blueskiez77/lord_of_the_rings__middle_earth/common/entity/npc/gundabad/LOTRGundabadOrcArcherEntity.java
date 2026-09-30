package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gundabad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGundabadOrcArcher: a Gundabad orc who shoots instead, at 16
 * blocks -- one time in four with an iron or bronze crossbow (leaving bolts),
 * else with an orc bow or a plain bow (leaving arrows).
 */
public class LOTRGundabadOrcArcherEntity extends LOTRGundabadOrcEntity {

    public LOTRGundabadOrcArcherEntity(EntityType<? extends LOTRGundabadOrcArcherEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 30, 60, 16.0f);
    }

    /** isCrossbowOrc. */
    public boolean isCrossbowOrc() {
        return this.npcItemsInv.getRangedWeapon().getItem() instanceof CrossbowItem;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (isCrossbowOrc()) {
            npcCrossbowAttack(target, power);
        } else {
            npcArrowAttack(target, power);
        }
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getRangedWeapon());
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        dropNPCAmmo(level, isCrossbowOrc() ? LOTRCombatItems.CROSSBOW_BOLT : Items.ARROW, looting);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (this.random.nextInt(4) == 0) {
            this.npcItemsInv.setRangedWeapon(new ItemStack(this.random.nextBoolean()
                    ? LOTRCombatItems.IRON_CROSSBOW : LOTRCombatItems.BRONZE_CROSSBOW));
        } else {
            this.npcItemsInv.setRangedWeapon(new ItemStack(this.random.nextBoolean() ? LOTRCombatItems.ORC_BOW : Items.BOW));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
