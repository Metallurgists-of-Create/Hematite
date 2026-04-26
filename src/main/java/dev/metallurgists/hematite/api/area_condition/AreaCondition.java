package dev.metallurgists.hematite.api.area_condition;

import com.mojang.serialization.Codec;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.common.weathering.block_growths.ConfigurableBlockGrowth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface AreaCondition {
    AreaCondition EMPTY = new AreaCondition() {
        @Override
        public boolean test(BlockPos pos, Level level, ConfigurableBlockGrowth config) {
            return true;
        }

        @Override
        public int getMaxRange() {
            return 0;
        }

        @Override
        public AreaConditionType<?> getType() {
            return null;
        }
    };

    Codec<AreaCondition> CODEC = Codec.lazyInitialized(HematiteRegistries.AREA_CONDITION_TYPE_REGISTRY::byNameCodec).dispatch("type", AreaCondition::getType, AreaConditionType::codec);

    boolean test(BlockPos pos, Level level, ConfigurableBlockGrowth config);

    int getMaxRange();

    AreaConditionType<?> getType();
}
