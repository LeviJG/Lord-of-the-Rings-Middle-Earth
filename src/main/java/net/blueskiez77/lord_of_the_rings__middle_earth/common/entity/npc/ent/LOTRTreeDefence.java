package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRGaladhrimWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFangornBiome;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.AABB;

/**
 * LOTREventHandler.onBlockBreak, the trees' part: wood broken by a player not in creative, rotten
 * wood aside, sets every Ent and Huorn within 16 blocks on them (but those the player has hired),
 * the first Ent among them telling them so; and in Fangorn it costs the player a point of Fangorn
 * alignment, once for the block. The Galadhrim's defence of the mallorn is called from here too.
 */
public final class LOTRTreeDefence {

    private LOTRTreeDefence() {
    }

    public static void init() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (level instanceof ServerLevel serverLevel) {
                LOTRGaladhrimWarriorEntity.defendTrees(serverLevel, player, pos, state);
            }
        });
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (level.isClientSide() || player.isCreative() || !state.is(BlockTags.LOGS)
                    || state.is(LOTRBuildingBlocks.ROTTEN_LOG)) {
                return;
            }
            List<LOTRTreeEntity> trees = level.getEntitiesOfClass(LOTRTreeEntity.class, new AABB(pos).inflate(16.0));
            boolean sentMessage = false;
            boolean penalty = false;
            for (LOTRTreeEntity tree : trees) {
                if (tree.hiredNPCInfo.isActive && tree.hiredNPCInfo.getHiringPlayer() == player) {
                    continue;
                }
                tree.setTarget(player);
                if (tree instanceof LOTREntEntity && !sentMessage) {
                    tree.sendSpeechBank(player, "ent/ent/defendTrees");
                    sentMessage = true;
                }
                if (!penalty && LOTRBiomes.of(level.getBiome(pos)) instanceof LOTRFangornBiome) {
                    LOTRPlayerAlignments.addAlignment(player, LOTRAlignmentValues.FANGORN_TREE_PENALTY,
                            LOTRFaction.FANGORN, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    penalty = true;
                }
            }
        });
    }
}
