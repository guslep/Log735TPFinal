package FileServerEntity.Message.ServerMessage;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.Message.Message;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class InitSymchronizerMessage extends Message implements Serializable {
    private static final long serialVersionUID = -1949727947876870557L;

    private ArrayList<File> listContainedFile;
    private HashMap<String,String> deletedFile;
    private Boolean shouldNotSyncWithServer =false;
    private String rootDirectory;





    public InitSymchronizerMessage(ArrayList<File> listeFichiers, String localDir) {
        this.listContainedFile = listeFichiers;
        this.rootDirectory = localDir;
        this.deletedFile= FileManager.getInstance().getNomFichierDelete();

    }

    public InitSymchronizerMessage(ArrayList<File> listContainedFile, Boolean shouldNotSyncWithServer, String rootDirectory) {
        this.listContainedFile = listContainedFile;
        this.shouldNotSyncWithServer = shouldNotSyncWithServer;
        this.rootDirectory = rootDirectory;
    }

    public ArrayList<File> getListContainedFile() {
        return listContainedFile;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }


    public Boolean getShouldNotSyncWithServer() {
        return shouldNotSyncWithServer;
    }

    public HashMap<String,String> getDeletedFile() {
        return deletedFile;
    }
}
