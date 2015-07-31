package FileServerEntity.Message.ClientMessage;

import FileServerEntity.FileManager.TransitFile;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.MessageFileWriteAttempt;
import FileServerEntity.Server.ActiveFileServer;
import FileServerEntity.Server.ClientResponseThread;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientAddFile extends Message implements Serializable,MessageExecutor {
//TODO : imp
    private String fileName;
    private int byteLength;

    public ClientAddFile(String fileName, int byteLength) {
        this.fileName = fileName;
        this.byteLength = byteLength;
    }

    @Override
    public void execute(ClientResponseThread caller) {

       if(ActiveFileServer.getInstance().reserveFileName(fileName)){
           TransitFile transit=new TransitFile(caller,fileName,byteLength);
           ActiveFileServer.getInstance().newVotePending(transit);
           ActiveFileServer.getInstance().pushToAllServer(new MessageFileWriteAttempt(fileName));
           ActiveFileServer.getInstance().voteReceived(fileName);

        caller.getFileBeingWritten().put(transit.getNom(), transit);

        }else{
            caller.sendMessage(new ErrorUploading(fileName));
        }

    }
}
