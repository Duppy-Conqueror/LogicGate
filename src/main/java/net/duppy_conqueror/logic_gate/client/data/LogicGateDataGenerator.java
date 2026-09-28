package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LogicGate.MOD_ID)
public class LogicGateDataGenerator {
    private static final RegistrySetBuilder RELOADABLE_BUILDER = new RegistrySetBuilder()
        .add(Registries.LOOT_TABLE, _ -> new LootTableProvider(
            BuiltInLootTables.all(),
            List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK))
        ))
        .add(ModRecipeProvider.create());

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new ModModelProvider(packOutput));

        generator.addProvider(true, DatapackBuiltinEntriesProvider.forReloadableLayer(
            packOutput,
            "reload_resinbilliards",
            event.getWorldLookupProvider(),
            event.getReloadableLookupProvider(),
            RELOADABLE_BUILDER,
            Set.of(LogicGate.MOD_ID)
        ));
    }
}
