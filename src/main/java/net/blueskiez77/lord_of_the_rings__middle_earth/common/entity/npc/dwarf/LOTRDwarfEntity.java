package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCAvoidEvilPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCFollowParentGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCFollowSpouseGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCMarryGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCMateGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDwarf: Durin's Folk, "Name son of Father". They keep out of the
 * water, open doors, stand their ground against the players they hate and
 * seek out their enemies, marry at +200 with a dwarven ring and raise up to
 * three children, and eat and drink as dwarves do. Most are men: one in three
 * spawned by hand or hired is a dwarf-woman, and one in twenty spawned by hand
 * a child. They cry out on attacking, cheer over a kill, and a woman's voice
 * is higher.
 *
 * <p>NOT ported yet: mini-quests (D14), the pull of the dwarven mountains on
 * their wandering and their natural spawning -- underground on lit rock below
 * y 60, or one time in 200 anywhere -- (with the biomes), and the killDwarf,
 * marryDwarf and talkDwarfWoman achievements (D7).
 */
public class LOTRDwarfEntity extends LOTRNPCEntity {

    public LOTRDwarfEntity(EntityType<? extends LOTRDwarfEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
        this.familyInfo.marriageEntityClass = LOTRDwarfEntity.class;
        this.familyInfo.marriageRing = LOTRMiscItems.DWARVEN_MARRIAGE_RING;
        this.familyInfo.marriageAlignmentRequired = 200.0f;
        this.familyInfo.potentialMaxChildren = 3;
        this.familyInfo.timeToMature = 72000;
        this.familyInfo.breedingDelay = 48000;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 26.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, new LOTRNPCAvoidEvilPlayerGoal(this, 8.0f, 1.5, 1.8));
        this.goalSelector.addGoal(3, createDwarfAttackAI());
        this.goalSelector.addGoal(4, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(5, new LOTRNPCMarryGoal(this, 1.3));
        this.goalSelector.addGoal(6, new LOTRNPCMateGoal(this, 1.3));
        this.goalSelector.addGoal(7, new LOTRNPCFollowParentGoal(this, 1.4));
        this.goalSelector.addGoal(8, new LOTRNPCFollowSpouseGoal(this, 1.1));
        this.goalSelector.addGoal(9, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(10, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(11, new LOTREatGoal(this, getDwarfFoods(), 6000));
        this.goalSelector.addGoal(11, new LOTRDrinkGoal(this, LOTRFoods.DWARF_DRINK, 6000));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    protected Goal createDwarfAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    public LOTRFoods getDwarfFoods() {
        return LOTRFoods.DWARF;
    }

    public Item getDwarfSteelDrop() {
        return LOTRMaterialItems.DWARVEN_STEEL_INGOT;
    }

    public LOTRChestContents.Pool getLarderDrops() {
        return LOTRChestContents.DWARF_HOUSE_LARDER;
    }

    public LOTRChestContents.Pool getGenericDrops() {
        return LOTRChestContents.DWARVEN_TOWER;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DURINS_FOLK;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getDwarfName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public void createNPCChildName(LOTRNPCEntity maleParent, LOTRNPCEntity femaleParent) {
        this.familyInfo.setName(LOTRNames.getDwarfChildNameForParent(this.random, this.familyInfo.isMale(), maleParent));
    }

    /** onArtificalSpawn: one in three spawned by hand is a woman, and one in twenty a child. */
    @Override
    public void onArtificalSpawn() {
        if (getClass() == this.familyInfo.marriageEntityClass) {
            if (this.random.nextInt(3) == 0) {
                this.familyInfo.setMale(false);
                setupNPCName();
            }
            if (this.random.nextInt(20) == 0) {
                this.familyInfo.setChild();
            }
        }
    }

    /** initCreatureForHire: and one in three hired is a woman. */
    @Override
    public void initCreatureForHire(ServerLevel level) {
        super.initCreatureForHire(level);
        if (getClass() == this.familyInfo.marriageEntityClass && this.random.nextInt(3) == 0) {
            this.familyInfo.setMale(false);
            setupNPCName();
        }
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "dwarf/dwarf/hired";
            }
            return isBaby() ? "dwarf/child/friendly" : "dwarf/dwarf/friendly";
        }
        return isBaby() ? "dwarf/child/hostile" : "dwarf/dwarf/hostile";
    }

    @Override
    public @Nullable SoundEvent getAttackSound() {
        return LOTRSounds.DWARF_ATTACK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.DWARF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.DWARF_HURT;
    }

    /** getSoundPitch: a dwarf-woman's voice is higher. */
    @Override
    public float getVoicePitch() {
        float pitch = super.getVoicePitch();
        return this.familyInfo.isMale() ? pitch : pitch * 1.4f;
    }

    /** onKillEntity: a cheer over the fallen. */
    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity victim, DamageSource source) {
        boolean result = super.killedEntity(level, victim, source);
        playSound(LOTRSounds.DWARF_KILL, getSoundVolume(), getVoicePitch());
        return result;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(3);
    }

    /**
     * dropFewItems: dwarf bones, sometimes something from a larder or the
     * people's halls, and, slain by a player, now and then iron, the people's
     * steel, gold or silver nuggets, or a Book of True-silver -- more often
     * with looting.
     */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.DWARF_BONE);
        }
        if (this.random.nextInt(4) == 0) {
            dropChestContents(level, getLarderDrops(), 1, 2 + looting);
        }
        if (this.random.nextInt(8) == 0) {
            dropChestContents(level, getGenericDrops(), 1, 2 + looting);
        }
        if (killedByPlayer) {
            int rareDropChance = 20 - looting * 4;
            if (this.random.nextInt(Math.max(rareDropChance, 1)) == 0) {
                switch (this.random.nextInt(4)) {
                    case 0 -> spawnAtLocation(level, new ItemStack(Items.IRON_INGOT));
                    case 1 -> spawnAtLocation(level, new ItemStack(getDwarfSteelDrop()));
                    case 2 -> spawnAtLocation(level, new ItemStack(Items.GOLD_NUGGET, 1 + this.random.nextInt(3)));
                    default -> spawnAtLocation(level, new ItemStack(LOTRMaterialItems.SILVER_NUGGET, 1 + this.random.nextInt(3)));
                }
            }
            int mithrilBookChance = 40 - looting * 5;
            if (this.random.nextInt(Math.max(mithrilBookChance, 1)) == 0) {
                spawnAtLocation(level, new ItemStack(LOTRMaterialItems.BOOK_OF_TRUE_SILVER));
            }
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.DWARVEN_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    /** The name was once kept as "DwarfName". */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("DwarfName").ifPresent(this.familyInfo::setName);
    }
}
