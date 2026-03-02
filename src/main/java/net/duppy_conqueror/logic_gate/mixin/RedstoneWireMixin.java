package net.duppy_conqueror.logic_gate.mixin;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public class RedstoneWireMixin {
    @Inject(at = @At("HEAD"), method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", cancellable = true)
    private static void disconnectFromLogicGate(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.SMART_REDSTONE_WIRE_CONNECTION.get()) {
            if (state.is(ModBlocks.LOGIC_GATE) && dir != null) {
                LogicGateMode mode = state.getValue(LogicGateBlock.MODE);
                Direction facing = state.getValue(LogicGateBlock.FACING);
                if (mode.isSingleInput()) {
                    cir.setReturnValue(facing.getAxis() == dir.getAxis());
                } else {
                    cir.setReturnValue(!facing.equals(dir.getOpposite()));
                }
            }
        }
    }
}