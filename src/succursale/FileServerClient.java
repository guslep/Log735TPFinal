package succursale;

import Banque.FileServer;

import java.net.InetAddress;

/**
 * Created by Gus on 6/11/2015.
 */
public class FileServerClient extends FileServer {
  private ResponseClientThread connectionThread;

    public FileServerClient(InetAddress succursaleIPAdresse, int montant, String nom, String port) {
        super(succursaleIPAdresse, montant, nom, port);
    }

    public ResponseClientThread getConnectionThread() {
        return connectionThread;
    }

    public void setConnectionThread(ResponseClientThread connectionThread) {
        this.connectionThread = connectionThread;
    }
}
