package dev.metallurgists.hematite.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGeneratorType;
import dev.metallurgists.hematite.common.weathering.fluid_generators.OtherFluidGenerator;
import dev.metallurgists.hematite.common.weathering.fluid_generators.SelfFluidGenerator;
import dev.metallurgists.hematite.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematiteFluidGeneratorTypes {
    public static final DeferredRegister<FluidGeneratorType<?>> GENERATOR_TYPES = ModUtils.createRegister(HematiteRegistries.FLUID_GENERATOR_TYPE);

    public static final DeferredHolder<FluidGeneratorType<?>, FluidGeneratorType<OtherFluidGenerator>> TARGET_OTHER = register("target_other", OtherFluidGenerator.CODEC);
    public static final DeferredHolder<FluidGeneratorType<?>, FluidGeneratorType<SelfFluidGenerator>> TARGET_SELF = register("target_self", SelfFluidGenerator.CODEC);

    public static <T extends FluidGenerator> DeferredHolder<FluidGeneratorType<?>, FluidGeneratorType<T>> register(String name, MapCodec<T> codec) {
        return GENERATOR_TYPES.register(name, () -> new FluidGeneratorType<>(codec));
    }

    public static void staticInit() {
        Hematite.logRegistry(HematiteRegistries.FLUID_GENERATOR_TYPE);
    }
}
