package net.duppy_conqueror.logic_gate.item;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.ModBlocks;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final ResourceKey<CreativeModeTab> LOGIC_GATE_ITEM_GROUP_KEY;

    public static final CreativeModeTab LOGIC_GATE_ITEM_GROUP;

    static {
        LOGIC_GATE_ITEM_GROUP_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(LogicGateMod.MOD_ID, "item_group"));

        LOGIC_GATE_ITEM_GROUP = FabricCreativeModeTab.builder()
            .title(Component.translatable("itemgroup.logic_gate"))
            .icon(() -> new ItemStack(ModBlocks.LOGIC_GATE))
            .build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, LOGIC_GATE_ITEM_GROUP_KEY, LOGIC_GATE_ITEM_GROUP);
    }


    public static void registerItemGroups() {
        LogicGateMod.LOGGER.info("Registering item groups for " + LogicGateMod.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(LOGIC_GATE_ITEM_GROUP_KEY)
            .register((entries) -> {
                entries.accept(ModBlocks.LOGIC_GATE);
            });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS)
            .register((output) -> {
                output.accept(ModBlocks.LOGIC_GATE);
            });
    }
}
