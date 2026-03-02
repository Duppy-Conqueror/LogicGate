package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {

    public ModBlockLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        this.add(ModBlocks.LOGIC_GATE, this.createSingleItemTable(ModBlocks.LOGIC_GATE));
    }

    @Override
    public String getName() {
        return "ModBlockLootTableProvider";
    }
}
