package dev.metallurgists.hematite.datagen;

import com.google.common.collect.Sets;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.api.weathering.fluid_generators.data.AdjacentBlocksBuilder;
import dev.metallurgists.hematite.api.weathering.fluid_generators.data.GeneratorOutput;
import dev.metallurgists.hematite.api.weathering.fluid_generators.data.OtherFluidGeneratorBuilder;
import dev.metallurgists.hematite.api.weathering.fluid_generators.data.SelfFluidGeneratorBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractFluidGeneratorProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    protected final String modId;

    public AbstractFluidGeneratorProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "fluid_generators");
        this.registries = registries;
        this.modId = modId;
    }

    public static SelfFluidGeneratorBuilder self(Fluid fluid, Block growth, AdjacentBlocksBuilder adjacentCondition) {
        return self(fluid, growth.defaultBlockState(), adjacentCondition);
    }

    public static SelfFluidGeneratorBuilder self(Fluid fluid, BlockState growth, AdjacentBlocksBuilder adjacentCondition) {
        return new SelfFluidGeneratorBuilder(fluid, growth, adjacentCondition);
    }

    public static OtherFluidGeneratorBuilder other(Fluid fluid, Block growth, RuleTest target) {
        return other(fluid, growth.defaultBlockState(), target);
    }

    public static OtherFluidGeneratorBuilder other(Fluid fluid, BlockState growth, RuleTest target) {
        return new OtherFluidGeneratorBuilder(fluid, growth, target);
    }

    public static void builtin(GeneratorOutput output, FluidGenerator generator, ResourceLocation location) {
        output.accept(location, generator);
    }

    protected abstract void buildGenerators(GeneratorOutput output, HolderLookup.Provider holderLookup);

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.buildGenerators(new GeneratorOutput() {
            public void accept(ResourceLocation location, FluidGenerator generator) {
                if (!set.add(location)) {
                    throw new IllegalStateException("Duplicate generator " + location);
                } else {
                    list.add(DataProvider.saveStable(output, registries, FluidGenerator.CODEC, generator, pathProvider.json(location)));
                }
            }
        }, registries);

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> this.run(output, provider));
    }

    @Override
    public @NotNull String getName() {
        return "Fluid Generators for " + this.modId;
    }
}
