package net.duppy_conqueror.logic_gate;

import net.duppy_conqueror.logic_gate.datagen.ModBlockLootTableProvider;
import net.duppy_conqueror.logic_gate.datagen.ModModelProvider;
import net.duppy_conqueror.logic_gate.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class LogicGateModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
	}
}
