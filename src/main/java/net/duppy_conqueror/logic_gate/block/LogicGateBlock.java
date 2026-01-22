package net.duppy_conqueror.logic_gate.block;

import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.minecraft.block.*;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;

public class LogicGateBlock extends AbstractRedstoneGateBlock {
    private enum LogicGateInputSide {
        BACK,
        LEFT,
        RIGHT
    }

    public static final EnumProperty<LogicGateMode> MODE = EnumProperty.of("mode", LogicGateMode.class);

    public LogicGateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(MODE, LogicGateMode.BUFFER)
                .with(POWERED, false)
        );
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            state = state.with(MODE, LogicGateMode.cycle(state.get(MODE), Screen.hasShiftDown()));
            world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, 0.5F);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
            this.updateBlockState(world, pos, state);
            return ActionResult.success(world.isClient);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.updateBlockState(world, pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, POWERED);
    }

    private boolean computeOutput(World world, BlockPos pos, BlockState state) {
        final boolean backPowered = this.getInputPower(world, pos, state, LogicGateInputSide.BACK);
        final boolean leftPowered = this.getInputPower(world, pos, state, LogicGateInputSide.LEFT);
        final boolean rightPowered = this.getInputPower(world, pos, state, LogicGateInputSide.RIGHT);

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

    private void updateBlockState(World world, BlockPos pos, BlockState state) {
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

    private boolean getInputPower(World world, BlockPos pos, BlockState state, LogicGateInputSide side) {
        if (side == null) {
            return false;
        }

        Direction direction = state.get(FACING);
        if (side == LogicGateInputSide.LEFT) {
            direction = direction.rotateYClockwise();
        } else if (side == LogicGateInputSide.RIGHT) {
            direction = direction.rotateYCounterclockwise();
        }

        final BlockPos blockPos = pos.offset(direction);

        final boolean isEmittingRedstonePower = world.isEmittingRedstonePower(blockPos, direction);
        // If the block is emitting redstone power, return true
        if (isEmittingRedstonePower) {
            return true;
        } else {
            final BlockState blockState = world.getBlockState(blockPos);
            // Check if the block is a redstone wire, return false if not
            if (blockState.isOf(Blocks.REDSTONE_WIRE)) {
                WireConnection wireConnection = blockState.get(RedstoneWireBlock.DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()));
                // If the redstone wire "connects" to the logic gate, return whether the redstone power is positive
                if (wireConnection.equals(WireConnection.SIDE)) {
                    return (Integer) blockState.get(RedstoneWireBlock.POWER) > 0;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
