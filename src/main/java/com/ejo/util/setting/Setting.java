package com.ejo.util.setting;

import com.ejo.util.math.Vector;
import com.ejo.util.time.DateTime;

import java.awt.*;

public class Setting<T> extends Container<T> {

    private final SettingManager settingManager;

    private final T defaultVal;

    private final String key;


    public Setting(SettingManager settingManager, String key, T defaultVal) {
        super(defaultVal);
        this.key = key;
        this.defaultVal = defaultVal;

        this.settingManager = settingManager;
        this.settingManager.getSettingList().put(key, this);
    }

    public Setting(String key, T defaultVal) {
        this(SettingManager.DEFAULT_MANAGER, key, defaultVal);
    }


    public Setting<T> reset() {
        set(getDefaultValue());
        return this;
    }


    public String getType() {
        if (getDefaultValue() instanceof String || get() instanceof String) return "string";
        else if (getDefaultValue() instanceof Character || get() instanceof Character) return "char";
        else if (getDefaultValue() instanceof Byte || get() instanceof Byte) return "byte";
        else if (getDefaultValue() instanceof Short || get() instanceof Short) return "short";
        else if (getDefaultValue() instanceof Integer || get() instanceof Integer) return "integer";
        else if (getDefaultValue() instanceof Long || get() instanceof Long) return "long";
        else if (getDefaultValue() instanceof Float || get() instanceof Float) return "float";
        else if (getDefaultValue() instanceof Double || get() instanceof Double) return "double";
        else if (getDefaultValue() instanceof Boolean || get() instanceof Boolean) return "boolean";
        else if (getDefaultValue() instanceof Vector || get() instanceof Vector) return "vector"; //Special Type
        else if (getDefaultValue() instanceof Color || get() instanceof Color) return "color"; //Special Type
        else if (getDefaultValue() instanceof DateTime || get() instanceof DateTime) return "dateTime"; //Special Type
        else if (getDefaultValue() == null || get() == null) return "nullType";
        else return "unloadable";
    }

    /**
     * This method should only return false if a defined type is not primitive or a specified type like Vector or Color
     * @return
     */
    public boolean isLoadable() {
        return !getType().equals("unloadable");
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultVal;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }


    @Override
    public String toString() {
        return "[" + getKey() + ": " + get() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Setting<?> setting)) return false;
        return setting.toString().equals(toString()) && setting.getDefaultValue().equals(getDefaultValue());
    }

}
