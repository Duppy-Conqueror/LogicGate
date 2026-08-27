package net.duppy_conqueror.logic_gate.command;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {
    @SubscribeEvent
    public static void init(RegisterCommandsEvent event) {
        LogicGate.LOGGER.info("Registering commands for " + LogicGate.MOD_ID);
        GateDelayCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        SmartRedstoneWireConnectionCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}
