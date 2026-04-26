package dev.metallurgists.hematite;

import com.mojang.logging.LogUtils;
import dev.metallurgists.hematite.config.HematiteConfigs;
import dev.metallurgists.hematite.registry.*;
import dev.metallurgists.hematite.util.ModUtils;
import dev.metallurgists.hematite.util.RegistryAccessJsonReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
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
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        HematiteAreaConditionTypes.staticInit();
        HematiteBlockGrowthTypes.staticInit();
        HematiteFluidGeneratorTypes.staticInit();
        HematitePositionTestTypes.staticInit();
        HematiteRuleTestTypes.staticInit();
        HematiteTickSources.staticInit();

        NeoForge.EVENT_BUS.addListener(TagsUpdatedEvent.class, (event) -> afterDataReloadOrDataSync(event.getRegistryAccess(), event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED));

        modContainer.registerConfig(ModConfig.Type.SERVER, HematiteConfigs.serverSpec);
    }

    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static <T> void logRegistry(ResourceKey<Registry<T>> key) {
        LOGGER.info("[Hematite] {} Registry", ModUtils.toEng(key.location()));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    private static void afterDataReloadOrDataSync(RegistryAccess registryAccess, boolean client) {
        RegistryAccessJsonReloadListener.runReloads(registryAccess);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
