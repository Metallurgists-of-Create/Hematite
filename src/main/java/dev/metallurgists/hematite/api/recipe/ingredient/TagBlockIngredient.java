package dev.metallurgists.hematite.api.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.registry.HematiteBlockIngredientTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TagBlockIngredient extends BlockIngredient {
    public static final MapCodec<TagBlockIngredient> CODEC;
    private final TagKey<Block> tag;

    public TagBlockIngredient(TagKey<Block> tag) {
        this.tag = tag;
    }

    public boolean test(BlockState state) {
        return state.is(this.tag);
    }

    protected Stream<BlockState> generateStates() {
        var holders = BuiltInRegistries.BLOCK.getTag(this.tag).stream().flatMap(HolderSet::stream);
        List<BlockState> states = new ArrayList<>();
        for (var holder : holders.toList()) {
            states.addAll(holder.value().getStateDefinition().getPossibleStates());
        }
        return states.stream();
    }

    public boolean isSimple() {
        return true;
    }

    public BlockIngredientType<?> getType() {
        return HematiteBlockIngredientTypes.TAG.get();
    }

    public int hashCode() {
        return this.tag.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            boolean var10000;
            if (obj instanceof TagBlockIngredient tag) {
                if (tag.tag.equals(this.tag)) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    public TagKey<Block> tag() {
        return this.tag;
    }

    static {
        CODEC = TagKey.codec(Registries.BLOCK).xmap(TagBlockIngredient::new, TagBlockIngredient::tag).fieldOf("tag");
    }
}
