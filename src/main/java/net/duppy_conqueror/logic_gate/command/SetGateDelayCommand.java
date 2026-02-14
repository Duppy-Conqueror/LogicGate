package net.duppy_conqueror.logic_gate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.mojang.brigadier.context.CommandContext;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.server.command.CommandManager.*;


public class SetGateDelayCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> argBuilder = literal("setLogicGateDelay").requires((ServerCommandSource source) -> source.hasPermissionLevel(2));

        for (LogicGateMode mode: LogicGateMode.values()) {
            argBuilder = argBuilder.then(
                literal(mode.asString()).then(argument("delay", IntegerArgumentType.integer(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)).executes(
                    (CommandContext<ServerCommandSource> context) -> {
                        final int newDelay = IntegerArgumentType.getInteger(context, "delay");
                        ModConfig.setDelay(mode, newDelay);
                        context.getSource().sendFeedback(() -> Text.translatable("logic_gate.command.setGateDelay.feedback.%s".formatted(mode.asString()), newDelay), true);
                        return SINGLE_SUCCESS;
                    }
                ))
            );
        }

        dispatcher.register(argBuilder);
    }
}
