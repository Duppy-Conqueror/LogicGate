package net.duppy_conqueror.logic_gate.config.values;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.duppy_conqueror.logic_gate.config.ModConfigBuilder;

import java.util.Objects;

public class IntegerModConfigValue extends ModConfigValue<Integer> {
    private final Integer minValue;
    private final Integer maxValue;

    public IntegerModConfigValue(String id, Integer defaultValue, Integer minValue, Integer maxValue) {
        super(id, defaultValue);
        this.minValue = Objects.requireNonNull(minValue);
        this.maxValue = Objects.requireNonNull(maxValue);
        Preconditions.checkState(isValid(defaultValue), "Config defaults are invalid");
    }

    @Override
    public boolean isValid(Integer value) {
        return value >= this.minValue && value <= this.maxValue;
    }

    @Override
    public void loadFromJson(JsonObject element) {
        if (element.has(this.id)) {
            try {
                this.value = element.get(this.id).getAsInt();
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

    public Integer getMax() {
        return this.maxValue;
    }

    public Integer getMin() {
        return this.minValue;
    }
}
