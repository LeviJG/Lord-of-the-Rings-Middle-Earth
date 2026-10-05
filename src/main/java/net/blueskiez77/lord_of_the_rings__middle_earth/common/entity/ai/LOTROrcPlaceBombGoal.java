package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryHiredReplacedItems;
import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTROrcBombEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIOrcPlaceBomb: a bombardier with a bomb runs at its target and,
 * within three blocks, drops the bomb lit at its own feet -- with a cry -- and
 * falls back on its blade. The bomb breaks terrain only if the orc was hired
 * or aiming at a player (see {@link LOTROrcBombEntity}).
 */
public class LOTROrcPlaceBombGoal extends Goal {

    private final LOTROrcEntity attacker;
    private final double moveSpeed;
    private @Nullable LivingEntity entityTarget;
    private @Nullable Path path;
    private int rePathDelay;

    public LOTROrcPlaceBombGoal(LOTROrcEntity attacker, double speed) {
        this.attacker = attacker;
        this.moveSpeed = speed;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.attacker.getTarget();
        if (target == null || this.attacker.npcItemsInv.getBomb().isEmpty()) {
            return false;
        }
        this.entityTarget = target;
        this.path = this.attacker.getNavigation().createPath(target, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.attacker.npcItemsInv.getBomb().isEmpty()) {
            return false;
        }
        return this.attacker.getTarget() != null && this.entityTarget.isAlive() && !this.attacker.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.moveSpeed);
        this.rePathDelay = 0;
    }

    @Override
    public void stop() {
        this.entityTarget = null;
        this.attacker.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entityTarget;
        this.attacker.getLookControl().setLookAt(target, 30.0f, 30.0f);
        if (this.attacker.getSensing().hasLineOfSight(target) && --this.rePathDelay <= 0) {
            this.rePathDelay = 4 + this.attacker.getRandom().nextInt(7);
            this.attacker.getNavigation().moveTo(target, this.moveSpeed);
        }
        if (this.attacker.distanceTo(target) < 3.0f) {
            ItemStack bombItem = this.attacker.npcItemsInv.getBomb();
            BlockState bombState = bombItem.getItem() instanceof BlockItem block
                    ? block.getBlock().defaultBlockState() : LOTRCombatBlocks.ORC_BOMB.defaultBlockState();
            LOTROrcBombEntity bomb = new LOTROrcBombEntity(LOTREntities.ORC_BOMB, this.attacker.level(),
                    this.attacker.getX(), this.attacker.getY() + 1.0, this.attacker.getZ(), this.attacker, bombState);
            bomb.droppedByHiredUnit = this.attacker.hiredNPCInfo.isActive;
            bomb.droppedTargetingPlayer = target instanceof Player;
            this.attacker.level().addFreshEntity(bomb);
            this.attacker.playSound(SoundEvents.TNT_PRIMED, 1.0f, 1.0f);
            this.attacker.playSound(LOTRSounds.ORC_FIRE, 1.0f,
                    (this.attacker.getRandom().nextFloat() - this.attacker.getRandom().nextFloat()) * 0.2f + 1.0f);
            this.attacker.npcItemsInv.setBomb(ItemStack.EMPTY);
            if (this.attacker.hiredReplacedInv.hasReplacedEquipment(LOTRInventoryHiredReplacedItems.BOMB)) {
                this.attacker.hiredReplacedInv.onEquipmentChanged(LOTRInventoryHiredReplacedItems.BOMB, ItemStack.EMPTY);
            }
            this.attacker.refreshCurrentAttackMode();
        }
    }
}
