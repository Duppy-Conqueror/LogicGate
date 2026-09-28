package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Set;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    public static MultiRegistryBootstrap create() {
        return new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(final BootstrapGetter registries) {
                new ModRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
            }
        };
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

    public String getName() {
            return "LogicGate Mod Recipes";
    }
}
