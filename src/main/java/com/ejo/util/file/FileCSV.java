package com.ejo.util.file;

public abstract class FileCSV<SAVE,LOAD> {

    protected final String folderPath;
    protected final String fileName;

    protected LOAD data;

    public FileCSV(String folderPath, String fileName) {
        this.folderPath = folderPath;
        this.fileName = CSVUtil.getWithFileExtension(fileName);
    }

    public FileCSV(String fileName) {
        this("",fileName);
    }

    public abstract boolean save(SAVE list);

    public abstract LOAD load();

    public void generateFile() {
        FileUtil.generateFile(folderPath,fileName);
    }

    public void unLoad() {
        data = null;
        System.gc(); //Calls the garbage collector to force immediate delete of the data
    }


    public LOAD getLoadedData() {
        return data;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getFileName() {
        return fileName;
    }
}
