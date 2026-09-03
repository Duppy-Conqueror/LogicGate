package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.LOGIC_GATE)
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
}
