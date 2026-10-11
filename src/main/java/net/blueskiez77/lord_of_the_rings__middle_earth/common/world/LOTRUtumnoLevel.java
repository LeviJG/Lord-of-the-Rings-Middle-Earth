package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRUtumnoLevel: Utumno's three levels -- the ice at the top (180 up), the obsidian, and the fire
 * at the bottom -- each a solid mass of its brick cut through by a lattice of corridors, one floor
 * above another, with its own fog, blocks and creatures; and generateTerrain, which builds them.
 */
public enum LOTRUtumnoLevel {
    ICE(13819887, 180, 240, 4, 4), OBSIDIAN(2104109, 92, 180, 6, 5), FIRE(6295040, 0, 92, 8, 7);

    private static final Random LIGHT_RAND = new Random();
    private static final LOTRNoiseGeneratorPerlin NOISE_GEN_XZ = new LOTRNoiseGeneratorPerlin(5628506078940526L, 1);
    private static final LOTRNoiseGeneratorPerlin NOISE_GEN_Y = new LOTRNoiseGeneratorPerlin(1820268708369704034L, 1);
    private static final LOTRNoiseGeneratorPerlin CORRIDOR_NOISE_Y = new LOTRNoiseGeneratorPerlin(89230369345425L, 1);
    private static final LOTRNoiseGeneratorPerlin CORRIDOR_NOISE_X = new LOTRNoiseGeneratorPerlin(824595069307073L, 1);
    private static final LOTRNoiseGeneratorPerlin CORRIDOR_NOISE_Z = new LOTRNoiseGeneratorPerlin(759206035530266067L, 1);

    public final int fogColor;
    public final int baseLevel;
    public final int topLevel;
    public final int corridorWidth;
    public final int corridorWidthStart;
    public final int corridorWidthEnd;
    public final int corridorHeight;
    public final int[] corridorBaseLevels;
    public final LOTRBiomeSpawnList npcSpawnList = new LOTRBiomeSpawnList("UtumnoLevel_" + name());

    LOTRUtumnoLevel(int fog, int base, int top, int cWidth, int cHeight) {
        this.fogColor = fog;
        this.baseLevel = base;
        this.topLevel = top;
        this.corridorWidth = cWidth;
        this.corridorWidthStart = 8 - cWidth / 2;
        this.corridorWidthEnd = this.corridorWidthStart + cWidth;
        this.corridorHeight = cHeight;
        List<Integer> baseLevels = new ArrayList<>();
        int y = base;
        while ((y += cHeight * 2) < top - 5) {
            baseLevels.add(y);
        }
        this.corridorBaseLevels = baseLevels.stream().mapToInt(Integer::intValue).toArray();
    }

    // setupLevels: each level's blocks.
    public BlockState brick() {
        return LOTRLegacyBlocks.mod("utumnoBrick").state(switch (this) {
            case ICE -> 2;
            case OBSIDIAN -> 4;
            case FIRE -> 0;
        });
    }

    public BlockState brickGlow() {
        return LOTRLegacyBlocks.mod("utumnoBrick").state(switch (this) {
            case ICE -> 3;
            case OBSIDIAN -> 5;
            case FIRE -> 1;
        });
    }

    public BlockState tile() {
        return LOTRLegacyBlocks.mod("utumnoBrick").state(switch (this) {
            case ICE -> 6;
            case OBSIDIAN -> 7;
            case FIRE -> 8;
        });
    }

    public BlockState pillar() {
        return LOTRLegacyBlocks.mod("utumnoPillar").state(switch (this) {
            case ICE -> 1;
            case OBSIDIAN -> 2;
            case FIRE -> 0;
        });
    }

    /** brickStairBlock, at the 1.7.10 stair metadata. */
    public BlockState brickStair(int meta) {
        return LOTRLegacyBlocks.mod(switch (this) {
            case ICE -> "stairsUtumnoBrickIce";
            case OBSIDIAN -> "stairsUtumnoBrickObsidian";
            case FIRE -> "stairsUtumnoBrickFire";
        }).state(meta);
    }

