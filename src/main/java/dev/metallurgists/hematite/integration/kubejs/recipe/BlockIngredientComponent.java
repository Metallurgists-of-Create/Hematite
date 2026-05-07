package dev.metallurgists.hematite.integration.kubejs.recipe;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.api.recipe.ingredient.BlockIngredient;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.metallurgists.hematite.integration.kubejs.bindings.IngredientBindings;

public enum BlockIngredientComponent implements RecipeComponent<BlockIngredient> {
    INSTANCE;

    public static final RecipeComponentType<BlockIngredient> TYPE = RecipeComponentType.unit(Hematite.asResource("block_ingredient"), INSTANCE);

    @Override
    public RecipeComponentType<BlockIngredient> type() {
        return TYPE;
    }

    @Override
    public Codec<BlockIngredient> codec() {
        return BlockIngredient.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return IngredientBindings.BLOCK_ING_TYPE_INFO;
    }

    @Override
    public BlockIngredient wrap(RecipeScriptContext cx, Object from) {
        return IngredientBindings.wrapBlock(cx.cx(), from);
    }

    @Override
    public boolean isEmpty(BlockIngredient value) {
        return value.isEmpty();
    }
}
