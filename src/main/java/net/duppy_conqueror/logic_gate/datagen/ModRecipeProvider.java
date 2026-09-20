package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.block.ModBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(final BootstrapContext<Recipe<?>> recipeOutput, final BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    public void buildRecipes() {
        shaped(RecipeCategory.REDSTONE, ModBlocks.LOGIC_GATE)
            .pattern("I#I")
            .pattern("#X#")
            .pattern("I#I")
            .define('I', Items.STONE)
            .define('#', Items.REDSTONE)
            .define('X', Items.QUARTZ)
            .unlockedBy(getHasName(Items.QUARTZ), this.has(Items.QUARTZ))
            .showNotification(true)
            .save(output);
    }

    public static class ModRecipeProviderRunner extends FabricRecipeProvider {
        public ModRecipeProviderRunner(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public String getName() {
            return "LogicGate Mod Recipes";
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancements) {
            return new ModRecipeProvider(recipeOutput, advancements);
        }
    }
}