package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import java.util.ArrayList;
import java.util.Collection;

import org.jspecify.annotations.Nullable;

/** LOTRBiomeVariantList: a biome's variants by weight, picked by a number from 0 to 1. */
public class LOTRBiomeVariantList {

    private float totalWeight;
    private final Collection<VariantBucket> variantList = new ArrayList<>();

    public void add(LOTRBiomeVariant v, float f) {
        this.variantList.add(new VariantBucket(v, this.totalWeight, this.totalWeight + f));
        this.totalWeight += f;
    }

    public void clear() {
        this.totalWeight = 0.0f;
        this.variantList.clear();
    }

    public @Nullable LOTRBiomeVariant get(float index) {
        if (index < 0.0f) {
            index = 0.0f;
        }
        if (index >= 1.0f) {
            index = 0.9999f;
        }
        float f = index * this.totalWeight;
        for (VariantBucket bucket : this.variantList) {
            if (f >= bucket.min && f < bucket.max) {
                return bucket.variant;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.totalWeight == 0.0f;
    }

    private record VariantBucket(LOTRBiomeVariant variant, float min, float max) {
    }
}
