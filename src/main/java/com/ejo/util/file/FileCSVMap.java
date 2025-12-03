package com.ejo.util.file;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class FileCSVMap extends FileCSV<HashMap<String,String[]>> {

    public FileCSVMap(String folderPath, String fileName) {
        super(folderPath, fileName);
    }

    public FileCSVMap(String fileName) {
        super(fileName);
    }

    //@Override
    public <K, V> boolean save(HashMap<K, V[]> hashMap) {
    //public boolean save(HashMap<Object, Object[]> hashMap) {
        FileUtil.createFolderPath(folderPath); //Creates the folder path if it does not exist
        String outputFile = folderPath + (folderPath.isEmpty() ? "" : "/") + fileName.replace(".csv", "") + ".csv";
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
    public HashMap<String, String[]> load() {
        File file = new File(folderPath + (folderPath.equals("") ? "" : "/") + fileName.replace(".csv", "") + ".csv");
        HashMap<String, String[]> rawDataHashMap = new HashMap<>();

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
