package FileServerEntity.Message.ServerMessage;

import FileServerEntity.Message.Message;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class InitSymchronizerMessage extends Message implements Serializable {
    private static final long serialVersionUID = -1949727947876870557L;

    private ArrayList<File> listContainedFile;
    private Boolean SynchWithNewServer=false;
    private String rootDirectory;





    public InitSymchronizerMessage(ArrayList<File> listeFichiers, String localDir) {
        this.listContainedFile = listeFichiers;
        this.rootDirectory = localDir;
    }

    public InitSymchronizerMessage(ArrayList<File> listContainedFile, Boolean synchWithNewServer, String rootDirectory) {
        this.listContainedFile = listContainedFile;
        SynchWithNewServer = synchWithNewServer;
        this.rootDirectory = rootDirectory;
    }

    public ArrayList<File> getListContainedFile() {
        return listContainedFile;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }


    public Boolean getSynchWithNewServer() {
        return SynchWithNewServer;
    }
}
