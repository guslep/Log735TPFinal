package FileServerEntity.Message.ClientMessage;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.Message.Message;
import FileServerEntity.Server.ClientResponseThread;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientDeleteFile extends Message implements Serializable,MessageExecutor{
    private String nomFichier;

    public ClientDeleteFile(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    @Override
    public void execute(ClientResponseThread caller) {
        FileManager.getInstance().supprimerFichier(nomFichier);
    }
}
