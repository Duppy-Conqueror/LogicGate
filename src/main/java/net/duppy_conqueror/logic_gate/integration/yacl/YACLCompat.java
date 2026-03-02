package net.duppy_conqueror.logic_gate.integration.yacl;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import net.duppy_conqueror.logic_gate.config.ModConfigEntry;
import net.duppy_conqueror.logic_gate.config.ModConfigHolder;
import net.duppy_conqueror.logic_gate.config.ModConfigSubCategory;
import net.duppy_conqueror.logic_gate.config.values.BooleanModConfigValue;
import net.duppy_conqueror.logic_gate.config.values.IntegerModConfigValue;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YACLCompat {
    public static Screen makeScreen(Screen parent, ModConfigHolder modConfigHolder) {
        modConfigHolder.loadConfig();

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();
        builder.title(modConfigHolder.getMainEntry().getTranslation());
        builder.save(modConfigHolder::saveConfig);

        for (ModConfigEntry configEntry: modConfigHolder.getMainEntry().getEntries()) {
            if (!(configEntry instanceof ModConfigSubCategory subCategory)) {
                continue;
            }

            ConfigCategory.Builder mainCategory = ConfigCategory.createBuilder().name(subCategory.getTranslation());

            for (ModConfigEntry innerConfigEntry: subCategory.getEntries()) {
                if (innerConfigEntry instanceof ModConfigSubCategory innerSubCategory) {
                    OptionGroup.Builder subCategoryBuilder = OptionGroup.createBuilder().name(innerSubCategory.getTranslation()).collapsed(true);
                    YACLCompat.addEntriesRecursive(mainCategory, subCategoryBuilder, innerSubCategory);
                    mainCategory.group(subCategoryBuilder.build());
                } else {
                    mainCategory.option(YACLCompat.buildEntry(innerConfigEntry));
                }
            }

            builder.category(mainCategory.build());
        }

        return builder.build().generateScreen(parent);
    }

    private static void addEntriesRecursive(ConfigCategory.Builder builder, OptionGroup.Builder subCategoryBuilder, ModConfigSubCategory subCategory) {
        for (ModConfigEntry configEntry: subCategory.getEntries()) {
            if (configEntry instanceof ModConfigSubCategory subSubCategory) {
//                OptionGroup.Builder subSubCategoryBuilder = OptionGroup.createBuilder().name(subSubCategory.getTranslation()).collapsed(true);
//                YACLCompat.addEntriesRecursive(builder, subSubCategoryBuilder, subSubCategory);
//                subCategoryBuilder.group(subSubCategoryBuilder.build());
            } else {
                subCategoryBuilder.option(YACLCompat.buildEntry(configEntry));
            }
        }
    }

    private static Option<?> buildEntry(ModConfigEntry configEntry) {

        if (configEntry instanceof IntegerModConfigValue icv) {
            Option.Builder<Integer> intOptionBuilder = Option.<Integer>createBuilder()
                .name(icv.getTranslation())
                .binding(icv.getDefaultValue(), icv, icv::set)
                .controller(oi -> IntegerSliderControllerBuilder.create(oi)
                    .range(icv.getMin(), icv.getMax())
                    .step(1)
                );
            Component descriptionText = icv.getDescription();
            if (descriptionText != null) {
                intOptionBuilder.description(OptionDescription.of(descriptionText));
            }
            return intOptionBuilder.build();
        } else if (configEntry instanceof BooleanModConfigValue bcv) {
            Option.Builder<Boolean> boolOptionBuilder = Option.<Boolean>createBuilder()
                .name(bcv.getTranslation())
                .binding(bcv.getDefaultValue(), bcv, bcv::set)
                .controller(TickBoxControllerBuilder::create);
            Component descriptionText = bcv.getDescription();
            if (descriptionText != null) {
                boolOptionBuilder.description(OptionDescription.of(descriptionText));
            }
            return boolOptionBuilder.build();
        } else {
            throw new UnsupportedOperationException("Unknown entry: " + configEntry.getClass().getName());
        }
    }
}
