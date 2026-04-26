package dev.metallurgists.hematite.common.weathering.fluid_generators;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGeneratorType;
import dev.metallurgists.hematite.registry.HematiteFluidGeneratorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OtherFluidGenerator implements FluidGenerator {

    public static final MapCodec<OtherFluidGenerator> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(OtherFluidGenerator::getFluid),
                    FluidType.CODEC.optionalFieldOf("fluid_type", FluidType.BOTH).forGetter(OtherFluidGenerator::getFluidType),
                    BlockState.CODEC.fieldOf("generate").forGetter(OtherFluidGenerator::getGrowth),
                    RuleTest.CODEC.fieldOf("target").forGetter(OtherFluidGenerator::getTarget),
                    PositionTest.CODEC.optionalFieldOf("additional_target_check").forGetter(OtherFluidGenerator::getExtraCheck),
                    Codec.INT.optionalFieldOf("priority", 0).forGetter(OtherFluidGenerator::getPriority)
            ).apply(instance, OtherFluidGenerator::new));

    private final Fluid fluid;
    private final FluidType fluidType;
    private final BlockState growth;
    private final RuleTest target;
    private final Optional<PositionTest> extraCheck;
    private final int priority;

    public OtherFluidGenerator(Fluid fluid, FluidType fluidType, BlockState growth,
                               RuleTest target, Optional<PositionTest> positionRuleTests, int priority) {
        this.fluid = fluid;
        this.fluidType = fluidType;
        this.growth = growth;
        this.target = target;
        this.extraCheck = positionRuleTests;
        this.priority = priority;
    }

    @Override
    public FluidType getFluidType() {
        return fluidType;
    }

    @Override
    public FluidGeneratorType<?> getType() {
        return HematiteFluidGeneratorTypes.TARGET_OTHER.get();
    }

    public Fluid getFluid() {
        return fluid;
    }

    public RuleTest getTarget() {
        return target;
    }

    public BlockState getGrowth() {
        return growth;
    }

    public Optional<PositionTest> getExtraCheck() {
        return extraCheck;
    }

    public int getPriority() {
        return priority;
    }

    public Optional<BlockPos> tryGenerating(List<Direction> possibleFlowDir, BlockPos pos, Level level, Map<Direction, BlockState> neighborCache) {
        Supplier<Holder<Biome>> b = Suppliers.memoize(() -> level.getBiome(pos));

        for (Direction d : possibleFlowDir) {
            BlockPos p = pos.relative(d);
            BlockState state = neighborCache.computeIfAbsent(d, c -> level.getBlockState(p));
            if (target.test(state, level.random)) {
                if (extraCheck.isPresent() && !extraCheck.get().test(b, p, level)) continue;
                level.setBlockAndUpdate(p, this.growth);
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
