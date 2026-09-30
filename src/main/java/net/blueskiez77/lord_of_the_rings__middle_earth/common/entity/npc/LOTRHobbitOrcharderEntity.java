package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHobbitOrcharder: a hobbit with an iron, stone or bronze axe who
 * stands and fights, in a green hat.
 *
 * <p>Unlike most traders it does not respawn.
 *
 * <p>NOT ported yet: the buyOrcharderFood achievement.
 */
public class LOTRHobbitOrcharderEntity extends LOTRHobbitEntity implements LOTRTradeable {

    public LOTRHobbitOrcharderEntity(EntityType<? extends LOTRHobbitOrcharderEntity> type, Level level) {
        super(type, level);
        this.isNPCPersistent = false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 1.2, false));
        addTargetTasks(false);
    }

    @Override
    protected boolean panics() {
        return false;
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.HOBBIT_ORCHARDER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.HOBBIT_ORCHARDER_SELL;
    }

    /** An orcharder brought back by a respawner comes back only once. */
    @Override
    public boolean shouldTraderRespawn() {
        return false;
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
        return isFriendly(player) ? "hobbit/orcharder/friendly" : "hobbit/hobbit/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.HEAD, LOTRHobbitBounderEntity.hat(4818735, -1));
        Item axe = switch (this.random.nextInt(3)) {
            case 0 -> Items.IRON_AXE;
            case 1 -> Items.STONE_AXE;
            default -> LOTRToolItems.BRONZE_AXE;
        };
        this.npcItemsInv.setMeleeWeapon(new ItemStack(axe));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }
}
