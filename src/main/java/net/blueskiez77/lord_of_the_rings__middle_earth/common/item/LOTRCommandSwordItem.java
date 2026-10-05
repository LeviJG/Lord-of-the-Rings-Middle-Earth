package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemCommandSword: not a sword at all, but a pointing stick for an army.
 *
 * <p>WHAT IT IS FOR. It is the Horn of Command's other half. The horn calls a
 * squadron to halt, to move, or to come to you; the sword tells them what to
 * KILL. Point it at something up to sixty-four blocks off and right-click:
 * every unit you have hired within twelve blocks that obeys the command sword
 * and carries the same squadron name is ordered onto the nearest enemy within
 * six blocks of the spot you indicated. Point it at open sky and the same click
 * CANCELS those orders and they stand down. So it is aim, click, charge -- and
 * click again at nothing to call them off.
 *
 * <p>It is deliberately useless as a weapon: setMaxDamage(0) makes it
 * unbreakable, getItemEnchantability returns zero, and lotrWeaponDamage is set
 * to 1.0 -- less than a fist. You carry it in place of a weapon, not as one.
 *
 * <p>Its squadron is named at a table of command (LOTRGuiSquadronItem).
 */
public class LOTRCommandSwordItem extends Item {

    /** How far the original looked for something to point at. */
    public static final double COMMAND_RANGE = 64.0;

    /** getEntitiesWithinAABB(player.boundingBox.expand(12)): who can hear it. */
    public static final double UNIT_RANGE = 12.0;

    /** The spread around the point indicated that a target may be found in. */
    public static final double SPREAD_RANGE = 6.0;

    /** lotrWeaponDamage = 1.0f, so the modifier is zero: a bare fist's worth. */
    public static final double ATTACK_DAMAGE = 1.0;

    public LOTRCommandSwordItem(Properties properties) {
        super(properties);
    }

    /**
     * onItemRightClick: swing, look for a target, and order the squadron onto
     * it. The swing happens either way -- the original called swingItem before
     * it had looked at anything -- so the gesture reads the same whether or not
     * there was anything to point at.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.swing(hand);
        if (!level.isClientSide()) {
            command(level, player, player.getItemInHand(hand));
        }
        return InteractionResult.SUCCESS;
    }

    /** LOTRSquadrons.getSquadron: the sword names its squadron as the horn does. */
    public static String getSquadron(ItemStack stack) {
        return LOTRCommandHornItem.getSquadron(stack);
    }

    /**
     * onItemRightClick's aim: the creature looked at within COMMAND_RANGE,
     * else the block, else nothing -- then the order.
     */
    private static void command(Level level, Player player, ItemStack stack) {
        Entity entity = getEntityTarget(player);
        if (entity != null) {
            command(level, player, stack, new Vec3(entity.getX(), entity.getBoundingBox().minY + entity.getBbHeight() / 2.0f,
                    entity.getZ()));
            return;
        }
        Vec3 eyePos = player.getEyePosition();
        Vec3 sight = eyePos.add(player.getLookAngle().scale(COMMAND_RANGE));
        BlockHitResult hit = level.clip(new ClipContext(eyePos, sight, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        command(level, player, stack, hit.getType() == HitResult.Type.BLOCK ? hit.getLocation() : null);
    }

    /**
     * command: every hired unit within UNIT_RANGE that obeys the sword and
     * shares its squadron is set on the best of the creatures it may attack
     * within SPREAD_RANGE of the spot -- by its own target sorting -- or, with
     * none, stands down. If any unit was set on a target, the sword is shown
     * falling onto the spot (LOTRPacketLocationFX SWORD_COMMAND).
     */
    private static void command(Level level, Player player, ItemStack stack, @Nullable Vec3 hit) {
        player.setLastHurtByMob(null);
        List<LivingEntity> spreadTargets = hit == null ? List.of() : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(hit, hit).inflate(SPREAD_RANGE),
                e -> e.isAlive() && LOTRAttackRules.canPlayerAttackEntity(player, e, false));
        String squadron = getSquadron(stack);
        boolean anyAttackCommanded = false;
        for (LOTRNPCEntity npc : level.getEntitiesOfClass(LOTRNPCEntity.class, player.getBoundingBox().inflate(UNIT_RANGE))) {
            if (!npc.hiredNPCInfo.isActive || npc.hiredNPCInfo.getHiringPlayer() != player
                    || !npc.hiredNPCInfo.getObeyCommandSword() || !npc.hiredNPCInfo.isSquadronCompatible(squadron)) {
                continue;
            }
            List<LivingEntity> validTargets = new ArrayList<>();
            for (LivingEntity target : spreadTargets) {
                if (LOTRAttackRules.canNPCAttackEntity(npc, target, true)) {
                    validTargets.add(target);
                }
            }
            if (!validTargets.isEmpty()) {
                validTargets.sort(Comparator.comparingDouble(t -> LOTRNearestAttackableTargetGoal.targetSortMetric(npc, t)));
                npc.hiredNPCInfo.commandSwordAttack(validTargets.get(0));
                npc.hiredNPCInfo.wasAttackCommanded = true;
                anyAttackCommanded = true;
            } else {
                npc.hiredNPCInfo.commandSwordCancel();
            }
        }
        if (anyAttackCommanded && hit != null && player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new LOTRHiredPayloads.SwordCommandFX(hit.x, hit.y, hit.z));
        }
    }

    /**
     * getEntityTarget: the nearest living, clickable thing whose box, grown by
     * a block all round, the player's line of sight passes through within
     * COMMAND_RANGE -- or one the player's eyes are already inside.
     */
    private static @Nullable Entity getEntityTarget(Player player) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 sight = eyePos.add(look.scale(COMMAND_RANGE));
        Entity pointedEntity = null;
        double entityDist = COMMAND_RANGE;
        for (Entity entity : player.level().getEntities(player,
                player.getBoundingBox().expandTowards(look.scale(COMMAND_RANGE)).inflate(1.0))) {
            if (!(entity instanceof LivingEntity) || !entity.isPickable()) {
                continue;
            }
            AABB box = entity.getBoundingBox().inflate(1.0);
            java.util.Optional<Vec3> intercept = box.clip(eyePos, sight);
            if (box.contains(eyePos)) {
                if (entityDist >= 0.0) {
                    pointedEntity = entity;
                    entityDist = 0.0;
                }
                continue;
            }
            if (intercept.isEmpty()) {
                continue;
            }
            double d = eyePos.distanceTo(intercept.get());
            if (d >= entityDist && entityDist != 0.0) {
                continue;
            }
            if (entity == player.getVehicle()) {
                if (entityDist == 0.0) {
                    pointedEntity = entity;
                }
                continue;
            }
            pointedEntity = entity;
            entityDist = d;
        }
        return pointedEntity;
    }
}
