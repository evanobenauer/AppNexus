package com.ejo.util.file;

public abstract class FileCSV<R> {

    protected final String folderPath;
    protected final String fileName;

    protected R data;

    public FileCSV(String folderPath, String fileName) {
        this.folderPath = folderPath;
        this.fileName = fileName.replace(".csv", "") + ".csv";
    }

    public FileCSV(String fileName) {
        this.folderPath = "";
        this.fileName = fileName;
    }

    //public abstract boolean save(O list);

    public abstract R load();

    public void unLoad() {
        data = null;
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
