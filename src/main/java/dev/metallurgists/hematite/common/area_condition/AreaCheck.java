package dev.metallurgists.hematite.common.area_condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.area_condition.AreaCondition;
import dev.metallurgists.hematite.api.area_condition.AreaConditionType;
import dev.metallurgists.hematite.common.weathering.block_growths.ConfigurableBlockGrowth;
import dev.metallurgists.hematite.registry.HematiteAreaConditionTypes;
import dev.metallurgists.hematite.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.Optional;

public record AreaCheck(int rX, int rY, int rZ, int requiredAmount, Optional<Integer> yOffset,
                        Optional<RuleTest> mustHavePredicate,
                        Optional<RuleTest> mustNotHavePredicate,
                        Optional<HolderSet<Block>> extraIncluded) implements AreaCondition {

    public static final MapCodec<AreaCheck> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("radiusX").forGetter(AreaCheck::rX),
            Codec.INT.fieldOf("radiusY").forGetter(AreaCheck::rY),
            Codec.INT.fieldOf("radiusZ").forGetter(AreaCheck::rZ),
            Codec.INT.fieldOf("requiredAmount").forGetter(AreaCheck::requiredAmount),
            Codec.INT.optionalFieldOf("yOffset").forGetter(AreaCheck::yOffset),
            RuleTest.CODEC.optionalFieldOf("must_have").forGetter(AreaCheck::mustHavePredicate),
            RuleTest.CODEC.optionalFieldOf("must_not_have").forGetter(AreaCheck::mustNotHavePredicate),
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("includes").forGetter(AreaCheck::extraIncluded)
    ).apply(instance, AreaCheck::new));

    @Override
    public AreaConditionType<AreaCheck> getType() {
        return HematiteAreaConditionTypes.GENERATE_IF_NOT_TOO_MANY.get();
    }

    @Override
    public boolean test(BlockPos pos, Level level, ConfigurableBlockGrowth config) {
        if (yOffset.isPresent()) pos = pos.above(yOffset.get());
        int count = 0;
        RandomSource random = RandomSource.create(Mth.getSeed(pos));
        boolean hasRequirement = this.mustHavePredicate.isEmpty();
        //shuffling. provides way better result that iterating through it conventionally
        //if(hasRequirement && requiredAmount == -1)return true;
        var list = ModUtils.grabBlocksAroundRandomly(pos, rX, rY, rZ);
        for (BlockPos p : list) {
            BlockState state = level.getBlockState(p);
            if (config.getPossibleBlocks().contains(state.getBlock()) ||
                    (extraIncluded.isPresent() && state.is(extraIncluded.get()))) count += 1;
            if (!hasRequirement &&
                    mustHavePredicate.get().test(state, random)) {
                hasRequirement = true;
                // if -1 means it can accept any number so we exit early
                if (requiredAmount == -1) {
                    break;
                }
            } else if (mustNotHavePredicate.isPresent() && mustNotHavePredicate.get().test(state, random)) {
                return false;
            }
            if (count >= requiredAmount) return false;
        }
        return hasRequirement;
    }

    @Override
    public int getMaxRange() {
        return Math.max(rX, Math.max(rY, rZ));
    }
}
