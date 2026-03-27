package net.duppy_conqueror.logic_gate.config;

import net.duppy_conqueror.logic_gate.config.values.BooleanModConfigValue;
import net.duppy_conqueror.logic_gate.config.values.ModConfigValue;
import net.duppy_conqueror.logic_gate.config.values.IntegerModConfigValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ModConfigBuilder {
    public static final Logger LOGGER = LoggerFactory.getLogger("LogicGate Configs");

    public static final boolean YACL = FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");

    public static final boolean CLOTH_CONFIG = FabricLoader.getInstance().isModLoaded("cloth-config");

    private final Identifier id;

    private final ModConfigSubCategory mainCategory;
    private final Deque<ModConfigSubCategory> subCategoryDeque = new ArrayDeque<>();

    private final Map<String, String> comments = new HashMap<>();
    private String currentCommentKey;
    private String currentComment;

    private ModConfigBuilder(String modId) {
        this.id = Identifier.fromNamespaceAndPath(modId, "config");
        this.mainCategory = new ModConfigSubCategory(this.getId().getNamespace());
        this.mainCategory.setTranslationKey("config.%s.title".formatted(modId));
        this.subCategoryDeque.push(mainCategory);
    }

    public static ModConfigBuilder create(String modId) {
        return new ModConfigBuilder(modId);
    }

    public ModConfigHolder build() {
        assert this.subCategoryDeque.size() == 1;
        return new ModConfigHolder(this.id, this.mainCategory);
    }

    public ModConfigBuilder comment(String comment) {
        this.currentComment = comment;
        return this;
    }

    public IntegerModConfigValue defineIntConfigValue(String entryId, int defaultValue, int minValue, int maxValue) {
        IntegerModConfigValue ret = new IntegerModConfigValue(entryId, defaultValue, minValue, maxValue);
        this.addConfigValueEntry(ret.getId(), ret);
        return ret;
    }

    public BooleanModConfigValue defineBoolConfigValue(String entryId, boolean defaultValue) {
        BooleanModConfigValue ret = new BooleanModConfigValue(entryId, defaultValue);
        this.addConfigValueEntry(ret.getId(), ret);
        return ret;
    }

    public ModConfigBuilder pushSubCategory(String categoryId) {
        ModConfigSubCategory subCategory = new ModConfigSubCategory(categoryId);
        subCategory.setTranslationKey(this.getCategoryKey(subCategory.getId()));
        this.subCategoryDeque.peek().addEntry(subCategory);
        this.subCategoryDeque.push(subCategory);
        return this;
    }

    public ModConfigBuilder popSubCategory() {
        assert this.subCategoryDeque.size() != 1;
        this.subCategoryDeque.pop();
        return this;
    }

    public Identifier getId() {
        return id;
    }

    public String getCategoryKey(String id) {
        return "config.%s.category.%s".formatted(this.id.getNamespace(), id);
    }

    public String getEntryKey(String id) {
        return "config.%s.entry.%s".formatted(this.id.getNamespace(), id);
    }

    public String getEntryDescriptionKey(String id) {
        return "config.%s.entry.%s.description".formatted(this.id.getNamespace(), id);
    }

    private void addConfigValueEntry(String id, ModConfigValue<?> entry) {
        final String entryKey = this.getEntryKey(id);
        final String descriptionKey = this.getEntryDescriptionKey(id);
        entry.setTranslationKey(entryKey);
        entry.setDescriptionKey(descriptionKey);

        this.currentCommentKey = descriptionKey;
        this.comments.put(this.currentCommentKey, this.currentComment);

        this.subCategoryDeque.peek().addEntry(entry);
        if (this.subCategoryDeque.size() <= 1) {
            throw new AssertionError();
        }
    }
}
