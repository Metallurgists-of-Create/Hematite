package dev.metallurgists.hematite.registry;

import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematiteTickSources {
    public static final DeferredRegister<TickSource> SOURCES = ModUtils.createRegister(HematiteRegistries.TICK_SOURCE);

    public static final DeferredHolder<TickSource, TickSource> BLOCK_TICK = makeSource("block_tick");

    public static final DeferredHolder<TickSource, TickSource> CLEAR_SKY = makeSource("clear_sky");
    public static final DeferredHolder<TickSource, TickSource> SNOW = makeSource("snow");
    public static final DeferredHolder<TickSource, TickSource> RAIN = makeSource("rain");
    public static final DeferredHolder<TickSource, TickSource> LIGHTNING = makeSource("lightning");

    public static DeferredHolder<TickSource, TickSource> makeSource(String name) {
        return SOURCES.register(name, TickSource::new);
    }

    public static void staticInit() {
        Hematite.logRegistry(HematiteRegistries.TICK_SOURCE);
    }
}
