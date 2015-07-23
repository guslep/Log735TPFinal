package FileServerEntity.Message.ServerMessage;

import FileServerEntity.Message.Message;

import java.io.Serializable;

/**
 * Created by Gus on 7/9/2015.
 */
public class MessageDelete extends Message implements Serializable{


	private static final long serialVersionUID = -1929727947836570957L;

    private String fileName;

    public MessageDelete( String fileName) {
        super();
        this.fileName = fileName;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
