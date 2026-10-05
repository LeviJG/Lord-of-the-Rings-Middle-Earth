package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBossInfo: what a boss keeps -- the last player to hurt it, whom it may
 * leap at; how much damage each player (or their hired units) has done it in
 * the last thirty seconds; and whether it is in a jump attack, clearing the
 * blocks about it as it goes (with mobGriefing on) and, as it lands,
 * striking every enemy within twelve blocks, the nearer the harder, and
 * flinging them up and away. It also carries the boss bar, which the original
 * put up (BossStatus) for anyone who had the boss in view; here it shows to
 * the players the boss is tracked by.
 *
 * <p>NOT ported yet: the kill achievement for everyone who did it forty
 * damage (D7).
 */
public class LOTRBossInfo {

    public static final int PLAYER_HURT_COOLDOWN = 600;
    public static final float PLAYER_DAMAGE_THRESHOLD = 40.0f;

    private final LOTRNPCEntity theNPC;
    private final LOTRBoss theBoss;
    public @Nullable Player lastAttackingPlayer;
    private Map<UUID, HurtRecord> playerHurtTimes = new HashMap<>();
    public boolean jumpAttack;
    private final ServerBossEvent bossEvent;

    private record HurtRecord(int time, float damage) {
    }

    public LOTRBossInfo(LOTRBoss boss) {
        this.theBoss = boss;
        this.theNPC = (LOTRNPCEntity) boss;
        this.bossEvent = new ServerBossEvent(this.theNPC.getUUID(), this.theNPC.getDisplayName(),
                BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    }

    public ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    /**
     * clearSurroundingBlocks: everything short of bedrock-hard within half
     * again its size, removed without a sound; each of a block's drops falls
     * with a chance of a hundredth of its blast resistance
     * (dropBlockAsItemWithChance), so stone now and then, dirt hardly ever.
     */
    private void clearSurroundingBlocks(ServerLevel level) {
        if (!level.getGameRules().get(GameRules.MOB_GRIEFING)) {
            return;
        }
        int xzRange = Mth.ceil(this.theNPC.getBbWidth() / 2.0f * 1.5f);
        int yRange = Mth.ceil(this.theNPC.getBbHeight() * 1.5f);
        int xzDist = xzRange * xzRange + xzRange * xzRange;
        int i = Mth.floor(this.theNPC.getX());
        int j = Mth.floor(this.theNPC.getBoundingBox().minY);
        int k = Mth.floor(this.theNPC.getZ());
        for (int i1 = i - xzRange; i1 <= i + xzRange; ++i1) {
            for (int j1 = j; j1 <= j + yRange; ++j1) {
                for (int k1 = k - xzRange; k1 <= k + xzRange; ++k1) {
                    int i2 = i1 - i;
                    int k2 = k1 - k;
                    if (i2 * i2 + k2 * k2 >= xzDist) {
                        continue;
                    }
                    BlockPos pos = new BlockPos(i1, j1, k1);
                    BlockState state = level.getBlockState(pos);
                    float resistance = state.getBlock().getExplosionResistance();
                    if (state.isAir() || LOTRLegacyWorld.isLiquid(state) || resistance >= 2000.0f) {
                        continue;
                    }
                    for (ItemStack drop : Block.getDrops(state, level, pos, level.getBlockEntity(pos), this.theNPC,
                            ItemStack.EMPTY)) {
                        if (level.getRandom().nextFloat() <= resistance / 100.0f) {
                            Block.popResource(level, pos, drop);
                        }
                    }
                    level.removeBlock(pos, false);
                }
            }
        }
    }

    public void doJumpAttack(double jumpSpeed) {
        this.jumpAttack = true;
        this.theNPC.setDeltaMovement(this.theNPC.getDeltaMovement().x, jumpSpeed, this.theNPC.getDeltaMovement().z);
    }

    /** doTargetedJumpAttack: a leap at the last player to hurt it, if they are far, or high above. */
    public void doTargetedJumpAttack(double jumpSpeed) {
        Player player = this.lastAttackingPlayer;
        if (!this.theNPC.level().isClientSide() && player != null && this.theNPC.onGround()
                && (player.getY() - this.theNPC.getY() > 10.0 || this.theNPC.distanceToSqr(player) > 400.0)) {
            doJumpAttack(jumpSpeed);
            double mx = (player.getX() - this.theNPC.getX()) / 10.0;
            double my = (player.getY() - this.theNPC.getY()) / 10.0;
            double mz = (player.getZ() - this.theNPC.getZ()) / 10.0;
            this.theNPC.setDeltaMovement(mx, Math.max(my, jumpSpeed), mz);
            this.theNPC.getLookControl().setLookAt(player, 100.0f, 100.0f);
            this.theNPC.getLookControl().tick();
            this.theNPC.setYRot(this.theNPC.getYHeadRot());
        }
    }

    /** getHealthChanceModifier: the square root of how much of its health is gone. */
    public float getHealthChanceModifier() {
        float f = 1.0f - this.theNPC.getHealth() / this.theNPC.getMaxHealth();
        return Mth.sqrt(f);
    }

    /** getNearbyEnemies: players who are not its friends, and creatures of hostile factions, within 12 (6 up or down). */
    public List<LivingEntity> getNearbyEnemies() {
        List<LivingEntity> enemies = new ArrayList<>();
        for (Player player : this.theNPC.level().getEntitiesOfClass(Player.class,
                this.theNPC.getBoundingBox().inflate(12.0, 6.0, 12.0))) {
            if (player.isCreative() || LOTRPlayerAlignments.getAlignment(player, this.theNPC.getFaction()) >= 0.0f) {
                continue;
            }
            enemies.add(player);
        }
        enemies.addAll(this.theNPC.level().getEntitiesOfClass(Mob.class, this.theNPC.getBoundingBox().inflate(12.0, 6.0, 12.0),
                mob -> LOTRNearestAttackableTargetGoal.isFactionTarget(this.theNPC, mob)));
        return enemies;
    }

    public void onDeath(DamageSource source) {
        onHurt(source, 0.0f);
    }

    /** onFall: landing a jump attack costs it nothing, and strikes all about. */
    public float onFall(float fallDistance) {
        if (this.theNPC.level() instanceof ServerLevel level && this.jumpAttack) {
            fallDistance = 0.0f;
            this.jumpAttack = false;
            float attackDamage = (float) this.theNPC.getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE);
            float yaw = this.theNPC.getYRot() * Mth.DEG_TO_RAD;
            for (LivingEntity entity : getNearbyEnemies()) {
                float strength = (12.0f - this.theNPC.distanceTo(entity) / 3.0f) / 12.0f;
                entity.hurtServer(level, this.theNPC.damageSources().mobAttack(this.theNPC), strength * attackDamage * 3.0f);
                float knockback = strength * 3.0f;
                entity.push(-Mth.sin(yaw) * knockback * 0.5f, 0.25 * knockback, Mth.cos(yaw) * knockback * 0.5f);
            }
            this.theBoss.onJumpAttackFall();
        }
        return fallDistance;
    }

