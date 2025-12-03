package com.ejo.util.setting;

import com.ejo.util.file.FileCSVMap;
import com.ejo.util.math.Vector;
import com.ejo.util.time.DateTime;

import java.awt.*;
import java.util.HashMap;

public class SettingManager {

    public static final SettingManager DEFAULT_MANAGER = new SettingManager(new FileCSVMap("settings", "setting"));

    private final HashMap<String, Setting<?>> settingList = new HashMap<>();
    private final FileCSVMap csvFile;

    public SettingManager(FileCSVMap csvFile) {
        this.csvFile = csvFile;
    }

    @SuppressWarnings(value = "all")
    public boolean saveAll() {
        HashMap<String, String[]> map = new HashMap<>();

        for (Setting<?> setting : settingList.values()) {
            String key = setting.getKey();
            String value = setting.get() == null ? "null" : setting.get().toString().replace(",","|"); //Removed all commas and replaces them with vertical lines
            String type = setting.getType();
            map.put(key, new String[]{type, value});
        }

        return getCSVFile().save(map);
    }

    @SuppressWarnings(value = "all")
    public boolean loadAll() {
        csvFile.load();
        HashMap<String, String[]> loadedData = csvFile.getLoadedData();

        for (String key : loadedData.keySet()) {
            try {
                Setting setting = settingList.get(key);
                String dataType = loadedData.get(key)[0];
                String savedValue = loadedData.get(key)[1];

                if (!setting.isLoadable()) continue;

                if (savedValue.equals("null") || dataType.equals("nullType")) setting.set(null);
                else if (dataType.equals("string")) setting.set(savedValue.toString());
                else if (dataType.equals("char")) setting.set(savedValue.charAt(0));
                else if (dataType.equals("byte")) setting.set(Byte.parseByte(savedValue));
                else if (dataType.equals("short")) setting.set(Short.parseShort(savedValue));
                else if (dataType.equals("integer")) setting.set(Integer.parseInt(savedValue));
                else if (dataType.equals("long")) setting.set(Long.parseLong(savedValue));
                else if (dataType.equals("float")) setting.set(Float.parseFloat(savedValue));
                else if (dataType.equals("double")) setting.set(Double.parseDouble(savedValue));
                else if (dataType.equals("boolean")) setting.set(Boolean.parseBoolean(savedValue));

                else if (dataType.equals("vector")) setting.set(Converter.getVectorFromString(savedValue));
                else if (dataType.equals("color")) setting.set(Converter.getColorFromString(savedValue));
                else if (dataType.equals("datetime")) setting.set(Converter.getDateTimeFromString(savedValue));
            } catch (Exception e) {
                //The specific setting could not get it's data set. Proceed to the next setting
                System.out.println("Could not load setting: " + key);
                continue;
            }
        }
        csvFile.unLoad();
        return true;
    }

    public FileCSVMap getCSVFile() {
        return csvFile;
    }

    public Setting<?> getSetting(String key) {
        return settingList.get(key);
    }

    public HashMap<String, Setting<?>> getSettingList() {
        return settingList;
    }


    private static class Converter {

        private static Vector getVectorFromString(String vector) {
            String[] vecVals = vector
                    .replace("<","")
                    .replace(">","")
                    .split("\\|");
            return new Vector(Double.parseDouble(vecVals[0]),Double.parseDouble(vecVals[1]),Double.parseDouble(vecVals[2]));
        }

        private static Color getColorFromString(String color) {
            String[] colVals = color
                    .replace(Color.class.getName(),"")
                    .replace("[","")
                    .replace("]","")
                    .replace("r","")
                    .replace("g","")
                    .replace("b","")
                    .replace("=","")
                    .split("\\|");
            return new Color(Integer.parseInt(colVals[0]), Integer.parseInt(colVals[1]), Integer.parseInt(colVals[2]), 255);
        }

        private static DateTime getDateTimeFromString(String dateTime) {
            return null; //TODO: Implement once you make dateTime updated
        }
    }

}
