package dev.metallurgists.hematite.api.weathering.fluid_generators;

import com.mojang.serialization.Codec;
import dev.metallurgists.hematite.HematiteRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FluidGenerator extends Comparable<FluidGenerator> {

    Codec<FluidGenerator> CODEC = Codec.lazyInitialized(HematiteRegistries.FLUID_GENERATOR_TYPE_REGISTRY::byNameCodec).dispatch("type", FluidGenerator::getType, FluidGeneratorType::codec);

    Optional<BlockPos> tryGenerating(List<Direction> possibleFlowDir, BlockPos pos, Level level, Map<Direction, BlockState> neighborCache);

    Fluid getFluid();

    FluidType getFluidType();

    FluidGeneratorType<?> getType();

    int getPriority();

    //NYI
    default SoundEvent getSound() {
        return null;
    }

    default int compareTo(@NotNull FluidGenerator o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    enum FluidType implements StringRepresentable {
        BOTH("both"),
        FLOWING("flowing"),
        STILL("still");

        public static final Codec<FluidType> CODEC = StringRepresentable.fromEnum(FluidType::values);

        private final String name;

        FluidType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }


        @Override
        public String getSerializedName() {
            return this.name;
        }

        public boolean isStill() {
            return this != FLOWING;
        }
        public boolean isFlowing() {
            return this != STILL;
        }
    }
}
