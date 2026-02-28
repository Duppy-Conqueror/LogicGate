package net.duppy_conqueror.logic_gate.mixin;

import net.duppy_conqueror.logic_gate.block.LogicGateBlock;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.block.enums.LogicGateMode;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireMixin {
    @Inject(at = @At("HEAD"), method = "connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z", cancellable = true)
    private static void disconnectFromLogicGate(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.SMART_REDSTONE_WIRE_CONNECTION.get()) {
            if (state.isOf(ModBlocks.LOGIC_GATE) && dir != null) {
                LogicGateMode mode = state.get(LogicGateBlock.MODE);
                Direction facing = state.get(Properties.HORIZONTAL_FACING);
                if (mode.isSingleInput()) {
                    cir.setReturnValue(facing.getAxis() == dir.getAxis());
                } else {
                    cir.setReturnValue(!facing.equals(dir.getOpposite()));
                }
            }
        }
    }
}