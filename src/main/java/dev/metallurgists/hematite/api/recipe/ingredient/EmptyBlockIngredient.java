package dev.metallurgists.hematite.api.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.registry.HematiteBlockIngredientTypes;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.Stream;

public class EmptyBlockIngredient extends BlockIngredient {
    public static final EmptyBlockIngredient INSTANCE = new EmptyBlockIngredient();
    public static final MapCodec<EmptyBlockIngredient> CODEC;

    private EmptyBlockIngredient() {
    }

    public boolean test(BlockState state) {
        return state.isEmpty();
    }

    protected Stream<BlockState> generateStates() {
        return Stream.empty();
    }

    public boolean isSimple() {
        return true;
    }

    public BlockIngredientType<?> getType() {
        return HematiteBlockIngredientTypes.EMPTY.get();
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object obj) {
        return this == obj;
    }

    static {
        CODEC = MapCodec.unit(INSTANCE);
    }
}
