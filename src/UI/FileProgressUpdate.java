package UI;

/**
 * Created by Gus on 7/26/2015.
 */
public class FileProgressUpdate {

    private int percent;
    private String fileName;

    public FileProgressUpdate(int percent, String fileName) {
        this.percent = percent;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPercent() {
        return percent;
    }
}
