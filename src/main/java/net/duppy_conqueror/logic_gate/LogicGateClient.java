package net.duppy_conqueror.logic_gate;

import net.duppy_conqueror.logic_gate.integration.cloth_config.ClothConfigCompat;
import net.duppy_conqueror.logic_gate.integration.yacl.YACLCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LogicGate.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = LogicGate.MOD_ID, value = Dist.CLIENT)
public class LogicGateClient {
    public static final boolean YACL = ModList.get().isLoaded("yet_another_config_lib_v3");
    public static final boolean CLOTH_CONFIG = ModList.get().isLoaded("cloth_config");

    public LogicGateClient(ModContainer modContainer) {
        if (YACL) {
            YACLCompat.register(modContainer);
        } else if (CLOTH_CONFIG) {
            ClothConfigCompat.register(modContainer);
        } else {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }
}
