package dev.metallurgists.hematite.common.weathering.operator;

import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.operator.WeatheringOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface AnimatedOperator extends WeatheringOperator {

    void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random);

    static Set<AnimatedOperator> getAll() {
        return HematiteRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof AnimatedOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
