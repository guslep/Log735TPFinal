package FileServerEntity.Message.ClientMessage;

import FileServerEntity.Message.Message;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-30.
 */
public class ClientAcceptUpload extends Message implements Serializable {

    private String filename;

    public ClientAcceptUpload(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}