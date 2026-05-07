package dev.metallurgists.hematite.integration.kubejs.bindings;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.BlockWrapper;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.type.ClassTypeInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.metallurgists.hematite.api.recipe.ingredient.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Collection;

public enum IngredientBindings {
    INSTANCE;

    public static final ClassTypeInfo BLOCK_ING_TYPE_INFO = Cast.to(TypeInfo.of(BlockIngredient.class));

    public static BlockIngredient wrapBlock(Context ctx, Object o) {
        o = Wrapper.unwrapped(o);

        return switch (o) {
            case BlockIngredient b -> b;
            case String str -> {
                if (str.charAt(0) == '#') {
                    yield new TagBlockIngredient(BlockTags.create(ResourceLocation.parse(str.substring(1))));
                } else {
                    yield BlockIngredient.of((Block) ctx.jsToJava(o, BlockWrapper.TYPE_INFO));
                }
            }
            case TagKey<?> tag -> new TagBlockIngredient(BlockTags.create(tag.location()));
            case Block b -> BlockIngredient.of(b);
            case JsonElement json -> BlockIngredient.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
            default -> BlockIngredient.ofStream(
                    ListJS.orSelf(o)
                            .stream()
                            .map(obj -> (Block) ctx.jsToJava(obj, BlockWrapper.TYPE_INFO))
            );
        };
    }

    @Info("Creates a new block ingredient of the given blocks")
    public BlockIngredient blockIngredient(Collection<Block> blocks) {
        return BlockIngredient.ofStream(blocks.stream());
    }

    @Info("Creates a new block ingredient of the given tag")
    public BlockIngredient tagBlockIngredient(TagKey<Block> tag) {
        return BlockIngredient.tag(tag);
    }

    @Info("Creates a new block ingredient of the given block ingredients")
    public BlockIngredient compoundBlockIngredient(Collection<BlockIngredient> ingredients) {
        return new CompoundBlockIngredient(ingredients.stream().toList());
    }

    public BlockIngredient propertySensitiveBlockIngredient(Block block, BlockStateConditionSet conditions) {
        return new PropertySensitiveBlockIngredient(block.builtInRegistryHolder(), conditions);
    }
}
