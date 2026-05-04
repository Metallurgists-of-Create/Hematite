package dev.metallurgists.hematite.api.weathering.block_growths.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.level.BlockEvent;

public class LightningStruckBlockEvent extends Event {
    private final Level level;
    private final BlockPos pos;
    private final BlockState state;
    private final LightningBolt entity;

    public LightningStruckBlockEvent(BlockState state, Level level, BlockPos pos, LightningBolt entity) {
        this.state = state;
        this.level = level;
        this.pos = pos;
        this.entity = entity;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockState getState() {
        return this.state;
    }

    public LightningBolt getEntity() {
        return this.entity;
    }
}
