package net.duppy_conqueror.logic_gate.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;

@Config(name = LogicGateMod.MOD_ID)
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded()
    public static final int MIN_DELAY = 0;

    @ConfigEntry.Gui.Excluded()
    public static final int MAX_DELAY = 20;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int bufferModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int notModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int orModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int andModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int xorModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int norModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int nandModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int implyModeDelay = 2;

    @ConfigEntry.BoundedDiscrete(min = MIN_DELAY, max = MAX_DELAY)
    @ConfigEntry.Gui.Tooltip()
    private int nimplyModeDelay = 2;

    public static void init() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public static int getDelay(LogicGateMode mode) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        return switch (mode) {
            case BUFFER -> config.bufferModeDelay;
            case NOT -> config.notModeDelay;
            case OR -> config.orModeDelay;
            case AND -> config.andModeDelay;
            case XOR -> config.xorModeDelay;
            case NOR -> config.norModeDelay;
            case NAND -> config.nandModeDelay;
            case IMPLY -> config.implyModeDelay;
            case NIMPLY -> config.nimplyModeDelay;
        };
    }

    public static void setDelay(LogicGateMode mode, int newDelay) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        switch (mode) {
            case BUFFER -> config.bufferModeDelay = newDelay;
            case NOT -> config.notModeDelay = newDelay;
            case OR -> config.orModeDelay = newDelay;
            case AND -> config.andModeDelay = newDelay;
            case XOR -> config.xorModeDelay = newDelay;
            case NOR -> config.norModeDelay = newDelay;
            case NAND -> config.nandModeDelay = newDelay;
            case IMPLY -> config.implyModeDelay = newDelay;
            case NIMPLY -> config.nimplyModeDelay = newDelay;
        };
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
