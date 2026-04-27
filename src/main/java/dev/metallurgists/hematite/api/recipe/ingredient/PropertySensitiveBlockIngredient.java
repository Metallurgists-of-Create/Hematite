package dev.metallurgists.hematite.api.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.registry.HematiteBlockIngredientTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PropertySensitiveBlockIngredient extends BlockIngredient {
    public static final MapCodec<PropertySensitiveBlockIngredient> CODEC;
    private final Holder<Block> block;
    private final BlockStateConditionSet conditionSet;

    public PropertySensitiveBlockIngredient(Holder<Block> block, BlockStateConditionSet conditionSet) {
        if (block.is(Blocks.AIR.builtInRegistryHolder())) {
            throw new IllegalStateException("SingleBlockIngredient must not be constructed with minecraft:air, use BlockIngredient.empty() instead!");
        } else {
            this.block = block;
        }
        this.conditionSet = conditionSet;
    }

    public boolean test(BlockState state) {
        return state.is(this.block) && this.conditionSet.matches(state.getBlock().getStateDefinition(), state);
    }

    protected Stream<BlockState> generateStates() {
        List<BlockState> states = new ArrayList<>();
        for (var blockState : this.block.value().getStateDefinition().getPossibleStates()) {
            if (!this.conditionSet.matches(blockState.getBlock().getStateDefinition(), blockState)) continue;
            states.add(blockState);
        }
        return states.stream();
    }

    public boolean isSimple() {
        return false;
    }

    public BlockIngredientType<?> getType() {
        return HematiteBlockIngredientTypes.PROPERTY_SENSITIVE.get();
    }

    public int hashCode() {
        return this.block.value().hashCode() + this.conditionSet.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            boolean var10000;
            if (obj instanceof PropertySensitiveBlockIngredient other) {
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

    public BlockStateConditionSet conditionSet() {
        return this.conditionSet;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(PropertySensitiveBlockIngredient::block),
                BlockStateConditionSet.CODEC.fieldOf("properties").forGetter(PropertySensitiveBlockIngredient::conditionSet)
        ).apply(instance, PropertySensitiveBlockIngredient::new));
    }
}
