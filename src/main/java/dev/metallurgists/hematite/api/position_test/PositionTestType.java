package dev.metallurgists.hematite.api.position_test;

import com.mojang.serialization.MapCodec;

public record PositionTestType<T extends PositionTest>(MapCodec<T> codec) {
}
