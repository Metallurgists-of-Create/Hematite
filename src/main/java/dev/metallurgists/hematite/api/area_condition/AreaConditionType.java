package dev.metallurgists.hematite.api.area_condition;

import com.mojang.serialization.MapCodec;

public record AreaConditionType<T extends AreaCondition>(MapCodec<T> codec) {
}
