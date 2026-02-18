package net.duppy_conqueror.logic_gate.block;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {
    public static final Block LOGIC_GATE;

    static {
        LOGIC_GATE = registerBlock("logic_gate",
            LogicGateBlock::new,
            AbstractBlock.Settings.create().breakInstantly().sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY),
            true
        );
    }

    public static void registerBlocks() {
        LogicGateMod.LOGGER.info("Registering blocks for " + LogicGateMod.MOD_ID);
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean registerItem) {
        Identifier blockId = Identifier.of(LogicGateMod.MOD_ID, name);
        RegistryKey<Block> blockRegistryKey = RegistryKey.of(RegistryKeys.BLOCK, blockId);
        Block block = blockFactory.apply(settings.registryKey(blockRegistryKey));
        if (registerItem) {
            RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, blockId);
            Registry.register(Registries.ITEM, blockId, new BlockItem(block, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(itemRegistryKey)));
        }
        return Registry.register(Registries.BLOCK, blockId, block);
    }
}
