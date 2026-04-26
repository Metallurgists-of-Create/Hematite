package dev.metallurgists.hematite.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.common.area_condition.AreaCheck;
import dev.metallurgists.hematite.api.area_condition.AreaCondition;
import dev.metallurgists.hematite.api.area_condition.AreaConditionType;
import dev.metallurgists.hematite.common.area_condition.NeighborCheck;
import dev.metallurgists.hematite.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematiteAreaConditionTypes {
    public static final DeferredRegister<AreaConditionType<?>> CONDITION_TYPES = ModUtils.createRegister(HematiteRegistries.AREA_CONDITION_TYPE);

    public static final DeferredHolder<AreaConditionType<?>, AreaConditionType<AreaCheck>> GENERATE_IF_NOT_TOO_MANY = register("generate_if_not_too_many", AreaCheck.CODEC);
    public static final DeferredHolder<AreaConditionType<?>, AreaConditionType<NeighborCheck>> NEIGHBOR_BASED_GENERATION = register("neighbor_based_generation", NeighborCheck.CODEC);

    public static <T extends AreaCondition> DeferredHolder<AreaConditionType<?>, AreaConditionType<T>> register(String name, MapCodec<T> codec) {
        return CONDITION_TYPES.register(name, () -> new AreaConditionType<>(codec));
    }

    public static void staticInit() {
        Hematite.logRegistry(HematiteRegistries.AREA_CONDITION_TYPE);
    }
}
