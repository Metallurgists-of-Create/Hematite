package dev.metallurgists.hematite.event;

import dev.metallurgists.hematite.api.weathering.block_growths.data.BlockGrowthHandler;
import dev.metallurgists.hematite.api.weathering.block_growths.events.LightningStruckBlockEvent;
import dev.metallurgists.hematite.api.weathering.block_growths.helpers.SkyAccess;
import dev.metallurgists.hematite.api.weathering.fluid_generators.data.FluidGeneratorHandler;
import dev.metallurgists.hematite.registry.HematiteTickSources;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class CommonEvents {

    @SubscribeEvent
    public void skyAccessSource(SkyAccess.TickSourceEvent event) {
        boolean isRaining = event.level.isRaining();
        if (isRaining) {
            Biome biome = event.level.getBiome(event.targetPos).value();
            Biome.Precipitation precipitation = biome.getPrecipitationAt(event.targetPos);
            event.setTickSource(precipitation == Biome.Precipitation.SNOW ? HematiteTickSources.SNOW : HematiteTickSources.RAIN);
        }
    }

    @SubscribeEvent
    public void registerListeners(AddReloadListenerEvent event) {
        event.addListener(BlockGrowthHandler.RELOAD_INSTANCE);
        event.addListener(FluidGeneratorHandler.RELOAD_INSTANCE);
    }

    @SubscribeEvent
    public void onLightningHit(LightningStruckBlockEvent event) {
        BlockPos blockPos = event.getPos();
        LevelAccessor level = event.getLevel();
        BlockState blockState = level.getBlockState(blockPos);
        BlockGrowthHandler.tickBlock(HematiteTickSources.LIGHTNING, blockState, (ServerLevel) level, blockPos);
    }
}
