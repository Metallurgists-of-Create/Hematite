package dev.metallurgists.hematite.integration.kubejs.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.api.weathering.block_growths.data.BlockGrowthHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;


public enum BlockGrowthBindings {
    INSTANCE;

    @Info("Calls a Growth")
    public static List<BlockPos> callGrowth(Holder<TickSource> source, Level level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
        return BlockGrowthHandler.tickBlock(source, state, level, blockPos);
    }
}
