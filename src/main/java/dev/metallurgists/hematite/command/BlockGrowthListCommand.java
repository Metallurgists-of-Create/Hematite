package dev.metallurgists.hematite.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.metallurgists.hematite.api.weathering.block_growths.data.BlockGrowthHandler;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;

public class BlockGrowthListCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext context) {
        return Commands.literal("list")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("block_growths").executes(BlockGrowthListCommand::all)
                        .then(Commands.argument("filter", ResourceOrTagArgument.resourceOrTag(context, Registries.BLOCK)).executes(BlockGrowthListCommand::filtered)));
    }

    private static int all(CommandContext<CommandSourceStack> ctx) {
        int size = BlockGrowthHandler.registeredGrowths(null);
        sendSuccess(ctx.getSource(), Component.translatable("hematite.command.block_growths.all.success", size));
        return 1;
    }

    private static int filtered(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var filter = ResourceOrTagArgument.getResourceOrTag(ctx, "filter", Registries.BLOCK);
        int size = BlockGrowthHandler.registeredGrowths(filter.unwrap());
        sendSuccess(ctx.getSource(), Component.translatable("hematite.command.block_growths.filtered.success", size, filter.asPrintable()));
        return 1;
    }

    private static void sendSuccess(CommandSourceStack source, Component text) {
        source.sendSuccess(() -> text, true);
    }
}
