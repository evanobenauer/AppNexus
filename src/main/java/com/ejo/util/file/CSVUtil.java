package com.ejo.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CSVUtil {

    /**
     * Combines all CSV files in a directory into one CSV file of a chosen directory. All combined files are deleted after the combination.
     *
     * @param combineDirectory
     * @param outputDirectory
     * @param outputFileName
     * @return
     */
    public static boolean combineCSVFiles(String combineDirectory, String outputDirectory, String outputFileName) {
        try {
            FileUtil.createFolderPath(outputDirectory);
            List<String> files = getCSVFilesInDirectory(combineDirectory);
            FileWriter writer = new FileWriter(FileUtil.getFilePath(outputDirectory,getWithFileExtension(outputFileName)));

            for (String file : files) {
                FileReader fileReader = new FileReader(combineDirectory + "/" + file);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.append(line);
                    writer.append("\n");
                }
                reader.close();
                fileReader.close();
                FileUtil.deleteFile(combineDirectory, file);
            }
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Could not combine CSV Files");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method inputs a CSV file and deletes all duplicate rows, leaving only 1 copy of each row
     *
     * @param directory
     * @param name
     * @return
     */
    public static boolean clearDuplicateCSVRows(String directory, String name) {
        HashSet<String> uniqueValues = new HashSet<>();
        try {
            FileReader reader = new FileReader(FileUtil.getFilePath(directory,getWithFileExtension(name)));
            BufferedReader br = new BufferedReader(reader);
            String line;
            FileWriter writer = new FileWriter(FileUtil.getFilePath(directory,getWithFileExtension(name + "_temp")));
            while ((line = br.readLine()) != null) {
                if (uniqueValues.add(line)) {
                    writer.append(line);
                    writer.append("\n");
                }
            }
            writer.flush();
            writer.close();
            reader.close();
            FileUtil.deleteFile(directory, getWithFileExtension(name));
            FileUtil.renameFile(directory, getWithFileExtension(name + "_temp"), getWithFileExtension(name));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method returns a list of all CSV files in a specified directory
     *
     * @param directory
     * @return
     */
    public static List<String> getCSVFilesInDirectory(String directory) {
        File folder = new File(directory);
        List<String> files = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".csv")) {
                files.add(file.getName());
            }
        }
        return files;
    }

    public static String getWithFileExtension(String name) {
        return name.replace(".csv", "") + ".csv";
    }

}
