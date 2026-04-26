package dev.metallurgists.hematite.common.weathering.operator;

import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.operator.WeatheringOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface PlacementOperator extends WeatheringOperator {

    default BlockState getWeatheredStateForPlacement(BlockState state, BlockPos pos, Level level){
        if (state != null) {
            WeatheringState weathering = this.shouldWeather(state, pos, level) ? WeatheringState.TRUE : WeatheringState.FALSE;
            if (state.hasProperty(WEATHERABLE))
                state = state.setValue(WEATHERABLE, weathering);
        }
        return state;
    }

    static Set<PlacementOperator> getAll() {
        return HematiteRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof PlacementOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
