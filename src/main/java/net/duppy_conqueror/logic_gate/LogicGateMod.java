package net.duppy_conqueror.logic_gate;

import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.command.ModCommands;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.duppy_conqueror.logic_gate.item.ModItemGroups;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogicGateMod implements ModInitializer {
	public static final String MOD_ID = "logic_gate";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing mod: " + MOD_ID);
		ModConfig.init();
		ModCommands.init();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerBlocks();
	}
}