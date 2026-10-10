package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import java.util.Collection;

import net.minecraft.util.RandomSource;

/** 1.7.10's WeightedRandom.getRandomItem: one of the items, by weight. */
public final class LOTRWeightedRandom {

    public interface Item {
        int itemWeight();
    }

    private LOTRWeightedRandom() {
    }

    public static <T extends Item> T getRandomItem(RandomSource random, Collection<T> items) {
        int total = 0;
        for (T item : items) {
            total += item.itemWeight();
        }
        int j = random.nextInt(total);
        for (T item : items) {
            j -= item.itemWeight();
            if (j < 0) {
                return item;
            }
        }
        throw new IllegalStateException("weighted list ran out");
    }
}
