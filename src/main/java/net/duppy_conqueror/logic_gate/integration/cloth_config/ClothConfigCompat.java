package net.duppy_conqueror.logic_gate.integration.cloth_config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;

import net.duppy_conqueror.logic_gate.config.ModConfigEntry;
import net.duppy_conqueror.logic_gate.config.ModConfigHolder;
import net.duppy_conqueror.logic_gate.config.ModConfigSubCategory;
import net.duppy_conqueror.logic_gate.config.values.BooleanModConfigValue;
import net.duppy_conqueror.logic_gate.config.values.IntegerModConfigValue;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigCompat {
    public static Screen makeScreen(Screen parent, ModConfigHolder modConfigHolder) {
        modConfigHolder.loadConfig();

        ConfigBuilder configBuilder = ConfigBuilder.create();

        configBuilder.setParentScreen(parent);
        configBuilder.setTitle(modConfigHolder.getMainEntry().getTranslation());
        configBuilder.setSavingRunnable(modConfigHolder::saveConfig);

        for (ModConfigEntry configEntry: modConfigHolder.getMainEntry().getEntries()) {
            if (!(configEntry instanceof ModConfigSubCategory subCategory)) {
                continue;
            }
            ConfigCategory mainCategory = configBuilder.getOrCreateCategory(subCategory.getTranslation());
            for (ModConfigEntry innerConfigEntry: subCategory.getEntries()) {
                if (innerConfigEntry instanceof ModConfigSubCategory innerSubCategory) {
                    SubCategoryBuilder subCategoryBuilder = configBuilder.entryBuilder().startSubCategory(innerSubCategory.getTranslation());
                    ClothConfigCompat.addEntriesRecursive(configBuilder, subCategoryBuilder, innerSubCategory);
                    mainCategory.addEntry(subCategoryBuilder.build());
                } else {
                    mainCategory.addEntry(ClothConfigCompat.buildEntry(configBuilder, innerConfigEntry));
                }
            }

        }
        return configBuilder.build();
    }

    private static void addEntriesRecursive(ConfigBuilder builder, SubCategoryBuilder subCategoryBuilder, ModConfigSubCategory subCategory) {
        for (ModConfigEntry configEntry: subCategory.getEntries()) {
            if (configEntry instanceof ModConfigSubCategory subSubCategory) {
                SubCategoryBuilder subSubCategoryBuilder = builder.entryBuilder().startSubCategory(subSubCategory.getTranslation());
                ClothConfigCompat.addEntriesRecursive(builder, subSubCategoryBuilder, subSubCategory);
                subCategoryBuilder.add(subSubCategoryBuilder.build());
            } else {
                subCategoryBuilder.add(ClothConfigCompat.buildEntry(builder, configEntry));
            }
        }
    }

    private static AbstractConfigListEntry<?> buildEntry(ConfigBuilder builder, ModConfigEntry configEntry) {
        if (configEntry instanceof IntegerModConfigValue icv) {
            IntSliderBuilder intFieldBuilder = builder.entryBuilder()
                .startIntSlider(icv.getTranslation(), icv.get(), icv.getMax(), icv.getMin())
                .setMax(icv.getMax())
                .setMin(icv.getMin())
                .setDefaultValue(icv.getDefaultValue())
                .setSaveConsumer(icv::set);
            Component descriptionText = icv.getDescription();
            if (descriptionText != null) {
                intFieldBuilder.setTooltip(descriptionText);
            }
            return intFieldBuilder.build();
        } else if (configEntry instanceof BooleanModConfigValue bcv) {
            BooleanToggleBuilder booleanToggleBuilder = builder.entryBuilder()
                .startBooleanToggle(bcv.getTranslation(), bcv.get())
                .setDefaultValue(bcv.getDefaultValue())
                .setSaveConsumer(bcv::set);
            Component descriptionText = bcv.getDescription();
            if (descriptionText != null) {
                booleanToggleBuilder.setTooltip(descriptionText);
            }
            return booleanToggleBuilder.build();
        } else {
            throw new UnsupportedOperationException("Unknown entry: " + configEntry.getClass().getName());
        }
    }
}
