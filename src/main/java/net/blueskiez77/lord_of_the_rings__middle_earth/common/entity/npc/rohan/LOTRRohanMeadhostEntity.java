package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRohanMeadhost: keeps a mead hall ("%s's Hall"), a mug of mead in
 * hand even in a fight. NPCs who drink near a friendly meadhost may get
 * drunk. Slain, it leaves a hall's odds and ends besides a Rohirrim's.
 *
 * <p>NOT ported yet: the buyRohanMead achievement.
 */
public class LOTRRohanMeadhostEntity extends LOTRRohanManEntity implements LOTRBartender {

    public LOTRRohanMeadhostEntity(EntityType<? extends LOTRRohanMeadhostEntity> type, Level level) {
        super(type, level);
        this.npcLocationName = "entity.lotr.rohan_meadhost.locationName";
    }

    @Override
    protected Goal createRohanAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.3, false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.ROHAN_MEADHOST_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.ROHAN_MEADHOST_SELL;
    }

    @Override
    public boolean canTradeWith(Player player) {
        return isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "rohan/meadhost/friendly" : "rohan/meadhost/hostile";
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int count = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int k = 0; k < count; ++k) {
            ItemStack drop = switch (this.random.nextInt(11)) {
                case 3 -> new ItemStack(Items.GOLD_NUGGET, 2 + this.random.nextInt(3));
                case 4 -> new ItemStack(Items.WHEAT, 1 + this.random.nextInt(4));
                case 5 -> new ItemStack(Items.SUGAR, 1 + this.random.nextInt(3));
                case 6 -> new ItemStack(Items.PAPER, 1 + this.random.nextInt(2));
                case 7, 8 -> new ItemStack(LOTRFoodItems.MUG);
                case 9, 10 -> LOTRDrinkItem.stack(LOTRFoodItems.MEAD, 1 + this.random.nextInt(3));
                default -> new ItemStack(LOTRFoods.ROHAN.getRandomFood(this.random).getItem());
            };
            spawnAtLocation(level, drop, 0.0f);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRFoodItems.MEAD));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }
}
