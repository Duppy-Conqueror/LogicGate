package net.duppy_conqueror.logic_gate.item;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> LOGIC_GATE_ITEM_GROUP_KEY;

    public static final ItemGroup LOGIC_GATE_ITEM_GROUP;

    static {
        LOGIC_GATE_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(LogicGateMod.MOD_ID, "item_group"));

        LOGIC_GATE_ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.logic_gate"))
            .icon(() -> new ItemStack(ModBlocks.LOGIC_GATE))
            .build();

        Registry.register(Registries.ITEM_GROUP, LOGIC_GATE_ITEM_GROUP_KEY, LOGIC_GATE_ITEM_GROUP);
    }


    public static void registerItemGroups() {
        LogicGateMod.LOGGER.info("Registering Item Groups for " + LogicGateMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(LOGIC_GATE_ITEM_GROUP_KEY)
            .register((entries) -> {
                entries.add(ModBlocks.LOGIC_GATE);
            });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
            .register((entries) -> {
                entries.add(ModBlocks.LOGIC_GATE);
            });
    }
}
