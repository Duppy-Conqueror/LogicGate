package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LogicGate.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.generateLogicGateBlockStates();
    }


    @Override
    public String getName() {
        return "ModModelProvider";
    }

    private void generateLogicGateBlockStates() {
        Block block = ModBlocks.LOGIC_GATE.get();
        MultiPartBlockStateBuilder multipartBuilder = this.getMultipartBuilder(block);

        final Map<Direction, Integer> southDefaultHorizontalYRotMap = Map.of(
            Direction.SOUTH, 0,
            Direction.WEST, 90,
            Direction.NORTH, 180,
            Direction.EAST, 270
        );

        final Map<BooleanProperty, String> poweredPropertySubtextMap = Map.of(
            LogicGateBlock.POWERED, "output",
            LogicGateBlock.BACK_POWERED, "input_back",
            LogicGateBlock.LEFT_POWERED, "input_left",
            LogicGateBlock.RIGHT_POWERED, "input_right"
        );

        // "facing" property
        for (Map.Entry<Direction, Integer> directionRotationEntry: southDefaultHorizontalYRotMap.entrySet()) {
            int yRot = directionRotationEntry.getValue();

            multipartBuilder.part()
                .modelFile(models().getExistingFile(ModelLocationUtils.getModelLocation(block)))
                .rotationY(yRot)
                .addModel()
                .condition(BlockStateProperties.HORIZONTAL_FACING, directionRotationEntry.getKey())
                .end();

            // "mode" property
            for (LogicGateMode mode: LogicGateMode.values()) {
                final ResourceLocation modelId = ModelLocationUtils.getModelLocation(block, "_mode_" + mode.getSerializedName());
                multipartBuilder.part()
                    .modelFile(models().getExistingFile(modelId))
                    .rotationY(yRot)
                    .addModel()
                    .condition(LogicGateBlock.MODE, mode)
                    .condition(BlockStateProperties.HORIZONTAL_FACING, directionRotationEntry.getKey())
                    .end();
            }

            // "powered" properties for all IOs
            for (Map.Entry<BooleanProperty, String> poweredSubtextEntry: poweredPropertySubtextMap.entrySet()) {
                // "=false"
                final ResourceLocation falseModelId = ModelLocationUtils.getModelLocation(block, "_" + poweredSubtextEntry.getValue());
                multipartBuilder.part()
                    .modelFile(models().getExistingFile(falseModelId))
                    .rotationY(yRot)
                    .addModel()
                    .condition(poweredSubtextEntry.getKey(), false)
                    .condition(BlockStateProperties.HORIZONTAL_FACING, directionRotationEntry.getKey())
                    .end();


                // "=true"
                final ResourceLocation trueModelId = ModelLocationUtils.getModelLocation(block, "_" + poweredSubtextEntry.getValue() + "_powered");
                multipartBuilder.part()
                    .modelFile(models().getExistingFile(trueModelId))
                    .rotationY(yRot)
                    .addModel()
                    .condition(poweredSubtextEntry.getKey(), true)
                    .condition(BlockStateProperties.HORIZONTAL_FACING, directionRotationEntry.getKey())
                    .end();
            }
        }
    }
}
