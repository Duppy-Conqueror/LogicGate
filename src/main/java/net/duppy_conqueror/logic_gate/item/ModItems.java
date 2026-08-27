package net.duppy_conqueror.logic_gate.item;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LogicGate.MOD_ID);

    public static final DeferredItem<Item> LOGIC_GATE = registerBlockItem(ModBlocks.LOGIC_GATE_BLOCK_ID, ModBlocks.LOGIC_GATE, BlockItem::new, new Item.Properties());

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    private static DeferredItem<Item> registerBlockItem(final Identifier blockItemId, final DeferredBlock<Block> block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties itemProperties) {
        return ITEMS.registerItem(blockItemId.getPath(), p -> itemFactory.apply(block.get(), p), itemProperties::useBlockDescriptionPrefix);
    }
}
