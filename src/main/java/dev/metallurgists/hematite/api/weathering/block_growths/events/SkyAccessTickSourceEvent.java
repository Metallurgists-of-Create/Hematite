package dev.metallurgists.hematite.api.weathering.block_growths.events;

import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.registry.HematiteTickSources;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class SkyAccessTickSourceEvent extends Event implements ICancellableEvent {
    public ServerLevel level; public LevelChunk levelChunk; public int randomTickSpeed;

    public float chance = this.randomTickSpeed / (3f * 16f);
    public Holder<TickSource> tickSource = HematiteTickSources.CLEAR_SKY;

    public SkyAccessTickSourceEvent(ServerLevel level, LevelChunk levelChunk, int randomTickSpeed) {
        this.level = level;
        this.levelChunk = levelChunk;
        this.randomTickSpeed = randomTickSpeed;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public float getChance() {
        return this.chance;
    }

    public void setTickSource(Holder<TickSource> tickSource) {
        this.tickSource = tickSource;
    }

    public Holder<TickSource> getTickSource() {
        return this.tickSource;
    }
}
