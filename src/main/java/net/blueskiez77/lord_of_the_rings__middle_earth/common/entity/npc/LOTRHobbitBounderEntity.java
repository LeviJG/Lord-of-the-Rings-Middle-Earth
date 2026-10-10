package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPebbleEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHobbitTargetRuffianGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHobbitBounder (registered as "HobbitShirriff", the Hobbit
 * Bounder): the Shire's watch. It keeps none of a hobbit's homely ways, seeks
 * out the Shire's enemies, and fights with a dagger up close and a sling from
 * afar, in a green hat with a white feather.
 *
 * One in three rides a Shire pony. In the Shire it hunts Bree ruffians.
 */
public class LOTRHobbitBounderEntity extends LOTRHobbitEntity implements RangedAttackMob {

    private @Nullable Goal rangedAttackAI;
    private @Nullable Goal meleeAttackAI;

    public LOTRHobbitBounderEntity(EntityType<? extends LOTRHobbitBounderEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(3) == 0;
    }

    /** createMountToRide: a Shire pony. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        return LOTREntities.SHIRE_PONY.create(level, EntitySpawnReason.JOCKEY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRHobbitEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(LOTRNPCAttributes.HORSE_ATTACK_SPEED, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        int target = addTargetTasks(true);
        this.targetSelector.addGoal(target + 1, new LOTRHobbitTargetRuffianGoal(this, 0, true));
    }

    protected Goal createHobbitMeleeAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    protected Goal createHobbitRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.5, 20, 40, 12.0f);
    }

    /** attackEntityWithRangedAttack: a slung pebble, aimed as an arrow at the target. */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        LOTRPebbleEntity pebble = new LOTRPebbleEntity(LOTREntities.PEBBLE, this, level,
                new ItemStack(LOTRCombatItems.PEBBLE)).setSlung();
        double dx = target.getX() - getX();
        double dy = target.getY() + target.getBbHeight() / 3.0 - pebble.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        pebble.shoot(dx, dy + horizontal * 0.2, dz, 1.0f, 0.5f);
        playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
        level.addFreshEntity(pebble);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = createHobbitMeleeAttackAI();
            this.rangedAttackAI = createHobbitRangedAttackAI();
        }
        this.goalSelector.removeGoal(this.meleeAttackAI);
        this.goalSelector.removeGoal(this.rangedAttackAI);
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else if (mode == AttackMode.MELEE) {
            this.goalSelector.addGoal(2, this.meleeAttackAI);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        } else if (mode == AttackMode.RANGED) {
            this.goalSelector.addGoal(2, this.rangedAttackAI);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(3) < 2
                ? LOTRCombatItems.IRON_DAGGER : LOTRCombatItems.BRONZE_DAGGER));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.SLING));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.HEAD, hat(6834742, 16777215));
        return data;
    }

    /** A leather hat of the given colour, with a feather of the given colour or none (-1). */
    public static ItemStack hat(int colour, int feather) {
        ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
        hat.set(DataComponents.DYED_COLOR, new DyedItemColor(colour));
        if (feather != -1) {
            hat.set(LOTRDataComponents.HAT_FEATHER, feather);
        }
        return hat;
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int dropChance = Math.max(1, 10 - looting * 2);
        if (this.random.nextInt(dropChance) == 0) {
            spawnAtLocation(level, new ItemStack(LOTRCombatItems.PEBBLE,
                    1 + this.random.nextInt(3) + this.random.nextInt(looting + 1)));
        }
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 2 + this.random.nextInt(3);
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "hobbit/bounder/hired" : "hobbit/bounder/friendly";
        }
        return "hobbit/bounder/hostile";
    }
}
