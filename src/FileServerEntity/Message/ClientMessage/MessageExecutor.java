package FileServerEntity.Message.ClientMessage;

import FileServerEntity.Server.ClientResponseThread;

/**
 * Created by Guillaume on 2015-07-12.
 */
public interface MessageExecutor  {

    public void execute(ClientResponseThread caller);
}
