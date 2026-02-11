package com.ejo.util.file;

import java.io.*;
import java.util.ArrayList;

public class FileCSVList<T> extends FileCSV<ArrayList<T[]>,ArrayList<String[]>> {

    public FileCSVList(String folderPath, String fileName) {
        super(folderPath, fileName);
    }

    public FileCSVList(String fileName) {
        super(fileName);
    }

    @Override
    public boolean save(ArrayList<T[]> list) {
        FileUtil.createFolderPath(folderPath); //Creates the folder path if it does not exist
        String outputFile = FileUtil.getFilePath(folderPath,fileName);
        try {
            FileWriter writer = new FileWriter(outputFile);
            ArrayList<String[]> tempList = new ArrayList<>();

            for (T[] rowData : list) {
                tempList.add((String[])rowData);
                writer.write(String.join(",", (String[]) rowData) + "\n");
            }
            data = tempList;
            writer.close();

            return true;
        } catch (IOException e) {
            System.out.println("Error writing data to " + outputFile);
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public ArrayList<String[]> load() {
        File file = new File(FileUtil.getFilePath(folderPath,fileName));
        ArrayList<String[]> rawDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                rawDataList.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = rawDataList;
        return rawDataList;
    }

}
