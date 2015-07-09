package succursale.Message;

import succursale.ResponseClientThread;

/**
 * Created by Gus on 7/9/2015.
 */
public class AsyncFileSender implements Runnable
{
    private Message messageTosend;
    private ResponseClientThread connection;

    public AsyncFileSender(Message messageTosend, ResponseClientThread connection) {
        this.messageTosend = messageTosend;
        this.connection = connection;
    }

    @Override
    public void run() {
    	



        connection.sendMessage(messageTosend);
        

    }
}
