package net.duppy_conqueror.logic_gate.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(SetGateDelayCommand::register);
    }
}
