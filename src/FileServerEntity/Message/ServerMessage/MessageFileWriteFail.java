package FileServerEntity.Message.ServerMessage;

import FileServerEntity.Message.Message;

import java.io.Serializable;

/**
 * Created by Gus on 7/28/2015.
 */
public class MessageFileWriteFail extends Message implements Serializable {
    String fileName;

    public MessageFileWriteFail(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
