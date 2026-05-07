package dev.metallurgists.hematite.common.weathering.block_growths;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.registry.HematiteBlockGrowthTypes;
import dev.metallurgists.hematite.registry.HematiteTickSources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class NoOpBlockGrowth implements BlockGrowth {

    public static final MapCodec<NoOpBlockGrowth> CODEC = MapCodec.unit(new NoOpBlockGrowth());

    public NoOpBlockGrowth() {}

    @Override
    public Type<?> getType() {
        return HematiteBlockGrowthTypes.NO_OP.get();
    }

    @Override
    public @Nullable Iterable<? extends Block> getOwners() {
        return null;
    }

    @Override
    public Collection<Holder<TickSource>> getTickSources() {
        return List.of(HematiteTickSources.BLOCK_TICK);
    }

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, Level level, Supplier<Holder<Biome>> biome) {
    }
}
