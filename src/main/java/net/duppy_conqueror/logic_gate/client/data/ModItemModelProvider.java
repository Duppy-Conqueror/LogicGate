package net.duppy_conqueror.logic_gate.client.data;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.duppy_conqueror.logic_gate.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LogicGate.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.LOGIC_GATE.get());
    }
}