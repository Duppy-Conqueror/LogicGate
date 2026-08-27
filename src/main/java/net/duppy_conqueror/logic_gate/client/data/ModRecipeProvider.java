package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        this.shaped(RecipeCategory.REDSTONE, ModBlocks.LOGIC_GATE)
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

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        protected RecipeProvider createRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }


        @Override
        public String getName() {
            return "LogicGate Mod Recipes";
        }
    }
}
