package com.ejo.util.file;

public abstract class FileCSV<R> {

    protected final String folderPath;
    protected final String fileName;

    protected R data;

    public FileCSV(String folderPath, String fileName) {
        this.folderPath = folderPath;
        this.fileName = CSVUtil.getWithFileExtension(fileName);
    }

    public FileCSV(String fileName) {
        this("",fileName);
    }

    public void generateFile() {
        FileUtil.generateFile(folderPath,fileName);
    }

    //TODO: Figure out how to create an abstract save method
    //public abstract boolean save(R list);

    public abstract R load();

    public void unLoad() {
        data = null;
        System.gc(); //Calls the garbage collector to force immediate delete of the data
    }

    public R getLoadedData() {
        return data;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getFileName() {
        return fileName;
    }
}
