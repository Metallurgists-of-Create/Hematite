package dev.metallurgists.hematite;

import com.mojang.serialization.Lifecycle;
import dev.metallurgists.hematite.api.area_condition.AreaConditionType;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGeneratorType;
import dev.metallurgists.hematite.api.weathering.operator.WeatheringOperator;
import dev.metallurgists.hematite.api.weathering.spreader.Spreader;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Hematite.ID)
public class HematiteRegistries {

    public static final ResourceKey<Registry<Spreader>> SPREADER = HematiteRegistries.createRegistryKey("spreaders");
    public static final ResourceKey<Registry<WeatheringOperator>> WEATHERING_OPERATOR = HematiteRegistries.createRegistryKey("weathering_operators");
    public static final ResourceKey<Registry<TickSource>> TICK_SOURCE = HematiteRegistries.createRegistryKey("tick_sources");
    public static final ResourceKey<Registry<BlockGrowth.Type<?>>> BLOCK_GROWTH_TYPE = HematiteRegistries.createRegistryKey("block_growth_types");
    public static final ResourceKey<Registry<FluidGeneratorType<?>>> FLUID_GENERATOR_TYPE = HematiteRegistries.createRegistryKey("fluid_generator_types");
    public static final ResourceKey<Registry<AreaConditionType<?>>> AREA_CONDITION_TYPE = HematiteRegistries.createRegistryKey("area_condition_types");
    public static final ResourceKey<Registry<PositionTestType<?>>> POSITION_TEST_TYPE = HematiteRegistries.createRegistryKey("position_test_types");


    public static final Registry<Spreader> SPREADER_REGISTRY = HematiteRegistries.makeSyncedRegistry(SPREADER);
    public static final Registry<WeatheringOperator> WEATHERING_OPERATOR_REGISTRY = HematiteRegistries.makeSyncedRegistry(WEATHERING_OPERATOR);
    public static final Registry<TickSource> TICK_SOURCE_REGISTRY = HematiteRegistries.registerSimpleWithIntrusiveHolders(TICK_SOURCE);
    public static final Registry<BlockGrowth.Type<?>> BLOCK_GROWTH_TYPE_REGISTRY = HematiteRegistries.makeSyncedRegistry(BLOCK_GROWTH_TYPE);
    public static final Registry<FluidGeneratorType<?>> FLUID_GENERATOR_TYPE_REGISTRY = HematiteRegistries.makeSyncedRegistry(FLUID_GENERATOR_TYPE);
    public static final Registry<AreaConditionType<?>> AREA_CONDITION_TYPE_REGISTRY = HematiteRegistries.makeSyncedRegistry(AREA_CONDITION_TYPE);
    public static final Registry<PositionTestType<?>> POSITION_TEST_TYPE_REGISTRY = HematiteRegistries.makeSyncedRegistry(POSITION_TEST_TYPE);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Hematite.asResource(name));
    }

    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
    private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), true);
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(SPREADER_REGISTRY);
        event.register(WEATHERING_OPERATOR_REGISTRY);
        event.register(TICK_SOURCE_REGISTRY);
        event.register(BLOCK_GROWTH_TYPE_REGISTRY);
        event.register(FLUID_GENERATOR_TYPE_REGISTRY);
        event.register(AREA_CONDITION_TYPE_REGISTRY);
        event.register(POSITION_TEST_TYPE_REGISTRY);
    }

    public static void staticInit() {

    }
}
