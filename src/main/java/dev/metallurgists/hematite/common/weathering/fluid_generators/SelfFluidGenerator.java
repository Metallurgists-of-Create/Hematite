package dev.metallurgists.hematite.common.weathering.fluid_generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.weathering.fluid_generators.AdjacentBlocks;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGeneratorType;
import dev.metallurgists.hematite.registry.HematiteFluidGeneratorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SelfFluidGenerator implements FluidGenerator {

    public static final MapCodec<SelfFluidGenerator> CODEC = RecordCodecBuilder.<SelfFluidGenerator>mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(SelfFluidGenerator::getFluid),
                    FluidType.CODEC.optionalFieldOf("fluid_type", FluidType.BOTH).forGetter(SelfFluidGenerator::getFluidType),
                    BlockState.CODEC.fieldOf("generate").forGetter(SelfFluidGenerator::getGrowth),
                    AdjacentBlocks.CODEC.fieldOf("adjacent_blocks").forGetter(SelfFluidGenerator::getAdjacentBlocksCondition),
                    PositionTest.CODEC.optionalFieldOf("additional_target_check").forGetter(SelfFluidGenerator::getPositionTests),
                    Codec.INT.optionalFieldOf("priority", 0).forGetter(SelfFluidGenerator::getPriority)
            ).apply(instance, SelfFluidGenerator::new));

    private final Fluid fluid;
    private final FluidType fluidType;
    private final BlockState growth;
    private final Optional<PositionTest> positionTests;
    private final int priority;
    private final AdjacentBlocks adjacentBlocksCondition;

    public SelfFluidGenerator(Fluid fluid, FluidType fluidType, BlockState growth,
                              AdjacentBlocks adjacentBlocks,
                              Optional<PositionTest> positionRuleTests, int priority) {
        this.fluid = fluid;
        this.fluidType = fluidType;
        this.growth = growth;
        this.adjacentBlocksCondition = adjacentBlocks;
        this.positionTests = positionRuleTests;
        this.priority = priority;
    }

    @Override
    public FluidType getFluidType() {
        return fluidType;
    }

    @Override
    public FluidGeneratorType<?> getType() {
        return HematiteFluidGeneratorTypes.TARGET_SELF.get();
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    public BlockState getGrowth() {
        return growth;
    }

    public Optional<PositionTest> getPositionTests() {
        return positionTests;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public AdjacentBlocks getAdjacentBlocksCondition() {
        return adjacentBlocksCondition;
    }

    @Override
    public Optional<BlockPos> tryGenerating(List<Direction> possibleFlowDir, BlockPos pos, Level level, Map<Direction, BlockState> neighborCache) {

        if (!adjacentBlocksCondition.isMet(possibleFlowDir, pos, level, neighborCache, positionTests))
            return Optional.empty();

        if (pos != null) {
            level.setBlockAndUpdate(pos, this.growth);
            return Optional.of(pos);
        }
        return Optional.empty();
    }
}