    public static LOTRUtumnoLevel forY(int y) {
        for (LOTRUtumnoLevel level : values()) {
            if (y >= level.baseLevel) {
                return level;
            }
        }
        return FIRE;
    }

    public int getHighestCorridorRoof() {
        return this.corridorBaseLevels[this.corridorBaseLevels.length - 1] + this.corridorHeight;
    }

    public int getLowestCorridorFloor() {
        return this.corridorBaseLevels[0] - 1;
    }

    public LOTRBiomeSpawnList getNPCSpawnList() {
        return this.npcSpawnList;
    }

    /** setupLevels' spawn lists: each level's own creatures. */
    public static void setupSpawnLists() {
        ICE.npcSpawnList.newFactionList(100).add(LOTRBiomeSpawnList.entry(net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList.UTUMNO_ICE, 10));
        OBSIDIAN.npcSpawnList.newFactionList(100).add(LOTRBiomeSpawnList.entry(net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList.UTUMNO_OBSIDIAN, 10));
        FIRE.npcSpawnList.newFactionList(100).add(LOTRBiomeSpawnList.entry(net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList.UTUMNO_FIRE, 10));
    }

    /**
     * generateTerrain: the levels' brick, glowing in three chunks of four (by pairs of chunks), with
     * ice and stone, obsidian and black clay, or obsidian between; the tiled corridor floors; bedrock
     * top and bottom; and the corridors themselves -- along every other chunk each way, wavering with
     * the noise -- with now and then a great shaft down a level, or a ravine along a corridor. Written
     * into the chunk as the original wrote it, each column's x and z the other way about.
     */
    public static void generateTerrain(long seed, Random rand, int chunkX, int chunkZ, LOTRChunkTerrain terrain) {
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState packedIce = Blocks.PACKED_ICE.defaultBlockState();
        BlockState blackClay = Blocks.DYED_TERRACOTTA.black().defaultBlockState();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState air = LOTRChunkTerrain.AIR;
        boolean hugeHoleChunk = rand.nextInt(16) == 0;
        boolean hugeRavineChunk = rand.nextInt(16) == 0;
        LIGHT_RAND.setSeed(seed * (chunkX / 2 * 67839703L + chunkZ / 2 * 368093693L));
        boolean chunkHasGlowing = LIGHT_RAND.nextInt(4) > 0;
        for (int i = 0; i < 16; ++i) {
            for (int k = 0; k < 16; ++k) {
                int blockX = chunkX * 16 + i;
                int blockZ = chunkZ * 16 + k;
                double genNoiseXZHere = NOISE_GEN_XZ.getValue(blockX * 0.2, blockZ * 0.2);
                double corridorNoiseYHere = CORRIDOR_NOISE_Y.getValue(blockX * 0.02, blockZ * 0.02) * 0.67
                        + CORRIDOR_NOISE_Y.getValue(blockX * 0.1, blockZ * 0.1) * 0.33;
                double corridorNoiseXHere = CORRIDOR_NOISE_X.getValue(blockX * 0.02, blockZ * 0.02) * 0.67
                        + CORRIDOR_NOISE_X.getValue(blockX * 0.1, blockZ * 0.1) * 0.33;
                double corridorNoiseZHere = CORRIDOR_NOISE_Z.getValue(blockX * 0.02, blockZ * 0.02) * 0.67
                        + CORRIDOR_NOISE_Z.getValue(blockX * 0.1, blockZ * 0.1) * 0.33;
                for (int j = 255; j >= 0; --j) {
                    LOTRUtumnoLevel utumnoLevel = forY(j);
                    int blockIndex = (k * 16 + i) * 256 + j;
                    BlockState[] blocks = terrain.blocks;
                    if (j <= rand.nextInt(5) || j >= 255 - rand.nextInt(3)) {
                        blocks[blockIndex] = bedrock;
                    } else {
                        double genNoiseYHere = NOISE_GEN_Y.getValue(j * 0.4, 0.0);
                        double genNoise = (genNoiseXZHere + genNoiseYHere * 0.5) / 1.5;
                        if (genNoise > -0.2) {
                            blocks[blockIndex] = utumnoLevel.brick();
                            if (chunkHasGlowing) {
                                boolean glowing = false;
                                if (utumnoLevel == ICE && rand.nextInt(16) == 0 || utumnoLevel == OBSIDIAN && rand.nextInt(12) == 0) {
                                    glowing = true;
                                } else if (utumnoLevel == FIRE && rand.nextInt(8) == 0) {
                                    glowing = true;
                                }
                                if (glowing) {
                                    blocks[blockIndex] = utumnoLevel.brickGlow();
                                }
                            }
                        } else if (utumnoLevel == ICE) {
                            blocks[blockIndex] = genNoise < -0.5 ? stone : packedIce;
                        } else if (utumnoLevel == OBSIDIAN) {
                            blocks[blockIndex] = genNoise < -0.5 ? blackClay : obsidian;
                        } else {
                            blocks[blockIndex] = obsidian;
                        }
                        int levelFuzz = 2;
                        if (j <= utumnoLevel.getLowestCorridorFloor() - levelFuzz || j >= utumnoLevel.getHighestCorridorRoof() + levelFuzz) {
                            blocks[blockIndex] = utumnoLevel.brick();
                        }
                        if (genNoise < 0.5) {
                            for (int corridorBase : utumnoLevel.corridorBaseLevels) {
                                if (j == corridorBase - 1) {
                                    blocks[blockIndex] = utumnoLevel.tile();
                                }
                            }
                        }
                    }
                    int actingY = j;
                    actingY += (int) Math.round(corridorNoiseYHere * 1.15);
                    actingY = Mth.clamp(actingY, 0, 255);
                    int actingXInChunk = blockX + (int) Math.round(corridorNoiseXHere * 1.7) & 0xF;
                    int actingZInChunk = blockZ + (int) Math.round(corridorNoiseZHere * 1.7) & 0xF;
                    int[] bases = utumnoLevel.corridorBaseLevels;
                    boolean carveHugeHole = hugeHoleChunk && actingY >= bases[0] && actingY < bases[bases.length - 1];
                    boolean carveHugeRavine = hugeRavineChunk && actingY >= bases[0] && actingY < bases[bases.length - 1];
                    boolean carveCorridor = false;
                    for (int corridorBase : bases) {
                        carveCorridor = actingY >= corridorBase && actingY < corridorBase + utumnoLevel.corridorHeight;
                        if (carveCorridor) {
                            break;
                        }
                    }
                    if (carveHugeHole && chunkX % 2 == 0 && chunkZ % 2 == 0) {
                        if (i >= utumnoLevel.corridorWidthStart + 1 && i <= utumnoLevel.corridorWidthEnd - 1
                                && k >= utumnoLevel.corridorWidthStart + 1 && k <= utumnoLevel.corridorWidthEnd - 1) {
                            blocks[blockIndex] = air;
                        } else if (i >= utumnoLevel.corridorWidthStart && i <= utumnoLevel.corridorWidthEnd
                                && k >= utumnoLevel.corridorWidthStart && k <= utumnoLevel.corridorWidthEnd) {
                            blocks[blockIndex] = utumnoLevel.brickGlow();
                        }
                    }
                    if (chunkX % 2 == 0) {
                        if (carveCorridor && actingZInChunk >= utumnoLevel.corridorWidthStart && actingZInChunk <= utumnoLevel.corridorWidthEnd) {
                            blocks[blockIndex] = air;
                        }
                        if (carveHugeRavine && actingXInChunk >= utumnoLevel.corridorWidthStart + 1 && actingXInChunk <= utumnoLevel.corridorWidthEnd - 1) {
                            blocks[blockIndex] = air;
                        }
                    }
                    if (chunkZ % 2 != 0) {
                        continue;
                    }
                    if (carveCorridor && actingXInChunk >= utumnoLevel.corridorWidthStart && actingXInChunk <= utumnoLevel.corridorWidthEnd) {
                        blocks[blockIndex] = air;
                    }
                    if (carveHugeRavine && actingZInChunk >= utumnoLevel.corridorWidthStart + 1 && actingZInChunk <= utumnoLevel.corridorWidthEnd - 1) {
                        blocks[blockIndex] = air;
                    }
                }
            }
        }
    }
}
