package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * LOTRTickHandlerServer.onWorldTick's New Year's Day display: on the first of January, now and then
 * a display of a few hundred ticks, fireworks going up in groups within 64 blocks of the players in
 * Middle-earth.
 */
public final class LOTRNewYearFireworks {

    /** fireworkDisplay: the ticks left of the display going on. */
    private static int fireworkDisplay;

    private LOTRNewYearFireworks() {
    }

    public static void init() {
        ServerTickEvents.END_LEVEL_TICK.register(LOTRNewYearFireworks::tick);
    }

    private static void tick(ServerLevel level) {
        if (!(level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator) || level.players().isEmpty()
                || !LOTRMod.isNewYearsDay()) {
            return;
        }
        RandomSource rand = level.getRandom();
        if (fireworkDisplay == 0 && rand.nextInt(4000) == 0) {
            fireworkDisplay = 100 + rand.nextInt(300);
        }
        if (fireworkDisplay <= 0) {
            return;
        }
        --fireworkDisplay;
        if (rand.nextInt(50) != 0) {
            return;
        }
        List<ServerPlayer> players = level.players();
        int launches = 1 + rand.nextInt(7 + players.size() / 2);
        int range = 64;
        for (int l = 0; l < launches; ++l) {
            ServerPlayer player = players.get(rand.nextInt(players.size()));
            int i = Mth.floor(player.getX()) + Mth.randomBetweenInclusive(rand, -range, range);
            int k = Mth.floor(player.getZ()) + Mth.randomBetweenInclusive(rand, -range, range);
            int fireworks = 1 + rand.nextInt(4);
            for (int l1 = 0; l1 < fireworks; ++l1) {
                int groupRange = 8;
                int i1 = i - rand.nextInt(groupRange) + rand.nextInt(groupRange);
                int k1 = k - rand.nextInt(groupRange) + rand.nextInt(groupRange);
                if (!level.hasChunkAt(i1, k1)) {
                    continue;
                }
                int j1 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, i1, k1);
                BlockPos below = new BlockPos(i1, j1 - 1, k1);
                if (!level.getBlockState(below).isRedstoneConductor(level, below)) {
                    continue;
                }
                List<FireworkExplosion> explosions = new ArrayList<>();
                int count = 1 + rand.nextInt(3);
                for (int l2 = 0; l2 < count; ++l2) {
                    boolean twinkle = rand.nextBoolean();
                    boolean trail = rand.nextBoolean();
                    IntList colors = new IntArrayList();
                    int colorCount = 1 + rand.nextInt(3);
                    for (int l3 = 0; l3 < colorCount; ++l3) {
                        colors.add(DyeColor.values()[rand.nextInt(DyeColor.values().length)].getFireworkColor());
                    }
                    int effectType = rand.nextInt(5);
                    if (effectType == 3) {
                        effectType = 0;
                    }
                    explosions.add(new FireworkExplosion(FireworkExplosion.Shape.byId(effectType), colors, IntList.of(), trail, twinkle));
                }
                ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
                stack.set(DataComponents.FIREWORKS, new Fireworks(1 + rand.nextInt(3), explosions));
                level.addFreshEntity(new FireworkRocketEntity(level, i1 + 0.5, j1 + 0.5, k1 + 0.5, stack));
            }
        }
    }
}
