package dev.metallurgists.hematite.common.rule_test;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.registry.HematiteRuleTestTypes;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class BurnableTest extends RuleTest {

    private static final BurnableTest INSTANCE = new BurnableTest();

    public static final MapCodec<BurnableTest> CODEC = MapCodec.unit(() -> INSTANCE);

    public boolean test(BlockState state, RandomSource random) {
        //hack since we don't have world and pos. hopefully mods aren't using those lol
        try {
            return state.getFlammability(null, null, Direction.UP) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    protected RuleTestType<BurnableTest> getType() {
        return HematiteRuleTestTypes.BURNABLE_TEST.get();
    }
}
