package dev.metallurgists.hematite;

import dev.metallurgists.hematite.config.HematiteConfigs;
import dev.metallurgists.hematite.event.CommonEvents;
import dev.metallurgists.hematite.registry.*;
import dev.metallurgists.hematite.util.ModUtils;
import dev.metallurgists.hematite.util.RegistryAccessJsonReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Hematite.ID)
public class Hematite {
    private static Hematite INSTANCE;
    public static final String ID = "hematite";
    public static final Logger LOGGER = LogManager.getLogger("Hematite");
    private final IEventBus modEventBus;

    public Hematite(IEventBus modEventBus, ModContainer modContainer) {
        this.modEventBus = modEventBus;
        INSTANCE = this;
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new CommonEvents());

        HematiteAreaConditionTypes.staticInit();
        HematiteBlockGrowthTypes.staticInit();
        HematiteFluidGeneratorTypes.staticInit();
        HematitePositionTestTypes.staticInit();
        HematiteRuleTestTypes.staticInit();
        HematiteTickSources.staticInit();

        NeoForge.EVENT_BUS.addListener(TagsUpdatedEvent.class, (event) -> afterDataReloadOrDataSync(event.getRegistryAccess()));

        modContainer.registerConfig(ModConfig.Type.SERVER, HematiteConfigs.serverSpec);
    }

    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static <T> void logRegistry(ResourceKey<Registry<T>> key) {
        LOGGER.info("[Hematite] {} Registry", ModUtils.toEng(key.location()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    private static void afterDataReloadOrDataSync(RegistryAccess registryAccess) {
        RegistryAccessJsonReloadListener.runReloads(registryAccess);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
