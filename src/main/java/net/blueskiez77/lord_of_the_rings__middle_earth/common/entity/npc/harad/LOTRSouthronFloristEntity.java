package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;

import org.jspecify.annotations.Nullable;

/** LOTREntitySouthronFlorist: a bazaar florist with one of the four Harad flowers in hand. */
public class LOTRSouthronFloristEntity extends LOTRSouthronTraderEntity {

    public LOTRSouthronFloristEntity(EntityType<? extends LOTRSouthronFloristEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.HARAD_FLORIST_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.HARAD_FLORIST_SELL;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Block[] flowers = {LOTRDecorationBlocks.RED_HARAD_FLOWER, LOTRDecorationBlocks.YELLOW_HARAD_FLOWER,
                LOTRDecorationBlocks.HARAD_FLOWER_DAISY, LOTRDecorationBlocks.PINK_HARAD_FLOWER};
        this.npcItemsInv.setIdleItem(new ItemStack(flowers[this.random.nextInt(flowers.length)]));
        return data;
    }
}
