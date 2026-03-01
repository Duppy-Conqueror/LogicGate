package net.duppy_conqueror.logic_gate.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ModConfigHolder {
    private final Identifier id;

    private final Path filePath;
    private final String fileName;
    private final File file;
    private boolean initialized = false;

    private final ModConfigSubCategory mainEntry;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ModConfigHolder(Identifier id, ModConfigSubCategory mainEntry) {
        this.id = id;
        this.mainEntry = mainEntry;
        this.fileName = id.getNamespace() + ".json";
        this.filePath = FabricLoader.getInstance().getConfigDir().resolve(fileName);
        this.file = this.getFullPath().toFile();
    }

    public boolean isLoaded() {
        return this.initialized;
    }

    public void loadConfig() {
        if (this.isLoaded()) {
            return;
        }
        try {
            JsonElement config = null;

            if (file.exists() && file.isFile()) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    config = GSON.fromJson(bufferedReader, JsonElement.class);
                }
            }

            if (config instanceof JsonObject jo) {
                mainEntry.getEntries().forEach(entry -> entry.loadFromJson(jo));
            }

            if (!this.initialized) {
                this.initialized = true;
                this.saveConfig();
                ModConfigBuilder.LOGGER.info("Loaded config {}", this.getFileName());
            }
        } catch (Exception e) {
            throw new ConfigLoadingException(this, e);
        }
    }

    public void saveConfig() {
        try (FileOutputStream stream = new FileOutputStream(this.file);
             Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            JsonObject jo = new JsonObject();
            mainEntry.getEntries().forEach(e -> e.saveToJson(jo));
            GSON.toJson(jo, writer);
        } catch (IOException e) {
            ModConfigBuilder.LOGGER.error("Failed to save config {}:", this.getFileName(), e);
        }
    }

    public ModConfigSubCategory getMainEntry() {
        return this.mainEntry;
    }

    public String getModId() {
        return id.getNamespace();
    }

    public Identifier getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Path getFullPath() {
        return this.filePath;
    }

    public static class ConfigLoadingException extends RuntimeException {
        public ConfigLoadingException(ModConfigHolder holder, Exception cause) {
            super("Failed to load config file " + holder.getFileName() + " for mod " + holder.getModId() + ". Try deleting it", cause);
        }
    }
}