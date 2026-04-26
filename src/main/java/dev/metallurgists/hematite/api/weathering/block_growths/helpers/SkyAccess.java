package dev.metallurgists.hematite.api.weathering.block_growths.helpers;

import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.api.weathering.block_growths.data.BlockGrowthHandler;
import dev.metallurgists.hematite.registry.HematiteTickSources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;

public class SkyAccess {

    public static void performSkyAccessTick(ServerLevel level, LevelChunk levelChunk, int randomTickSpeed) {
        ChunkPos chunkpos = levelChunk.getPos();
        ChanceEvent chanceEvent = NeoForge.EVENT_BUS.post(new ChanceEvent(level, levelChunk, randomTickSpeed));

        float chance = chanceEvent.getChance();
        int minX = chunkpos.getMinBlockX();
        int minZ = chunkpos.getMinBlockZ();
        do {
            if (chance > level.getRandom().nextFloat()) {
                BlockPos firstAirPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(minX, 0, minZ, 15));
                BlockPos targetPos = firstAirPos.below();
                BlockState state = level.getBlockState(targetPos);
                TickSourceEvent tickSourceEvent = NeoForge.EVENT_BUS.post(new TickSourceEvent(level, levelChunk, randomTickSpeed, targetPos));
                BlockGrowthHandler.tickBlock(tickSourceEvent.getTickSource(), state, level, new BlockPos(targetPos));
            }
            chance--;
        } while (chance > 0 && !chanceEvent.isCanceled());
    }

    public static class ChanceEvent extends Event implements ICancellableEvent {
        public final ServerLevel level;
        public final LevelChunk levelChunk;
        public final int randomTickSpeed;

        public float chance;

        public ChanceEvent(ServerLevel level, LevelChunk levelChunk, int randomTickSpeed) {
            this.level = level;
            this.levelChunk = levelChunk;
            this.randomTickSpeed = randomTickSpeed;
            this.chance = this.randomTickSpeed / (3f * 16f);
        }

        public void setChance(float chance) {
            this.chance = chance;
        }

        public float getChance() {
            return this.chance;
        }
    }

    public static class TickSourceEvent extends Event implements ICancellableEvent {
        public final ServerLevel level;
        public final LevelChunk levelChunk;
        public final int randomTickSpeed;
        public final BlockPos targetPos;

        public Holder<TickSource> tickSource = HematiteTickSources.CLEAR_SKY;

        public TickSourceEvent(ServerLevel level, LevelChunk levelChunk, int randomTickSpeed, BlockPos targetPos) {
            this.level = level;
            this.levelChunk = levelChunk;
            this.randomTickSpeed = randomTickSpeed;
            this.targetPos = targetPos;
        }

        public void setTickSource(Holder<TickSource> tickSource) {
            this.tickSource = tickSource;
        }

        public Holder<TickSource> getTickSource() {
            return this.tickSource;
        }
    }
}
