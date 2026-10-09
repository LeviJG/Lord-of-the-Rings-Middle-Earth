package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAvoidOrcBombGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTROrcAvoidGoodPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTROrcPlaceBombGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTROrcSkirmishGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTROrcTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityOrc: the orcs of every people. They keep out of the water, open
 * doors, run from lit orc bombs, eat maggoty bread and drink orc draught, seek
 * out their enemies (Mordor's warily -- {@link LOTROrcTargetGoal}) and now and
 * then fall to fighting one another ({@link LOTROrcSkirmishGoal}). A weak orc
 * of a people other than Mordor's runs from a player at -500 or worse; out
 * under the day sky it is slowed; and its armour counts for three quarters.
 * A bombardier carries a bomb in its left hand, which the client is told of.
 *
 * <p>The daylight Resistance at amplifier -1 is not ported: it reduced damage
 * by nothing, and an amplifier of -1 does not survive saving now (it reads
 * back as 255, full immunity).
 *
 * <p>They hunt rabbits (vanilla's, standing in for LOTREntityRabbit as they
 * do for the wargs), one chance in 2000 a tick, bombardiers excepted.
 *
 * <p>NOT ported yet: spawning in darkness and the dwarven biomes' top-block rule, and the biomes
 * that let hostiles walk by day (D10/D12).
 */
public abstract class LOTROrcEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<ItemStack> DATA_BOMB =
            SynchedEntityData.defineId(LOTROrcEntity.class, EntityDataSerializers.ITEM_STACK);

    public boolean isWeakOrc = true;
    public int orcSkirmishTick;
    public @Nullable LivingEntity currentRevengeTarget;

    protected LOTROrcEntity(EntityType<? extends LOTROrcEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BOMB, ItemStack.EMPTY);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        if (avoidsOrcBombs()) {
            this.goalSelector.addGoal(2, new LOTRAvoidOrcBombGoal(this, 12.0f, 1.5, 2.0));
        }
        this.goalSelector.addGoal(3, new LOTROrcAvoidGoodPlayerGoal(this, 8.0f, 1.5));
        if (isOrcBombardier()) {
            this.goalSelector.addGoal(4, new LOTROrcPlaceBombGoal(this, getPlaceBombSpeed()));
        }
        this.goalSelector.addGoal(4, createOrcAttackAI());
        this.goalSelector.addGoal(5, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LOTREatGoal(this, LOTRFoods.ORC, 6000));
        this.goalSelector.addGoal(8, new LOTRDrinkGoal(this, LOTRFoods.ORC_DRINK, 6000));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.05f));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.05f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        addOrcTargetTasks(true);
    }

    /**
     * addTargetTasks with the orcs' own player goal, then skirmishing. The
     * traders and captains call plain addTargetTasks(false) afterwards, which
     * clears both, as the original's did.
     */
    protected void addOrcTargetTasks(boolean seekTargets) {
        int target = addTargetTasks(seekTargets, LOTROrcTargetGoal::new);
        this.targetSelector.addGoal(target + 1, new LOTROrcSkirmishGoal(this, true));
        // LOTREntityRabbit: vanilla's rabbit stands in, as for the wargs.
        if (!isOrcBombardier()) {
            this.targetSelector.addGoal(target + 2, new LOTRNearestAttackableTargetGoal(this, Rabbit.class, 2000, false, null));
        }
    }

    protected abstract Goal createOrcAttackAI();

    public boolean isOrcBombardier() {
        return false;
    }

    /** False for the Uruk sapper, whose constructor removed the flight from bombs. */
    protected boolean avoidsOrcBombs() {
        return true;
    }

    /** How fast a bombardier runs in to drop its bomb (LOTREntityAIOrcPlaceBomb's speed). */
    protected double getPlaceBombSpeed() {
        return 1.4;
    }

    /** canOrcSkirmish: not while any player is on one of its quests. */
    public boolean canOrcSkirmish() {
        return !this.questInfo.anyActiveQuestPlayers();
    }

    public boolean isOrcSkirmishing() {
        return this.orcSkirmishTick > 0;
    }

    public String getOrcSkirmishSpeech() {
        return "";
    }

    /** The steel an orc's rare drop is made of. */
    protected Item getOrcSteelDrop() {
        return LOTRMaterialItems.ORC_STEEL_INGOT;
    }

    /** setOrcSkirmishing: 160 ticks of it, and a jeer to every player within 24 as it starts. */
    public void setOrcSkirmishing() {
        int prevSkirmishTick = this.orcSkirmishTick;
        this.orcSkirmishTick = 160;
        if (!level().isClientSide() && prevSkirmishTick == 0) {
            List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(24.0));
            for (Player player : players) {
                LOTRSpeech.sendSpeech(player, this,
                        LOTRSpeech.getRandomSpeechForPlayer(this, getOrcSkirmishSpeech(), player, null, null));
            }
        }
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entity) {
        super.setLastHurtByMob(entity);
        if (entity != null) {
            this.currentRevengeTarget = entity;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel level) {
            if (getTarget() == null) {
                this.currentRevengeTarget = null;
            }
            if (this.isWeakOrc && level.isBrightOutside() && level.canSeeSky(BlockPos.containing(getX(), getBoundingBox().minY, getZ()))
                    && this.tickCount % 20 == 0) {
                addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200));
            }
            if (isOrcSkirmishing() && !LOTRConfig.enableOrcSkirmish) {
                this.orcSkirmishTick = 0;
            } else if (isOrcSkirmishing() && !(getTarget() instanceof LOTROrcEntity)) {
                --this.orcSkirmishTick;
            }
            if (isOrcBombardier()) {
                this.entityData.set(DATA_BOMB, this.npcItemsInv.getBomb().copy());
            }
        }
    }

    /** getHeldItemLeft: a bombardier's bomb, while it still has one. */
    @Override
    public ItemStack getHeldItemLeft() {
        if (isOrcBombardier()) {
            ItemStack bomb = level().isClientSide() ? this.entityData.get(DATA_BOMB) : this.npcItemsInv.getBomb();
            if (!bomb.isEmpty()) {
                return bomb;
            }
        }
        return super.getHeldItemLeft();
    }

    /** getTotalArmorValue: a weak orc's armour counts for three quarters. */
    @Override
    public int getArmorValue() {
        int armor = super.getArmorValue();
        return this.isWeakOrc ? Mth.floor(armor * 0.75) : armor;
    }

    @Override
    public float getPoisonedArrowChance() {
        return 0.06666667f;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        if (!this.npcItemsInv.getBomb().isEmpty()) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getBombingItem());
        } else {
            setItemSlot(EquipmentSlot.MAINHAND,
                    mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
        }
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getOrcName(this.random));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return LOTRSounds.ORC_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.ORC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.ORC_DEATH;
    }

    /**
     * dropFewItems: rotten flesh and orc bones, one time in ten maggoty bread,
     * and, slain by a player, now and then a strong orc draught or some of the
     * people's steel -- more often with looting.
     */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int flesh = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < flesh; ++l) {
            spawnAtLocation(level, Items.ROTTEN_FLESH);
        }
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.ORC_BONE);
        }
        if (this.random.nextInt(10) == 0) {
            int breads = 1 + this.random.nextInt(2) + this.random.nextInt(looting + 1);
            for (int l = 0; l < breads; ++l) {
                spawnAtLocation(level, LOTRFoodItems.MAGGOTY_BREAD);
            }
        }
        if (killedByPlayer) {
            int rareDropChance = 20 - looting * 4;
            if (this.random.nextInt(Math.max(rareDropChance, 1)) == 0) {
                if (this.random.nextInt(2) == 0) {
                    ItemStack orcDrink = LOTRDrinkItem.stack(LOTRFoodItems.ORC_DRAUGHT, 1 + this.random.nextInt(3));
                    LOTRVessel[] vessels = LOTRFoods.ORC_DRINK.getDrinkVessels();
                    orcDrink.set(LOTRDataComponents.VESSEL, vessels[this.random.nextInt(vessels.length)]);
                    spawnAtLocation(level, orcDrink);
                } else {
                    int ingots = 1 + this.random.nextInt(2) + this.random.nextInt(looting + 1);
                    for (int l = 0; l < ingots; ++l) {
                        spawnAtLocation(level, getOrcSteelDrop());
                    }
                }
            }
        }
        dropOrcItems(level, killedByPlayer, looting);
    }

    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("OrcSkirmish", this.orcSkirmishTick);
    }

    /** The name was once kept as "OrcName". */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("OrcName").ifPresent(this.familyInfo::setName);
        this.orcSkirmishTick = input.getIntOr("OrcSkirmish", 0);
    }
}
