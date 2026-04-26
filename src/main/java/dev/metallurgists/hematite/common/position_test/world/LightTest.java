package dev.metallurgists.hematite.common.position_test.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import dev.metallurgists.hematite.util.Operator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public record LightTest(int targetLight, Operator operator, Optional<Vec3i> offset) implements PositionTest {

    public static final MapCodec<LightTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(0,15).fieldOf("light").forGetter(LightTest::targetLight),
            Operator.CODEC.fieldOf("operator").forGetter(LightTest::operator),
            Vec3i.offsetCodec(16).optionalFieldOf("offset").forGetter(LightTest::offset)
    ).apply(instance, LightTest::new));

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        //if offset only look there if not look everywhere to see if at least one matches
        return offset.map(off -> operator.apply(level.getLightEmission(pos.offset(off)), targetLight)).orElseGet(
                () -> Arrays.stream(Direction.values()).anyMatch(d ->
                        operator.apply(level.getLightEmission(pos), targetLight)));
    }

    @Override
    public PositionTestType<?> getType() {
        return HematitePositionTestTypes.LIGHT.get();
    }
}
