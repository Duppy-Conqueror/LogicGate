package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.mojang.brigadier.context.CommandContext;
import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
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


public class GateDelayCommand {
    public static final PermissionCheck PERMISSION_CHECK;

    static {
        PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        LiteralArgumentBuilder<CommandSourceStack> argBuilder = literal(LogicGateMod.MOD_ID)
            .then(literal("gateDelay")
                .then(buildQueryCommand())
                .then(buildSetCommand())
            );
        dispatcher.register(argBuilder);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildQueryCommand() {
        // /logic_gate gateDelay query [buffer|not|or|and|xor|nor|nand|imply|nimply]
        LiteralArgumentBuilder<CommandSourceStack> argBuilder = literal("query");
        for (LogicGateMode mode: LogicGateMode.values()) {
            argBuilder = argBuilder.then(literal(mode.getSerializedName())
                .executes((CommandContext<CommandSourceStack> context) -> {
                    executeQueryCommand(context, mode);
                    return SINGLE_SUCCESS;
                })
            );
        }
        return argBuilder;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildSetCommand() {
        // /logic_gate gateDelay set [buffer|not|or|and|xor|nor|nand|imply|nimply] [0-20|default]
        LiteralArgumentBuilder<CommandSourceStack> argBuilder = literal("set")
            .requires(Commands.hasPermission(PERMISSION_CHECK));
        for (LogicGateMode mode: LogicGateMode.values()) {
            argBuilder = argBuilder.then(literal(mode.getSerializedName())
                .then(argument("delay", IntegerArgumentType.integer(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY))
                    .executes((CommandContext<CommandSourceStack> context) -> {
                            executeSetCommand(context, mode, IntegerArgumentType.getInteger(context, "delay"));
                            return SINGLE_SUCCESS;
                    })
                )
                .then(literal("default")
                    .executes((CommandContext<CommandSourceStack> context) -> {
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

    private static void executeQueryCommand(CommandContext<CommandSourceStack> context, LogicGateMode mode) {
        int delay = ModConfig.getDelay(mode);
        context.getSource().sendSuccess(() -> Component.translatable(queryCommandFeedbackTemplateKey.formatted(mode.getSerializedName()), delay), false);
    }

    private static void executeSetCommand(CommandContext<CommandSourceStack> context, LogicGateMode mode, int newDelay) {
        if (newDelay == ModConfig.getDelay(mode)) {
            return;
        }
        ModConfig.setDelay(mode, newDelay);
        context.getSource().sendSuccess(() -> Component.translatable(setCommandFeedbackTemplateKey.formatted(mode.getSerializedName()), newDelay), true);
    }
}
