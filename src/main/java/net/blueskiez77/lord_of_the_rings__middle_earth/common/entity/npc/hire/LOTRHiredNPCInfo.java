package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import java.util.List;
import java.util.UUID;

import it.unimi.dsi.fastutil.ints.IntList;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCMount;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRPlayerNPCOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTRHiredNPCInfo: an NPC in a player's service. It follows its player (or
 * holds position, or guards a spot), fights whoever the player fights or is
 * fought by, deserts if the player's alignment or pledge no longer meets its
 * terms, and -- as a warrior -- gains experience from slain enemies, a heart
 * of health and a firework for each level.
 *
 * <p>Saved under "HiredNPCInfo" with the original's keys. The hiring player,
 * task, squadron and level are sent to watching players
 * ({@link LOTRNPCEntity#syncHiredInfo}).
 *
 * <p>NOT ported yet: the hired unit screens and their packets
 * (LOTRGuiHiredWarrior/Farmer/Dismiss, LOTRPacketHiredGui/UnitCommand/
 * UnitDismiss/UnitInteract/NPCSquadron, isGuiOpen -- D16), the faction hire
 * counter (LOTRFactionData.addHire, with D9's faction data).
 */
public class LOTRHiredNPCInfo {

    public static final int XP_COLOR = 16733440;
    public static final int GUARD_RANGE_MIN = 1;
    public static final int GUARD_RANGE_DEFAULT = 8;
    public static final int GUARD_RANGE_MAX = 64;
    /** LOTRConfig.enableUnitLevelling, on by default; the option is not ported yet. */
    private static final boolean ENABLE_UNIT_LEVELLING = true;

    private final LOTRNPCEntity theEntity;
    private @Nullable UUID hiringPlayerUUID;
    public boolean isActive;
    public float alignmentRequiredToCommand;
    public LOTRUnitPledgeType pledgeType = LOTRUnitPledgeType.NONE;
    private LOTRHiredTask hiredTask = LOTRHiredTask.WARRIOR;
    public boolean canMove = true;
    public boolean teleportAutomatically = true;
    public int mobKills;
    public int xp;
    public int xpLevel = 1;
    private @Nullable String hiredSquadron;
    private boolean guardMode;
    private int guardRange = GUARD_RANGE_DEFAULT;
    private @Nullable LOTRInventoryNPC hiredInventory;
    public boolean inCombat;
    public boolean targetFromCommandSword;
    public boolean wasAttackCommanded;

    public LOTRHiredNPCInfo(LOTRNPCEntity npc) {
        this.theEntity = npc;
    }

    public static int totalXPForLevel(int lvl) {
        if (lvl <= 1) {
            return 0;
        }
        return Mth.floor(3.0 * (lvl - 1) * Math.pow(1.08, lvl - 2));
    }

    public void addExperience(int xpAdd) {
        addExperience(xpAdd, true);
    }

    public void addExperience(int xpAdd, boolean passToRiderOrMount) {
        this.xp += xpAdd;
        while (this.xp >= totalXPForLevel(this.xpLevel + 1)) {
            ++this.xpLevel;
            markDirty();
            onLevelUp();
        }
        if (passToRiderOrMount) {
            addExperienceIfApplicable(this.theEntity.getFirstPassenger(), xpAdd);
            addExperienceIfApplicable(this.theEntity.getVehicle(), xpAdd);
        }
    }

    private void addExperienceIfApplicable(@Nullable Entity maybeNPC, int xpAdd) {
        if (maybeNPC instanceof LOTRNPCEntity other && other.hiredNPCInfo.isActive
                && this.hiringPlayerUUID != null && this.hiringPlayerUUID.equals(other.hiredNPCInfo.hiringPlayerUUID)) {
            other.hiredNPCInfo.addExperience(xpAdd, false);
        }
    }

    private static void addLevelUpHealthGain(LivingEntity entity) {
        float healthBoost = 1.0f;
        AttributeInstance health = entity.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(health.getBaseValue() + healthBoost);
        }
        entity.heal(healthBoost);
    }

    /** commandSwordAttack: the command sword's target, if the unit may attack it. */
    public void commandSwordAttack(LivingEntity target) {
        if (net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules
                .canNPCAttackEntity(this.theEntity, target, true)) {
            this.theEntity.getNavigation().stop();
            this.theEntity.setLastHurtByMob(target);
            this.theEntity.setTarget(target);
            this.targetFromCommandSword = true;
        }
    }

    public void commandSwordCancel() {
        if (this.targetFromCommandSword) {
            this.theEntity.getNavigation().stop();
            this.theEntity.setLastHurtByMob(null);
            this.theEntity.setTarget(null);
            this.targetFromCommandSword = false;
        }
    }

    /** dismissUnit: let go (or deserting), with a word to the player; a farmer drops what it carries. */
    public void dismissUnit(boolean isDesertion) {
        Player player = getHiringPlayer();
        if (player != null) {
            player.sendSystemMessage(Component.translatable(isDesertion ? "lotr.hiredNPC.desert" : "lotr.hiredNPC.dismiss",
                    this.theEntity.getName()));
        }
        if (this.hiredTask == LOTRHiredTask.FARMER && this.hiredInventory != null) {
            this.hiredInventory.dropAllItems();
        }
        this.isActive = false;
        this.canMove = true;
        setHiringPlayer(null);
    }

    public int getGuardRange() {
        return this.guardRange;
    }

    public void setGuardRange(int range) {
        this.guardRange = Mth.clamp(range, GUARD_RANGE_MIN, GUARD_RANGE_MAX);
        if (this.guardMode) {
            this.theEntity.setHomeTo(this.theEntity.blockPosition(), this.guardRange);
        }
    }

    public @Nullable LOTRInventoryNPC getHiredInventory() {
        return this.hiredInventory;
    }

    public @Nullable Player getHiringPlayer() {
        return this.hiringPlayerUUID == null ? null : this.theEntity.level().getPlayerByUUID(this.hiringPlayerUUID);
    }

    public void setHiringPlayer(@Nullable Player player) {
        this.hiringPlayerUUID = player == null ? null : player.getUUID();
        markDirty();
    }

    public @Nullable UUID getHiringPlayerUUID() {
        return this.hiringPlayerUUID;
    }

    public boolean getObeyCommandSword() {
        return this.hiredTask == LOTRHiredTask.WARRIOR && !this.guardMode;
    }

    public boolean getObeyHornHaltReady() {
        return this.hiredTask == LOTRHiredTask.WARRIOR && !this.guardMode;
    }

    public boolean getObeyHornSummon() {
        return this.hiredTask == LOTRHiredTask.WARRIOR && !this.guardMode;
    }

    /** LOTRSquadrons.areSquadronsCompatible: no squadron answers only a blank item, else the name, in any case. */
    public boolean isSquadronCompatible(String itemSquadron) {
        if (this.hiredSquadron == null || this.hiredSquadron.isEmpty()) {
            return itemSquadron == null || itemSquadron.isEmpty();
        }
        return this.hiredSquadron.equalsIgnoreCase(itemSquadron);
    }

    public float getProgressToNextLevel() {
        int cap = totalXPForLevel(this.xpLevel + 1);
        int start = totalXPForLevel(this.xpLevel);
        return (float) (this.xp - start) / (cap - start);
    }

    public @Nullable String getSquadron() {
        return this.hiredSquadron;
    }

    public void setSquadron(@Nullable String squadron) {
        this.hiredSquadron = squadron;
        markDirty();
    }

    public LOTRHiredTask getTask() {
        return this.hiredTask;
    }

    public void setTask(LOTRHiredTask task) {
        if (task != this.hiredTask) {
            this.hiredTask = task;
            markDirty();
        }
        if (this.hiredTask == LOTRHiredTask.FARMER) {
            this.hiredInventory = new LOTRInventoryNPC("HiredInventory", this.theEntity, 4);
        }
    }

    public void halt() {
        this.canMove = false;
        this.theEntity.setTarget(null);
    }

    public void ready() {
        this.canMove = true;
    }

    /** hasHiringRequirements: a unit of a real faction, with terms to keep. */
    public boolean hasHiringRequirements() {
        return this.theEntity.getHiringFaction().isPlayableAlignmentFaction() && this.alignmentRequiredToCommand >= 0.0f;
    }

    /**
     * hireUnit: in the player's service -- placed at their feet, facing away,
     * if it was newly made -- on the terms of the trade it was hired by.
     */
    public void hireUnit(Player player, boolean setLocation, @Nullable LOTRFaction hiringFaction, LOTRUnitTradeEntry trade,
                         @Nullable String squadron, @Nullable Mob mount) {
        if (setLocation) {
            this.theEntity.snapTo(player.getX(), player.getBoundingBox().minY, player.getZ(), player.getYRot() + 180.0f, 0.0f);
        }
        this.isActive = true;
        this.alignmentRequiredToCommand = trade.alignmentRequired;
        this.pledgeType = trade.getPledgeType();
        setHiringPlayer(player);
        setTask(trade.task);
        setSquadron(squadron);
        if (mount != null) {
            // The mount at the unit's feet: hired too if it is an NPC, else the unit's own.
            mount.snapTo(this.theEntity.getX(), this.theEntity.getBoundingBox().minY, this.theEntity.getZ(),
                    this.theEntity.getYRot(), 0.0f);
            if (mount instanceof LOTRNPCEntity hiredMount) {
                hiredMount.hiredNPCInfo.hireUnit(player, setLocation, hiringFaction, trade, squadron, null);
            } else if (mount instanceof LOTRNPCMount npcMount) {
                this.theEntity.setRidingHorse(true);
                npcMount.setBelongsToNPC(true);
                LOTRNPCMount.setNavigatorRangeFromNPC(mount, this.theEntity);
            }
        }
    }

    public boolean isGuardMode() {
        return this.guardMode;
    }

    public void setGuardMode(boolean flag) {
        this.guardMode = flag;
        if (flag) {
            this.theEntity.setHomeTo(this.theEntity.blockPosition(), this.guardRange);
        } else {
            this.theEntity.clearHome();
        }
    }

    public boolean isHalted() {
        return !this.guardMode && !this.canMove;
    }

    public boolean shouldFollowPlayer() {
        return !this.guardMode && this.canMove;
    }

    /** markDirty: the watching players hear of it. */
    private void markDirty() {
        this.theEntity.syncHiredInfo();
    }

    /** onDeath: word to the player, if they want it, and a farmer's load dropped. */
    public void onDeath(DamageSource source) {
        if (!(this.theEntity.level() instanceof ServerLevel)) {
            return;
        }
        Player player = getHiringPlayer();
        if (this.isActive && player != null && LOTRPlayerNPCOptions.getEnableHiredDeathMessages(player)) {
            player.sendSystemMessage(Component.translatable("lotr.hiredNPC.death",
                    this.theEntity.getCombatTracker().getDeathMessage()));
        }
        if (this.hiredInventory != null) {
            this.hiredInventory.dropAllItems();
        }
    }

    /**
     * onKillEntity: a warrior's kill of an enemy -- a player the faction
     * dislikes, or a creature of an enemy faction -- may draw a word to its
     * player, and a creature is worth a point of experience.
     */
    public void onKillEntity(LivingEntity target) {
        if (!(this.theEntity.level() instanceof ServerLevel) || !this.isActive) {
            return;
        }
        ++this.mobKills;
        if (this.hiredTask != LOTRHiredTask.WARRIOR) {
            return;
        }
        boolean wasEnemy = false;
        int addXP = 0;
        LOTRFaction unitFaction = this.theEntity.getHiringFaction();
        if (target instanceof Player player) {
            wasEnemy = LOTRPlayerAlignments.getAlignment(player, unitFaction) < 0.0f;
        } else {
            LOTRFaction targetFaction = LOTRNearestAttackableTargetGoal.factionOf(target);
            if (targetFaction.isBadRelation(unitFaction) || unitFaction == LOTRFaction.RUFFIAN
                    && targetFaction != LOTRFaction.UNALIGNED && targetFaction != LOTRFaction.RUFFIAN) {
                wasEnemy = true;
                addXP = 1;
            }
        }
        if (wasEnemy && this.theEntity.getRandom().nextInt(3) == 0) {
            speakToHiringPlayer(256.0);
        }
        if (addXP > 0 && ENABLE_UNIT_LEVELLING) {
            addExperience(addXP);
        }
    }

    private void speakToHiringPlayer(double rangeSq) {
        Player player = getHiringPlayer();
        if (player != null && this.theEntity.distanceToSqr(player) < rangeSq) {
            String bank = this.theEntity.getSpeechBank(player);
            if (bank != null) {
                this.theEntity.sendSpeechBank(player, bank);
            }
        }
    }

    /** onLevelUp: a heart more for the unit and a mount it rides, a word, and a firework. */
    private void onLevelUp() {
        addLevelUpHealthGain(this.theEntity);
        if (this.theEntity.getVehicle() instanceof LivingEntity mount && !(mount instanceof LOTRNPCEntity)) {
            addLevelUpHealthGain(mount);
        }
        Player player = getHiringPlayer();
        if (player != null) {
            player.sendSystemMessage(Component.translatable("lotr.hiredNPC.levelUp", this.theEntity.getName(), this.xpLevel));
        }
        spawnLevelUpFireworks();
    }

    public void onSetTarget(@Nullable LivingEntity newTarget, @Nullable LivingEntity prevTarget) {
        if (newTarget == null || newTarget != prevTarget) {
            this.targetFromCommandSword = false;
            this.wasAttackCommanded = false;
        }
    }

    /** onUpdate, server side. */
    public void tick() {
        Player player;
        if (hasHiringRequirements() && this.isActive && (player = getHiringPlayer()) != null) {
            LOTRFaction faction = this.theEntity.getHiringFaction();
            boolean canCommand = LOTRPlayerAlignments.getAlignment(player, faction) >= this.alignmentRequiredToCommand
                    && this.pledgeType.canAcceptPlayer(player, faction);
            if (!canCommand) {
                dismissUnit(true);
            }
        }
        this.inCombat = this.theEntity.getTarget() != null;
        if (this.hiredTask == LOTRHiredTask.WARRIOR && !this.inCombat && shouldFollowPlayer()
                && this.theEntity.getRandom().nextInt(4000) == 0) {
            speakToHiringPlayer(16.0 * 16.0);
        }
    }

    /** spawnLevelUpFireworks: orange and the faction's colour; a bigger burst every fifth level. */
    private void spawnLevelUpFireworks() {
        if (!(this.theEntity.level() instanceof ServerLevel level)) {
            return;
        }
        boolean bigLvlUp = this.xpLevel % 5 == 0;
        FireworkExplosion explosion = new FireworkExplosion(
                bigLvlUp ? FireworkExplosion.Shape.LARGE_BALL : FireworkExplosion.Shape.SMALL_BALL,
                IntList.of(XP_COLOR, this.theEntity.getFaction().getFactionColor()), IntList.of(), bigLvlUp, true);
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        stack.set(DataComponents.FIREWORKS, new Fireworks(1, List.of(explosion)));
        FireworkRocketEntity firework = new FireworkRocketEntity(level, this.theEntity.getX(),
                this.theEntity.getBoundingBox().minY + this.theEntity.getBbHeight(), this.theEntity.getZ(), stack);
        // The original rewrote the rocket's "LifeTime".
        firework.lifetime = bigLvlUp ? 20 : 15;
        level.addFreshEntity(firework);
    }

    /**
     * tryTeleportToHiringPlayer: to a free spot 3-6 blocks (plus half its
     * width) from the player on solid ground, at most four blocks up or down;
     * failing that, if {@code failsafe}, onto the player's own spot. A unit
     * on a mount brings the mount.
     */
    public void tryTeleportToHiringPlayer(boolean failsafe) {
        Level world = this.theEntity.level();
        if (world.isClientSide()) {
            return;
        }
        Player player = getHiringPlayer();
        if (!this.isActive || player == null || this.theEntity.getFirstPassenger() != null) {
            return;
        }
        int i = Mth.floor(player.getX());
        int j = Mth.floor(player.getBoundingBox().minY);
        int k = Mth.floor(player.getZ());
        float extraDist = this.theEntity.getBbWidth() / 2.0f;
        Mob mount = this.theEntity.getVehicle() instanceof Mob m ? m : null;
        if (mount != null) {
            extraDist = Math.max(this.theEntity.getBbWidth(), mount.getBbWidth()) / 2.0f;
        }
        float minDist = 3.0f + extraDist;
        float maxDist = 6.0f + extraDist;
        RandomSource rand = world.getRandom();
        for (int l = 0; l < 120; ++l) {
            float angle = rand.nextFloat() * Mth.TWO_PI;
            float r = Mth.randomBetween(rand, minDist, maxDist);
            int i1 = Mth.floor(i + 0.5 + Mth.cos(angle) * r);
            int k1 = Mth.floor(k + 0.5 + Mth.sin(angle) * r);
            int j1 = Mth.nextInt(rand, j - 4, j + 4);
            double d = i1 + 0.5;
            double d2 = k1 + 0.5;
            if (!world.noCollision(boxAt(this.theEntity, d, j1, d2)) || !isSolidTop(world, new BlockPos(i1, j1 - 1, k1))) {
                continue;
            }
            if (mount != null) {
                if (!world.noCollision(boxAt(mount, d, j1, d2))) {
                    continue;
                }
                moveTo(mount, d, j1, d2);
                return;
            }
            moveTo(this.theEntity, d, j1, d2);
            return;
        }
        if (failsafe && isSolidTop(world, new BlockPos(i, j - 1, k))) {
            moveTo(mount != null ? mount : this.theEntity, i + 0.5, j, k + 0.5);
        }
    }

    private static AABB boxAt(Entity entity, double x, double y, double z) {
        float half = entity.getBbWidth() / 2.0f;
        return new AABB(x - half, y, z - half, x + half, y + entity.getBbHeight(), z + half);
    }

    private static boolean isSolidTop(Level world, BlockPos pos) {
        return world.getBlockState(pos).isFaceSturdy(world, pos, Direction.UP);
    }

    /** Where it lands: no fall damage carried over, no path, no target -- for the unit and a mount alike. */
    private void moveTo(Mob entity, double x, double y, double z) {
        entity.snapTo(x, y, z, this.theEntity.getYRot(), this.theEntity.getXRot());
        entity.resetFallDistance();
        entity.getNavigation().stop();
        entity.setTarget(null);
        if (entity != this.theEntity) {
            this.theEntity.resetFallDistance();
            this.theEntity.getNavigation().stop();
            this.theEntity.setTarget(null);
        }
    }

    /** The data watching players need: the player, task, squadron and level. */
    public void receiveBasicData(@Nullable UUID hiringPlayer, LOTRHiredTask task, @Nullable String squadron, int level) {
        this.hiringPlayerUUID = hiringPlayer;
        this.hiredTask = task;
        this.hiredSquadron = squadron;
        this.xpLevel = level;
    }

    public void save(ValueOutput nbt) {
        ValueOutput data = nbt.child("HiredNPCInfo");
        data.putBoolean("IsActive", this.isActive);
        if (this.hiringPlayerUUID != null) {
            data.putString("HiringPlayerUUID", this.hiringPlayerUUID.toString());
        }
        data.putFloat("AlignReqF", this.alignmentRequiredToCommand);
        data.putByte("PledgeType", (byte) this.pledgeType.typeID);
        data.putBoolean("CanMove", this.canMove);
        data.putBoolean("TeleportAutomatically", this.teleportAutomatically);
        data.putInt("MobKills", this.mobKills);
        data.putBoolean("GuardMode", this.guardMode);
        data.putInt("GuardRange", this.guardRange);
        data.putInt("Task", this.hiredTask.ordinal());
        data.putInt("Xp", this.xp);
        data.putInt("XpLevel", this.xpLevel);
        if (this.hiredSquadron != null && !this.hiredSquadron.isEmpty()) {
            data.putString("Squadron", this.hiredSquadron);
        }
        if (this.hiredInventory != null) {
            this.hiredInventory.save(data);
        }
    }

    public void load(ValueInput nbt) {
        nbt.child("HiredNPCInfo").ifPresent(data -> {
            this.hiringPlayerUUID = data.getString("HiringPlayerUUID").filter(s -> !s.isEmpty())
                    .map(UUID::fromString).orElse(null);
            this.isActive = data.getBooleanOr("IsActive", false);
            this.alignmentRequiredToCommand = data.getFloatOr("AlignReqF", 0.0f);
            this.pledgeType = LOTRUnitPledgeType.forID(data.getByteOr("PledgeType", (byte) 0));
            this.canMove = data.getBooleanOr("CanMove", false);
            this.teleportAutomatically = data.getBooleanOr("TeleportAutomatically", true);
            this.mobKills = data.getIntOr("MobKills", 0);
            setGuardMode(data.getBooleanOr("GuardMode", false));
            setGuardRange(data.getIntOr("GuardRange", GUARD_RANGE_DEFAULT));
            setTask(LOTRHiredTask.forID(data.getIntOr("Task", 0)));
            this.xp = data.getIntOr("Xp", 0);
            this.xpLevel = data.getIntOr("XpLevel", 1);
            this.hiredSquadron = data.getString("Squadron").orElse(null);
            if (this.hiredInventory != null) {
                this.hiredInventory.load(data);
            }
        });
    }
}
