package dev.metallurgists.hematite.integration.kubejs.bindings;

import dev.latvian.mods.kubejs.typings.Info;

public interface HematiteBindings {

    @Info("Miscellaneous Hematite ingredient helpers")
    IngredientBindings ingredient = IngredientBindings.INSTANCE;

    @Info("Miscellaneous Block Growth helpers")
    BlockGrowthBindings blockGrowth = BlockGrowthBindings.INSTANCE;
}
