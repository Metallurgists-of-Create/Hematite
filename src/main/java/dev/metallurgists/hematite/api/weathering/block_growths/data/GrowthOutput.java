package dev.metallurgists.hematite.api.weathering.block_growths.data;

import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import net.minecraft.resources.ResourceLocation;

public interface GrowthOutput extends IGrowthOutputExtension {
    default void accept(ResourceLocation location, BlockGrowth growth) {
        accept(location, growth);
    }
}
