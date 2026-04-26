package dev.metallurgists.hematite.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.common.weathering.block_growths.ConfigurableBlockGrowth;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.common.weathering.block_growths.NoOpBlockGrowth;
import dev.metallurgists.hematite.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematiteBlockGrowthTypes {
    public static final DeferredRegister<BlockGrowth.Type<?>> GROWTH_TYPES = ModUtils.createRegister(HematiteRegistries.BLOCK_GROWTH_TYPE);

    public static final DeferredHolder<BlockGrowth.Type<?>, BlockGrowth.Type<NoOpBlockGrowth>> NO_OP = register("no_op", NoOpBlockGrowth.CODEC);
    public static final DeferredHolder<BlockGrowth.Type<?>, BlockGrowth.Type<ConfigurableBlockGrowth>> CONFIGURABLE = register("configurable", ConfigurableBlockGrowth.CODEC);

    public static <T extends BlockGrowth> DeferredHolder<BlockGrowth.Type<?>, BlockGrowth.Type<T>> register(String name, MapCodec<T> codec) {
        return GROWTH_TYPES.register(name, () -> new BlockGrowth.Type<>(codec));
    }

    public static void staticInit() {
        Hematite.logRegistry(HematiteRegistries.BLOCK_GROWTH_TYPE);
    }
}
