package dev.metallurgists.hematite.api.weathering.block_growths.data;

import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import net.minecraft.resources.ResourceLocation;

public interface IGrowthOutputExtension {
    private GrowthOutput self() {
        return (GrowthOutput) this;
    }
    void accept(ResourceLocation id, BlockGrowth growth);
}
