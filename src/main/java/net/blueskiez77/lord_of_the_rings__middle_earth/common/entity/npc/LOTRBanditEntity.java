package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditFleeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditStealGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryNPC;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
 * LOTREntityBandit: a cutthroat of no people, quick and keen-eyed, with a
 * dagger always in hand and one time in three a black hat with a white
 * feather. He creeps up on a player and robs them of up to three things,
 * then runs; a player with nothing he could take he simply attacks. Every
 * NPC leaves him alone but a hired unit, which hunts him. Slain, he gives up
 * his loot, bones, coins in plenty, and one time in five a skull cup.
 *
 * <p>NOT ported yet: his spawning in the wilds (LOTREventSpawner, with the
 * biomes' bandit kinds, D10/D12), and the killThievingBandit achievement (D7).
 */
public class LOTRBanditEntity extends LOTRManEntity implements LOTRBandit {

    public static final int MAX_THEFTS = 3;
    private static final Item[] WEAPONS = {LOTRCombatItems.BRONZE_DAGGER, LOTRCombatItems.IRON_DAGGER};

    private final LOTRInventoryNPC banditInventory = LOTRBandit.createInv(this, MAX_THEFTS);

    public LOTRBanditEntity(EntityType<? extends LOTRBanditEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new LOTRBanditStealGoal(this, 1.2));
        this.goalSelector.addGoal(3, new LOTRBanditFleeGoal(this, 1.0));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.1f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.05f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        // addTargetTasks(true, LOTREntityAINearestAttackableTargetBandit.class): both of its goals take players only.
        addTargetTasks(true, LOTRBanditTargetGoal::new, LOTRBanditTargetGoal::new);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HOSTILE;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return "misc/bandit/hostile";
    }

    @Override
    public boolean canTargetPlayerForTheft(Player player) {
        return true;
    }

    @Override
    public LOTRNPCEntity getBanditAsNPC() {
        return this;
    }

    @Override
    public LOTRInventoryNPC getBanditInventory() {
        return this.banditInventory;
    }

    @Override
    public int getMaxThefts() {
        return MAX_THEFTS;
    }

    @Override
    public Component getTheftChatMsg(Player player) {
        return Component.translatable("chat.lotr.banditSteal");
    }

    @Override
    public String getTheftSpeechBank(Player player) {
        return getSpeechBank(player);
    }

    /** getTotalArmorValue: 10, whatever he wears. */
    @Override
    public int getArmorValue() {
        return 10;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    /** onSpawnWithEgg: a dagger in hand, and one time in three a black hat with a white feather. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon());
        if (this.random.nextInt(3) == 0) {
            ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
            LOTRLeatherHatItem.setHatColor(hat, 0);
            LOTRLeatherHatItem.setFeatherColor(hat, LOTRLeatherHatItem.FEATHER_WHITE);
            setItemSlot(EquipmentSlot.HEAD, hat);
        }
        return data;
    }

    /** dropFewItems: bones, 10 to 19 coins and more with looting, and one time in five a skull cup. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, new ItemStack(Items.BONE));
        }
        int coins = 10 + this.random.nextInt(10) + this.random.nextInt((looting + 1) * 10);
        for (int l = 0; l < coins; ++l) {
            spawnAtLocation(level, new ItemStack(LOTRMiscItems.SILVER_COIN));
        }
        if (this.random.nextInt(5) == 0) {
            spawnAtLocation(level, LOTRVessel.SKULL.emptyStack());
        }
    }

    /** onDeath: what he stole falls with him. */
    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide()) {
            this.banditInventory.dropAllItems();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.banditInventory.save(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.banditInventory.load(input);
    }
}
