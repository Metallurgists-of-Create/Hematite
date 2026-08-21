package dev.metallurgists.hematite.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.api.weathering.block_growths.data.BlockGrowthHandler;
import dev.metallurgists.hematite.api.weathering.block_growths.events.LightningStruckBlockEvent;
import dev.metallurgists.hematite.api.weathering.block_growths.helpers.SkyAccess;
import dev.metallurgists.hematite.api.weathering.fluid_generators.data.FluidGeneratorHandler;
import dev.metallurgists.hematite.command.BlockGrowthListCommand;
import dev.metallurgists.hematite.command.FluidGeneratorListCommand;
import dev.metallurgists.hematite.registry.HematiteTickSources;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

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

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Hematite.ID)
                .requires(cs -> cs.hasPermission(0))
                .then(BlockGrowthListCommand.register(event.getBuildContext()))
                .then(FluidGeneratorListCommand.register(event.getBuildContext()));

        event.getDispatcher().register(root);
    }
}
