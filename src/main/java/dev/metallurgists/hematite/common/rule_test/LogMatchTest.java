package dev.metallurgists.hematite.common.rule_test;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.registry.HematiteRuleTestTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class LogMatchTest extends RuleTest {

    public static final LogMatchTest INSTANCE = new LogMatchTest();

    public static final MapCodec<LogMatchTest> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public boolean test(BlockState state, RandomSource random) {
        return state.is(BlockTags.LOGS) && (!state.hasProperty(RotatedPillarBlock.AXIS) ||
                state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y) &&
                !BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("stripped");
    }

    @Override
    protected RuleTestType<LogMatchTest> getType() {
        return HematiteRuleTestTypes.LOG_TEST.get();
    }
}
