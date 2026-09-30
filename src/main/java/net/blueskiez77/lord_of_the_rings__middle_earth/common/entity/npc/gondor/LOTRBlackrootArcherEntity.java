package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/** LOTREntityBlackrootArcher (the Blackroot Vale Bowman): a soldier on foot with a Blackroot bow, far-sighted and
 * shooting from 24 blocks, if less truly than most; he leaves arrows. */
public class LOTRBlackrootArcherEntity extends LOTRBlackrootSoldierEntity {

    public LOTRBlackrootArcherEntity(EntityType<? extends LOTRBlackrootArcherEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRGondorSoldierEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5);
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.45, 30, 40, 24.0f);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getRangedWeapon());
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        dropNPCAmmo(level, Items.ARROW, looting);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.BLACKROOT_BOW));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        return data;
    }
}
