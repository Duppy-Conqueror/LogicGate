package net.duppy_conqueror.logic_gate.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ModConfigSubCategory extends ModConfigEntry {
    private String translationKey;
    private final List<ModConfigEntry> entries = new ArrayList<>();

    public ModConfigSubCategory(String categoryId) {
        super(categoryId);
    }

    public void addEntry(ModConfigEntry entry){
        this.entries.add(entry);
    }

    public List<ModConfigEntry> getEntries() {
        return entries;
    }

    @Override
    public void loadFromJson(JsonObject object) {
        if (object.has(this.id)) {
            JsonElement element = object.get(this.id);
            if (element instanceof JsonObject object1) {
                entries.forEach((ModConfigEntry configEntry) -> configEntry.loadFromJson(object1));
            }
            return;
        }
        ModConfigBuilder.LOGGER.warn("Config file had missing category {}", this.id);
    }

    @Override
    public void saveToJson(JsonObject object) {
        JsonObject category = new JsonObject();
        entries.forEach((ModConfigEntry configEntry) -> configEntry.saveToJson(category));
        object.add(this.id, category);
    }

    public Text getTranslation() {
        return Text.translatable(this.translationKey);
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }
}
