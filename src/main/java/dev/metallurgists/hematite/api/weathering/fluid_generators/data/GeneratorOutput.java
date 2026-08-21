package dev.metallurgists.hematite.api.weathering.fluid_generators.data;

import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import net.minecraft.resources.ResourceLocation;

public interface GeneratorOutput extends IGeneratorOutputExtension {
    default void accept(ResourceLocation location, FluidGenerator generator) {
        accept(location, generator);
    }
}
