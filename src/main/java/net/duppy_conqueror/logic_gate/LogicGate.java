package net.duppy_conqueror.logic_gate;

import com.mojang.logging.LogUtils;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.duppy_conqueror.logic_gate.command.ModCommands;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.duppy_conqueror.logic_gate.item.ModCreativeModeTabs;

import net.duppy_conqueror.logic_gate.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(LogicGate.MOD_ID)
public class LogicGate {
	public static final String MOD_ID = "logic_gate";
	public static final Logger LOGGER = LogUtils.getLogger();

	public LogicGate(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.CONFIG_HOLDER);
		NeoForge.EVENT_BUS.register(ModCommands.class);
		ModItems.init(modEventBus);
		ModBlocks.init(modEventBus);
		ModCreativeModeTabs.init(modEventBus);

		NeoForge.EVENT_BUS.register(this);
	}

	private void commonSetup(FMLCommonSetupEvent event) {

	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {

	}
}