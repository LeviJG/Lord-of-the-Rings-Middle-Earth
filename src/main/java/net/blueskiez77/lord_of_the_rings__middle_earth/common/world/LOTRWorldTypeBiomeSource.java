package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

/**
 * The overworld's biome source in a world of the Middle-earth type
 * (LOTRWorldTypeMiddleEarth): the vanilla source it wraps, unchanged. It is
 * there to be saved with the world, since 26.2 keeps a world's dimensions but
 * not the name of the type it was made with, and so marks the world as of
 * that type ({@link LOTRWorldTypeMiddleEarth#isMiddleEarthWorldType}).
 */
public class LOTRWorldTypeBiomeSource extends BiomeSource {

    public static final MapCodec<LOTRWorldTypeBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(source -> source.delegate)
    ).apply(instance, LOTRWorldTypeBiomeSource::new));

    private final BiomeSource delegate;

    public LOTRWorldTypeBiomeSource(BiomeSource delegate) {
        this.delegate = delegate;
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.delegate.possibleBiomes().stream();
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return this.delegate.getNoiseBiome(x, y, z, sampler);
    }

    @Override
    public void addDebugInfo(List<String> info, BlockPos pos, Climate.Sampler sampler) {
        this.delegate.addDebugInfo(info, pos, sampler);
    }
}
