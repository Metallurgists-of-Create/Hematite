package dev.metallurgists.hematite.integration.kubejs.event;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;

import javax.annotation.Nullable;

public class HematiteDataEventJS extends KubeDataEvent {

    public HematiteDataEventJS(KubeResourceGenerator gen) {
        super(gen);
    }

    public void blockGrowth(BlockGrowth growth, @Nullable KubeResourceLocation id) {
        add(growth, BlockGrowth.CODEC, id, "hematite/block_growths");
    }

    public void blockGrowth(BlockGrowth growth) {
        blockGrowth(growth, null);
    }

    public void fluidGenerator(FluidGenerator generator, @Nullable KubeResourceLocation id) {
        add(generator, FluidGenerator.CODEC, id, "hematite/fluid_generators");
    }

    public void fluidGenerator(FluidGenerator generator) {
        fluidGenerator(generator, null);
    }
}
