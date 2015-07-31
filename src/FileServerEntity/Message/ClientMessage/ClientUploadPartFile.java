package FileServerEntity.Message.ClientMessage;

import FileServerEntity.FileManager.TransitFile;
import FileServerEntity.Message.Message;
import FileServerEntity.Server.ClientResponseThread;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientUploadPartFile extends Message implements Serializable,MessageExecutor {
    //Todo même stratégie que lorsque le serveur recoit des fichiers d'un autre serveur mais s'assurer de pas mettre le fichier dans le hashmap qui trigger pas les updates

    private byte[] byteArray;
    private String filename;
    private int position;

    public ClientUploadPartFile(byte[] byteArray, String filename, int position) {
        this.byteArray = byteArray;
        this.filename = filename;
        this.position = position;
    }


    @Override
    public void execute(ClientResponseThread caller) {
        TransitFile file=caller.getFileBeingWritten().get(filename);

       if(file!=null) {
           file.addByte(byteArray, position);
       }else{
           caller.fileWasWritten(filename);
       }

    }
}
