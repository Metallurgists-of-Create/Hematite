package dev.metallurgists.hematite.integration.kubejs.event;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.api.weathering.block_growths.helpers.SkyAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class SkyTickSourceEventJS implements KubeLevelEvent {
    public final SkyAccess.TickSourceEvent event;

    public SkyTickSourceEventJS(SkyAccess.TickSourceEvent event) {
        this.event = event;
    }

    @Override
    public Level getLevel() {
        return event.level;
    }

    @Info(value = """
		    Returns the chunk containing the checked block.
		""")
    public LevelChunk getLevelChunk() {
        return event.levelChunk;
    }

    @Info(value = """
		    Returns the randomTickSpeed value of the current world.
		""")
    public int getRandomTickSpeed() {
        return event.randomTickSpeed;
    }

    @Info(value = """
		    Returns the position of the checked block.
		""")
    public BlockPos getTargetPos() {
        return event.targetPos;
    }

    public Holder<TickSource> getTickSource() {
        return event.getTickSource();
    }

    public void setTickSource(Holder<TickSource> tickSource) {
        if (tickSource != null) event.setTickSource(tickSource);
    }
}
