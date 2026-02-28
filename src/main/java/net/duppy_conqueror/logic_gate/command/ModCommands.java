package net.duppy_conqueror.logic_gate.command;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void init() {
        LogicGateMod.LOGGER.info("Registering commands for " + LogicGateMod.MOD_ID);
        CommandRegistrationCallback.EVENT.register(GateDelayCommand::register);
        CommandRegistrationCallback.EVENT.register(SmartRedstoneWireConnectionCommand::register);
    }
}
