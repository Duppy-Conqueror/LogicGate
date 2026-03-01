package net.duppy_conqueror.logic_gate.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.duppy_conqueror.logic_gate.config.ModConfig;
import net.duppy_conqueror.logic_gate.integration.cloth_config.ClothConfigCompat;
import net.duppy_conqueror.logic_gate.integration.yacl.YACLCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import static net.duppy_conqueror.logic_gate.config.ModConfigBuilder.*;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (YACL) {
            return (Screen parent) -> YACLCompat.makeScreen(parent, ModConfig.CONFIG_HOLDER);
        } else if (CLOTH_CONFIG) {
            return (Screen parent) -> ClothConfigCompat.makeScreen(parent, ModConfig.CONFIG_HOLDER);
        } else {
            return ModMenuApi.super.getModConfigScreenFactory();
        }
    }
}

