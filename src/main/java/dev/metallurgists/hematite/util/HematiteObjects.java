package dev.metallurgists.hematite.util;

import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public class HematiteObjects {

    public static Holder<TickSource> getTickSource(String id) {
        return HematiteRegistries.TICK_SOURCE_REGISTRY.getOptional(ResourceLocation.parse(id)).map(HematiteRegistries.TICK_SOURCE_REGISTRY::wrapAsHolder).orElse(null);
    }

}
