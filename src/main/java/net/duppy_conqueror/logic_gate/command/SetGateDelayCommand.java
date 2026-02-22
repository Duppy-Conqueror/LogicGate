package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.mojang.brigadier.context.CommandContext;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.server.command.CommandManager.*;


public class SetGateDelayCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> argBuilder = literal("setLogicGateDelay")
            .requires((ServerCommandSource source) -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)));

        for (LogicGateMode mode: LogicGateMode.values()) {
            argBuilder = argBuilder.then(literal(mode.asString())
                .then(
                    argument("delay", IntegerArgumentType.integer(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)).executes(
                        (CommandContext<ServerCommandSource> context) -> {
                            executeCommand(context, mode, IntegerArgumentType.getInteger(context, "delay"));
                            return SINGLE_SUCCESS;
                        }
                    )
                )
                .then(
                    literal("default").executes(
                        (CommandContext<ServerCommandSource> context) -> {
                            executeCommand(context, mode, ModConfig.DEFAULT_DELAY);
                            return SINGLE_SUCCESS;
                        }
                    )
                )
            );
        }

        dispatcher.register(argBuilder);
    }

    private static final String commandFeedBackTemplateKey = "logic_gate.command.setGateDelay.feedback.%s";

    private static void executeCommand(CommandContext<ServerCommandSource> context, LogicGateMode mode, int newDelay) {
        ModConfig.setDelay(mode, newDelay);
        context.getSource().sendFeedback(() -> Text.translatable(commandFeedBackTemplateKey.formatted(mode.asString()), newDelay), true);
    }
}
