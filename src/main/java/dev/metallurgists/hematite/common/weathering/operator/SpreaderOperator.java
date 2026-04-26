package dev.metallurgists.hematite.common.weathering.operator;

import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.operator.WeatheringOperator;
import dev.metallurgists.hematite.api.weathering.spreader.Spreader;
import net.minecraft.core.Holder;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SpreaderOperator extends WeatheringOperator {

    Optional<Spreader> getPatchSpreader(Holder<? extends Spreader> spreader);

    static Set<SpreaderOperator> getAll() {
        return HematiteRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof SpreaderOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
