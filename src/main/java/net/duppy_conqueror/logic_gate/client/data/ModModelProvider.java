package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.item.ModItems;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;
import java.util.Map;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, LogicGate.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.generateBlockStateModels(blockModels);
        this.generateItemModels(itemModels);
    }

    public void generateBlockStateModels(BlockModelGenerators generators) {
        this.generateLogicGateBlockStates(generators);
    }

    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModItems.LOGIC_GATE.get(), ModelTemplates.FLAT_ITEM);
    }

    @Override
    public String getName() {
        return "ModModelProvider";
    }

    private void generateLogicGateBlockStates(BlockModelGenerators generators) {
        Block block = ModBlocks.LOGIC_GATE.get();

        MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);

        final Map<Direction, VariantMutator> southDefaultHorizontalRotationMap = Map.of(
            Direction.SOUTH, NOP,
            Direction.WEST, Y_ROT_90,
            Direction.NORTH, Y_ROT_180,
            Direction.EAST, Y_ROT_270
        );

        final Map<BooleanProperty, String> poweredPropertySubtextMap = Map.of(
            LogicGateBlock.POWERED, "output",
            LogicGateBlock.BACK_POWERED, "input_back",
            LogicGateBlock.LEFT_POWERED, "input_left",
            LogicGateBlock.RIGHT_POWERED, "input_right"
        );

        // "facing" property
        for (Map.Entry<Direction, VariantMutator> directionRotationEntry: southDefaultHorizontalRotationMap.entrySet()) {
            final Condition whenFacing = condition().term(BlockStateProperties.HORIZONTAL_FACING, directionRotationEntry.getKey()).build();
            final MultiVariant applyFacing = plainVariant(ModelLocationUtils.getModelLocation(block)).with(directionRotationEntry.getValue());
            multiPartGenerator.with(whenFacing, applyFacing);

            // "mode" property
            for (LogicGateMode mode: LogicGateMode.values()) {
                final Identifier modelId = ModelLocationUtils.getModelLocation(block, "_mode_" + mode.getSerializedName());
                final Condition whenMode = condition().term(LogicGateBlock.MODE, mode).build();
                final CombinedCondition whenAnd = new CombinedCondition(CombinedCondition.Operation.AND, List.of(whenFacing, whenMode));
                final MultiVariant applyAndMode = plainVariant(modelId).with(directionRotationEntry.getValue());
                multiPartGenerator.with(whenAnd, applyAndMode);
            }

            // "powered" properties for all IOs
            for (Map.Entry<BooleanProperty, String> poweredSubtextEntry: poweredPropertySubtextMap.entrySet()) {
                // "=false"
                final Identifier falseModelId = ModelLocationUtils.getModelLocation(block, "_" + poweredSubtextEntry.getValue());
                final Condition whenIoPoweredFalse = condition().term(poweredSubtextEntry.getKey(), false).build();
                final CombinedCondition whenAndFalse = new CombinedCondition(CombinedCondition.Operation.AND, List.of(whenFacing, whenIoPoweredFalse));
                final MultiVariant applyAndFalse = plainVariant(falseModelId).with(directionRotationEntry.getValue());
                multiPartGenerator.with(whenAndFalse, applyAndFalse);

                // "=true"
                final Identifier trueModelId = ModelLocationUtils.getModelLocation(block, "_" + poweredSubtextEntry.getValue() + "_powered");
                final Condition whenIoPoweredTrue = condition().term(poweredSubtextEntry.getKey(), true).build();
                final CombinedCondition whenAndTrue = new CombinedCondition(CombinedCondition.Operation.AND, List.of(whenFacing, whenIoPoweredTrue));
                final MultiVariant applyAndTrue = plainVariant(trueModelId).with(directionRotationEntry.getValue());
                multiPartGenerator.with(whenAndTrue, applyAndTrue);
            }
        }

        generators.blockStateOutput.accept(multiPartGenerator);
    }
}
