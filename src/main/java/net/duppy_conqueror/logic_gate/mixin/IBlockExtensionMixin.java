package net.duppy_conqueror.logic_gate.mixin;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IBlockExtension.class)
public interface IBlockExtensionMixin {
    @Inject(at = @At("HEAD"), method = "canConnectRedstone", cancellable = true)
    private static void onCanConnectRedstone_handleSmartConnection(BlockState state, BlockGetter level, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.SMART_REDSTONE_WIRE_CONNECTION.get() && state.is(ModBlocks.LOGIC_GATE) && direction != null) {
            LogicGateMode mode = state.getValue(LogicGateBlock.MODE);
            Direction facing = state.getValue(LogicGateBlock.FACING);
            if (mode.isSingleInput()) {
                cir.setReturnValue(facing.getAxis() == direction.getAxis());
            } else {
                cir.setReturnValue(!facing.equals(direction.getOpposite()));
            }
        }
    }
}