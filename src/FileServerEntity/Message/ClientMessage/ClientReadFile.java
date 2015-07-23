package FileServerEntity.Message.ClientMessage;

import FileServerEntity.Message.Message;
import FileServerEntity.Server.ClientResponseThread;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientReadFile extends Message implements Serializable,MessageExecutor {

    String fileName;

    public ClientReadFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute(ClientResponseThread caller) {

    }
}
