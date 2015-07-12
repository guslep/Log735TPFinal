package FileManager;

import succursale.FileServerListener;
import succursale.Message.InitSymchronizerMessage;
import succursale.ResponseClientThread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class MissingFileSender implements Runnable{


    private InitSymchronizerMessage otherFilesServerState;

    public MissingFileSender(InitSymchronizerMessage otherFilesServerState) {

        this.otherFilesServerState = otherFilesServerState;
    }

    @Override
    public void run() {
        ArrayList<File> currentFile=FileManager.getInstance().getListeFichiers();
        HashMap<String,File> hashAllFile= new HashMap<String, File>();
            String rootDir=FileManager.getInstance().getLocalDir();



        for(File file:otherFilesServerState.getListContainedFile()){


            hashAllFile.put(file.getPath().replace(otherFilesServerState.getRootDirectory(),""),file);

        }

        for(File fileDistantFileServer:currentFile){

            if(!fileDistantFileServer.isDirectory())
            {
            String rootDirDistant=otherFilesServerState.getRootDirectory();
                String test=rootDir;

                String relativePath=fileDistantFileServer.getPath().replace(rootDir,"");
                File fileFound=(File) hashAllFile.get(relativePath);
                if(fileFound==null){
                   new Thread(new FileServerListener(fileDistantFileServer,relativePath)).start();
                    System.out.println("sending "+fileDistantFileServer);

                }else if(fileFound.getTotalSpace()!=fileFound.getTotalSpace()){
                    //TODO: peut être faire dequoi si le fichier existe déjà et qui sont différent genre sender un delete ou whatever


                }

            }
        }



    }
}
