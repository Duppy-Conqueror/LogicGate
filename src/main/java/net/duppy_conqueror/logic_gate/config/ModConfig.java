package net.duppy_conqueror.logic_gate.config;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.values.BooleanModConfigValue;
import net.duppy_conqueror.logic_gate.config.values.IntegerModConfigValue;

public class ModConfig {
    public static ModConfigHolder CONFIG_HOLDER;

    public static final String CATEGORY_DELAY = "gateDelay";
    public static IntegerModConfigValue BUFFER_MODE_DELAY;
    public static IntegerModConfigValue NOT_MODE_DELAY;
    public static IntegerModConfigValue OR_MODE_DELAY;
    public static IntegerModConfigValue AND_MODE_DELAY;
    public static IntegerModConfigValue XOR_MODE_DELAY;
    public static IntegerModConfigValue NOR_MODE_DELAY;
    public static IntegerModConfigValue NAND_MODE_DELAY;
    public static IntegerModConfigValue IMPLY_MODE_DELAY;
    public static IntegerModConfigValue NIMPLY_MODE_DELAY;

    public static final int MIN_DELAY = 0;
    public static final int MAX_DELAY = 20;
    public static final int DEFAULT_DELAY = 2;

    public static final String CATEGORY_CONNECT = "redstoneWireConnection";
    public static BooleanModConfigValue SMART_REDSTONE_WIRE_CONNECTION;

    public static final boolean DEFAULT_SMART_REDSTONE_WIRE_CONNECTION_ENABLED = false;

    static {
        ModConfigBuilder builder = ModConfigBuilder.create(LogicGateMod.MOD_ID);

        builder.pushSubCategory(CATEGORY_DELAY);
        BUFFER_MODE_DELAY = builder.comment("Propagation delay for BUFFER gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("bufferModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        NOT_MODE_DELAY = builder.comment("Propagation delay for NOT gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("notModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        OR_MODE_DELAY = builder.comment("Propagation delay for OR gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("orModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        AND_MODE_DELAY = builder.comment("Propagation delay for AND gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("andModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        XOR_MODE_DELAY = builder.comment("Propagation delay for XOR gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("xorModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        NOR_MODE_DELAY = builder.comment("Propagation delay for NOR gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("norModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        NAND_MODE_DELAY = builder.comment("Propagation delay for NAND gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("nandModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        IMPLY_MODE_DELAY = builder.comment("Propagation delay for IMPLY gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("implyModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        NIMPLY_MODE_DELAY = builder.comment("Propagation delay for NIMPLY gates.\nDefault value: 2 ticks")
                .defineIntConfigValue("nimplyModeDelay", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY);
        builder.popSubCategory();

        builder.pushSubCategory(CATEGORY_CONNECT);
        SMART_REDSTONE_WIRE_CONNECTION = builder.comment("If enabled, redstone wires will only automatically connect to input sides used by the current mode.\n" +
                        "For instance, redstone wires will only automatically connect to the back input side when the logic gate is in BUFFER mode, not the left and right input sides.\n" +
                        "Note: Existing redstone wires need to receive a block update after changing this setting.")
                .defineBoolConfigValue("smartRedstoneWireConnection", DEFAULT_SMART_REDSTONE_WIRE_CONNECTION_ENABLED);
        builder.popSubCategory();

        CONFIG_HOLDER = builder.build();
        CONFIG_HOLDER.loadConfig();
    }

    public static int getDelay(LogicGateMode mode) {
        return switch (mode) {
            case BUFFER -> BUFFER_MODE_DELAY.get();
            case NOT -> NOT_MODE_DELAY.get();
            case OR -> OR_MODE_DELAY.get();
            case AND -> AND_MODE_DELAY.get();
            case XOR -> XOR_MODE_DELAY.get();
            case NOR -> NOR_MODE_DELAY.get();
            case NAND -> NAND_MODE_DELAY.get();
            case IMPLY -> IMPLY_MODE_DELAY.get();
            case NIMPLY -> NIMPLY_MODE_DELAY.get();
        };
    }

    public static boolean isSmartRedstoneConnectionEnabled() {
        return SMART_REDSTONE_WIRE_CONNECTION.get();
    }

    public static void setDelay(LogicGateMode mode, int newDelay) {
        switch (mode) {
            case BUFFER -> BUFFER_MODE_DELAY.set(newDelay);
            case NOT -> NOT_MODE_DELAY.set(newDelay);
            case OR -> OR_MODE_DELAY.set(newDelay);
            case AND -> AND_MODE_DELAY.set(newDelay);
            case XOR -> XOR_MODE_DELAY.set(newDelay);
            case NOR -> NOR_MODE_DELAY.set(newDelay);
            case NAND -> NAND_MODE_DELAY.set(newDelay);
            case IMPLY -> IMPLY_MODE_DELAY.set(newDelay);
            case NIMPLY -> NIMPLY_MODE_DELAY.set(newDelay);
        };
        CONFIG_HOLDER.saveConfig();
    }

    public static void setSmartRedstoneWireConnection(boolean b) {
        SMART_REDSTONE_WIRE_CONNECTION.set(b);
    }

    public static void init() {}
}
