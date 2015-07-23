package FileServerEntity.FileManager;

import FileServerEntity.Server.ActiveFileServer;
import FileServerEntity.Server.FileServerClient;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Server.FileServerResponseThread;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class InitFileSynchronizer implements Runnable{

    FileServerResponseThread caller;

    public InitFileSynchronizer(FileServerResponseThread caller) {
        this.caller = caller;
    }

    public InitFileSynchronizer() {
        caller=null;
    }

    @Override
    public void run() {


        if(ActiveFileServer.getInstance().getListeSuccursale().size()==0){
           return;
        }
        if(caller==null){
            int size =ActiveFileServer.getInstance().getListeSuccursale().size();

            Random rand=new Random();
            int randomServer=(rand.nextInt(size));
            randomServer--;
            if(randomServer<0){
                randomServer=0;
            }
            int index=0;

            Iterator it = ActiveFileServer.getInstance().getListeSuccursale().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                FileServerClient currentClient = (FileServerClient) pair.getValue();
                if(index==randomServer){

                    synchWithServer(currentClient);
                }else{
                    index++;
                }

            }
        }else{
            synchWithServer(caller);
        }



    }

    private void synchWithServer(FileServerClient currentClient) {


        InitSymchronizerMessage message= new InitSymchronizerMessage( FileManager.getInstance().getListeFichiers(),FileManager.getInstance().getLocalDir());
        while (currentClient.getConnectionThread()==null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        currentClient.getConnectionThread().sendMessage(message);




    }

    private void synchWithServer(FileServerResponseThread caller) {


        InitSymchronizerMessage message= new InitSymchronizerMessage( FileManager.getInstance().getListeFichiers(),true,FileManager.getInstance().getLocalDir());

            caller.sendMessage(message);




    }


}
