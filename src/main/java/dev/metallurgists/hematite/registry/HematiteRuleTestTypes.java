package dev.metallurgists.hematite.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.common.rule_test.*;
import dev.metallurgists.hematite.util.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematiteRuleTestTypes {
    public static final DeferredRegister<RuleTestType<?>> RULE_TESTS = ModUtils.createRegister(Registries.RULE_TEST);

    public static final DeferredHolder<RuleTestType<?>, RuleTestType<BlockPropertyTest>> BLOCK_PROPERTY_TEST = register("block_property_test", BlockPropertyTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<BlockSetMatchTest>> BLOCK_SET_MATCH_TEST = register("block_set_match", BlockSetMatchTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<BurnableTest>> BURNABLE_TEST = register("burnable_test", BurnableTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<FluidMatchTest>> FLUID_MATCH_TEST = register("fluid_match", FluidMatchTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<FluidTagMatchTest>> FLUID_TAG_MATCH_TEST = register("fluid_tag_match", FluidTagMatchTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<LogMatchTest>> LOG_TEST = register("tree_log", LogMatchTest.CODEC);

    public static <T extends RuleTest> DeferredHolder<RuleTestType<?>, RuleTestType<T>> register(String name, MapCodec<T> codec) {
        return RULE_TESTS.register(name, () -> RuleTestType.register(name, codec));
    }

    public static void staticInit() {
        Hematite.logRegistry(Registries.RULE_TEST);
    }
}
