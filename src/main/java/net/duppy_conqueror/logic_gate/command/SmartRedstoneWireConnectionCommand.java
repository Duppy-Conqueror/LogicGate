package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.function.Predicate;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SmartRedstoneWireConnectionCommand {
    public static final Predicate<CommandSourceStack> PERMISSION_CHECK;

    static {
        PERMISSION_CHECK = (commandSourceStack) -> commandSourceStack.hasPermission(2);;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        LiteralArgumentBuilder<CommandSourceStack> argBuilder = literal(LogicGate.MOD_ID)
            .then(literal("smartRedstoneWireConnection")
                .then(SmartRedstoneWireConnectionCommand.buildQueryCommand())
                .then(SmartRedstoneWireConnectionCommand.buildSetCommand())
            );
        dispatcher.register(argBuilder);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildQueryCommand() {
        // /logic_gate smartRedstoneWireConnection query
        return literal("query")
            .executes((CommandContext<CommandSourceStack> context) -> {
                executeQueryCommand(context);
                return SINGLE_SUCCESS;
            }
        );
    }
    private static LiteralArgumentBuilder<CommandSourceStack> buildSetCommand() {
        // /logic_gate smartRedstoneWireConnection set [true|false|default]
        return literal("set")
            .requires(PERMISSION_CHECK)
            .then(argument("enabled", BoolArgumentType.bool())
                .executes((CommandContext<CommandSourceStack> context) -> {
                    executeSetCommand(context, BoolArgumentType.getBool(context, "enabled"));
                    return SINGLE_SUCCESS;
                })
            )
            .then(literal("default")
                .executes((CommandContext<CommandSourceStack> context) -> {
                    executeSetCommand(context, ModConfig.DEFAULT_SMART_REDSTONE_WIRE_CONNECTION_ENABLED);
                    return SINGLE_SUCCESS;
                })
            );
    }

    private static final String queryCommandFeedBackTemplateKey = "command." + LogicGate.MOD_ID + ".smartRedstoneWireConnection.query.feedback.%s";
    private static final String setCommandFeedBackTemplateKey =  "command." + LogicGate.MOD_ID + ".smartRedstoneWireConnection.set.feedback.%s";

    private static void executeQueryCommand(CommandContext<CommandSourceStack> context) {
        final boolean enabled = ModConfig.isSmartRedstoneConnectionEnabled();
        context.getSource().sendSuccess(() -> Component.translatable(queryCommandFeedBackTemplateKey.formatted(enabled)), false);
    }

    private static void executeSetCommand(CommandContext<CommandSourceStack> context, boolean newValue) {
        if (newValue == ModConfig.isSmartRedstoneConnectionEnabled()) {
            return;
        }
        ModConfig.setSmartRedstoneWireConnection(newValue);
        context.getSource().sendSuccess(() -> Component.translatable(setCommandFeedBackTemplateKey.formatted(newValue)), true);
    }
}
