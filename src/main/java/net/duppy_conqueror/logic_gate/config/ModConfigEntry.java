package net.duppy_conqueror.logic_gate.config;

import com.google.gson.JsonObject;

public abstract class ModConfigEntry {
    protected String id;

    protected ModConfigEntry(String entryId) {
        this.id = entryId;
    }

    public abstract void loadFromJson(JsonObject object);

    public abstract void saveToJson(JsonObject object);

    public String getId() {
        return this.id;
    }
}
