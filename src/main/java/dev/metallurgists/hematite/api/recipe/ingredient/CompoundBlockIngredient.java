package dev.metallurgists.hematite.api.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.registry.HematiteBlockIngredientTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CompoundBlockIngredient extends BlockIngredient {
    public static final MapCodec<CompoundBlockIngredient> CODEC;
    private final List<BlockIngredient> children;

    public CompoundBlockIngredient(List<? extends BlockIngredient> children) {
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Compound block ingredient must have at least one child");
        } else {
            this.children = List.copyOf(children);
        }
    }

    public static BlockIngredient of(BlockIngredient... children) {
        if (children.length == 0) {
            return BlockIngredient.empty();
        } else {
            return children.length == 1 ? children[0] : new CompoundBlockIngredient(List.of(children));
        }
    }

    public static BlockIngredient of(List<BlockIngredient> children) {
        if (children.isEmpty()) {
            return BlockIngredient.empty();
        } else {
            return children.size() == 1 ? children.getFirst() : new CompoundBlockIngredient(children);
        }
    }

    public static BlockIngredient of(Stream<BlockIngredient> stream) {
        return of(stream.toList());
    }

    public Stream<BlockState> generateStates() {
        return this.children.stream().flatMap(BlockIngredient::generateStates);
    }

    public boolean test(BlockState state) {
        for(BlockIngredient child : this.children) {
            if (child.test(state)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSimple() {
        for(BlockIngredient child : this.children) {
            if (!child.isSimple()) {
                return false;
            }
        }

        return true;
    }

    public BlockIngredientType<?> getType() {
        return HematiteBlockIngredientTypes.COMPOUND.get();
    }

    public int hashCode() {
        return Objects.hash(this.children);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            boolean result;
            if (obj instanceof CompoundBlockIngredient other) {
                if (other.children.equals(this.children)) {
                    result = true;
                    return result;
                }
            }

            result = false;
            return result;
        }
    }

    public List<BlockIngredient> children() {
        return this.children;
    }

    static {
        CODEC = NeoForgeExtraCodecs.aliasedFieldOf(BlockIngredient.LIST_CODEC_NON_EMPTY, new String[]{"children", "ingredients"}).xmap(CompoundBlockIngredient::new, CompoundBlockIngredient::children);
    }
}
