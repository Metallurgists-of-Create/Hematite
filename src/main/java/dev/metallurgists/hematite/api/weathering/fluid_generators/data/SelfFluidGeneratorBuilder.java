package dev.metallurgists.hematite.api.weathering.fluid_generators.data;

import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.common.position_test.logic.AndTest;
import dev.metallurgists.hematite.common.position_test.logic.NandTest;
import dev.metallurgists.hematite.common.position_test.logic.OrTest;
import dev.metallurgists.hematite.common.weathering.fluid_generators.SelfFluidGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.Optional;

public class SelfFluidGeneratorBuilder {
    private final Fluid fluid;
    private FluidGenerator.FluidType fluidType = FluidGenerator.FluidType.BOTH;
    private final BlockState growth;
    private Optional<PositionTest> positionTests = Optional.empty();
    private int priority = 0;
    private final AdjacentBlocksBuilder adjacentCondition;

    public SelfFluidGeneratorBuilder(Fluid fluid, BlockState growth, AdjacentBlocksBuilder adjacentCondition) {
        this.fluid = fluid;
        this.growth = growth;
        this.adjacentCondition = adjacentCondition;
    }

    public SelfFluidGeneratorBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public SelfFluidGeneratorBuilder type(FluidGenerator.FluidType fluidType) {
        this.fluidType = fluidType;
        return this;
    }

    public SelfFluidGeneratorBuilder flowing() {
        this.fluidType = FluidGenerator.FluidType.FLOWING;
        return this;
    }

    public SelfFluidGeneratorBuilder still() {
        this.fluidType = FluidGenerator.FluidType.STILL;
        return this;
    }

    public SelfFluidGeneratorBuilder check(PositionTest check) {
        this.positionTests = Optional.of(check);
        return this;
    }

    public SelfFluidGeneratorBuilder checkAll(PositionTest... checks) {
        this.positionTests = Optional.of(new AndTest(Arrays.asList(checks)));
        return this;
    }

    public SelfFluidGeneratorBuilder checkAny(PositionTest... checks) {
        this.positionTests = Optional.of(new OrTest(Arrays.asList(checks)));
        return this;
    }

    public SelfFluidGeneratorBuilder checkNone(PositionTest... checks) {
        this.positionTests = Optional.of(new NandTest(Arrays.asList(checks)));
        return this;
    }

    public SelfFluidGenerator build() {
        return new SelfFluidGenerator(this.fluid, this.fluidType, this.growth, this.adjacentCondition.build(), this.positionTests, this.priority);
    }

    public void save(GeneratorOutput output, String id) {
        save(output, ResourceLocation.parse(id));
    }

    public void save(GeneratorOutput output, ResourceLocation recipeId) {
        SelfFluidGenerator generator = this.build();
        output.accept(recipeId, generator);
    }
}
