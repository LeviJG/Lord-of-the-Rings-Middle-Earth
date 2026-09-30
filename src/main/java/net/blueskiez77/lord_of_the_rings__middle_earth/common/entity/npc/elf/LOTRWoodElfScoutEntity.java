package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityWoodElfScout: a Wood-elf in scout's garb -- nine in ten hooded --
 * with its bow in hand, shooting fast from 24 blocks. Pressed within four
 * blocks by whoever last hurt it, one tick in twenty it vanishes, to reappear
 * somewhere clear on solid ground between six and sixteen blocks off.
 *
 * <p>NOT ported yet: the green leaves it leaves as it vanishes (with the leaf
 * particles, deferred by the user).
 */
public class LOTRWoodElfScoutEntity extends LOTRWoodElfEntity {

    public LOTRWoodElfScoutEntity(EntityType<? extends LOTRWoodElfScoutEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRElfEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 25, 35, 24.0f);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "woodElf/elf/hired";
            }
            return isTrusted(player) ? "woodElf/warrior/friendly" : "woodElf/elf/neutral";
        }
        return "woodElf/warrior/hostile";
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel level) || !isAlive() || getVehicle() != null
                || !(getMainHandItem().getItem() instanceof BowItem)) {
            return;
        }
        LivingEntity lastAttacker = getLastHurtByMob();
        if (lastAttacker == null || distanceToSqr(lastAttacker) >= 16.0 || this.random.nextInt(20) != 0) {
            return;
        }
        for (int l = 0; l < 32; ++l) {
            int i = getBlockX() - this.random.nextInt(16) + this.random.nextInt(16);
            int j = getBlockY() - this.random.nextInt(3) + this.random.nextInt(3);
            int k = getBlockZ() - this.random.nextInt(16) + this.random.nextInt(16);
            BlockPos pos = new BlockPos(i, j, k);
            if (Math.sqrt(distanceToSqr(i, j, k)) <= 6.0
                    || !level.getBlockState(pos.below()).isRedstoneConductor(level, pos.below())
                    || level.getBlockState(pos).isRedstoneConductor(level, pos)
                    || level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above())) {
                continue;
            }
            double d = i + 0.5;
            double d1 = j;
            double d2 = k + 0.5;
            AABB aabb = getBoundingBox().move(d - getX(), d1 - getY(), d2 - getZ());
            if (!level.noCollision(this, aabb) || level.containsAnyLiquid(aabb)) {
                continue;
            }
            playSound(LOTRSounds.ELF_WOOD_ELF_TELEPORT, getSoundVolume(), 0.5f + this.random.nextFloat());
            setPos(d, d1, d2);
            break;
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.WOOD_ELVEN_SCOUT_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.WOOD_ELVEN_SCOUT_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.WOOD_ELVEN_SCOUT_TUNIC));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.WOOD_ELVEN_SCOUT_HOOD));
        }
        return data;
    }
}
