package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import com.mojang.logging.LogUtils;

/**
 * LOTRGenLayerWorld: the map, {@code assets/lotr/map/map.png}, one pixel a biome by its colour,
 * the world's origin at pixel (810, 730). Beyond the map is ocean. One pixel is 128 blocks once
 * the layers above have zoomed it (SCALE).
 */
public class LOTRGenLayerWorld extends LOTRGenLayer {

    public static final int SCALE_POWER = 7;
    public static final int SCALE = 1 << SCALE_POWER;
    public static final int ORIGIN_X = 810;
    public static final int ORIGIN_Z = 730;
    private static byte[] biomeImageData;
    private static int imageWidth;
    private static int imageHeight;

    public LOTRGenLayerWorld() {
        super(0L);
        loadBiomeImage();
    }

    private static synchronized void loadBiomeImage() {
        if (biomeImageData != null) {
            return;
        }
        try (InputStream in = LOTRGenLayerWorld.class.getResourceAsStream("/assets/lotr/map/map.png")) {
            if (in == null) {
                throw new IOException("missing assets/lotr/map/map.png");
            }
            BufferedImage image = ImageIO.read(in);
            int width = image.getWidth();
            int height = image.getHeight();
            int[] colors = image.getRGB(0, 0, width, height, null, 0, width);
            byte[] data = new byte[colors.length];
            for (int i = 0; i < colors.length; ++i) {
                Integer biomeID = LOTRDimension.MIDDLE_EARTH.colorsToBiomeIDs.get(colors[i]);
                if (biomeID == null) {
                    LogUtils.getLogger().error("Found unknown biome on map {}", Integer.toHexString(colors[i]));
                    biomeID = LOTRBiomes.OCEAN.biomeID;
                }
                data[i] = (byte) (int) biomeID;
            }
            imageWidth = width;
            imageHeight = height;
            biomeImageData = data;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load the LOTR biome map image", e);
        }
    }

    public static int getBiomeImageID(int x, int z) {
        return biomeImageData[z * imageWidth + x] & 0xFF;
    }

    public static boolean loadedBiomeImage() {
        return biomeImageData != null;
    }

    public static int getImageWidth() {
        return imageWidth;
    }

    public static int getImageHeight() {
        return imageHeight;
    }

    public static LOTRBiome getBiomeOrOcean(int mapX, int mapZ) {
        int biomeID = mapX >= 0 && mapX < imageWidth && mapZ >= 0 && mapZ < imageHeight ? getBiomeImageID(mapX, mapZ) : LOTRBiomes.OCEAN.biomeID;
        return LOTRDimension.MIDDLE_EARTH.biomeList[biomeID];
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize) {
        int[] ints = new int[xSize * zSize];
        for (int k1 = 0; k1 < zSize; ++k1) {
            for (int i1 = 0; i1 < xSize; ++i1) {
                int i2 = i + i1 + ORIGIN_X;
                int k2 = k + k1 + ORIGIN_Z;
                ints[i1 + k1 * xSize] = i2 < 0 || i2 >= imageWidth || k2 < 0 || k2 >= imageHeight
                        ? LOTRBiomes.OCEAN.biomeID : getBiomeImageID(i2, k2);
            }
        }
        return ints;
    }
}
