package FileServerEntity.FileManager;

import FileServerEntity.Server.FileServerListener;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Server.ResponseClientThread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class MissingFileSender implements Runnable{


    private InitSymchronizerMessage otherFilesServerState;
    private ResponseClientThread caller;

    public MissingFileSender(InitSymchronizerMessage otherFilesServerState, ResponseClientThread responseClientThread) {

        this.otherFilesServerState = otherFilesServerState;
        this.caller=responseClientThread;
    }

    @Override
    public void run()
    {
        ArrayList<File> currentFile=FileManager.getInstance().getListeFichiers();
        HashMap<String,File> hashAllFile= new HashMap<String, File>();
            String rootDir=FileManager.getInstance().getLocalDir();



        for(File file:otherFilesServerState.getListContainedFile()){


            hashAllFile.put(file.getPath().replace(otherFilesServerState.getRootDirectory()+"\\",""),file);

        }

        for(File fileDistantFileServer:currentFile){

            if(!fileDistantFileServer.isDirectory())
            {
            String rootDirDistant=otherFilesServerState.getRootDirectory();
                String test=rootDir;

                String relativePath=fileDistantFileServer.getPath().replace(rootDir+"\\","");
                File fileFound=(File) hashAllFile.get(relativePath);
                Boolean directory=fileDistantFileServer.isDirectory();
                if(fileFound==null&&!fileDistantFileServer.isDirectory()){
                   new Thread(new FileServerListener(fileDistantFileServer,relativePath,caller)).start();
                    System.out.println("sending " + fileDistantFileServer);

                }else if(fileFound.getTotalSpace()!=fileFound.getTotalSpace()){
                    //TODO: peut être faire dequoi si le fichier existe déjà et qui sont différent genre sender un delete ou whatever


                }

            }
        }

        //TODO: envoyer un InitFileSynchroniser au client qui vient de l'envoyer et setter un flag pour dire qui doit push to all
        //TODO: implementer le thetering à 10 mb/s

        /*
        We don't wnat to end up in an infinite loop of updates
         */
        if(!otherFilesServerState.getSynchWithNewServer()){
            new Thread(new InitFileSynchronizer(caller)).start();

        }

    }
}
