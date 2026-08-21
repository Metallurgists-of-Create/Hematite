package dev.metallurgists.hematite.api.weathering.fluid_generators.data;

import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import net.minecraft.resources.ResourceLocation;

public interface IGeneratorOutputExtension {
    private GeneratorOutput self() {
        return (GeneratorOutput) this;
    }
    void accept(ResourceLocation id, FluidGenerator generator);
}
