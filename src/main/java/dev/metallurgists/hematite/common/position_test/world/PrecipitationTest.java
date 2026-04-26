package dev.metallurgists.hematite.common.position_test.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public record PrecipitationTest(Biome.Precipitation precipitation) implements PositionTest {

    public static final MapCodec<PrecipitationTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(PrecipitationTest::precipitation)
    ).apply(instance, PrecipitationTest::new));

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        for (var d : Direction.values()) {
            if (d != Direction.DOWN) {
                switch (precipitation) {
                    case NONE -> {
                        if (level.isRainingAt(pos.relative(d))){
                            return false;
                        }
                    }
                    case SNOW -> {
                        if (level.isRainingAt(pos.relative(d)) && biome.get().value().coldEnoughToSnow(pos.relative(d))) {
                            return true;
                        }
                    }
                    case RAIN -> {
                        if (level.isRainingAt(pos.relative(d)) && biome.get().value().warmEnoughToRain(pos.relative(d))) {
                            return true;
                        }
                    }
                }
            }
        }
        return precipitation == Biome.Precipitation.NONE;
    }

    @Override
    public PositionTestType<?> getType() {
        return HematitePositionTestTypes.PRECIPITATION.get();
    }
}
