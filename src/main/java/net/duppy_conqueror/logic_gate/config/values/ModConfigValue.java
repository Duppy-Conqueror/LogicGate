package net.duppy_conqueror.logic_gate.config.values;

import net.duppy_conqueror.logic_gate.config.ModConfigEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class ModConfigValue<T> extends ModConfigEntry implements Supplier<T> {
    protected final T defaultValue;
    protected T value;
    private String translationKey;
    private String descriptionKey;

    protected ModConfigValue(String id, T defaultValue) {
        super(id);
        this.defaultValue = defaultValue;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public abstract boolean isValid(T value);

    public void set(T newValue) {
        this.value = newValue;
    }

    @Override
    public T get() {
        return this.value;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public Component getTranslation() {
        return Component.translatable(this.translationKey);
    }

    @Nullable
    public Component getDescription() {
        return this.descriptionKey == null ? null : Component.translatable(this.descriptionKey);
    }

}
