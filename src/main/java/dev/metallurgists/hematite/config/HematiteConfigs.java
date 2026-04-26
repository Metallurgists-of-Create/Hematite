package dev.metallurgists.hematite.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class HematiteConfigs {

    public static class Server {
        public final ModConfigSpec.BooleanValue blockGrowths;
        public final ModConfigSpec.ConfigValue<List<String>> disabledGrowths;

        Server(ModConfigSpec.Builder builder) {
            builder.push("general");
            blockGrowths = builder.define("block_growths", true);
            disabledGrowths = builder
                    .comment("put here the name of a block growth json you want to disable i.e: [hematite:weeds, hematite:weeds_spread].\nNote that this is not the preferred way to do this as block growths are all data driven so it would be best to disable or tweak them by creating a datapack that overrides them\nCheck the mod data folder for the required names. Requires resource reload (/data reload)")
                    .translation("fell.configgui.block_growth_blacklist")
                    .define("block_growth_blacklist", new ArrayList<>());
        }
    }

    public static final ModConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }
}
