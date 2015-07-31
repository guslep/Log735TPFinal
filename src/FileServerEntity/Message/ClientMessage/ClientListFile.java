package FileServerEntity.Message.ClientMessage;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Server.ClientResponseThread;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientListFile extends Message implements Serializable,MessageExecutor {


    @Override
    public void execute(ClientResponseThread caller) {
        InitSymchronizerMessage allFile=new InitSymchronizerMessage(FileManager.getInstance().getListeFichiers(),false,FileManager.getInstance().getLocalDir());
        caller.sendMessage(allFile);
    }
}
