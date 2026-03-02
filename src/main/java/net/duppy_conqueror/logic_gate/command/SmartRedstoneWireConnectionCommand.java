package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SmartRedstoneWireConnectionCommand {
    public static final PermissionCheck PERMISSION_CHECK;

    static {
        PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        LiteralArgumentBuilder<CommandSourceStack> argBuilder = literal(LogicGateMod.MOD_ID)
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
            .requires(Commands.hasPermission(PERMISSION_CHECK))
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

    private static final String queryCommandFeedBackTemplateKey = "command." + LogicGateMod.MOD_ID + ".smartRedstoneWireConnection.query.feedback.%s";
    private static final String setCommandFeedBackTemplateKey =  "command." + LogicGateMod.MOD_ID + ".smartRedstoneWireConnection.set.feedback.%s";

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
