package dev.metallurgists.hematite.integration.kubejs.builder;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import net.minecraft.resources.ResourceLocation;

@ReturnsSelf
public class TickSourceJSBuilder extends BuilderBase<TickSource> {

    public TickSourceJSBuilder(ResourceLocation sourceId) {
        super(sourceId);
    }

    @Override
    public TickSource createObject() {
        return new TickSource();
    }
}
