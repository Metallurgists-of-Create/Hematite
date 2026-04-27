package dev.metallurgists.hematite.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.recipe.ingredient.*;
import dev.metallurgists.hematite.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematiteBlockIngredientTypes {
    public static final DeferredRegister<BlockIngredientType<?>> INGREDIENT_TYPES = ModUtils.createRegister(HematiteRegistries.BLOCK_INGREDIENT_TYPE);

    public static final DeferredHolder<BlockIngredientType<?>, BlockIngredientType<EmptyBlockIngredient>> EMPTY = register("empty", EmptyBlockIngredient.CODEC);
    public static final DeferredHolder<BlockIngredientType<?>, BlockIngredientType<CompoundBlockIngredient>> COMPOUND = register("compound", CompoundBlockIngredient.CODEC);
    public static final DeferredHolder<BlockIngredientType<?>, BlockIngredientType<SingleBlockIngredient>> SINGLE = register("single", SingleBlockIngredient.CODEC);
    public static final DeferredHolder<BlockIngredientType<?>, BlockIngredientType<TagBlockIngredient>> TAG = register("tag", TagBlockIngredient.CODEC);
    public static final DeferredHolder<BlockIngredientType<?>, BlockIngredientType<PropertySensitiveBlockIngredient>> PROPERTY_SENSITIVE = register("property_sensitive", PropertySensitiveBlockIngredient.CODEC);

    public static <T extends BlockIngredient> DeferredHolder<BlockIngredientType<?>, BlockIngredientType<T>> register(String name, MapCodec<T> codec) {
        return INGREDIENT_TYPES.register(name, () -> new BlockIngredientType<>(codec));
    }

    public static void staticInit() {
        Hematite.logRegistry(HematiteRegistries.BLOCK_INGREDIENT_TYPE);
    }
}
