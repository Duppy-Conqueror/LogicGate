package net.duppy_conqueror.logic_gate.item;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup LOGIC_GATE_GROUP = Registry.register(Registries.ITEM_GROUP,
        new Identifier(LogicGateMod.MOD_ID, "logic_gate"),
        FabricItemGroup.builder().displayName(Text.translatable("itemgroup.logic_gate"))
            .icon(() -> new ItemStack(ModBlocks.LOGIC_GATE))
            .entries((displayContext, entries) -> {
                entries.add(ModBlocks.LOGIC_GATE);
            }).build());

    public static void registerItemGroups() {
        LogicGateMod.LOGGER.info("Registering Item Groups for " + LogicGateMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
                .register((FabricItemGroupEntries entries) -> {
                    entries.add(ModBlocks.LOGIC_GATE);
                });
    }
}
