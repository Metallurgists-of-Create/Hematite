package dev.metallurgists.hematite.api.weathering.fluid_generators.data;

import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.common.position_test.logic.AndTest;
import dev.metallurgists.hematite.common.position_test.logic.NandTest;
import dev.metallurgists.hematite.common.position_test.logic.OrTest;
import dev.metallurgists.hematite.common.weathering.fluid_generators.OtherFluidGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.Optional;

public class OtherFluidGeneratorBuilder {
    private final Fluid fluid;
    private  FluidGenerator.FluidType fluidType = FluidGenerator.FluidType.BOTH;
    private final BlockState growth;
    private final RuleTest target;
    private Optional<PositionTest> extraCheck = Optional.empty();
    private int priority = 0;

    public OtherFluidGeneratorBuilder(Fluid fluid, BlockState growth, RuleTest target) {
        this.fluid = fluid;
        this.growth = growth;
        this.target = target;
    }

    public OtherFluidGeneratorBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public OtherFluidGeneratorBuilder type(FluidGenerator.FluidType fluidType) {
        this.fluidType = fluidType;
        return this;
    }

    public OtherFluidGeneratorBuilder flowing() {
        this.fluidType = FluidGenerator.FluidType.FLOWING;
        return this;
    }

    public OtherFluidGeneratorBuilder still() {
        this.fluidType = FluidGenerator.FluidType.STILL;
        return this;
    }

    public OtherFluidGeneratorBuilder check(PositionTest check) {
        this.extraCheck = Optional.of(check);
        return this;
    }

    public OtherFluidGeneratorBuilder checkAll(PositionTest... checks) {
        this.extraCheck = Optional.of(new AndTest(Arrays.asList(checks)));
        return this;
    }

    public OtherFluidGeneratorBuilder checkAny(PositionTest... checks) {
        this.extraCheck = Optional.of(new OrTest(Arrays.asList(checks)));
        return this;
    }

    public OtherFluidGeneratorBuilder checkNone(PositionTest... checks) {
        this.extraCheck = Optional.of(new NandTest(Arrays.asList(checks)));
        return this;
    }

    public OtherFluidGenerator build() {
        return new OtherFluidGenerator(this.fluid, this.fluidType, this.growth, this.target, this.extraCheck, this.priority);
    }

    public void save(GeneratorOutput output, String id) {
        save(output, ResourceLocation.parse(id));
    }

    public void save(GeneratorOutput output, ResourceLocation recipeId) {
        OtherFluidGenerator generator = this.build();
        output.accept(recipeId, generator);
    }
}
