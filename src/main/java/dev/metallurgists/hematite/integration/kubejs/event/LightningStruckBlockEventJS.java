package dev.metallurgists.hematite.integration.kubejs.event;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightningStruckBlockEventJS implements KubeLevelEvent {
    public final BlockState state;
    public final Level level;
    public final BlockPos pos;
    public final LightningBolt entity;


    public LightningStruckBlockEventJS(BlockState state, Level level, BlockPos pos, LightningBolt entity) {
        this.state = state;
        this.level = level;
        this.pos = pos;
        this.entity = entity;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockState getState() {
        return this.state;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    public LightningBolt getEntity() {
        return this.entity;
    }
}
