package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.MultipartModelCombinedCondition;
import net.minecraft.client.render.model.json.MultipartModelCondition;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Map;

import static net.minecraft.client.data.BlockStateModelGenerator.*;

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
        Block block = ModBlocks.LOGIC_GATE;

        MultipartBlockModelDefinitionCreator multipartBlockModelDefinitionCreator = MultipartBlockModelDefinitionCreator.create(block);

        final Map<Direction, ModelVariantOperator> southDefaultHorizontalRotationMap = Map.of(
            Direction.SOUTH, NO_OP,
            Direction.WEST, ROTATE_Y_90,
            Direction.NORTH, ROTATE_Y_180,
            Direction.EAST, ROTATE_Y_270
        );

        final Map<BooleanProperty, String> poweredPropertySubtextMap = Map.of(
            LogicGateBlock.POWERED, "output",
            LogicGateBlock.BACK_POWERED, "input_back",
            LogicGateBlock.LEFT_POWERED, "input_left",
            LogicGateBlock.RIGHT_POWERED, "input_right"
        );

        // "facing" property
        for (Map.Entry<Direction, ModelVariantOperator> directionRotationEntry: southDefaultHorizontalRotationMap.entrySet()) {
            final MultipartModelCondition whenFacing = createMultipartConditionBuilder().put(Properties.HORIZONTAL_FACING, directionRotationEntry.getKey()).build();
            final WeightedVariant applyFacing = createWeightedVariant(TextureMap.getId(ModBlocks.LOGIC_GATE)).apply(directionRotationEntry.getValue());
            multipartBlockModelDefinitionCreator.with(whenFacing, applyFacing);

            // "mode" property
            for (LogicGateMode mode: LogicGateMode.values()) {
                final Identifier modelId = TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_mode_" + mode.asString());
                final MultipartModelCondition whenMode = createMultipartConditionBuilder().put(LogicGateBlock.MODE, mode).build();
                final MultipartModelCombinedCondition whenAnd = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(whenFacing, whenMode));
                final WeightedVariant applyAndMode = createWeightedVariant(modelId).apply(directionRotationEntry.getValue());
                multipartBlockModelDefinitionCreator.with(whenAnd, applyAndMode);
            }

            // "powered" properties for all IOs
            for (Map.Entry<BooleanProperty, String> poweredSubtextEntry: poweredPropertySubtextMap.entrySet()) {
                // "=false"
                final Identifier falseModelId = TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_" + poweredSubtextEntry.getValue());
                final MultipartModelCondition whenIoPoweredFalse = createMultipartConditionBuilder().put(poweredSubtextEntry.getKey(), false).build();
                final MultipartModelCombinedCondition whenAndFalse = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(whenFacing, whenIoPoweredFalse));
                final WeightedVariant applyAndFalse = createWeightedVariant(falseModelId).apply(directionRotationEntry.getValue());
                multipartBlockModelDefinitionCreator.with(whenAndFalse, applyAndFalse);

                // "=true"
                final Identifier trueModelId = TextureMap.getSubId(ModBlocks.LOGIC_GATE, "_" + poweredSubtextEntry.getValue() + "_powered");
                final MultipartModelCondition whenIoPoweredTrue = createMultipartConditionBuilder().put(poweredSubtextEntry.getKey(), true).build();
                final MultipartModelCombinedCondition whenAndTrue = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(whenFacing, whenIoPoweredTrue));
                final WeightedVariant applyAndTrue = createWeightedVariant(trueModelId).apply(directionRotationEntry.getValue());
                multipartBlockModelDefinitionCreator.with(whenAndTrue, applyAndTrue);
            }
        }

        blockStateModelGenerator.blockStateCollector.accept(multipartBlockModelDefinitionCreator);
    }
}
