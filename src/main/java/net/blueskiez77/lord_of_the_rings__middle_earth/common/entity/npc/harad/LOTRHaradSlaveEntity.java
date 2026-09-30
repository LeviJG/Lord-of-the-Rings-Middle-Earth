package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFarmGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCHurtByTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRFarmhand;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
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
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHaradSlave: a slave bought from the Corsairs to farm, a man taken
 * from Gondor (three in ten), Near Harad (one in two), the Morwaith (three in
 * twenty) or the Taurethrim (one in twenty), named and dressed as his people
 * and of their faction -- though only enemies of Near Harad, his masters, may
 * set upon him. He keeps out of the water, opens doors, farms, eats bread,
 * dates and kebabs and drinks water from a skin, and fights only when struck.
 */
public class LOTRHaradSlaveEntity extends LOTRManEntity implements LOTRFarmhand {

    public enum SlaveType {
        GONDOR(LOTRFaction.GONDOR, "gondor"), NEAR_HARAD(LOTRFaction.NEAR_HARAD, "nearHarad"),
        MORWAITH(LOTRFaction.MORWAITH, "morwaith"), TAURETHRIM(LOTRFaction.TAURETHRIM, "taurethrim");

        public final LOTRFaction faction;
        public final String skinDir;

        SlaveType(LOTRFaction faction, String skinDir) {
            this.faction = faction;
            this.skinDir = skinDir;
        }

        public static @Nullable SlaveType forName(String name) {
            for (SlaveType type : values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }

    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRHaradSlaveEntity.class, EntityDataSerializers.BYTE);

    private @Nullable Item seedsItem;

    public LOTRHaradSlaveEntity(EntityType<? extends LOTRHaradSlaveEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.3, false));
        this.goalSelector.addGoal(2, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(3, new LOTRFarmGoal(this, 1.0, 1.0f));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.HARAD_SLAVE, 12000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, LOTRFoods.HARAD_SLAVE_DRINK, 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.removeAllGoals(g -> true);
        this.targetSelector.addGoal(1, new LOTRNPCHurtByTargetGoal(this));
    }

    public SlaveType getSlaveType() {
        int i = Mth.clamp(this.entityData.get(DATA_TYPE), 0, SlaveType.values().length - 1);
        return SlaveType.values()[i];
    }

    public void setSlaveType(SlaveType type) {
        this.entityData.set(DATA_TYPE, (byte) type.ordinal());
    }

    @Override
    public LOTRFaction getFaction() {
        return getSlaveType().faction;
    }

    public LOTRFaction getHiringFaction() {
        return LOTRFaction.NEAR_HARAD;
    }

    /** canBeFreelyTargetedBy: only by enemies of Near Harad. */
    @Override
    public boolean canBeFreelyTargetedBy(Mob attacker) {
        if (!LOTRNearestAttackableTargetGoal.factionOf(attacker).isBadRelation(getHiringFaction())) {
            return false;
        }
        return super.canBeFreelyTargetedBy(attacker);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    /** setupNPCName: his people first, then a name of theirs. */
    @Override
    public void setupNPCName() {
        float f = this.random.nextFloat();
        if (f < 0.05f) {
            setSlaveType(SlaveType.TAURETHRIM);
        } else if (f < 0.2f) {
            setSlaveType(SlaveType.MORWAITH);
        } else if (f < 0.7f) {
            setSlaveType(SlaveType.NEAR_HARAD);
        } else {
            setSlaveType(SlaveType.GONDOR);
        }
        boolean male = this.familyInfo.isMale();
        this.familyInfo.setName(switch (getSlaveType()) {
            case GONDOR -> LOTRNames.getGondorName(this.random, male);
            case NEAR_HARAD -> this.random.nextBoolean()
                    ? LOTRNames.getHarnennorName(this.random, male) : LOTRNames.getNomadName(this.random, male);
            case MORWAITH -> LOTRNames.getMoredainName(this.random, male);
            case TAURETHRIM -> LOTRNames.getTauredainName(this.random, male);
        });
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public Item getUnhiredSeeds() {
        return this.seedsItem == null ? Items.WHEAT_SEEDS : this.seedsItem;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return this.hiredNPCInfo.getHiringPlayer() == player ? "nearHarad/slave/hired" : "nearHarad/slave/neutral";
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(3);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRToolItems.BRONZE_HOE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("SlaveType", getSlaveType().name());
        if (this.seedsItem != null) {
            output.putString("SeedsID", BuiltInRegistries.ITEM.getKey(this.seedsItem).toString());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("SlaveType").map(SlaveType::forName).ifPresent(this::setSlaveType);
        input.getString("SeedsID").map(Identifier::tryParse).flatMap(BuiltInRegistries.ITEM::getOptional)
                .ifPresent(item -> this.seedsItem = item);
    }
}
