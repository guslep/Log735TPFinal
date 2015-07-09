package succursale.Message;

import Banque.FileServer;

import java.io.Serializable;

/**
 * Created by Gus on 6/11/2015.
 */
public   class NewFileServerMessage extends Message implements Serializable {

    FileServer fileServer;

    public NewFileServerMessage(FileServer fileServer) {
        this.fileServer = fileServer;
    }

    public FileServer getFileServer() {
        return fileServer;
    }
}
