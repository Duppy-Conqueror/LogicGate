package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.*;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.Map;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        this.generateLogicGateBlockStates(blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModBlocks.LOGIC_GATE.asItem(), Models.GENERATED);
    }

    @Override
    public String getName() {
        return "ModModelProvider";
    }

    private void generateLogicGateBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        MultipartBlockStateSupplier multipartBlockStateSupplier = MultipartBlockStateSupplier.create(ModBlocks.LOGIC_GATE);

        final Map<Direction, VariantSettings.Rotation> southDefaultHorizontalRotationMap = Map.of(
            Direction.SOUTH, VariantSettings.Rotation.R0,
            Direction.WEST, VariantSettings.Rotation.R90,
            Direction.NORTH, VariantSettings.Rotation.R180,
            Direction.EAST, VariantSettings.Rotation.R270
        );

        final Map<BooleanProperty, String> poweredPropertySubtextMap = Map.of(
            LogicGateBlock.POWERED, "output",
            LogicGateBlock.BACK_POWERED, "input_back",
            LogicGateBlock.LEFT_POWERED, "input_left",
            LogicGateBlock.RIGHT_POWERED, "input_right"
        );

        // "facing" property
        for (Map.Entry<Direction, VariantSettings.Rotation> directionRotationEntry: southDefaultHorizontalRotationMap.entrySet()) {
            final When whenFacing = When.create().set(Properties.HORIZONTAL_FACING, directionRotationEntry.getKey());
            final BlockStateVariant applyFacing = BlockStateVariant.create()
                .put(VariantSettings.MODEL, TextureMap.getId(ModBlocks.LOGIC_GATE))
                .put(VariantSettings.Y, directionRotationEntry.getValue());
            multipartBlockStateSupplier = multipartBlockStateSupplier.with(whenFacing, applyFacing);

            // "mode" property
            for (LogicGateMode mode: LogicGateMode.values()) {
                final When whenMode = When.create().set(LogicGateBlock.MODE, mode);
                final When whenAnd = When.allOf(whenFacing, whenMode);
                final BlockStateVariant applyAnd = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_mode_" + mode.asString()))
                    .put(VariantSettings.Y, directionRotationEntry.getValue());
                multipartBlockStateSupplier = multipartBlockStateSupplier.with(whenAnd, applyAnd);
            }

            // "powered" properties for all IOs
            for (Map.Entry<BooleanProperty, String> poweredSubtextEntry: poweredPropertySubtextMap.entrySet()) {
                // "=false"
                final When whenIoPoweredFalse = When.create().set(poweredSubtextEntry.getKey(), false);
                final When whenAndFalse = When.allOf(whenFacing, whenIoPoweredFalse);
                final BlockStateVariant applyAndFalse = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_" + poweredSubtextEntry.getValue()))
                    .put(VariantSettings.Y, directionRotationEntry.getValue());
                multipartBlockStateSupplier = multipartBlockStateSupplier.with(whenAndFalse, applyAndFalse);

                // "=true"
                final When whenIoPoweredTrue = When.create().set(poweredSubtextEntry.getKey(), true);
                final When whenAndTrue = When.allOf(whenFacing, whenIoPoweredTrue);
                final BlockStateVariant applyAndTrue = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_" + poweredSubtextEntry.getValue() + "_powered"))
                    .put(VariantSettings.Y, directionRotationEntry.getValue());
                multipartBlockStateSupplier = multipartBlockStateSupplier.with(whenAndTrue, applyAndTrue);
            }
        }

        blockStateModelGenerator.blockStateCollector.accept(multipartBlockStateSupplier);
    }
}
