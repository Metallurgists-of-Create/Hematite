package dev.metallurgists.hematite.api.weathering.fluid_generators;

import com.mojang.serialization.MapCodec;

public record FluidGeneratorType<T extends FluidGenerator>(MapCodec<T> codec) {
}
