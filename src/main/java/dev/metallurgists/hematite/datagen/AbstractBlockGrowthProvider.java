package dev.metallurgists.hematite.datagen;

import com.google.common.collect.Sets;
import dev.metallurgists.hematite.api.area_condition.AreaCondition;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.api.weathering.block_growths.data.BlockGrowthBuilder;
import dev.metallurgists.hematite.api.weathering.block_growths.data.GrowthOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractBlockGrowthProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    protected final String modId;

    public AbstractBlockGrowthProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "block_growths");
        this.registries = registries;
        this.modId = modId;
    }

    public static BlockGrowthBuilder configured(HolderSet<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        return BlockGrowthBuilder.growth(owners, targetPredicate, areaCheck);
    }

    public static BlockGrowthBuilder configured(Block owner, RuleTest targetPredicate, AreaCondition areaCheck) {
        return BlockGrowthBuilder.growth(HolderSet.direct(owner.builtInRegistryHolder()), targetPredicate, areaCheck);
    }

    public static BlockGrowthBuilder configured(List<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        List<Holder.Reference<Block>> holders = owners.stream().map(Block::builtInRegistryHolder).toList();
        HolderSet<Block> direct = HolderSet.direct(holders);
        return BlockGrowthBuilder.growth(direct, targetPredicate, areaCheck);
    }

    public static void builtin(GrowthOutput output, BlockGrowth growth, ResourceLocation location) {
        output.accept(location, growth);
    }

    protected abstract void buildGrowths(GrowthOutput output, HolderLookup.Provider holderLookup);

    public final @NotNull CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose((provider) -> this.run(output, provider));
    }

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.buildGrowths(new GrowthOutput() {
            public void accept(ResourceLocation location, BlockGrowth growth) {
                if (!set.add(location)) {
                    throw new IllegalStateException("Duplicate growth " + location);
                } else {
                    list.add(DataProvider.saveStable(output, registries, BlockGrowth.CODEC, growth, pathProvider.json(location)));
                }
            }
        }, registries);

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull String getName() {
        return "Block Growths for " + this.modId;
    }
}
