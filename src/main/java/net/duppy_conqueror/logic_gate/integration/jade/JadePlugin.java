package net.duppy_conqueror.logic_gate.integration.jade;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.integration.jade.providers.LogicGateBlockProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class JadePlugin implements IWailaPlugin {
    public void register(IWailaCommonRegistration registration) {
        IWailaPlugin.super.register(registration);
    }

    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(LogicGateBlockProvider.INSTANCE, LogicGateBlock.class);
        IWailaPlugin.super.registerClient(registration);
    }
}
