package com.ejo.util.setting;

import com.ejo.util.file.FileCSVMap;
import com.ejo.util.math.Vector;
import com.ejo.util.time.DateTime;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SettingManager {

    public static final SettingManager DEFAULT_MANAGER = new SettingManager(new FileCSVMap<>("settings", "setting"));

    private final HashMap<String, Setting<?>> settingList = new HashMap<>();
    private final FileCSVMap<String,String> csvFile;

    public SettingManager(FileCSVMap<String,String> csvFile) {
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
        Map<String, String[]> loadedData = csvFile.load();

        for (String key : loadedData.keySet()) {
            try {
                Setting setting = settingList.get(key);
                String dataType = loadedData.get(key)[0];
                String savedValue = loadedData.get(key)[1];

                if (!setting.isLoadable()) continue;

                if (savedValue.equals("null")) setting.set(null);
                else switch (dataType) {
                    case "nullType" -> setting.set(null);
                    case "string" -> setting.set(savedValue.toString());
                    case "char" -> setting.set(savedValue.charAt(0));
                    case "byte" -> setting.set(Byte.parseByte(savedValue));
                    case "short" -> setting.set(Short.parseShort(savedValue));
                    case "integer" -> setting.set(Integer.parseInt(savedValue));
                    case "long" -> setting.set(Long.parseLong(savedValue));
                    case "float" -> setting.set(Float.parseFloat(savedValue));
                    case "double" -> setting.set(Double.parseDouble(savedValue));
                    case "boolean" -> setting.set(Boolean.parseBoolean(savedValue));

                    case "vector" -> setting.set(Converter.getVectorFromString(savedValue));
                    case "color" -> setting.set(Converter.getColorFromString(savedValue));
                    case "datetime" -> setting.set(Converter.getDateTimeFromString(savedValue));
                }

            } catch (Exception e) {
                //The specific setting could not get it's data set. Proceed to the next setting
                System.out.println("Could not load setting: " + key);
                continue;
            }
        }
        csvFile.unLoad();
        return true;
    }

    public FileCSVMap<String,String> getCSVFile() {
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
