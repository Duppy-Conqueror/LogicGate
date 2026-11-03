package net.duppy_conqueror.logic_gate.integration.jade.providers;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class LogicGateBlockProvider implements IBlockComponentProvider {
    public static LogicGateBlockProvider INSTANCE = new LogicGateBlockProvider();
    public static Identifier UID = Identifier.fromNamespaceAndPath(LogicGateMod.MOD_ID, "logic_gate");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        final LogicGateMode mode = blockAccessor.getBlockState().getValue(LogicGateBlock.MODE);
        final Component modeComponent = Component.translatable("logic_gate.jade.text.logic_gate.mode." + mode.getSerializedName());
        iTooltip.add(Component.translatable("logic_gate.jade.text.logic_gate.mode", modeComponent));
    }

    @Override
    public Identifier getUid() {
        return UID;
    }

    private LogicGateBlockProvider() {

    }
}
