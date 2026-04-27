package dev.metallurgists.hematite.api.recipe.ingredient;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.registry.HematiteBlockIngredientTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.Stream;

public class SingleBlockIngredient extends BlockIngredient {
    public static final MapCodec<SingleBlockIngredient> CODEC;
    private final Holder<Block> block;

    public SingleBlockIngredient(Holder<Block> block) {
        if (block.is(Blocks.AIR.builtInRegistryHolder())) {
            throw new IllegalStateException("SingleBlockIngredient must not be constructed with minecraft:air, use BlockIngredient.empty() instead!");
        } else {
            this.block = block;
        }
    }

    public boolean test(BlockState state) {
        return state.is(this.block);
    }

    protected Stream<BlockState> generateStates() {
        return this.block.value().getStateDefinition().getPossibleStates().stream();
    }

    public boolean isSimple() {
        return true;
    }

    public BlockIngredientType<?> getType() {
        return HematiteBlockIngredientTypes.SINGLE.get();
    }

    public int hashCode() {
        return this.block.value().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            boolean var10000;
            if (obj instanceof SingleBlockIngredient other) {
                if (other.block.is(this.block)) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    public Holder<Block> block() {
        return this.block;
    }

    static {
        var BLOCK_NON_AIR_CODEC = BuiltInRegistries.BLOCK.holderByNameCodec().validate((holder) -> holder.is(Blocks.AIR.builtInRegistryHolder()) ? DataResult.error(() -> "Block must not be minecraft:air") : DataResult.success(holder));
        CODEC = BLOCK_NON_AIR_CODEC.xmap(SingleBlockIngredient::new, SingleBlockIngredient::block).fieldOf("block");
    }
}
