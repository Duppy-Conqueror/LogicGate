package net.duppy_conqueror.logic_gate.block;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class ModBlocks {
    public static final Block LOGIC_GATE;

    static {
        LOGIC_GATE = registerBlock("logic_gate",
            LogicGateBlock::new,
            BlockBehaviour.Properties.of().instabreak().sound(SoundType.STONE).pushReaction(PushReaction.DESTROY),
            true
        );
    }

    public static void registerBlocks() {
        LogicGateMod.LOGGER.info("Registering blocks for " + LogicGateMod.MOD_ID);
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties, boolean registerItem) {
        Identifier blockId = Identifier.fromNamespaceAndPath(LogicGateMod.MOD_ID, name);
        ResourceKey<Block> blockResourceKey = ResourceKey.create(Registries.BLOCK, blockId);
        Block block = blockFactory.apply(properties.setId(blockResourceKey));
        if (registerItem) {
            ResourceKey<Item> itemResourceKey = ResourceKey.create(Registries.ITEM, blockId);
            Registry.register(BuiltInRegistries.ITEM, blockId, new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemResourceKey)));
        }
        return Registry.register(BuiltInRegistries.BLOCK, blockId, block);
    }
}
