package dev.metallurgists.hematite.integration.kubejs.event;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface KubeBlockEvent extends KubeLevelEvent {
    BlockPos getPos();
    BlockState getState();

}
