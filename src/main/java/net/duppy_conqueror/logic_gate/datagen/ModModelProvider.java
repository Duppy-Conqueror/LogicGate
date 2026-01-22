package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

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

    private void generateLogicGateBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(ModBlocks.LOGIC_GATE)
                .coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates())
                .coordinate(BlockStateVariantMap.create(LogicGateBlock.MODE, Properties.POWERED)
                        .register((mode, powered) ->
                                BlockStateVariant.create().put(VariantSettings.MODEL, TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_" + mode.asString() + (powered ? "_on" : "")))
                        )
                )
        );
    }
}
