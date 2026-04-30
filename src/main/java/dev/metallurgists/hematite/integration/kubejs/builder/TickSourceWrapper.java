package dev.metallurgists.hematite.integration.kubejs.builder;

import dev.latvian.mods.kubejs.typings.Info;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;

public interface TickSourceWrapper {

    @Info(value = """
		    Returns a TickSource registry object.
		""")
    @Nullable
    static TickSource of(ResourceKey<TickSource> key) {
        return HematiteRegistries.TICK_SOURCE_REGISTRY.get(key);
    }

    static Holder<TickSource> ofHolder(Holder<TickSource> holder) {
        return holder;
    }

    @Info(value = """
		    Returns whether a TickSource is registered or not.
		""")
    static boolean exists(ResourceKey<TickSource> key) {
        return HematiteRegistries.TICK_SOURCE_REGISTRY.containsKey(key);
    }

    @Info(value = """
		    Returns whether an object is a TickSource or not.
		""")
    static boolean isTickSource(Object o) {
        return o instanceof TickSource;
    }
}
