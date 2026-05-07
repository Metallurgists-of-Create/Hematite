package dev.metallurgists.hematite.api.weathering.block_growths.data;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.common.weathering.block_growths.NoOpBlockGrowth;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.config.HematiteConfigs;
import dev.metallurgists.hematite.util.RegistryAccessJsonReloadListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Supplier;

public class BlockGrowthHandler extends RegistryAccessJsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); //json object that will write stuff

    public static final BlockGrowthHandler RELOAD_INSTANCE = new BlockGrowthHandler();

    //these need to be as fast as possible as tick will be called very often
    //block specific growth. fast access with map
    private static final Map<Holder<TickSource>, Map<Block, Set<BlockGrowth>>> GROWTH_FOR_BLOCK = new HashMap<>();
    //set or universal ones
    private static final Map<Holder<TickSource>, Set<BlockGrowth>> UNIVERSAL_GROWTHS = new HashMap<>();

    public BlockGrowthHandler() {
        super(GSON, "block_growths");
    }

    public static Optional<Set<BlockGrowth>> getBlockGrowths(Holder<TickSource> source, Block block) {
        return Optional.ofNullable(GROWTH_FOR_BLOCK.get(source)).map(m -> m.get(block));
    }

    public static List<BlockPos> tickBlock(Holder<TickSource> source, BlockState state, Level level, BlockPos pos) {
        if (!HematiteConfigs.SERVER.blockGrowths.get()) return List.of();

        Supplier<Holder<Biome>> biome = Suppliers.memoize(() -> level.getBiome(pos));

        var universalGroup = UNIVERSAL_GROWTHS.get(source);

        List<BlockPos> affectedBlocks = new ArrayList<>();

        if (universalGroup != null) {
            for (var config : universalGroup) {
                List<BlockPos> success = config.tryGrowing(pos, state, level, biome);
                affectedBlocks.addAll(success);
            }
        }
        var growth = getBlockGrowths(source, state.getBlock());
        if (growth.isPresent()) {
            for (var config : growth.get()) {
                List<BlockPos> success = config.tryGrowing(pos, state, level, biome);
                affectedBlocks.addAll(success);
            }
        }
        return affectedBlocks;
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> jsonMap, RegistryAccess registryAccess) {
        Map<ResourceLocation, JsonElement> map = new HashMap<>();
        for (var e : jsonMap.entrySet()) {
            map.put(e.getKey(), e.getValue().deepCopy());
        }

        List<BlockGrowth> growths = new ArrayList<>();

        for (var e : map.entrySet()) {
            var json = e.getValue();

            var result = BlockGrowth.CODEC.parse(RegistryOps.create(JsonOps.INSTANCE, registryAccess), json);
            var o = result.resultOrPartial(error -> Hematite.LOGGER.error("Failed to read block growth JSON object for {} : {}", e.getKey(), error));

            if (o.isPresent()) {
                BlockGrowth g = o.get();
                if (!(g instanceof NoOpBlockGrowth)) {
                    growths.add(g);
                }
            }
            o.ifPresent(growths::add);
        }
        Hematite.LOGGER.info("Loaded {} block growths configurations", map.size());

        GROWTH_FOR_BLOCK.clear();
        UNIVERSAL_GROWTHS.clear();

        for (var config : growths) {

            var sources = config.getTickSources();
            for (var s : sources) {
                var owners = config.getOwners();

                if (owners == null) { //null owners mean it applies to everything
                    var group = UNIVERSAL_GROWTHS.computeIfAbsent(s, e -> new HashSet<>());
                    group.add(config);
                } else {
                    var group = GROWTH_FOR_BLOCK.computeIfAbsent(s, e -> Maps.newIdentityHashMap());
                    config.getOwners().forEach(b -> {

                        group.computeIfAbsent(b, k -> new HashSet<>()).add(config);
                    });
                }

            }
        }
    }
}
