package FileServerEntity.Server;

import NameNode.FileServer;

import java.net.InetAddress;

/**
 * Created by Gus on 6/11/2015.
 */
public class FileServerClient extends FileServer {
  private ResponseClientThread connectionThread;

    public FileServerClient(InetAddress succursaleIPAdresse,  String nom, String port) {
        super(succursaleIPAdresse,  nom, port);
    }

    public ResponseClientThread getConnectionThread() {
        return connectionThread;
    }

    public void setConnectionThread(ResponseClientThread connectionThread) {
        this.connectionThread = connectionThread;
    }
}
