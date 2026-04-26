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

public record HasAroundTest(PositionTest predicate) implements PositionTest {

    public static final MapCodec<HasAroundTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PositionTest.CODEC.fieldOf("predicate").forGetter(HasAroundTest::predicate)
    ).apply(instance, HasAroundTest::new));

    @Override
    public PositionTestType<HasAroundTest> getType() {
        return HematitePositionTestTypes.HAS_AROUND.get();
    }

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        for(var d : Direction.values()){
            if(!predicate.test(biome, pos.relative(d),  level))return false;
        }
        return true;
    }
}