    /** onHurt: whom to leap at, and the damage each player's side has done. */
    public void onHurt(DamageSource source, float damage) {
        if (this.theNPC.level().isClientSide()) {
            return;
        }
        if (source.getEntity() instanceof Player attacker && !attacker.isCreative()) {
            this.lastAttackingPlayer = attacker;
        }
        Player damager = source.getEntity() instanceof Player p ? p
                : source.getEntity() instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive
                ? npc.hiredNPCInfo.getHiringPlayer() : null;
        if (damager != null) {
            HurtRecord previous = this.playerHurtTimes.get(damager.getUUID());
            float total = damage + (previous == null ? 0.0f : previous.damage());
            this.playerHurtTimes.put(damager.getUUID(), new HurtRecord(PLAYER_HURT_COOLDOWN, total));
        }
    }

    public void onUpdate() {
        if (this.lastAttackingPlayer != null && (!this.lastAttackingPlayer.isAlive() || this.lastAttackingPlayer.isCreative())) {
            this.lastAttackingPlayer = null;
        }
        if (this.theNPC.level() instanceof ServerLevel level) {
            Map<UUID, HurtRecord> updated = new HashMap<>();
            for (Map.Entry<UUID, HurtRecord> entry : this.playerHurtTimes.entrySet()) {
                int time = entry.getValue().time() - 1;
                if (time > 0) {
                    updated.put(entry.getKey(), new HurtRecord(time, entry.getValue().damage()));
                }
            }
            this.playerHurtTimes = updated;
            if (this.jumpAttack && this.theNPC.tickCount % 5 == 0) {
                clearSurroundingBlocks(level);
            }
            this.bossEvent.setName(this.theNPC.getDisplayName());
            this.bossEvent.setProgress(this.theNPC.getHealth() / this.theNPC.getMaxHealth());
        }
    }

    public void startSeenByPlayer(ServerPlayer player) {
        this.bossEvent.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayer player) {
        this.bossEvent.removePlayer(player);
    }

    public void save(ValueOutput output) {
        ValueOutput data = output.child("NPCBossInfo");
        ValueOutput.ValueOutputList hurt = data.childrenList("PlayerHurtTimes");
        for (Map.Entry<UUID, HurtRecord> entry : this.playerHurtTimes.entrySet()) {
            ValueOutput tag = hurt.addChild();
            tag.putString("UUID", entry.getKey().toString());
            tag.putInt("Time", entry.getValue().time());
            tag.putFloat("Damage", entry.getValue().damage());
        }
        data.putBoolean("JumpAttack", this.jumpAttack);
    }

    public void load(ValueInput input) {
        input.child("NPCBossInfo").ifPresent(data -> {
            for (ValueInput tag : data.childrenListOrEmpty("PlayerHurtTimes")) {
                tag.getString("UUID").ifPresent(uuid -> this.playerHurtTimes.put(UUID.fromString(uuid),
                        new HurtRecord(tag.getIntOr("Time", 0), tag.getFloatOr("Damage", 0.0f))));
            }
            this.jumpAttack = data.getBooleanOr("JumpAttack", false);
        });
    }
}
