package FileServerEntity.Message.ClientMessage;

import FileServerEntity.Message.Message;

import java.io.Serializable;

/**
 * Created by Gus on 7/28/2015.
 */
public class ErrorUploading extends Message implements Serializable {

    private String filename;

    public ErrorUploading(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
