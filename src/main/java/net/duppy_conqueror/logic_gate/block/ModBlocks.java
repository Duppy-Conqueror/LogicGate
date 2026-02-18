package net.duppy_conqueror.logic_gate.block;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block LOGIC_GATE = registerBlock("logic_gate", new LogicGateBlock(AbstractBlock.Settings.create().breakInstantly().sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(LogicGateMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(LogicGateMod.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerBlocks() {
        LogicGateMod.LOGGER.info("Registering blocks for " + LogicGateMod.MOD_ID);
    }
}
