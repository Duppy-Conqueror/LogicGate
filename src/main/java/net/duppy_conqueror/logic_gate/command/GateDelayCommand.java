package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.mojang.brigadier.context.CommandContext;
import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.server.command.CommandManager.*;


public class GateDelayCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> argBuilder = literal(LogicGateMod.MOD_ID)
            .then(literal("gateDelay")
                .then(buildQueryCommand())
                .then(buildSetCommand())
            );
        dispatcher.register(argBuilder);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildQueryCommand() {
        // /logic_gate gateDelay query [buffer|not|or|and|xor|nor|nand|imply|nimply]
        LiteralArgumentBuilder<ServerCommandSource> argBuilder = literal("query");
        for (LogicGateMode mode: LogicGateMode.values()) {
            argBuilder = argBuilder.then(literal(mode.asString())
                .executes((CommandContext<ServerCommandSource> context) -> {
                    executeQueryCommand(context, mode);
                    return SINGLE_SUCCESS;
                })
            );
        }
        return argBuilder;
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildSetCommand() {
        // /logic_gate gateDelay set [buffer|not|or|and|xor|nor|nand|imply|nimply] [0-20|default]
        LiteralArgumentBuilder<ServerCommandSource> argBuilder = literal("set")
            .requires((ServerCommandSource source) -> source.hasPermissionLevel(2));
        for (LogicGateMode mode: LogicGateMode.values()) {
            argBuilder = argBuilder.then(literal(mode.asString())
                .then(argument("delay", IntegerArgumentType.integer(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY))
                    .executes((CommandContext<ServerCommandSource> context) -> {
                            executeSetCommand(context, mode, IntegerArgumentType.getInteger(context, "delay"));
                            return SINGLE_SUCCESS;
                    })
                )
                .then(literal("default")
                    .executes((CommandContext<ServerCommandSource> context) -> {
                        executeSetCommand(context, mode, ModConfig.DEFAULT_DELAY);
                        return SINGLE_SUCCESS;
                    })
                )
            );
        }
        return argBuilder;
    }

    private static final String queryCommandFeedbackTemplateKey = "command." + LogicGateMod.MOD_ID + ".gateDelay.query.feedback.%s";
    private static final String setCommandFeedbackTemplateKey = "command." + LogicGateMod.MOD_ID + ".gateDelay.set.feedback.%s";

    private static void executeQueryCommand(CommandContext<ServerCommandSource> context, LogicGateMode mode) {
        int delay = ModConfig.getDelay(mode);
        context.getSource().sendFeedback(() -> Text.translatable(queryCommandFeedbackTemplateKey.formatted(mode.asString()), delay), false);
    }

    private static void executeSetCommand(CommandContext<ServerCommandSource> context, LogicGateMode mode, int newDelay) {
        if (newDelay == ModConfig.getDelay(mode)) {
            return;
        }
        ModConfig.setDelay(mode, newDelay);
        context.getSource().sendFeedback(() -> Text.translatable(setCommandFeedbackTemplateKey.formatted(mode.asString()), newDelay), true);
    }
}
