package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRCraftingMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

// A faction table. Two gates, both from LOTRBlockCraftingTable: alignment with the table's faction must be at least 1.0 before the GUI opens, and the menu it opens only ever searches that table's recipe type.
public class LOTRCraftingTableBlock extends Block {

    public static final MapCodec<LOTRCraftingTableBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            LOTRCraftingTable.CODEC.fieldOf("table").forGetter(b -> b.table),
            propertiesCodec()).apply(i, LOTRCraftingTableBlock::new));

    public static final float REQUIRED_ALIGNMENT = 1.0f;

    private final LOTRCraftingTable table;

    public LOTRCraftingTableBlock(LOTRCraftingTable table, BlockBehaviour.Properties properties) {
        super(properties);
        this.table = table;
    }

    public LOTRCraftingTable table() {
        return table;
    }

    @Override
    public MapCodec<? extends LOTRCraftingTableBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel) {
            if (LOTRPlayerAlignments.getAlignment(player, table.faction()) < REQUIRED_ALIGNMENT) {
                // Eight puffs of smoke on the table top. The original spawned
                // them client-side, where the client knew the alignment.
                for (int l = 0; l < 8; ++l) {
                    serverLevel.sendParticles(ParticleTypes.SMOKE,
                            pos.getX() + level.getRandom().nextFloat(), pos.getY() + 1.0,
                            pos.getZ() + level.getRandom().nextFloat(), 1, 0.0, 0.0, 0.0, 0.0);
                }
                LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, REQUIRED_ALIGNMENT, table.faction());
                return InteractionResult.SUCCESS;
            }
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new LOTRCraftingMenu(
                        containerId, inventory, ContainerLevelAccess.create(level, pos), table),
                Component.translatable(table.translationKey()));
    }
}