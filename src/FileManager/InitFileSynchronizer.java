package FileManager;

import succursale.ActiveFileServer;
import succursale.FileServerClient;
import succursale.Message.InitSymchronizerMessage;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class InitFileSynchronizer implements Runnable{



    @Override
    public void run() {

        if(ActiveFileServer.getInstance().getListeSuccursale().size()==0){
           return;
        }
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


}
