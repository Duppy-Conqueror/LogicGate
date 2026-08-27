package net.duppy_conqueror.logic_gate.block;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LogicGate.MOD_ID);

    public static final Identifier LOGIC_GATE_BLOCK_ID = Identifier.fromNamespaceAndPath(LogicGate.MOD_ID, "logic_gate");

    public static final DeferredBlock<Block> LOGIC_GATE = registerBlock(
        LOGIC_GATE_BLOCK_ID,
        LogicGateBlock::new,
        BlockBehaviour.Properties.of().instabreak().sound(SoundType.STONE).pushReaction(PushReaction.DESTROY)
    );

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

    private static DeferredBlock<Block> registerBlock(Identifier blockId, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        return BLOCKS.registerBlock(blockId.getPath(), blockFactory, () -> properties);
    }
}
