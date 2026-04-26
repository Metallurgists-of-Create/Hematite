package dev.metallurgists.hematite.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.common.position_test.logic.AndTest;
import dev.metallurgists.hematite.common.position_test.logic.NandTest;
import dev.metallurgists.hematite.common.position_test.logic.OrTest;
import dev.metallurgists.hematite.common.position_test.world.*;
import dev.metallurgists.hematite.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HematitePositionTestTypes {
    public static final DeferredRegister<PositionTestType<?>> CONDITION_TYPES = ModUtils.createRegister(HematiteRegistries.POSITION_TEST_TYPE);

    public static final DeferredHolder<PositionTestType<?>, PositionTestType<AndTest>> AND = register("and", AndTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<NandTest>> NAND = register("nand", NandTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<OrTest>> OR = register("or", OrTest.CODEC);

    public static final DeferredHolder<PositionTestType<?>, PositionTestType<BiomeSetMatchTest>> BIOME_MATCH = register("biome_match", BiomeSetMatchTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<BlockTest>> BLOCK_MATCH = register("block_match", BlockTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<EntityTest>> ENTITY_MATCH = register("entity_match", EntityTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<HasAroundTest>> HAS_AROUND = register("has_around", HasAroundTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<HeightTest>> HEIGHT = register("height", HeightTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<IsDayTest>> IS_DAY = register("is_day", IsDayTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<LightTest>> LIGHT = register("light", LightTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<PosRandomTest>> POS_RANDOM = register("pos_random", PosRandomTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<PrecipitationTest>> PRECIPITATION = register("precipitation", PrecipitationTest.CODEC);
    public static final DeferredHolder<PositionTestType<?>, PositionTestType<TemperatureMatchTest>> TEMPERATURE_MATCH = register("temperature_match", TemperatureMatchTest.CODEC);

    public static <T extends PositionTest> DeferredHolder<PositionTestType<?>, PositionTestType<T>> register(String name, MapCodec<T> codec) {
        return CONDITION_TYPES.register(name, () -> new PositionTestType<>(codec));
    }

    public static void staticInit() {
        Hematite.logRegistry(HematiteRegistries.POSITION_TEST_TYPE);
    }
}
