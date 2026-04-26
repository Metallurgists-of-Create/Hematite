package dev.metallurgists.hematite.common.position_test.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.function.Supplier;

public record BlockTest(Vec3i offset, RuleTest predicate) implements PositionTest {

    public static final MapCodec<BlockTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(BlockTest::offset),
            RuleTest.CODEC.fieldOf("predicate").forGetter(BlockTest::predicate)
    ).apply(instance, BlockTest::new));

    @Override
    public PositionTestType<BlockTest> getType() {
        return HematitePositionTestTypes.BLOCK_MATCH.get();
    }

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        return predicate.test(level.getBlockState(pos.offset(offset)), RandomSource.create(Mth.getSeed(pos)));
    }
}
