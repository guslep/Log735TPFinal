package FileServerEntity.Message.ClientMessage;

import FileServerEntity.Message.Message;
import NameNode.FileServer;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientDispatchAnswer extends Message implements Serializable {

    private static final long serialVersionUID = -1941777947876870557L;
    private FileServer serverAvailaible;

    public ClientDispatchAnswer(FileServer serverAvailaible) {
        this.serverAvailaible = serverAvailaible;
    }

    public FileServer getServerAvailaible() {
        return serverAvailaible;
    }
}
