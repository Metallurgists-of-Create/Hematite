package dev.metallurgists.hematite.api.weathering.fluid_generators.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.api.weathering.fluid_generators.FluidGenerator;
import dev.metallurgists.hematite.util.RegistryAccessJsonReloadListener;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FluidGeneratorHandler extends RegistryAccessJsonReloadListener {

    public static final ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS = ImmutableList.of(
            Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST
    );

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final FluidGeneratorHandler RELOAD_INSTANCE = new FluidGeneratorHandler();

    private static final Map<Fluid, ImmutableList<FluidGenerator>> STILL_GENERATORS = new Object2ObjectOpenHashMap<>();
    private static final Map<Fluid, ImmutableList<FluidGenerator>> FLOWING_GENERATORS = new Object2ObjectOpenHashMap<>();
    private static final Set<Fluid> HAS_GENERATOR = new HashSet<>();

    public static Map<Fluid, ImmutableList<FluidGenerator>> getStillGenerators() {
        return STILL_GENERATORS;
    }

    public static Map<Fluid, ImmutableList<FluidGenerator>> getFlowingGenerators() {
        return FLOWING_GENERATORS;
    }

    public FluidGeneratorHandler() {
        super(GSON, "fluid_generators");
    }

    public static int stillGenerators(@Nullable Either<Holder.Reference<Fluid>, HolderSet.Named<Fluid>> filter) {
        AtomicInteger size = new AtomicInteger(0);
        if (filter != null) {
            filter.ifLeft(fluidReference -> {
                if (STILL_GENERATORS.containsKey(fluidReference.value())) {
                    size.getAndAdd(STILL_GENERATORS.get(fluidReference.value()).size());
                }
            });
            filter.ifRight(fluidSet -> {
                for (Holder.Reference<Fluid> fluidReference : BuiltInRegistries.FLUID.holders().filter(fluidSet::contains).toList()) {
                    if (STILL_GENERATORS.containsKey(fluidReference.value())) {
                        size.getAndAdd(STILL_GENERATORS.get(fluidReference.value()).size());
                    }
                }
            });
        } else {
            size.getAndAdd(STILL_GENERATORS.values().stream().mapToInt(ImmutableList::size).sum());
        }
        return size.get();
    }

    public static int flowingGenerators(@Nullable Either<Holder.Reference<Fluid>, HolderSet.Named<Fluid>> filter) {
        AtomicInteger size = new AtomicInteger(0);
        if (filter != null) {
            filter.ifLeft(fluidReference -> {
                if (FLOWING_GENERATORS.containsKey(fluidReference.value())) {
                    size.getAndAdd(FLOWING_GENERATORS.get(fluidReference.value()).size());
                }
            });
            filter.ifRight(fluidSet -> {
                for (Holder.Reference<Fluid> fluidReference : BuiltInRegistries.FLUID.holders().filter(fluidSet::contains).toList()) {
                    if (FLOWING_GENERATORS.containsKey(fluidReference.value())) {
                        size.getAndAdd(FLOWING_GENERATORS.get(fluidReference.value()).size());
                    }
                }
            });
        } else {
            size.getAndAdd(FLOWING_GENERATORS.values().stream().mapToInt(ImmutableList::size).sum());
        }
        return size.get();
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> jsonMap, RegistryAccess registryAccess) {
        Map<ResourceLocation, JsonElement> map = new HashMap<>();

        for (var e : jsonMap.entrySet()) {
            map.put(e.getKey(), e.getValue().deepCopy());
        }

        List<FluidGenerator> generators = new ArrayList<>();

        for (var e : map.entrySet()) {
            var json = e.getValue();

            var result = FluidGenerator.CODEC.parse(RegistryOps.create(JsonOps.INSTANCE, registryAccess), json);
            var o = result.resultOrPartial(error -> Hematite.LOGGER.error("Failed to read liquid generator JSON object for {} : {}", e.getKey(), error));

            o.ifPresent(generators::add);
        }
        Hematite.LOGGER.info("Loaded {} liquid generators configurations", map.size());

        STILL_GENERATORS.clear();
        FLOWING_GENERATORS.clear();
        HAS_GENERATOR.clear();

        Map<Fluid, List<FluidGenerator>> flowingMap = new HashMap<>();
        Map<Fluid, List<FluidGenerator>> stillMap = new HashMap<>();

        for (var g : generators) {
            HAS_GENERATOR.add(g.getFluid());

            if (g.getFluidType().isFlowing()) {
                var list = flowingMap.computeIfAbsent(g.getFluid(), e -> new ArrayList<>());

                list.add(g);
                Collections.sort(list);
            }

            if (g.getFluidType().isStill()) {
                var list = stillMap.computeIfAbsent(g.getFluid(), e -> new ArrayList<>());

                list.add(g);
                Collections.sort(list);
            }
        }
        flowingMap.forEach((key, value) -> FLOWING_GENERATORS.put(key, ImmutableList.copyOf(value)));
        stillMap.forEach((key, value) -> STILL_GENERATORS.put(key, ImmutableList.copyOf(value)));
    }

    public static Optional<Pair<BlockPos, @Nullable SoundEvent>> applyGenerators(FlowingFluid fluid, List<Direction> possibleFlowDir,
                                                                                 BlockPos pos, Level level) {
        var source = fluid.getSource();
        if (HAS_GENERATOR.contains(source)) {
            var list = level.getFluidState(pos).isSource() ? STILL_GENERATORS.get(source) : FLOWING_GENERATORS.get(source);
            return generate(possibleFlowDir, pos, level, list);
        }
        return Optional.empty();
    }

    private static Optional<Pair<BlockPos, @Nullable SoundEvent>> generate(List<Direction> possibleFlowDir, BlockPos pos, Level level, ImmutableList<FluidGenerator> list) {
        if (list != null && !list.isEmpty() && level.isAreaLoaded(pos, 3)) {
            Map<Direction, BlockState> neighborCache = new EnumMap<>(Direction.class);
            for (var generator : list) {
                var res = generator.tryGenerating(possibleFlowDir, pos, level, neighborCache);
                if (res.isPresent()) return res.map(a -> Pair.of(a, generator.getSound()));
            }
        }
        return Optional.empty();
    }
}
