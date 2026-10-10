package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityScrapTrader: the oddment collector, a travelling trader of no
 * people in a brown leather hat with a feather, a sprig of Shire heather in
 * his hand and a dagger for trouble. He buys heather and suspicious meat,
 * takes all manner of scrap, comes alone, and once slain does not come back.
 * His anvil is half the price of any other smith's, and he cannot leave a
 * job alone (LOTRAnvilMenu).
 *
 * <p>NOT ported yet: his fading away in Utumno, where he will not trade and
 * cannot be hurt (D15).
 */
public class LOTRScrapTraderEntity extends LOTRManEntity implements LOTRTravellingTrader, LOTRSmith {

    public LOTRScrapTraderEntity(EntityType<? extends LOTRScrapTraderEntity> type, Level level) {
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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.3, true));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new LOTREatGoal(this, LOTRFoods.DUNLENDING, 8000));
        this.goalSelector.addGoal(4, new LOTRDrinkGoal(this, LOTRFoods.DUNLENDING_DRINK, 8000));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 10.0f, 0.1f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.05f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        addTargetTasks(false);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    /** A Dunlending, Rohirric or Gondorian name, one time in three each. */
    @Override
    public void setupNPCName() {
        switch (this.random.nextInt(3)) {
            case 0 -> this.familyInfo.setName(LOTRNames.getDunlendingName(this.random, this.familyInfo.isMale()));
            case 1 -> this.familyInfo.setName(LOTRNames.getRohirricName(this.random, this.familyInfo.isMale()));
            default -> this.familyInfo.setName(LOTRNames.getGondorName(this.random, this.familyInfo.isMale()));
        }
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.SCRAP_TRADER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.SCRAP_TRADER_SELL;
    }

    @Override
    public boolean canTradeWith(Player player) {
        return isFriendly(player);
    }

    /** He travels alone. */
    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return null;
    }

    @Override
    public String getDepartureSpeech() {
        return "misc/scrapTrader/departure";
    }

    /** Brought back by a respawner, he comes back only once. */
    @Override
    public boolean shouldTraderRespawn() {
        return false;
    }

    /** What he grumbles as a player leaves his anvil after his mischief. */
    public String getSmithSpeechBank() {
        return "misc/scrapTrader/smith";
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "misc/scrapTrader/friendly" : "misc/scrapTrader/hostile";
    }

    /** getTotalArmorValue: 5, whatever he wears. */
    @Override
    public int getArmorValue() {
        return 5;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    /** dropFewItems: a bone or two. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, new ItemStack(Items.BONE));
        }
    }

    /**
     * onSpawnWithEgg: an iron or bronze dagger, a sprig of heather, and a
     * dull brown hat with a bright feather (one time in three) or a grey one.
     */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(2) == 0
                ? LOTRCombatItems.IRON_DAGGER : LOTRCombatItems.BRONZE_DAGGER));
        this.npcItemsInv.setIdleItem(new ItemStack(LOTRDecorationBlocks.SHIRE_HEATHER));
        ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
        float h = 0.06111111f;
        float s = Mth.randomBetween(this.random, 0.0f, 0.5f);
        float b = Mth.randomBetween(this.random, 0.0f, 0.5f);
        LOTRLeatherHatItem.setHatColor(hat, Mth.hsvToRgb(h, s, b) & 0xFFFFFF);
        if (this.random.nextInt(3) == 0) {
            h = this.random.nextFloat();
            s = Mth.randomBetween(this.random, 0.7f, 0.9f);
            b = Mth.randomBetween(this.random, 0.8f, 1.0f);
        } else {
            h = 0.0f;
            s = 0.0f;
            b = this.random.nextFloat();
        }
        LOTRLeatherHatItem.setFeatherColor(hat, Mth.hsvToRgb(h, s, b) & 0xFFFFFF);
        setItemSlot(EquipmentSlot.HEAD, hat);
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_SCRAP_TRADER);
    }
}
