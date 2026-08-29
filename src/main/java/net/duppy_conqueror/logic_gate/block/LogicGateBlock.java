package net.duppy_conqueror.logic_gate.block;

import com.mojang.serialization.MapCodec;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.ticks.TickPriority;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class LogicGateBlock extends DiodeBlock {
    public static final MapCodec<LogicGateBlock> CODEC = simpleCodec(LogicGateBlock::new);

    public static final EnumProperty<LogicGateMode> MODE = EnumProperty.create("mode", LogicGateMode.class);

    public static final BooleanProperty BACK_POWERED = BooleanProperty.create("back_powered");
    public static final BooleanProperty LEFT_POWERED = BooleanProperty.create("left_powered");
    public static final BooleanProperty RIGHT_POWERED = BooleanProperty.create("right_powered");

    public LogicGateBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(MODE, LogicGateMode.BUFFER)
                .setValue(POWERED, false)
                .setValue(BACK_POWERED, false)
                .setValue(LEFT_POWERED, false)
                .setValue(RIGHT_POWERED, false)
        );
    }

    @Override
    protected MapCodec<LogicGateBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            state = state.setValue(MODE, LogicGateMode.cycle(state.getValue(MODE), player.isCrouching()));
            level.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, 0.5F);
            level.setBlock(pos, state, Block.UPDATE_CLIENTS);
            this.updateOutputPowered(level, pos, state);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        // Move super `tick` method to updateOutputPowered
        this.updateOutputPowered(level, pos, state);
    }

    @Override
    protected int getDelay(BlockState state) {
        return ModConfig.getDelay(state.getValue(MODE));
    }

    @Override
    protected boolean shouldTurnOn(final Level level, final BlockPos pos, final BlockState state) {
        return this.computeOutput(level, pos, state);
    }

    @Override
    protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, POWERED, BACK_POWERED, LEFT_POWERED, RIGHT_POWERED);
    }

    private static final List<BooleanProperty> inputPowerProperties = List.of(BACK_POWERED, LEFT_POWERED, RIGHT_POWERED);

    private boolean computeOutput(final Level level, BlockPos pos, BlockState state) {
        this.updateInputPowered(level, pos, state);
        state = level.getBlockState(pos);
        final boolean backPowered = state.getValue(BACK_POWERED);
        final boolean leftPowered = state.getValue(LEFT_POWERED);
        final boolean rightPowered = state.getValue(RIGHT_POWERED);

        return switch (state.getValue(MODE)) {
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

    private void updateOutputPowered(final Level level, final BlockPos pos, final BlockState state) {
        final boolean bl1 = state.getValue(POWERED);
        final boolean bl2 = this.shouldTurnOn(level, pos, state);

        if (bl1 != bl2) {
            if (bl1) {
                level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS);
            } else {
                level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
                level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
            }
        }
    }

    private void updateInputPowered(final Level level, final BlockPos pos, final BlockState state) {
        BlockState newState = state;

        for (BooleanProperty property: inputPowerProperties) {
            Direction direction = state.getValue(FACING);
            if (property.equals(LEFT_POWERED)) {
                direction = direction.getClockWise();
            } else if (property.equals(RIGHT_POWERED)) {
                direction = direction.getCounterClockWise();
            }
            final BlockPos blockPos = pos.relative(direction);

            final boolean isEmittingRedstonePower = level.hasSignal(blockPos, direction);
            // If the block is emitting redstone power, return true
            if (isEmittingRedstonePower) {
                newState = newState.setValue(property, true);
            } else {
                // Check if the block is a redstone wire, return false if not
                final BlockState neighbourBlockState = level.getBlockState(blockPos);
                if (neighbourBlockState.is(Blocks.REDSTONE_WIRE)) {
                    // Get the property of the redstone wire connection that is in the direction towards the logic gate block
                    // The WireConnection property should have the value "side"
                    final RedstoneSide wireConnection = neighbourBlockState.getValue(RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()));
                    final boolean isPoweredByWire = wireConnection.equals(RedstoneSide.SIDE) && neighbourBlockState.getValue(RedStoneWireBlock.POWER) > 0;

                    // If the redstone wire "connects" to the logic gate, check whether the redstone power is positive as well
                    newState = newState.setValue(property, isPoweredByWire);
                } else {
                    newState = newState.setValue(property, false);
                }
            }
        }
        level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Direction from the center towards the back input
        final Direction direction = state.getValue(FACING);

        // Based on block center
        final double x = pos.getX() + 0.5;
        final double z = pos.getZ() + 0.5;

        // Particle y-level offset: [0.2, 0.4]
        final double y = pos.getY() + 0.3 + (random.nextDouble() - 0.5) * 0.2;
        double epsilon = (random.nextDouble() - 0.5) / 16.0;

        if (state.getValue(POWERED) && random.nextBoolean()) {
            double xo = 0.25 * direction.getOpposite().getStepX() + epsilon;
            double zo = 0.25 * direction.getOpposite().getStepZ() + epsilon;
            level.addParticle(DustParticleOptions.REDSTONE, x + xo, y, z + zo, 0.0, 0.0, 0.0);
        }
        if (state.getValue(BACK_POWERED) && random.nextBoolean()) {
            double xo = 0.25 * direction.getStepX() + epsilon;
            double zo = 0.25 * direction.getStepZ() + epsilon;
            level.addParticle(DustParticleOptions.REDSTONE, x + xo, y, z + zo, 0.0, 0.0, 0.0);
        }
        if (state.getValue(LEFT_POWERED) && random.nextBoolean()) {
            double xo = 0.25 * direction.getClockWise().getStepX() + epsilon;
            double zo = 0.25 * direction.getClockWise().getStepZ() + epsilon;
            level.addParticle(DustParticleOptions.REDSTONE, x + xo, y, z + zo, 0.0, 0.0, 0.0);
        }
        if (state.getValue(RIGHT_POWERED) && random.nextBoolean()) {
            double xo = 0.25 * direction.getCounterClockWise().getStepX() + epsilon;
            double zo = 0.25 * direction.getCounterClockWise().getStepZ() + epsilon;
            level.addParticle(DustParticleOptions.REDSTONE, x + xo, y, z + zo, 0.0, 0.0, 0.0);
        }
    }
}
