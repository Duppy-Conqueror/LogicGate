package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.REDSTONE, ModBlocks.LOGIC_GATE)
                    .pattern("I#I")
                    .pattern("#X#")
                    .pattern("I#I")
                    .input('I', Items.STONE)
                    .input('#', Items.REDSTONE)
                    .input('X', Items.QUARTZ)
                    .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                    .showNotification(true)
                    .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "ModRecipeProvider";
    }
}