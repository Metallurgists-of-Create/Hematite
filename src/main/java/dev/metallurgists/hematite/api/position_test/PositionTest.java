package dev.metallurgists.hematite.api.position_test;

import com.mojang.serialization.Codec;
import dev.metallurgists.hematite.HematiteRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public interface PositionTest {
    Codec<PositionTest> CODEC = Codec.lazyInitialized(HematiteRegistries.POSITION_TEST_TYPE_REGISTRY::byNameCodec).dispatch("type", PositionTest::getType, PositionTestType::codec);

    boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level);

    PositionTestType<?> getType();

    PositionTest EMPTY = new PositionTest() {

        @Override
        public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
            return false;
        }

        @Override
        public PositionTestType<?> getType() {
            return null;
        }
    };
}
