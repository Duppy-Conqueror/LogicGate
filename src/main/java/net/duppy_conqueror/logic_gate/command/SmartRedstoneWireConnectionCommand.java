package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SmartRedstoneWireConnectionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> argBuilder = literal(LogicGateMod.MOD_ID)
            .then(literal("smartRedstoneWireConnection")
                .then(SmartRedstoneWireConnectionCommand.buildQueryCommand())
                .then(SmartRedstoneWireConnectionCommand.buildSetCommand())
            );
        dispatcher.register(argBuilder);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildQueryCommand() {
        // /logic_gate smartRedstoneWireConnection query
        return literal("query")
            .executes((CommandContext<ServerCommandSource> context) -> {
                executeQueryCommand(context);
                return SINGLE_SUCCESS;
            }
        );
    }
    private static LiteralArgumentBuilder<ServerCommandSource> buildSetCommand() {
        // /logic_gate smartRedstoneWireConnection set [true|false|default]
        return literal("set")
            .requires((ServerCommandSource source) -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
            .then(argument("enabled", BoolArgumentType.bool())
                .executes((CommandContext<ServerCommandSource> context) -> {
                    executeSetCommand(context, BoolArgumentType.getBool(context, "enabled"));
                    return SINGLE_SUCCESS;
                })
            )
            .then(literal("default")
                .executes((CommandContext<ServerCommandSource> context) -> {
                    executeSetCommand(context, ModConfig.DEFAULT_SMART_REDSTONE_WIRE_CONNECTION_ENABLED);
                    return SINGLE_SUCCESS;
                })
            );
    }

    private static final String queryCommandFeedBackTemplateKey = "command." + LogicGateMod.MOD_ID + ".smartRedstoneWireConnection.query.feedback.%s";
    private static final String setCommandFeedBackTemplateKey =  "command." + LogicGateMod.MOD_ID + ".smartRedstoneWireConnection.set.feedback.%s";

    private static void executeQueryCommand(CommandContext<ServerCommandSource> context) {
        final boolean enabled = ModConfig.isSmartRedstoneConnectionEnabled();
        context.getSource().sendFeedback(() -> Text.translatable(queryCommandFeedBackTemplateKey.formatted(enabled)), false);
    }

    private static void executeSetCommand(CommandContext<ServerCommandSource> context, boolean newValue) {
        if (newValue == ModConfig.isSmartRedstoneConnectionEnabled()) {
            return;
        }
        ModConfig.setSmartRedstoneWireConnection(newValue);
        context.getSource().sendFeedback(() -> Text.translatable(setCommandFeedBackTemplateKey.formatted(newValue)), true);
    }
}
