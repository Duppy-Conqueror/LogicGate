package net.duppy_conqueror.logic_gate.config.values;

import com.google.gson.JsonObject;
import net.duppy_conqueror.logic_gate.config.ModConfigBuilder;

public class BooleanModConfigValue extends ModConfigValue<Boolean> {
    public BooleanModConfigValue(String id, Boolean defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public boolean isValid(Boolean value) {
        return true;
    }

    @Override
    public void loadFromJson(JsonObject element) {
        if (element.has(this.id)) {
            try {
                this.value = element.get(this.id).getAsBoolean();
                if (!this.isValid(this.value)) {
                    this.value = defaultValue;
                }
                return;
            } catch (Exception ignored) {
            }
            ModConfigBuilder.LOGGER.warn("Config file had incorrect entry {}, correcting", this.id);
        } else {
            ModConfigBuilder.LOGGER.warn("Config file had missing entry {}", this.id);
        }
    }

    @Override
    public void saveToJson(JsonObject object) {
        if (this.value == null) this.value = defaultValue;
        object.addProperty(this.id, this.value);
    }
}
