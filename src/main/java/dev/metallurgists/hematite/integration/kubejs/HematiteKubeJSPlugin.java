package dev.metallurgists.hematite.integration.kubejs;


import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.metallurgists.hematite.Hematite;
import dev.metallurgists.hematite.HematiteRegistries;
import dev.metallurgists.hematite.api.area_condition.AreaCondition;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.weathering.block_growths.BlockGrowth;
import dev.metallurgists.hematite.api.weathering.block_growths.TickSource;
import dev.metallurgists.hematite.integration.kubejs.builder.TickSourceJSBuilder;

public class HematiteKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void init() {
        Hematite.LOGGER.debug("Initiating KubeJS integration for Hematite");
    }

    //This motherfucker doesn't actually work...
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.addDefault(HematiteRegistries.TICK_SOURCE, TickSourceJSBuilder.class, TickSourceJSBuilder::new);
    }

    @Override
    public void registerBindings(BindingRegistry event) {
        event.add("TickSource", TickSource.class);
        event.add("AreaCondition", AreaCondition.class);
        event.add("PositionTest", PositionTest.class);
        event.add("BlockGrowth", BlockGrowth.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(HematiteJSEvents.GROUP);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        var hematite = registry.namespace("hematite");
    }
}
