package FileServerEntity.Message.ServerMessage;

import FileServerEntity.Message.Message;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-30.
 */
public class MessageFileWriteAccepted extends Message implements Serializable
{
    String fileName;

    public MessageFileWriteAccepted(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}