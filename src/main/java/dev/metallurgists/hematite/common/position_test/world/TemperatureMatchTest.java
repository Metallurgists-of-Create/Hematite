package dev.metallurgists.hematite.common.position_test.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.mixin.accessor.BiomeAccessor;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;
import java.util.function.Supplier;

public record TemperatureMatchTest(float max, float min, boolean useLocalPos) implements PositionTest {

    public static final MapCodec<TemperatureMatchTest> CODEC = MapCodec.assumeMapUnsafe(RecordCodecBuilder.<TemperatureMatchTest>create(
            instance -> instance.group(
                            Codec.FLOAT.fieldOf("min").forGetter(g -> g.min),
                            Codec.FLOAT.fieldOf("max").forGetter(g -> g.max),
                            Codec.BOOL.optionalFieldOf("use_local_pos", true).forGetter(TemperatureMatchTest::useLocalPos))
                    .apply( instance, TemperatureMatchTest::new)).comapFlatMap(t -> {
        if (t.max < t.min) {
            return DataResult.error(() -> "Max must be at least min, min_inclusive: " + t.min + ", max_inclusive: " + t.max);
        }
        return DataResult.success(t);
    }, Function.identity()));

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        float temp;
        if (level.dimensionType().ultraWarm()) {
            temp = 3;
        } else if (useLocalPos) {
            temp = ((BiomeAccessor) (Object) biome.get().value()).invokeGetTemperature(pos);
        } else {
            temp = biome.get().value().getBaseTemperature();
        }
        return temp >= min && temp <= max;
    }

    @Override
    public PositionTestType<?> getType() {
        return HematitePositionTestTypes.TEMPERATURE_MATCH.get();
    }
}
