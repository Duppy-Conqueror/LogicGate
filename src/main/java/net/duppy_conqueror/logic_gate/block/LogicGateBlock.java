package net.duppy_conqueror.logic_gate.block;

import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.minecraft.block.*;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;

import java.util.List;

public class LogicGateBlock extends AbstractRedstoneGateBlock {
    public static final EnumProperty<LogicGateMode> MODE = EnumProperty.of("mode", LogicGateMode.class);

    public static final BooleanProperty BACK_POWERED = BooleanProperty.of("back_powered");
    public static final BooleanProperty LEFT_POWERED = BooleanProperty.of("left_powered");
    public static final BooleanProperty RIGHT_POWERED = BooleanProperty.of("right_powered");

    public LogicGateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(MODE, LogicGateMode.BUFFER)
                .with(POWERED, false)
                .with(BACK_POWERED, false)
                .with(LEFT_POWERED, false)
                .with(RIGHT_POWERED, false)
        );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            state = state.with(MODE, LogicGateMode.cycle(state.get(MODE), player.isSneaking()));
            world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, 0.5F);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
            this.updateOutputPowered(world, pos, state);
            return ActionResult.success(world.isClient);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Move super scheduledTick method to updateOutputPowered
        this.updateOutputPowered(world, pos, state);
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

    @Override
    protected boolean hasPower(World world, BlockPos pos, BlockState state) {
        return this.getPower(world, pos, state) > 0;
    }

    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        return this.computeOutput(world, pos, state) ? 15 : 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, POWERED, BACK_POWERED, LEFT_POWERED, RIGHT_POWERED);
    }

    private static final List<BooleanProperty> inputPowerProperties = List.of(BACK_POWERED, LEFT_POWERED, RIGHT_POWERED);

    private boolean computeOutput(World world, BlockPos pos, BlockState state) {
        this.updateInputPowered(world, pos, state);
        state = world.getBlockState(pos);
        final boolean backPowered = state.get(BACK_POWERED);
        final boolean leftPowered = state.get(LEFT_POWERED);
        final boolean rightPowered = state.get(RIGHT_POWERED);

        return switch (state.get(MODE)) {
            case BUFFER -> backPowered;
            case NOT -> !backPowered;
            case OR -> leftPowered || rightPowered;
            case AND -> leftPowered && rightPowered;
            case XOR -> leftPowered ^ rightPowered;
            case NOR -> !leftPowered && !rightPowered;
            case NAND -> !leftPowered || !rightPowered;
            case IMPLY -> !leftPowered || rightPowered;
            case NIMPLY -> leftPowered && !rightPowered;
        };
    }

    private void updateOutputPowered(World world, BlockPos pos, BlockState state) {
        final boolean bl1 = state.get(POWERED);
        final boolean bl2 = this.hasPower(world, pos, state);

        if (bl1 != bl2) {
            if (bl1) {
                world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
            } else {
                world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
                world.scheduleBlockTick(pos, this, this.getUpdateDelayInternal(state), TickPriority.VERY_HIGH);
            }
        }
    }

    private void updateInputPowered(World world, BlockPos pos, BlockState state) {
        BlockState newState = state;

        for (BooleanProperty property: inputPowerProperties) {
            Direction direction = state.get(FACING);
            if (property.equals(LEFT_POWERED)) {
                direction = direction.rotateYClockwise();
            } else if (property.equals(RIGHT_POWERED)) {
                direction = direction.rotateYCounterclockwise();
            }
            final BlockPos blockPos = pos.offset(direction);

            final boolean isEmittingRedstonePower = world.isEmittingRedstonePower(blockPos, direction);
            // If the block is emitting redstone power, return true
            if (isEmittingRedstonePower) {
                newState = newState.with(property, true);
            } else {
                // Check if the block is a redstone wire, return false if not
                final BlockState neighbourBlockState = world.getBlockState(blockPos);
                if (neighbourBlockState.isOf(Blocks.REDSTONE_WIRE)) {
                    // Get the property of the redstone wire connection that is in the direction towards the logic gate block
                    // The WireConnection property should have the value "side"
                    final WireConnection wireConnection = neighbourBlockState.get(RedstoneWireBlock.DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()));
                    final boolean isPoweredByWire = wireConnection.equals(WireConnection.SIDE) && neighbourBlockState.get(RedstoneWireBlock.POWER) > 0;

                    // If the redstone wire "connects" to the logic gate, check whether the redstone power is positive as well
                    newState = newState.with(property, isPoweredByWire);
                } else {
                    newState = newState.with(property, false);
                }
            }
        }
        world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
    }
}
