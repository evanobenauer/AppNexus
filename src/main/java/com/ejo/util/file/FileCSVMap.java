package com.ejo.util.file;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileCSVMap<K, V> extends FileCSV<Map<K,V[]>,Map<String,String[]>> {

    public FileCSVMap(String folderPath, String fileName) {
        super(folderPath, fileName);
    }

    public FileCSVMap(String fileName) {
        super(fileName);
    }

    @Override
    public boolean save(Map<K, V[]> hashMap) {
        FileUtil.createFolderPath(folderPath); //Creates the folder path if it does not exist
        String outputFile = FileUtil.getFilePath(folderPath,fileName);
        try {
            FileWriter writer = new FileWriter(outputFile);
            HashMap<String, String[]> tempMap = new HashMap<>();

            for (K key : hashMap.keySet()) {
                tempMap.put(key.toString(), (String[]) hashMap.get(key));
                writer.write(key + "," + Arrays.toString(hashMap.get(key)).replace("[", "").replace("]", "").replace(", ",",") + "\n");
            }
            data = tempMap;
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing data to " + outputFile);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, String[]> load() {
        File file = new File(FileUtil.getFilePath(folderPath,fileName));
        Map<String, String[]> rawDataHashMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                String key = row[0];
                String[] rowCut = line.replace(key + ",", "").split(",");
                rawDataHashMap.put(row[0], rowCut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = rawDataHashMap;
        return rawDataHashMap;
    }

}
