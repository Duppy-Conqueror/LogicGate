package net.duppy_conqueror.logic_gate.block.enums;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public enum LogicGateMode implements StringRepresentable {
    BUFFER("buffer"),
    NOT("not"),
    OR("or"),
    AND("and"),
    XOR("xor"),
    NOR("nor"),
    NAND("nand"),
    IMPLY("imply"),
    NIMPLY("nimply");

    private static Iterator<LogicGateMode> getIterator(boolean reverse) {
        if (reverse) {
            List<LogicGateMode> enumValues = Arrays.asList(values());
            Collections.reverse(enumValues);
            return enumValues.iterator();
        } else {
            return Arrays.asList(values()).iterator();
        }
    }

    public static LogicGateMode cycle(LogicGateMode currentMode, boolean reverse) {
        Iterator<LogicGateMode> iterator = getIterator(reverse);
        while (iterator.hasNext()) {
            if (iterator.next().equals(currentMode)) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
                return getIterator(reverse).next();
            }
        }
        return iterator.next();
    }

    private final String name;

    private LogicGateMode(String name) {
        this.name = name;
    }

    public boolean isSingleInput() {
        return this == BUFFER || this == NOT;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }
}