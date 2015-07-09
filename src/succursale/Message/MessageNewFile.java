package succursale.Message;

/**
 * Created by Gus on 7/9/2015.
 */
public class MessageNewFile extends Message{

    private int fileLength;
    private String fileName;

    public MessageNewFile(int fileLength, String fileName) {
        super();
        this.fileLength = fileLength;
        this.fileName = fileName;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
