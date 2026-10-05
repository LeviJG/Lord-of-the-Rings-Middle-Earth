package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifierSpecials;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRCrossbowBoltEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPlateEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPoisonedArrowEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTraderRespawnEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBurningPanicGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiringPlayerHurtByTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiringPlayerHurtTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCHurtByTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNPCInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryHiredReplacedItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRMercenary;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTraderNPCInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTraderInfo;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBowItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCrossbowItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItemOwnership;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredInfoPayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityNPC: every person of Middle-earth -- Hobbits, Men, Elves, Dwarves,
 * Orcs and the rest. What they have in common:
 *
 * <ul>
 * <li>a faction ({@link #getFaction}), which decides whom they fight
 *     ({@link #addTargetTasks}) and what killing them costs or earns a player
 *     (LOTRNPCKillEvents);</li>
 * <li>a {@link LOTRFamilyInfo} -- age, sex, name, drunkenness, marriage;</li>
 * <li>an item store ({@link #npcItemsInv}) of idle, melee and ranged items,
 *     swapped into the hand as its attack mode changes between idle, melee
 *     and ranged ({@link #updateCombat}, {@link #onAttackModeChange});</li>
 * <li>speech: a line from a speech bank when a player talks to it or it
 *     turns on one;</li>
 * <li>their own drops: equipment at wear, coins, a rare nugget, a smith's
 *     scroll -- each unstackable one marked as theirs;</li>
 * <li>for a trader, its trades ({@link #traderNPCInfo}) and a respawner
 *     when it dies; for a hired unit, its service ({@link #hiredNPCInfo}).</li>
 * </ul>
 *
 * <p>NOT ported yet: mini-quests (LOTREntityQuestInfo, D14); invasions and
 * conquest spawning (D12); the biomes' part in spawning -- where hostiles
 * walk by day, and the spawn count multiplier (D10); the kill and talk
 * achievements (D7); and Utumno's drops (D15). Pouches are left out (user).
 */
public abstract class LOTRNPCEntity extends PathfinderMob implements RangedAttackMob {

    public static final float MOUNT_RANGE_BONUS = 1.5f;

    static final EntityDataAccessor<Integer> DATA_AGE =
            SynchedEntityData.defineId(LOTRNPCEntity.class, EntityDataSerializers.INT);
    static final EntityDataAccessor<Boolean> DATA_MALE =
            SynchedEntityData.defineId(LOTRNPCEntity.class, EntityDataSerializers.BOOLEAN);
    static final EntityDataAccessor<String> DATA_NAME =
            SynchedEntityData.defineId(LOTRNPCEntity.class, EntityDataSerializers.STRING);
    static final EntityDataAccessor<Boolean> DATA_DRUNK =
            SynchedEntityData.defineId(LOTRNPCEntity.class, EntityDataSerializers.BOOLEAN);
    /** LOTRPacketNPCIsEating. */
    private static final EntityDataAccessor<Boolean> DATA_EATING =
            SynchedEntityData.defineId(LOTRNPCEntity.class, EntityDataSerializers.BOOLEAN);
    /** LOTRPacketNPCCombatStance. */
    private static final EntityDataAccessor<Boolean> DATA_COMBAT_STANCE =
            SynchedEntityData.defineId(LOTRNPCEntity.class, EntityDataSerializers.BOOLEAN);

    public final LOTRFamilyInfo familyInfo = new LOTRFamilyInfo(this);
    public final LOTRInventoryNPCItems npcItemsInv = new LOTRInventoryNPCItems(this);
    public final LOTRHiredNPCInfo hiredNPCInfo = new LOTRHiredNPCInfo(this);
    /** What a hired unit had on before its player re-equipped it. */
    public final LOTRInventoryHiredReplacedItems hiredReplacedInv = new LOTRInventoryHiredReplacedItems(this);

    public boolean isPassive;
    public boolean isImmuneToFrost;
    public boolean isChilly;
    public boolean spawnsInDarkness;
    public boolean isNPCPersistent;
    /** A boss's own record (LOTRBoss); null for any other NPC. */
    public final @Nullable LOTRBossInfo bossInfo;
    public boolean liftSpawnRestrictions;
    /** Spawns even where a banner protects the land against it (the Grey Wanderer's arrival, D12). */
    public boolean liftBannerRestrictions;
    public boolean isTargetSeeker;
    public final List<LOTRFaction> killBonusFactions = new ArrayList<>();
    public @Nullable String npcLocationName;
    public boolean hasSpecificLocationName;
    public boolean ridingMount;
    /** Spawns on a mount of its own ({@link #createMountToRide}). */
    public boolean spawnRidingHorse;
    public boolean canBannerBearerSpawnRiding;
    /** The cape on its back (LOTRCapes), drawn by its renderer; none for most. */
    public @Nullable Identifier npcCape;
    public boolean hurtOnlyByPlates = true;
    public boolean isTraderEscort;
    public boolean shouldTraderRespawn;
    /** The visit of a {@link LOTRTravellingTrader}; null for any other NPC. */
    public final @Nullable LOTRTravellingTraderInfo travellingTraderInfo;
    /** The trades of a {@link LOTRTradeable}; null for any other NPC. */
    public final @Nullable LOTRTraderNPCInfo traderNPCInfo;
    /** Where a structure-bound trader first had its home (preventTraderKidnap). */
    private boolean setInitialHome;
    private BlockPos initHome = BlockPos.ZERO;
    private int initHomeRange;
    public AttackMode currentAttackMode = AttackMode.IDLE;
    private boolean firstUpdatedAttackMode;
    private @Nullable UUID prevAttackTarget;
    private int combatCooldown;
    public int nearbyBannerFactor;
    public int npcTalkTick;
    private boolean addedBurningPanic;
    private boolean loadingFromNBT;
    /** The festive hat or pumpkin its client shows on its head, and the entity ID it was chosen for. */
    private @Nullable ItemStack festiveHead;
    private int festiveHeadId;

    protected LOTRNPCEntity(EntityType<? extends LOTRNPCEntity> type, Level level) {
        super(type, level);
        setupNPCGender();
        setupNPCName();
        this.traderNPCInfo = this instanceof LOTRTradeable ? new LOTRTraderNPCInfo(this) : null;
        this.travellingTraderInfo = this instanceof LOTRTravellingTrader travelling
                ? new LOTRTravellingTraderInfo(travelling) : null;
        this.bossInfo = this instanceof LOTRBoss boss ? new LOTRBossInfo(boss) : null;
        if (this instanceof LOTRBoss || this instanceof LOTRCharacter) {
            this.isNPCPersistent = true;
        }
    }

    /** applyEntityAttributes: the NPC attributes on top of a creature's. */
    public static AttributeSupplier.Builder createNPCAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE_EXTRA)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE_DRUNK)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY)
                .add(LOTRNPCAttributes.HORSE_ATTACK_SPEED);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_AGE, 0);
        builder.define(DATA_MALE, false);
        builder.define(DATA_NAME, "");
        builder.define(DATA_DRUNK, false);
        builder.define(DATA_EATING, false);
        builder.define(DATA_COMBAT_STANCE, false);
    }

    // --- What each kind of NPC says about itself --------------------------------

    public LOTRFaction getFaction() {
        return LOTRFaction.UNALIGNED;
    }

    public LOTRFaction getHiringFaction() {
        return getFaction();
    }

    public LOTRFaction getInfluenceZoneFaction() {
        return getFaction();
    }

    /** The alignment a kill of it is worth. */
    public float getAlignmentBonus() {
        return 0.0f;
    }

    /** isFriendly: neither fighting this player nor last hurt by them. */
    public boolean isFriendly(Player player) {
        return getTarget() != player && getLastHurtByPlayer() != player;
    }

    public boolean isFriendlyAndAligned(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    /** getAttackSound: the cry it gives on picking a new target, if any. */
    public @Nullable SoundEvent getAttackSound() {
        return null;
    }

    /** A speech bank to answer this player with, or null to say nothing. */
    public @Nullable String getSpeechBank(Player player) {
        return null;
    }

    public void setupNPCGender() {
    }

    public void setupNPCName() {
    }

    /** setSpecificLocationName: this NPC's own place (a tavern's name), for its speech. */
    public void setSpecificLocationName(String name) {
        this.npcLocationName = name;
        this.hasSpecificLocationName = true;
    }

    /** getNPCName: its own name; the kind's name unless a subclass gives it one. */
    public String getNPCName() {
        return getType().getDescription().getString();
    }

    public boolean shouldBurningPanic() {
        return true;
    }

    public boolean shouldRenderNPCHair() {
        return true;
    }

    /** shouldRenderNPCChest: a woman without a chestplate. */
    public boolean shouldRenderNPCChest() {
        return !this.familyInfo.isMale() && !isBaby() && getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    public float getNPCScale() {
        return isBaby() ? 0.5f : 1.0f;
    }

    public int getNPCTalkInterval() {
        return 40;
    }

    /** getTalkInterval: an NPC's living sound, every ten seconds or so. */
    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    /**
     * getHeldItemLeft, where a kind holds something of its own in the left hand
     * (an orc bombardier's bomb); empty for the usual banner or trader's coin.
     */
    public ItemStack getHeldItemLeft() {
        return ItemStack.EMPTY;
    }

    public float getPoisonedArrowChance() {
        return 0.0f;
    }

    public boolean lootsExtraCoins() {
        return false;
    }

    public boolean canRenameNPC() {
        return false;
    }

    public void onAttackModeChange(AttackMode mode, boolean mounted) {
    }

    /** changeNPCNameForMarriage: a people's surname custom on marrying. */
    public void changeNPCNameForMarriage(LOTRNPCEntity spouse) {
    }

    /** createNPCChildName: a newborn's name, from its parents. */
    public void createNPCChildName(LOTRNPCEntity maleParent, LOTRNPCEntity femaleParent) {
    }

    public void onArtificalSpawn() {
    }

    /** The kinds that fight when attacked but go looking for no one, hire out no one, and are no boss. */
    public boolean isCivilianNPC() {
        return !this.isTargetSeeker && !(this instanceof LOTRUnitTradeable) && !(this instanceof LOTRMercenary)
                && !(this instanceof LOTRBoss);
    }

    /**
     * getBlockPathWeight: anywhere, once spawn restrictions are lifted; for a
     * creature of the dark, the darker the better (half less the light's
     * brightness); otherwise nowhere in particular.
     *
     * <p>NOT ported yet: a creature of the dark being at home anywhere in a
     * biome where hostiles walk by day (LOTRBiome.canSpawnHostilesInDay, with
     * the biomes, D10), and conquest spawning's exemption (D12).
     */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (this.liftSpawnRestrictions) {
            return 1.0f;
        }
        if (this.spawnsInDarkness) {
            return 0.5f - LOTRLegacyWorld.brightness(level, pos);
        }
        return 0.0f;
    }

    /**
     * getCanSpawnHere: a creature of the dark only where it is dark enough,
     * unless its spawn restrictions are lifted; and no NPC where a banner
     * protects the land against it or a respawner blocks its faction's
     * spawns, unless its banner restrictions are lifted.
     *
     * <p>NOT ported yet: the biomes where hostiles walk by day (D10) and
     * conquest spawning's exemptions (D12).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        if ((!this.spawnsInDarkness || this.liftSpawnRestrictions || isValidLightLevelForDarkSpawn(level))
                && super.checkSpawnRules(level, reason)) {
            return this.liftBannerRestrictions
                    || !LOTRBannerProtection.isProtected(level(), this, LOTRBannerProtection.forNPC(this), false)
                    && !LOTRNPCRespawnerEntity.isSpawnBlocked(this);
        }
        return false;
    }

    /**
     * isValidLightLevelForDarkSpawn: a monster's darkness. The original copied
     * 1.7.10's monster light check, so this is 26.2's.
     */
    private boolean isValidLightLevelForDarkSpawn(LevelAccessor level) {
        return level instanceof ServerLevelAccessor serverLevel
                && Monster.isDarkEnoughToSpawn(serverLevel, blockPosition(), this.random);
    }

    /** Hired by a player (LOTRHiredNPCInfo, D9b). */
    public boolean isHired() {
        return this.hiredNPCInfo.isActive;
    }

    /** canReEquipHired: whether its player may put this on it in this slot (the hired inventory). */
    public boolean canReEquipHired(int slot, ItemStack stack) {
        return true;
    }

    /** isTrader: a trader, a hirer or a mercenary. */
    public boolean isTrader() {
        return this instanceof LOTRTradeable || this instanceof LOTRUnitTradeable || this instanceof LOTRMercenary;
    }

    /**
     * A trader or a hirer, but not a mercenary: those whose structure home
     * holds them (preventTraderKidnap) and who leave a respawner on death.
     */
    private boolean tradesOrHires() {
        return this instanceof LOTRTradeable || this instanceof LOTRUnitTradeable;
    }

    public boolean canBeFreelyTargetedBy(Mob attacker) {
        return true;
    }

    // --- Names ------------------------------------------------------------------

    /** getCommandSenderName: "Name, the Kind" (entity.lotr.generic.entityName); every NPC is Gandalf on the first of April. */
    @Override
    public Component getName() {
        if (hasCustomName()) {
            return super.getName();
        }
        Component kind = getEntityClassName();
        String npcName = LOTRMod.isAprilFools() ? "Gandalf" : getNPCName();
        if (npcName.equals(kind.getString())) {
            return kind;
        }
        return getNPCFormattedName(npcName, kind);
    }

    /** getEntityClassName: the kind of NPC it is, as its name shows it. */
    public Component getEntityClassName() {
        return getType().getDescription();
    }

    /** getNPCFormattedName: "Name, the Kind", unless a people words it otherwise. */
    protected Component getNPCFormattedName(String npcName, Component kind) {
        return Component.translatable("entity.lotr.generic.entityName", npcName, kind);
    }

    /** setCustomNameTag: only for NPCs that may be renamed. */
    @Override
    public void setCustomName(@Nullable Component name) {
        if (canRenameNPC() || this.loadingFromNBT) {
            super.setCustomName(name);
        }
    }

    // --- Age ----------------------------------------------------------------------

    @Override
    public boolean isBaby() {
        return this.familyInfo.getAge() < 0;
    }

    public boolean canGetDrunk() {
        return !isBaby() && !isTrader() && !this.isTraderEscort && !isHired();
    }

    public boolean isDrunkard() {
        return this.familyInfo.isDrunk();
    }

    public float getDrunkenSpeechFactor() {
        return this.random.nextInt(3) == 0 ? Mth.randomBetween(this.random, 0.0f, 0.3f) : 0.0f;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_AGE.equals(accessor)) {
            refreshDimensions();
        }
    }

    @Override
    public float getAgeScale() {
        return getNPCScale();
    }

    // --- Items -------------------------------------------------------------------

    /**
     * getEquipmentInSlot: on its client, at Halloween one NPC in three wears a
     * pumpkin (one in ten of those lit), and at Christmas one in three a red
     * hat with a white feather or a party hat of any colour. Each NPC's is
     * chosen once; none of it is real.
     */
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (slot == EquipmentSlot.HEAD && level().isClientSide()) {
            if (this.festiveHead == null || this.festiveHeadId != getId()) {
                this.festiveHead = createFestiveHead();
                this.festiveHeadId = getId();
            }
            if (!this.festiveHead.isEmpty()) {
                return this.festiveHead;
            }
        }
        return super.getItemBySlot(slot);
    }

    private ItemStack createFestiveHead() {
        RandomSource festiveRand = RandomSource.create(getId() * 341873128712L);
        if (LOTRMod.isHalloween()) {
            if (festiveRand.nextInt(3) == 0) {
                return new ItemStack(festiveRand.nextInt(10) == 0 ? Items.JACK_O_LANTERN : Items.CARVED_PUMPKIN);
            }
        } else if (LOTRMod.isChristmas() && festiveRand.nextInt(3) == 0) {
            if (this.random.nextBoolean()) {
                ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
                LOTRLeatherHatItem.setHatColor(hat, 0xCC231B);
                LOTRLeatherHatItem.setFeatherColor(hat, LOTRLeatherHatItem.FEATHER_WHITE);
                return hat;
            }
            ItemStack hat = new ItemStack(LOTRMiscItems.PARTY_HAT);
            hat.set(DataComponents.DYED_COLOR, new DyedItemColor(Mth.hsvToRgb(this.random.nextFloat(), 1.0f, 1.0f)));
            return hat;
        }
        return ItemStack.EMPTY;
    }

    public boolean isEating() {
        return this.entityData.get(DATA_EATING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(DATA_EATING, eating);
    }

    public boolean isInCombatStance() {
        return this.entityData.get(DATA_COMBAT_STANCE);
    }

    /** isAimingRanged: a bow (not a spear or trident) drawn on a target in range. */
    public boolean isAimingRanged() {
        ItemStack stack = getMainHandItem();
        if (!stack.isEmpty() && stack.getUseAnimation() == ItemUseAnimation.BOW) {
            LivingEntity target = getTarget();
            return target != null && distanceToSqr(target) < getMaxCombatRangeSq();
        }
        return false;
    }

    public double getMaxCombatRange() {
        return getAttributeValue(Attributes.FOLLOW_RANGE) * 0.95;
    }

    public double getMaxCombatRangeSq() {
        double d = getMaxCombatRange();
        return d * d;
    }

    public double getMeleeRange() {
        double d = 4.0 + getBbWidth() * getBbWidth();
        return this.ridingMount ? d * MOUNT_RANGE_BONUS : d;
    }

    public double getMeleeRangeSq() {
        double d = getMeleeRange();
        return d * d;
    }

    // --- Targets ----------------------------------------------------------------

    /**
     * addTargetTasks: retaliate against whoever hurts it and, for a target
     * seeker, look for players of low alignment and NPCs of hostile factions;
     * a hired unit also takes on whoever hurts its player or whom its player
     * hurts. Returns the last priority used.
     */
    public int addTargetTasks(boolean seekTargets) {
        return addTargetTasks(seekTargets, LOTRNearestAttackableTargetGoal::forPlayers);
    }

    /**
     * addTargetTasks(seekTargets, c): as above, with the people's own goal for
     * seeking out players (the Wood-elves' is warier).
     */
    public int addTargetTasks(boolean seekTargets,
                              Function<PathfinderMob, LOTRNearestAttackableTargetGoal> playerGoal) {
        return addTargetTasks(seekTargets, playerGoal, LOTRNearestAttackableTargetGoal::forFactions);
    }

    /**
     * addTargetTasks(seekTargets, c) for a goal class whose difference touches
     * NPC targets too (the huorns'): the original built both goals of it.
     */
    public int addTargetTasks(boolean seekTargets,
                              Function<PathfinderMob, LOTRNearestAttackableTargetGoal> playerGoal,
                              Function<PathfinderMob, LOTRNearestAttackableTargetGoal> factionGoal) {
        this.targetSelector.removeAllGoals(g -> true);
        this.targetSelector.addGoal(1, new LOTRHiringPlayerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LOTRHiringPlayerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new LOTRNPCHurtByTargetGoal(this));
        this.isTargetSeeker = seekTargets;
        if (seekTargets) {
            this.targetSelector.addGoal(4, playerGoal.apply(this));
            this.targetSelector.addGoal(4, factionGoal.apply(this));
            return 4;
        }
        return 3;
    }

    /** setAttackTarget: and, sometimes, a threat spoken as it turns on a player. */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        boolean speak = target != null && getSensing().hasLineOfSight(target) && this.random.nextInt(3) == 0;
        setTarget(target, speak);
    }

    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        LivingEntity prevTarget = getTarget();
        super.setTarget(target);
        this.hiredNPCInfo.onSetTarget(target, prevTarget);
        if (target != null && !target.getUUID().equals(this.prevAttackTarget)) {
            this.prevAttackTarget = target.getUUID();
            SoundEvent attackSound = getAttackSound();
            if (attackSound != null && level() instanceof ServerLevel) {
                playSound(attackSound, getSoundVolume(), getVoicePitch());
            }
            if (level() instanceof ServerLevel level && target instanceof Player player && speak) {
                String bank = getSpeechBank(player);
                if (bank != null) {
                    List<LOTRNPCEntity> attackers = level.getEntitiesOfClass(LOTRNPCEntity.class,
                            getBoundingBox().inflate(16.0), npc -> npc != this && npc.isAlive() && npc.getTarget() == player);
                    if (attackers.size() <= 5) {
                        sendSpeechBank(player, bank);
                    }
                }
            }
        }
    }

    // --- Combat -----------------------------------------------------------------

    /**
     * attackEntityAsMob: three quarters of the weapon's damage bonus (or the
     * NPC attack damage if it is unarmed or the weapon adds none), the extra
     * and drunken damage, half a point per nearby banner, and the vanilla
     * enchantment effects.
     */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        float damage = (float) getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE);
        ItemStack weapon = getMainHandItem();
        DamageSource source = damageSources().mobAttack(this);
        // getMeleeDamageBonus counted the weapon's enchantment damage, and the
        // enchantments were then added again below -- kept, as it was.
        float weaponDamage = weapon.isEmpty() ? 0.0f : (LOTRModifiers.getMeleeDamageBonus(weapon)
                + EnchantmentHelper.modifyDamage(level, weapon, target, source, 0.0f)) * 0.75f;
        if (weaponDamage > 0.0f) {
            damage = weaponDamage;
        }
        damage += (float) getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE_EXTRA);
        if (isDrunkard()) {
            damage += (float) getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE_DRUNK);
        }
        damage += this.nearbyBannerFactor * 0.5f;
        damage = EnchantmentHelper.modifyDamage(level, weapon, target, source, damage);
        boolean hit = target.hurtServer(level, source, damage);
        if (hit) {
            float knockback = getKnockback(target, source);
            if (knockback > 0.0f) {
                float yaw = getYRot() * Mth.DEG_TO_RAD;
                target.push(-Mth.sin(yaw) * knockback * 0.5f, 0.1, Mth.cos(yaw) * knockback * 0.5f);
                setDeltaMovement(getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            if (target instanceof LivingEntity living && !weapon.isEmpty()) {
                weapon.hurtEnemy(living, this);
            }
            EnchantmentHelper.doPostAttackEffects(level, target, source);
            setLastHurtMob(target);
        }
        return hit;
    }

    /**
     * The original sat every rider of an animal mount with its feet at half the
     * mount's height (getMountedYOffset). The port's animal mounts carry their
     * passengers 0.51875 above that (LOTREntities.mount), so an NPC rider
     * sits back down by the same amount. Mounts that are NPCs themselves (wargs,
     * spiders) already carry riders at the original's height.
     */
    @Override
    public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
        if (!(vehicle instanceof LOTRNPCEntity)) {
            return new Vec3(0.0, LOTREntities.MOUNT_PASSENGER_RAISE, 0.0);
        }
        return super.getVehicleAttachmentPoint(vehicle);
    }

    /**
     * EntityAITarget.isSuitableTarget never asked the difficulty: on Peaceful an
     * NPC still turns on a player who hurts it (or one it seeks out), as in the
     * original -- its blows then do nothing, as mob damage to players does on
     * Peaceful. 26.2's LivingEntity.canAttack refuses players outright there,
     * which left NPCs ignoring every attack; only that refusal is dropped.
     */
    @Override
    public boolean canAttack(LivingEntity target) {
        return target.canBeSeenAsEnemy();
    }

    /** attackEntityFrom: nearby banners soften the blow, a twelfth each. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (getFirstPassenger() != null && source.getEntity() == getFirstPassenger()) {
            return false;
        }
        if (this.nearbyBannerFactor > 0) {
            damage = damage * (12 - this.nearbyBannerFactor) / 12.0f;
        }
        boolean hurt = super.hurtServer(level, source, damage);
        // Hurt by a hired unit: dies as if to a player, but no player is credited.
        if (hurt && source.getEntity() instanceof LOTRNPCEntity attacker && attacker.hiredNPCInfo.isActive
                && attacker.hiredNPCInfo.getHiringPlayer() != null) {
            this.lastHurtByPlayerMemoryTime = 100;
            this.lastHurtByPlayer = null;
        }
        if (hurt && this.hurtOnlyByPlates) {
            this.hurtOnlyByPlates = source.getDirectEntity() instanceof LOTRPlateEntity;
        }
        return hurt;
    }

    /** damageEntity: a boss notes who hurt it, and how badly. */
    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource source, float damage) {
        super.actuallyHurt(level, source, damage);
        if (this.bossInfo != null) {
            this.bossInfo.onHurt(source, damage);
        }
    }

    /** fall: a boss's jump attack lands without hurting it. */
    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        if (this.bossInfo != null) {
            fallDistance = this.bossInfo.onFall((float) fallDistance);
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    /** The boss bar, for those the boss is seen by. */
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.bossInfo != null) {
            this.bossInfo.startSeenByPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (this.bossInfo != null) {
            this.bossInfo.stopSeenByPlayer(player);
        }
    }

    /** updateCombat: the attack mode, the combat stance, and a dead or creative target dropped. */
    private void updateCombat(ServerLevel level) {
        LivingEntity target = getTarget();
        if (target != null && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
            setTarget(null);
        }
        boolean changedMounted = false;
        boolean changedAttackMode = false;
        boolean ridingNow = getVehicle() instanceof Mob vehicle && vehicle.isAlive() && !(vehicle instanceof LOTRNPCEntity);
        if (this.ridingMount != ridingNow) {
            setRidingHorse(ridingNow);
            changedMounted = true;
        }
        if (!isBaby()) {
            target = getTarget();
            if (target != null) {
                double d = distanceToSqr(target);
                boolean spearWithBackup = this.npcItemsInv.hasSpearBackup(getMainHandItem());
                if (d < getMeleeRangeSq() || spearWithBackup) {
                    if (this.currentAttackMode != AttackMode.MELEE) {
                        this.currentAttackMode = AttackMode.MELEE;
                        changedAttackMode = true;
                    }
                } else if (d < getMaxCombatRangeSq() && this.currentAttackMode != AttackMode.RANGED) {
                    this.currentAttackMode = AttackMode.RANGED;
                    changedAttackMode = true;
                }
            } else if (this.currentAttackMode != AttackMode.IDLE) {
                this.currentAttackMode = AttackMode.IDLE;
                changedAttackMode = true;
            }
            if (!this.firstUpdatedAttackMode) {
                this.firstUpdatedAttackMode = true;
                changedAttackMode = true;
            }
        }
        if (changedAttackMode || changedMounted) {
            onAttackModeChange(this.currentAttackMode, this.ridingMount);
        }
        if (getTarget() != null) {
            this.combatCooldown = 40;
        } else if (this.combatCooldown > 0) {
            --this.combatCooldown;
        }
        this.entityData.set(DATA_COMBAT_STANCE, this.combatCooldown > 0);
    }

    public void refreshCurrentAttackMode() {
        onAttackModeChange(this.currentAttackMode, this.ridingMount);
    }

    /** setRidingHorse: half again the follow range while mounted. */
    public void setRidingHorse(boolean riding) {
        this.ridingMount = riding;
        var range = getAttribute(Attributes.FOLLOW_RANGE);
        range.setBaseValue(riding ? range.getBaseValue() * 1.5 : range.getBaseValue() / 1.5);
    }

    // --- Talking ----------------------------------------------------------------

    public boolean canNPCTalk() {
        return isAlive() && this.npcTalkTick >= getNPCTalkInterval();
    }

    public void markNPCSpoken() {
        this.npcTalkTick = 0;
    }

    public void sendSpeechBank(Player player, String bank) {
        CharSequence location = null;
        if (this.npcLocationName != null) {
            location = this.hasSpecificLocationName ? this.npcLocationName
                    : Component.translatable(this.npcLocationName, getNPCName()).getString();
        }
        LOTRSpeech.sendSpeech(player, this, LOTRSpeech.getRandomSpeechForPlayer(this, bank, player, location, null));
        markNPCSpoken();
    }

    public void sendSpeechBankLine(Player player, String bank, int line) {
        LOTRSpeech.sendSpeech(player, this, LOTRSpeech.getSpeechLineForPlayer(this, bank, line, player, null, null));
        markNPCSpoken();
    }

    /**
     * speakTo: its own bank, now and then a holiday one, and once in ten
     * thousand the Easter egg.
     */
    public boolean speakTo(Player player) {
        String bank = getSpeechBank(player);
        if (this.random.nextInt(8) == 0) {
            if (LOTRMod.isChristmas()) {
                bank = "special/christmas";
            } else if (LOTRMod.isNewYearsDay()) {
                bank = "special/newYear";
            } else if (LOTRMod.isAprilFools()) {
                bank = "special/aprilFool";
            } else if (LOTRMod.isHalloween()) {
                bank = "special/halloween";
            }
        }
        if (this.random.nextInt(10000) == 0) {
            bank = "special/smilebc";
        }
        if (bank != null) {
            sendSpeechBank(player, bank);
            return true;
        }
        return false;
    }

    /**
     * LOTREventHandler.onEntityInteract's NPC branches ran before any NPC's own
     * interact: a trader, hirer or mercenary who will deal with the player,
     * or the player's own hired unit, offers its choice of screen. Here they
     * come before every subclass's mobInteract.
     */
    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (LOTRTradeNetworking.tryOpenInteract(this, player)) {
            return InteractionResult.SUCCESS;
        }
        // An operator in creative with a clock is told whose hired unit this is.
        if (this.hiredNPCInfo.isActive && player.getAbilities().instabuild
                && player.getItemInHand(hand).is(Items.CLOCK)) {
            UUID hiringUUID = this.hiredNPCInfo.getHiringPlayerUUID();
            if (player instanceof ServerPlayer serverPlayer && hiringUUID != null
                    && serverPlayer.level().getServer().getPlayerList().isOp(serverPlayer.nameAndId())) {
                String name = serverPlayer.level().getServer().services().nameToIdCache().get(hiringUUID)
                        .map(NameAndId::name).orElse(hiringUUID.toString());
                player.sendSystemMessage(Component.literal("Hired unit belongs to " + name)
                        .withStyle(ChatFormatting.GREEN));
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand, location);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return talkInteract(player, hand);
    }

    /** The trade screen's Talk button: interactFirst, without the item in hand. */
    public InteractionResult talkInteract(Player player) {
        return talkInteract(player, InteractionHand.MAIN_HAND);
    }

    /** interact: marriage first, then a word -- if it is not fighting. */
    private InteractionResult talkInteract(Player player, InteractionHand hand) {
        if (this.familyInfo.interact(player, player.getItemInHand(hand))) {
            return InteractionResult.SUCCESS;
        }
        if (!level().isClientSide() && canNPCTalk() && getTarget() == null && speakTo(player)) {
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    // --- Ticking -----------------------------------------------------------------

    @Override
    public void aiStep() {
        // handleNPCMovement's updateArmSwingProgress: 26.2 advances the swing
        // only for monsters and players, so an NPC's arm never moved.
        updateSwingTime();
        super.aiStep();
        if (this.bossInfo != null) {
            this.bossInfo.onUpdate();
        }
        if (level() instanceof ServerLevel level) {
            updateCombat(level);
            if (this.tickCount % 10 == 0) {
                updateNearbyBanners(level);
            }
            this.familyInfo.tick();
            if (!this.addedBurningPanic) {
                if (shouldBurningPanic()) {
                    this.goalSelector.addGoal(0, new LOTRBurningPanicGoal(this, 1.5));
                }
                this.addedBurningPanic = true;
            }
            this.hiredNPCInfo.tick();
            if (this.traderNPCInfo != null) {
                this.traderNPCInfo.tick();
            }
            if (this.travellingTraderInfo != null) {
                this.travellingTraderInfo.tick();
            }
            if (tradesOrHires()) {
                recordInitialHome();
            }
            updateHealingLogic();
            checkGUIOpenAndNavigation(level);
            if (this.npcTalkTick < getNPCTalkInterval()) {
                ++this.npcTalkTick;
            }
            returnHome();
        }
        if (this.isChilly && getDeltaMovement().lengthSqr() >= 0.01 && level().isClientSide()) {
            double x = getX() + Mth.randomBetween(this.random, -0.3f, 0.3f) * getBbWidth();
            double y = getBoundingBox().minY + Mth.randomBetween(this.random, 0.2f, 0.7f) * getBbHeight();
            double z = getZ() + Mth.randomBetween(this.random, -0.3f, 0.3f) * getBbWidth();
            level().addParticle(LOTRParticles.CHILL, x, y, z,
                    -getDeltaMovement().x * 0.5, 0.0, -getDeltaMovement().z * 0.5);
        }
    }

    /**
     * checkGUIOpenAndNavigation: a trader stands still -- and its mount with
     * it -- while a player has its trade, coin exchange, smith's anvil or hire
     * screen open; and a hired unit while its player has its screen open.
     *
     * <p>NOT ported yet: the same for an open mini-quest offer (with quests).
     */
    private void checkGUIOpenAndNavigation(ServerLevel level) {
        if (!isAlive() || getTarget() != null) {
            return;
        }
        boolean guiOpen = false;
        if (isTrader()) {
            for (Player player : level.players()) {
                if (LOTRTradeNetworking.isGuiOpen(this, player)) {
                    guiOpen = true;
                    break;
                }
            }
        }
        if (this.hiredNPCInfo.isActive && this.hiredNPCInfo.isGuiOpen) {
            guiOpen = true;
        }
        if (guiOpen) {
            getNavigation().stop();
            if (getVehicle() instanceof LOTRNPCMount && getVehicle() instanceof Mob mount) {
                mount.getNavigation().stop();
            }
        }
    }

    /**
     * updateNearbyBanners: how many living banner bearers of its own faction
     * are within sixteen blocks, up to five. A banner bearer, or an NPC of no
     * faction, has none.
     */
    private void updateNearbyBanners(ServerLevel level) {
        if (getFaction() == LOTRFaction.UNALIGNED || this instanceof LOTRBannerBearer) {
            this.nearbyBannerFactor = 0;
            return;
        }
        List<LivingEntity> bannerBearers = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(16.0),
                e -> e instanceof LOTRBannerBearer && e != this && e.isAlive()
                        && LOTRNearestAttackableTargetGoal.factionOf(e) == getFaction());
        this.nearbyBannerFactor = Math.min(bannerBearers.size(), 5);
    }

    /**
     * updateNPCState's trader home: where a structure-bound trader first had
     * its home, and -- with "Prevent trader transport range" above 0 -- the
     * trader put back there, off any mount, once carried further than that.
     */
    private void recordInitialHome() {
        if (!this.setInitialHome) {
            if (hasHome()) {
                this.initHome = getHomePosition();
                this.initHomeRange = getHomeRadius();
            }
            this.setInitialHome = true;
        }
        int preventKidnap = LOTRConfig.preventTraderKidnap;
        if (preventKidnap > 0 && this.initHomeRange > 0
                && distanceToSqr(Vec3.atCenterOf(this.initHome)) > (double) preventKidnap * preventKidnap) {
            if (getVehicle() != null) {
                stopRiding();
            }
            snapTo(this.initHome.getX() + 0.5, this.initHome.getY(), this.initHome.getZ() + 0.5, getYRot(), getXRot());
        }
    }

    /**
     * updateHealingLogic: a trader or hired unit out of a fight heals half a
     * heart every two seconds, and a hired unit near its banners more.
     */
    private void updateHealingLogic() {
        if (isAlive() && (isTrader() || isHired()) && getTarget() == null) {
            float healAmount = 0.0f;
            if (this.tickCount % 40 == 0) {
                healAmount += 1.0f;
            }
            if (isHired() && this.nearbyBannerFactor > 0 && this.tickCount % (240 - this.nearbyBannerFactor * 40) == 0) {
                healAmount += 1.0f;
            }
            if (healAmount > 0.0f) {
                heal(healAmount);
                if (getVehicle() instanceof LivingEntity mount && !(mount instanceof LOTRNPCEntity)) {
                    mount.heal(healAmount);
                }
            }
        }
    }

    /** createMountToRide: a horse, unless its people ride something else. */
    public Mob createMountToRide(ServerLevel level) {
        return LOTREntities.HORSE.create(level, EntitySpawnReason.JOCKEY);
    }

    /**
     * onSpawnWithEgg's mount: made where the NPC stands and ridden, if there
     * is room -- an NPC's own, seeing as far as its rider.
     */
    private void spawnOnMount(ServerLevelAccessor accessor) {
        ServerLevel level = accessor.getLevel();
        Mob mount = createMountToRide(level);
        if (mount == null) {
            return;
        }
        mount.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
        if (!level.noCollision(mount)) {
            return;
        }
        mount.finalizeSpawn(accessor, level.getCurrentDifficultyAt(mount.blockPosition()), EntitySpawnReason.JOCKEY, null);
        level.addFreshEntity(mount);
        startRiding(mount, true, false);
        if (mount instanceof LOTRNPCMount npcMount && !(mount instanceof LOTRNPCEntity)) {
            setRidingHorse(true);
            npcMount.setBelongsToNPC(true);
            LOTRNPCMount.setNavigatorRangeFromNPC(mount, this);
        }
    }

    /** initCreatureForHire: equipped as on spawning, without a mount. */
    public void initCreatureForHire(ServerLevel level) {
        this.spawnRidingHorse = false;
        finalizeSpawn(level, level.getCurrentDifficultyAt(blockPosition()), EntitySpawnReason.MOB_SUMMONED, null);
    }

    /** attackEntityWithRangedAttack: an arrow, unless the NPC looses something else. */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        npcArrowAttack(target, power);
    }

    /**
     * npcArrowAttack: an arrow (poisoned, at the NPC's chance) at 1.3 plus a
     * block's worth per 80 of distance, as true as the NPC's ranged accuracy,
     * carrying its bow's modifiers. A plain arrow also takes the bow's launch
     * speed; the poisoned one did not. The spread per point of accuracy is
     * today's vanilla's.
     */
    public void npcArrowAttack(LivingEntity target, float power) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack held = getMainHandItem();
        float str = 1.3f + distanceTo(target) / 80.0f;
        float accuracy = (float) getAttributeValue(LOTRNPCAttributes.NPC_RANGED_ACCURACY);
        AbstractArrow arrow;
        if (this.random.nextFloat() < getPoisonedArrowChance()) {
            arrow = new LOTRPoisonedArrowEntity(LOTREntities.POISONED_ARROW, this, level,
                    new ItemStack(LOTRCombatItems.POISONED_ARROW), held.isEmpty() ? null : held);
        } else {
            arrow = ProjectileUtil.getMobArrow(this, new ItemStack(Items.ARROW), power, held);
            float launch = LOTRModifiers.rangedDamageFactor(held);
            if (held.getItem() instanceof LOTRBowItem bow) {
                launch *= bow.getVelocityFactor();
            }
            str *= launch;
        }
        if (!held.isEmpty()) {
            LOTRModifierSpecials.onLaunch(held, arrow);
        }
        double dx = target.getX() - getX();
        double dy = target.getBoundingBox().minY + target.getBbHeight() / 3.0f - arrow.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontal * 0.2, dz, str, accuracy);
        playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
        level.addFreshEntity(arrow);
    }

    /**
     * npcCrossbowAttack: a bolt (poisoned, at the NPC's chance) at a touch
     * over 1.5 -- a hair more the further the target -- times the crossbow's
     * launch speed, dead true, carrying the crossbow's modifiers. The sound is
     * vanilla's crossbow shot, as the port's crossbows use.
     */
    public void npcCrossbowAttack(LivingEntity target, float power) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack held = getMainHandItem();
        float str = 1.0f + distanceTo(target) / 16.0f * 0.015f;
        boolean poison = this.random.nextFloat() < getPoisonedArrowChance();
        ItemStack boltItem = new ItemStack(poison ? LOTRCombatItems.POISONED_CROSSBOW_BOLT : LOTRCombatItems.CROSSBOW_BOLT);
        LOTRCrossbowBoltEntity bolt = new LOTRCrossbowBoltEntity(LOTREntities.CROSSBOW_BOLT, this, level, boltItem,
                held.isEmpty() ? null : held);
        float launch = LOTRModifiers.rangedDamageFactor(held);
        if (held.getItem() instanceof LOTRCrossbowItem crossbow) {
            launch *= crossbow.getBoltVelocityFactor();
        }
        if (!held.isEmpty()) {
            LOTRModifierSpecials.onLaunch(held, bolt);
        }
        double dx = target.getX() - getX();
        double dy = target.getY() + target.getEyeHeight() - 0.7 - bolt.getY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        bolt.shoot(dx, dy + horizontal * 0.2, dz, str * launch * 1.5f, 1.0f);
        playSound(SoundEvents.CROSSBOW_SHOOT, 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
        level.addFreshEntity(bolt);
    }

    /**
     * shouldTraderRespawn(): whether the trader a respawner brings back will
     * itself leave a respawner. Most keep this NPC's own flag; the original's
     * travelling traders and a few others said no, so they come back once.
     */
    public boolean shouldTraderRespawn() {
        return this.shouldTraderRespawn;
    }

    /** startTraderVisiting: begins a travelling trader's visit to the player. */
    public void startTraderVisiting(Player player) {
        if (this.travellingTraderInfo != null) {
            this.travellingTraderInfo.startVisiting(player);
        }
    }

    public void playTradeSound() {
        playSound(LOTRSounds.EVENT_TRADE, 0.5f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f);
    }

    /** onPlayerStartTracking / markDirty: the hired unit's basic data to those who see it. */
    public void syncHiredInfo() {
        if (level() instanceof ServerLevel level && this.tickCount > 0) {
            LOTRHiredInfoPayload payload = LOTRHiredInfoPayload.of(this);
            for (ServerPlayer player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }

    private void returnHome() {
        if (!hasHome() || isWithinHome()) {
            return;
        }
        BlockPos home = getHomePosition();
        int range = getHomeRadius();
        double dist = Math.sqrt(distanceToSqr(home.getX() + 0.5, home.getY() + 0.5, home.getZ() + 0.5));
        if (dist > range + 128.0) {
            clearHome();
        } else if (getTarget() == null && getNavigation().isDone()) {
            clearHome();
            // A guarding unit near its post walks straight back to it.
            boolean goDirectlyHome = level().isLoaded(home) && this.hiredNPCInfo.isGuardMode() && dist < 16.0;
            if (goDirectlyHome) {
                getNavigation().moveTo(home.getX() + 0.5, home.getY() + 0.5, home.getZ() + 0.5, 1.3);
            } else {
                Vec3 path = null;
                for (int l = 0; l < 16 && path == null; ++l) {
                    path = DefaultRandomPos.getPosTowards(this, 8, 7, Vec3.atBottomCenterOf(home), Math.PI / 2);
                }
                if (path != null) {
                    getNavigation().moveTo(path.x, path.y, path.z, 1.3);
                }
            }
            setHomeTo(home, range);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        // A banner bearer walks, unless allowed to ride (nothing sets that yet).
        if (this.spawnRidingHorse && (!(this instanceof LOTRBannerBearer) || this.canBannerBearerSpawnRiding)) {
            spawnOnMount(level);
        }
        // One trader in ten thousand asks a hundred times the price, and is called Noah.
        if (this.traderNPCInfo != null && this.random.nextInt(10000) == 0) {
            this.traderNPCInfo.inflateBuyPrices();
            this.familyInfo.setName("Noah");
        }
        // LOTRItemSpawnEgg.onItemUse and LOTRDispenseSpawnEgg: kept for good;
        // and, from an egg used by hand, onArtificalSpawn.
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE || reason == EntitySpawnReason.DISPENSER) {
            this.isNPCPersistent = true;
            this.shouldTraderRespawn = true;
            if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
                onArtificalSpawn();
            }
        }
        return data;
    }

    // --- Death and drops ------------------------------------------------------------

    /** canDespawn. */
    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return !this.isNPCPersistent && !this.shouldTraderRespawn && !isHired();
    }

    /**
     * func_110163_bv (enablePersistence): nothing -- a name tag or a dispensed
     * piece of armour does not keep an NPC for good; isNPCPersistent does.
     */
    @Override
    public void setPersistenceRequired() {
    }

    /** allowLeashing: no NPC is led on a lead, unless its kind says otherwise. */
    @Override
    public boolean canBeLeashed() {
        return false;
    }

    /** getExperiencePoints: 4 to 6, unless its kind says otherwise. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(3);
    }

    /**
     * entityDropItem / npcDropItem: everything an NPC drops that does not
     * stack remembers it as a previous owner.
     */
    @Override
    public @Nullable ItemEntity spawnAtLocation(ServerLevel level, ItemStack stack, Vec3 offset) {
        if (!stack.isEmpty() && stack.getMaxStackSize() == 1) {
            LOTRItemOwnership.addPreviousOwner(stack, getName().getString());
        }
        return super.spawnAtLocation(level, stack, offset);
    }

    /** onDeath: a trader who should come back leaves its respawner. */
    @Override
    public void die(DamageSource source) {
        this.hiredNPCInfo.onDeath(source);
        if (this.travellingTraderInfo != null) {
            this.travellingTraderInfo.onDeath();
        }
        if (this.bossInfo != null) {
            this.bossInfo.onDeath(source);
        }
        super.die(source);
        if (level() instanceof ServerLevel level && tradesOrHires() && this.shouldTraderRespawn) {
            LOTRTraderRespawnEntity respawn = new LOTRTraderRespawnEntity(LOTREntities.TRADER_RESPAWN, level);
            respawn.snapTo(getX(), getBoundingBox().minY + getBbHeight() / 2.0f, getZ(), 0.0f, 0.0f);
            respawn.copyTraderDataFrom(this);
            level.addFreshEntity(respawn);
            respawn.onSpawn();
        }
    }

    public boolean canDropRares() {
        return !isHired();
    }

    public int getRandomCoinDropAmount() {
        return 1 + (int) Math.round(Math.pow(1.0 + Math.abs(this.random.nextGaussian()), 3.0) * 0.25);
    }

    protected int lootingLevel(ServerLevel level, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity killer) {
            return EnchantmentHelper.getEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), killer);
        }
        return 0;
    }

    public void dropChestContents(ServerLevel level, LOTRChestContents.Pool pool, int min, int max) {
        int count = Mth.nextInt(this.random, min, max);
        for (int i = 0; i < count; ++i) {
            spawnAtLocation(level, LOTRChestContents.pick(pool, this.random, true));
        }
    }

    public void dropNPCAmmo(ServerLevel level, Item item, int looting) {
        int ammo = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < ammo; ++l) {
            spawnAtLocation(level, new ItemStack(item));
        }
    }

    /**
     * dropFewItems for every NPC: its equipment (see
     * {@link #dropNPCEquipment}), and to a player's kill a silver coin handful
     * 1 in (8 - 2 x looting), a rare nugget 1 in (50 - 5 x looting) and a
     * smith's scroll 1 in (60 - 5 x looting). Subclasses add their own in
     * {@link #dropNPCItems}.
     */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        int looting = lootingLevel(level, source);
        this.hiredReplacedInv.dropAllReplacedItems();
        dropNPCEquipment(level, killedByPlayer, looting);
        if (killedByPlayer && canDropRares()) {
            if (this.random.nextInt(Math.max(8 - looting * 2, 1)) == 0) {
                int coins = getRandomCoinDropAmount() * Mth.nextInt(this.random, 1, looting + 1);
                spawnAtLocation(level, new ItemStack(LOTRMiscItems.SILVER_COIN, coins));
            }
            if (this.random.nextInt(Math.max(50 - looting * 5, 1)) == 0) {
                dropChestContents(level, LOTRChestContents.RARE_DROPS, 1, 1);
            }
            if (this.random.nextInt(Math.max(60 - looting * 5, 1)) == 0) {
                spawnAtLocation(level, LOTRModifiers.randomTemplate(this.random));
            }
        }
        dropNPCItems(level, killedByPlayer, looting);
    }

    /** The subclass's own dropFewItems. */
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
    }

    /**
     * dropNPCEquipment: to a player's kill, each piece of equipment it wears
     * or holds drops 1 in (20 - 4 x looting) x pieces, at 50-75% wear --
     * unless it is a guaranteed drop, which always falls, whole.
     */
    private void dropNPCEquipment(ServerLevel level, boolean killedByPlayer, int looting) {
        if (!killedByPlayer) {
            return;
        }
        EquipmentSlot[] slots = {EquipmentSlot.MAINHAND, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST,
                EquipmentSlot.HEAD};
        int count = 0;
        for (EquipmentSlot slot : slots) {
            if (!getItemBySlot(slot).isEmpty()) {
                ++count;
            }
        }
        if (count == 0) {
            return;
        }
        for (EquipmentSlot slot : slots) {
            ItemStack drop = getItemBySlot(slot);
            if (drop.isEmpty()) {
                continue;
            }
            boolean guaranteed = getDropChances().byEquipment(slot) >= 1.0f;
            if (!guaranteed) {
                int chance = 20 * count - looting * 4 * count;
                if (this.random.nextInt(Math.max(chance, 1)) != 0) {
                    continue;
                }
                if (drop.isDamageableItem()) {
                    drop.setDamageValue(Mth.floor(drop.getMaxDamage() * (0.5f + this.random.nextFloat() * 0.25f)));
                }
            }
            spawnAtLocation(level, drop);
            setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    /** dropEquipment: nothing -- dropNPCEquipment drops it instead. */
    @Override
    protected void dropEquipment(ServerLevel level) {
    }

    /** onKillEntity: a coin-looting NPC shakes coins out of its victim half the time. */
    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity victim, DamageSource source) {
        boolean result = super.killedEntity(level, victim, source);
        this.hiredNPCInfo.onKillEntity(victim);
        if (lootsExtraCoins() && victim instanceof LOTRNPCEntity npc && npc.canDropRares() && this.random.nextInt(2) == 0) {
            int coins = (int) (getRandomCoinDropAmount() * Mth.randomBetween(this.random, 1.0f, 3.0f));
            if (coins > 0) {
                victim.spawnAtLocation(level, new ItemStack(LOTRMiscItems.SILVER_COIN, coins));
            }
        }
        return result;
    }

    /** setDead: a mount it was riding goes with it. */
    @Override
    public void remove(RemovalReason reason) {
        Entity vehicle = getVehicle();
        super.remove(reason);
        if (this.deathTime == 0 && vehicle != null && reason.shouldDestroy()) {
            vehicle.discard();
        }
    }

    // --- Particles ------------------------------------------------------------------

    public void spawnHearts() {
        if (level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.HEART, getX(), getY() + 0.5 + getBbHeight() * 0.5,
                    getZ(), 8, getBbWidth(), getBbHeight() * 0.5, getBbWidth(), 0.02);
        }
    }

    /** spawnFoodParticles: crumbs of the held item from the mouth (the EATING NPCFX packet). */
    public void spawnFoodParticles() {
        ItemStack held = getMainHandItem();
        if (held.isEmpty() || !(level() instanceof ServerLevel level)) {
            return;
        }
        var particle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(held));
        for (int i = 0; i < 5; ++i) {
            Vec3 speed = new Vec3((this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0)
                    .xRot(-getXRot() * Mth.DEG_TO_RAD).yRot(-getYRot() * Mth.DEG_TO_RAD);
            Vec3 pos = new Vec3((this.random.nextFloat() - 0.5) * 0.3, -this.random.nextFloat() * 0.6 - 0.3, 0.6)
                    .xRot(-getXRot() * Mth.DEG_TO_RAD).yRot(-getYRot() * Mth.DEG_TO_RAD)
                    .add(getX(), getEyeY(), getZ());
            level.sendParticles(particle, pos.x, pos.y, pos.z, 0, speed.x, speed.y + 0.05, speed.z, 1.0);
        }
    }

    public void spawnSmokes() {
        if (level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.SMOKE, getX(), getY() + 0.5 + getBbHeight() * 0.5,
                    getZ(), 8, getBbWidth(), getBbHeight() * 0.5, getBbWidth(), 0.02);
        }
    }

    // --- Saving -------------------------------------------------------------------------

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.familyInfo.save(output);
        this.npcItemsInv.save(output);
        if (hasHome()) {
            output.putInt("NPCHomeX", getHomePosition().getX());
            output.putInt("NPCHomeY", getHomePosition().getY());
            output.putInt("NPCHomeZ", getHomePosition().getZ());
            output.putInt("NPCHomeRadius", getHomeRadius());
        }
        output.putBoolean("NPCPersistent", this.isNPCPersistent);
        if (this.bossInfo != null) {
            this.bossInfo.save(output);
        }
        if (this.npcLocationName != null) {
            output.putString("NPCLocationName", this.npcLocationName);
        }
        output.putBoolean("SpecificLocationName", this.hasSpecificLocationName);
        output.putBoolean("HurtOnlyByPlates", this.hurtOnlyByPlates);
        output.putBoolean("RidingHorse", this.ridingMount);
        output.putBoolean("NPCPassive", this.isPassive);
        output.putBoolean("TraderEscort", this.isTraderEscort);
        output.putBoolean("TraderShouldRespawn", this.shouldTraderRespawn);
        if (this.traderNPCInfo != null) {
            this.traderNPCInfo.save(output);
        }
        if (this.travellingTraderInfo != null) {
            this.travellingTraderInfo.save(output);
        }
        this.hiredNPCInfo.save(output);
        this.hiredReplacedInv.save(output);
        output.putBoolean("SetInitHome", this.setInitialHome);
        output.putInt("InitHomeX", this.initHome.getX());
        output.putInt("InitHomeY", this.initHome.getY());
        output.putInt("InitHomeZ", this.initHome.getZ());
        output.putInt("InitHomeR", this.initHomeRange);
        if (!this.killBonusFactions.isEmpty()) {
            ValueOutput.TypedOutputList<String> list = output.list("BonusFactions", Codec.STRING);
            this.killBonusFactions.forEach(f -> list.add(f.codeName()));
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.loadingFromNBT = true;
        super.readAdditionalSaveData(input);
        this.familyInfo.load(input);
        this.npcItemsInv.load(input);
        if (this.bossInfo != null) {
            this.bossInfo.load(input);
        }
        int radius = input.getIntOr("NPCHomeRadius", -1);
        if (radius >= 0) {
            setHomeTo(new BlockPos(input.getIntOr("NPCHomeX", 0), input.getIntOr("NPCHomeY", 0),
                    input.getIntOr("NPCHomeZ", 0)), radius);
        }
        this.isNPCPersistent = input.getBooleanOr("NPCPersistent", this.isNPCPersistent);
        this.npcLocationName = input.getString("NPCLocationName").orElse(null);
        this.hasSpecificLocationName = input.getBooleanOr("SpecificLocationName", false);
        this.hurtOnlyByPlates = input.getBooleanOr("HurtOnlyByPlates", false);
        this.ridingMount = input.getBooleanOr("RidingHorse", false);
        this.isPassive = input.getBooleanOr("NPCPassive", false);
        this.isTraderEscort = input.getBooleanOr("TraderEscort", false);
        this.shouldTraderRespawn = input.getBooleanOr("TraderShouldRespawn",
                tradesOrHires() && !(this instanceof LOTRTravellingTrader) && this.isNPCPersistent);
        if (this.traderNPCInfo != null) {
            this.traderNPCInfo.load(input);
        }
        if (this.travellingTraderInfo != null) {
            this.travellingTraderInfo.load(input);
        }
        this.hiredNPCInfo.load(input);
        this.hiredReplacedInv.load(input);
        this.setInitialHome = input.getBooleanOr("SetInitHome", false);
        this.initHome = new BlockPos(input.getIntOr("InitHomeX", 0), input.getIntOr("InitHomeY", 0),
                input.getIntOr("InitHomeZ", 0));
        this.initHomeRange = input.getIntOr("InitHomeR", 0);
        this.killBonusFactions.clear();
        for (String name : input.listOrEmpty("BonusFactions", Codec.STRING)) {
            LOTRFaction f = LOTRFaction.forName(name);
            if (f != null) {
                this.killBonusFactions.add(f);
            }
        }
        this.loadingFromNBT = false;
    }

    public enum AttackMode {
        MELEE, RANGED, IDLE
    }
}
