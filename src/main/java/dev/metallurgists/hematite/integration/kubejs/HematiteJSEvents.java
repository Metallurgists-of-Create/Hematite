package dev.metallurgists.hematite.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.api.weathering.block_growths.events.LightningStruckBlockEvent;
import dev.metallurgists.hematite.api.weathering.block_growths.helpers.SkyAccess;
import dev.metallurgists.hematite.integration.kubejs.event.HematiteDataEventJS;
import dev.metallurgists.hematite.integration.kubejs.event.LightningStruckBlockEventJS;
import dev.metallurgists.hematite.integration.kubejs.event.SkyTickSourceEventJS;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Hematite.ID)
public class HematiteJSEvents {
    public static final EventGroup GROUP = EventGroup.of("HematiteEvents");

    public static final EventHandler skyTickSource = GROUP.server("skyTickSource", () -> SkyTickSourceEventJS.class);
    public static final EventHandler data = GROUP.server("data", () -> HematiteDataEventJS.class);
    public static final EventHandler lightningStruckBlock = GROUP.server("lightningStruckBlock", () -> LightningStruckBlockEventJS.class);

    @SubscribeEvent
    public static void skyTickSource(SkyAccess.TickSourceEvent event) {
        if (skyTickSource.hasListeners()) {
            skyTickSource.post(new SkyTickSourceEventJS(event)).applyCancel(event);
        }
    }

    @SubscribeEvent
    public static void lightningStruckBlock(LightningStruckBlockEvent event) {
        if (lightningStruckBlock.hasListeners()) {
            lightningStruckBlock.post(new LightningStruckBlockEventJS(event.getState(), event.getLevel(), event.getPos(), event.getEntity()));
        }
    }
}
