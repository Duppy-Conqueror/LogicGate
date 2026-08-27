package net.duppy_conqueror.logic_gate.integration.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.duppy_conqueror.logic_gate.config.ModConfig;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class YACLCompat {
    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (_, parent) -> makeScreen(parent));
    }

    public static Screen makeScreen(Screen parent) {
        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();
        builder.title(Component.translatable("logic_gate.configuration.title"));
        builder.save(ModConfig.CONFIG_HOLDER::save);

        ConfigCategory.Builder gateDelayCategory = ConfigCategory.createBuilder().name(Component.translatable("logic_gate.configuration.gateDelay"));
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.bufferModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.bufferModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.BUFFER_MODE_DELAY, ModConfig.BUFFER_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.notModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.notModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.NOT_MODE_DELAY, ModConfig.NOT_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.orModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.orModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.OR_MODE_DELAY, ModConfig.OR_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.andModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.andModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.AND_MODE_DELAY, ModConfig.AND_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.xorModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.xorModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.XOR_MODE_DELAY, ModConfig.XOR_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.norModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.norModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.NOR_MODE_DELAY, ModConfig.NOR_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.nandModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.nandModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.NAND_MODE_DELAY, ModConfig.NAND_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.implyModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.implyModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.IMPLY_MODE_DELAY, ModConfig.IMPLY_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );
        gateDelayCategory.option(
            Option.<Integer>createBuilder().name(Component.translatable("logic_gate.configuration.nimplyModeDelay"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.nimplyModeDelay.tooltip")))
                .binding(ModConfig.DEFAULT_DELAY, ModConfig.NIMPLY_MODE_DELAY, ModConfig.NIMPLY_MODE_DELAY::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(ModConfig.MIN_DELAY, ModConfig.MAX_DELAY)
                    .step(1)
                ).build()
        );

        ConfigCategory.Builder redstoneWireConnectionCategory = ConfigCategory.createBuilder().name(Component.translatable("logic_gate.configuration.redstoneWireConnection"));
        redstoneWireConnectionCategory.option(
            Option.<Boolean>createBuilder().name(Component.translatable("logic_gate.configuration.smartRedstoneWireConnection"))
                .description(OptionDescription.of(Component.translatable("logic_gate.configuration.smartRedstoneWireConnection.tooltip")))
                .binding(ModConfig.DEFAULT_SMART_REDSTONE_WIRE_CONNECTION_ENABLED, ModConfig.SMART_REDSTONE_WIRE_CONNECTION, ModConfig.SMART_REDSTONE_WIRE_CONNECTION::set)
                .controller(TickBoxControllerBuilder::create)
                .build()
        );

        builder.category(gateDelayCategory.build());
        builder.category(redstoneWireConnectionCategory.build());

        return builder.build().generateScreen(parent);
    }
}
