package dev.metallurgists.hematite.api.weathering.block_growths;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.HematiteRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Supplier;

public interface BlockGrowth {
    Codec<BlockGrowth> CODEC = Codec.lazyInitialized(HematiteRegistries.BLOCK_GROWTH_TYPE_REGISTRY::byNameCodec).dispatch("type", BlockGrowth::getType, Type::codec);

    Type<?> getType();

    @Nullable
    Iterable<? extends Block> getOwners();

    Collection<Holder<TickSource>> getTickSources();

    void tryGrowing(BlockPos pos, BlockState state, Level level, Supplier<Holder<Biome>> biome);


    record Type<T extends BlockGrowth>(MapCodec<T> codec) { }
}
