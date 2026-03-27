package net.duppy_conqueror.logic_gate.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.duppy_conqueror.logic_gate.config.ModConfig;

// Uncomment the line below when Cloth Config is out
import net.duppy_conqueror.logic_gate.integration.cloth_config.ClothConfigCompat;

// Uncomment the line below when YACL is out
import net.duppy_conqueror.logic_gate.integration.yacl.YACLCompat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

import static net.duppy_conqueror.logic_gate.config.ModConfigBuilder.*;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (YACL) {
            // Uncomment the line below when YACL is out
            return (Screen parent) -> YACLCompat.makeScreen(parent, ModConfig.CONFIG_HOLDER);

            // Comment the line below when YACL is out
//            return ModMenuApi.super.getModConfigScreenFactory();
        } else if (CLOTH_CONFIG) {
            // Uncomment the line below when Cloth Config is out
            return (Screen parent) -> ClothConfigCompat.makeScreen(parent, ModConfig.CONFIG_HOLDER);

            // Comment the line below when Cloth Config is out
//            return ModMenuApi.super.getModConfigScreenFactory();
        } else {
            return ModMenuApi.super.getModConfigScreenFactory();
        }
    }
}

