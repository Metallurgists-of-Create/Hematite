package dev.metallurgists.hematite.common.position_test.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import dev.metallurgists.hematite.util.DummyWorldGenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

import java.util.function.Supplier;

public record HeightTest(HeightProvider height, int tolerance) implements PositionTest {

    public static final MapCodec<HeightTest> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(
            HeightProvider.CODEC.fieldOf("distribution").forGetter(HeightTest::height),
            Codec.INT.fieldOf("tolerance").forGetter(HeightTest::tolerance)
    ).apply(i, HeightTest::new));

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        int sampledY = this.height.sample(level.random, new DummyWorldGenerationContext(level));

        return (Math.abs(sampledY - pos.getY()) <= tolerance);
    }

    @Override
    public PositionTestType<?> getType() {
        return HematitePositionTestTypes.HEIGHT.get();
    }
}
