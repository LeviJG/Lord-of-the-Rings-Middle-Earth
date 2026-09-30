package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureSpawning;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructures;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemStructureSpawner: builds the structure its id names ("Build Hobbit
 * Hole") on the block above the face used, facing the way the player does --
 * unless structures are banned on the server or for the player, or another
 * was built less than a second ago. Used up outside creative.
 */
public class LOTRStructureSpawnerItem extends Item {

    public LOTRStructureSpawnerItem(Properties properties) {
        super(properties);
    }

    public static ItemStack of(int structureId) {
        ItemStack stack = new ItemStack(LOTRSpawnItems.STRUCTURE_SPAWNER);
        stack.set(LOTRDataComponents.STRUCTURE_ID, structureId);
        return stack;
    }

    public static int getStructureId(ItemStack stack) {
        Integer id = stack.get(LOTRDataComponents.STRUCTURE_ID);
        return id == null ? 0 : id;
    }

    public static LOTRStructures.@Nullable StructureInfo getStructure(ItemStack stack) {
        return LOTRStructures.get(getStructureId(stack));
    }

    /** getItemStackDisplayName: "Build", then the structure's name. */
    @Override
    public Component getName(ItemStack stack) {
        LOTRStructures.StructureInfo info = getStructure(stack);
        Component name = super.getName(stack);
        if (info == null) {
            return name;
        }
        return Component.empty().append(name).append(" ")
                .append(Component.translatable("lotr.structure." + info.name()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (!(context.getLevel() instanceof ServerLevel level) || player == null) {
            return InteractionResult.SUCCESS;
        }
        if (LOTRLevelData.structuresBanned()) {
            player.sendSystemMessage(Component.translatable("chat.lotr.spawnStructure.disabled"));
            return InteractionResult.FAIL;
        }
        if (LOTRStructureSpawning.isPlayerBanned(player)) {
            player.sendSystemMessage(Component.translatable("chat.lotr.spawnStructure.banned"));
            return InteractionResult.FAIL;
        }
        if (LOTRStructureSpawning.lastStructureSpawnTick > 0) {
            player.sendSystemMessage(Component.translatable("chat.lotr.spawnStructure.wait",
                    LOTRStructureSpawning.lastStructureSpawnTick / 20.0));
            return InteractionResult.FAIL;
        }
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (spawnStructure(player, level, getStructureId(context.getItemInHand()), pos) && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    public static boolean spawnStructure(Player player, ServerLevel level, int id, BlockPos pos) {
        LOTRStructures.StructureInfo info = LOTRStructures.get(id);
        if (info == null) {
            return false;
        }
        boolean generated = info.provider().generateStructure(level, player, pos.getX(), pos.getY(), pos.getZ());
        if (generated) {
            LOTRStructureSpawning.lastStructureSpawnTick = 20;
            level.playSound(null, player.getX(), player.getY(), player.getZ(), LOTRSounds.ITEM_STRUCTURE_SPAWNER,
                    SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return generated;
    }
}
