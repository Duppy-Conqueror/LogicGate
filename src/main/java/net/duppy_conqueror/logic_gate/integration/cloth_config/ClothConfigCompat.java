package net.duppy_conqueror.logic_gate.integration.cloth_config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.duppy_conqueror.logic_gate.config.ModConfig;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClothConfigCompat {
    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (_, parent) -> makeScreen(parent));
    }

    public static Screen makeScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Component.translatable("logic_gate.configuration.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory gateDelayCategory = builder.getOrCreateCategory(Component.translatable("logic_gate.configuration.gateDelay"));
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.bufferModeDelay"), ModConfig.BUFFER_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.bufferModeDelay.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.BUFFER_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.notModeDelay"), ModConfig.NOT_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.notModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.NOT_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.orModeDelay"), ModConfig.OR_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.orModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.OR_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.andModeDelay"), ModConfig.AND_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.andModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.AND_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.xorModeDelay"), ModConfig.XOR_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.xorModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.XOR_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.norModeDelay"), ModConfig.NOR_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.norModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.NOR_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.nandModeDelay"), ModConfig.NAND_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.nandModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.NAND_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.implyModeDelay"), ModConfig.IMPLY_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.implyModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.IMPLY_MODE_DELAY::set)
                .build()
        );
        gateDelayCategory.addEntry(
            entryBuilder.startIntSlider(Component.translatable("logic_gate.configuration.nimplyModeDelay"), ModConfig.NIMPLY_MODE_DELAY.get(), ModConfig.MAX_DELAY, ModConfig.MIN_DELAY)
                .setTooltip(Component.translatable("logic_gate.configuration.nimplyModeDelay.tooltip"))
                .setMax(ModConfig.MAX_DELAY)
                .setMin(ModConfig.MIN_DELAY)
                .setDefaultValue(ModConfig.DEFAULT_DELAY)
                .setSaveConsumer(ModConfig.NIMPLY_MODE_DELAY::set)
                .build()
        );

        ConfigCategory redstoneWireConnectionCategory = builder.getOrCreateCategory(Component.translatable("logic_gate.configuration.redstoneWireConnection"));
        redstoneWireConnectionCategory.addEntry(
            entryBuilder.startBooleanToggle(Component.translatable("logic_gate.configuration.smartRedstoneWireConnection"), ModConfig.SMART_REDSTONE_WIRE_CONNECTION.get())
                .setTooltip(Component.translatable("logic_gate.configuration.smartRedstoneWireConnection.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_SMART_REDSTONE_WIRE_CONNECTION_ENABLED)
                .setSaveConsumer(ModConfig.SMART_REDSTONE_WIRE_CONNECTION::set)
                .build()
        );

        builder.setSavingRunnable(ModConfig.CONFIG_HOLDER::save);
        return builder.build();
    }
}
