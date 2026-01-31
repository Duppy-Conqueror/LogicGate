package net.duppy_conqueror.logic_gate.datagen;

import net.duppy_conqueror.logic_gate.LogicGateMod;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.LOGIC_GATE)
                .pattern("I#I")
                .pattern("#X#")
                .pattern("I#I")
                .input('I', Items.STONE)
                .input('#', Items.REDSTONE)
                .input('X', Items.QUARTZ)
                .criterion(FabricRecipeProvider.hasItem(Items.QUARTZ), FabricRecipeProvider.conditionsFromItem(Items.QUARTZ))
                .showNotification(true)
                .offerTo(exporter, new Identifier(LogicGateMod.MOD_ID, getRecipeName(ModBlocks.LOGIC_GATE)));
    }
}