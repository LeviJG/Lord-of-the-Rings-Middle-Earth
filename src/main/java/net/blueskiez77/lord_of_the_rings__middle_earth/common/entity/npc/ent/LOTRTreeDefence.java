package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;

/**
 * LOTREventHandler.onBlockBreak, the trees' part: wood broken by a player not in creative, rotten
 * wood aside, sets every Ent and Huorn within 16 blocks on them (but those the player has hired),
 * the first Ent among them telling them so; and in Fangorn it costs the player a point of Fangorn
 * alignment, once for the block.
 */
public final class LOTRTreeDefence {

    /** LOTRBiomeGenFangorn, by the id the Middle-earth biomes will have (D10); until then no land is Fangorn's. */
    private static final ResourceKey<Biome> FANGORN =
            ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "fangorn"));

    private LOTRTreeDefence() {
    }

    public static void init() {
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
                if (!penalty && level.getBiome(pos).is(FANGORN)) {
                    LOTRPlayerAlignments.addAlignment(player, LOTRAlignmentValues.FANGORN_TREE_PENALTY,
                            LOTRFaction.FANGORN, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    penalty = true;
                }
            }
        });
    }
}
