package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBandit;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRValuableItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIBanditSteal: a bandit with empty pockets picks a player within
 * 32 blocks it may rob and who has something outside their hand, tells them
 * so (unless it just chased them), and runs at them for up to thirty seconds.
 * Within reach it takes one to its maximum thefts of up to eight items each:
 * coins always if it can, and then by preference valuables (gems, the metals
 * of good tools, rings, armour), then weapons and tools, then pouches, then anything. The
 * player is told, and the bandit drops any fight to make off with it.
 *
 * <p>NOT ported yet: cancelling the player's fast travel (D13).
 */
public class LOTRBanditStealGoal extends Goal {

    private final LOTRBandit theBandit;
    private final LOTRNPCEntity theBanditAsNPC;
    private final double speed;
    private @Nullable Player targetPlayer;
    private @Nullable Player prevTargetPlayer;
    private int chaseTimer;
    private int rePathDelay;

    public LOTRBanditStealGoal(LOTRBandit bandit, double speed) {
        this.theBandit = bandit;
        this.theBanditAsNPC = bandit.getBanditAsNPC();
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.theBandit.getBanditInventory().isEmpty()) {
            return false;
        }
        List<Player> validTargets = new ArrayList<>();
        for (Player player : this.theBanditAsNPC.level().getEntitiesOfClass(Player.class,
                this.theBanditAsNPC.getBoundingBox().inflate(32.0))) {
            if (!player.isCreative() && this.theBandit.canTargetPlayerForTheft(player)
                    && LOTRBandit.canStealFromPlayerInv(player)) {
                validTargets.add(player);
            }
        }
        if (validTargets.isEmpty()) {
            return false;
        }
        this.targetPlayer = validTargets.get(this.theBanditAsNPC.getRandom().nextInt(validTargets.size()));
        if (this.targetPlayer != this.prevTargetPlayer) {
            this.theBanditAsNPC.sendSpeechBank(this.targetPlayer, this.theBandit.getTheftSpeechBank(this.targetPlayer));
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetPlayer == null || !this.targetPlayer.isAlive() || this.targetPlayer.isCreative()
                || !LOTRBandit.canStealFromPlayerInv(this.targetPlayer)) {
            return false;
        }
        return this.chaseTimer > 0 && this.theBanditAsNPC.distanceToSqr(this.targetPlayer) < 256.0;
    }

    @Override
    public void start() {
        this.chaseTimer = 600;
    }

    @Override
    public void stop() {
        this.chaseTimer = 0;
        this.rePathDelay = 0;
        if (this.targetPlayer != null) {
            this.prevTargetPlayer = this.targetPlayer;
        }
        this.targetPlayer = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.targetPlayer == null) {
            return;
        }
        --this.chaseTimer;
        this.theBanditAsNPC.getLookControl().setLookAt(this.targetPlayer, 30.0f, 30.0f);
        if (--this.rePathDelay <= 0) {
            this.rePathDelay = 10;
            this.theBanditAsNPC.getNavigation().moveTo(this.targetPlayer, this.speed);
        }
        if (this.theBanditAsNPC.distanceToSqr(this.targetPlayer) <= 2.0) {
            this.chaseTimer = 0;
            steal(this.targetPlayer);
        }
    }

    private void steal(Player player) {
        Inventory inv = player.getInventory();
        int thefts = Mth.randomBetweenInclusive(this.theBanditAsNPC.getRandom(), 1, this.theBandit.getMaxThefts());
        boolean stolenSomething = false;
        for (int i = 0; i < thefts; ++i) {
            if (tryStealItem(inv, LOTRValuableItems::isCoin)) {
                stolenSomething = true;
            }
            if (tryStealItem(inv, LOTRValuableItems::isGem) || tryStealItem(inv, LOTRValuableItems::isToolMaterial)
                    || tryStealItem(inv, LOTRValuableItems::isRing) || tryStealItem(inv, LOTRBanditStealGoal::isArmor)) {
                stolenSomething = true;
                continue;
            }
            if (tryStealItem(inv, LOTRBanditStealGoal::isWeaponOrTool) || tryStealItem(inv, LOTRPouchItem::isPouch)) {
                stolenSomething = true;
                continue;
            }
            if (tryStealItem(inv, stack -> true)) {
                stolenSomething = true;
            }
        }
        if (stolenSomething) {
            player.sendSystemMessage(this.theBandit.getTheftChatMsg(player));
            this.theBanditAsNPC.playSound(SoundEvents.HORSE_SADDLE.value(), 0.5f, 1.0f);
            if (this.theBanditAsNPC.getTarget() != null) {
                this.theBanditAsNPC.setTarget(null);
            }
        }
    }

    /** ItemArmor: anything worn that gives armour. */
    private static boolean isArmor(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).modifiers().stream()
                .anyMatch(entry -> entry.attribute().equals(Attributes.ARMOR));
    }

    /**
     * ItemSword or ItemTool: anything that is a weapon or a mining tool --
     * but not a hoe or shears, which 1.7.10 did not count as tools.
     */
    private static boolean isWeaponOrTool(ItemStack stack) {
        return (stack.has(DataComponents.WEAPON) || stack.has(DataComponents.TOOL))
                && !(stack.getItem() instanceof HoeItem) && !(stack.getItem() instanceof ShearsItem);
    }

    /** tryStealItem_do: the first slot, in random order, that is not in hand and passes the filter. */
    private boolean tryStealItem(Inventory inv, Predicate<ItemStack> filter) {
        List<Integer> slots = new ArrayList<>();
        for (int slot = 0; slot < Inventory.INVENTORY_SIZE; ++slot) {
            slots.add(slot);
        }
        Util.shuffle(slots, this.theBanditAsNPC.getRandom());
        for (int slot : slots) {
            ItemStack stack = inv.getItem(slot);
            if (slot != inv.getSelectedSlot() && !stack.isEmpty() && filter.test(stack) && stealItem(inv, slot)) {
                return true;
            }
        }
        return false;
    }

    /** stealItem: one to eight of the stack into the bandit's first empty slot, if it has one. */
    private boolean stealItem(Inventory inv, int slot) {
        ItemStack playerItem = inv.getItem(slot);
        int theft = Math.min(Mth.randomBetweenInclusive(this.theBanditAsNPC.getRandom(), 1, 8), playerItem.getCount());
        int banditSlot = -1;
        for (int i = 0; i < this.theBandit.getBanditInventory().getContainerSize(); ++i) {
            if (this.theBandit.getBanditInventory().getItem(i).isEmpty()) {
                banditSlot = i;
                break;
            }
        }
        if (banditSlot < 0) {
            return false;
        }
        this.theBandit.getBanditInventory().setItem(banditSlot, playerItem.copyWithCount(theft));
        playerItem.shrink(theft);
        if (playerItem.isEmpty()) {
            inv.setItem(slot, ItemStack.EMPTY);
        }
        this.theBanditAsNPC.isNPCPersistent = true;
        return true;
    }
}
