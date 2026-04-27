package dev.metallurgists.hematite.api.recipe.ingredient;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class BlockStateLinkedSet {
    public static final Hash.Strategy<? super BlockState> TYPE_AND_COMPONENTS = new Hash.Strategy<BlockState>() {
        public int hashCode(@Nullable BlockState state) {
            return hashBlockAndProperties(state);
        }

        public boolean equals(@Nullable BlockState first, @Nullable BlockState second) {
            return first == second || first != null && second != null && first.isEmpty() == second.isEmpty() && isSameBlockSameProperties(first, second);
        }
    };

    public BlockStateLinkedSet() {
    }

    public static Set<BlockState> createTypeAndComponentsSet() {
        return new ObjectLinkedOpenCustomHashSet<>(TYPE_AND_COMPONENTS);
    }

    public static int hashBlockAndProperties(@Nullable BlockState state) {
        if (state != null) {
            int i = 31 + state.getBlock().hashCode();
            return 31 * i + state.getProperties().hashCode();
        } else {
            return 0;
        }
    }

    public static boolean isSameBlockSameProperties(BlockState first, BlockState second) {
        if (!first.is(second.getBlock())) {
            return false;
        } else {
            return first.isEmpty() && second.isEmpty() || Objects.equals(first.getProperties(), second.getProperties());
        }
    }
}
