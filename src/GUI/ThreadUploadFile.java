package GUI;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.Message.ClientMessage.ClientAddFile;
import FileServerEntity.Message.ClientMessage.ClientUploadPartFile;
import FileServerEntity.Message.ServerMessage.FileMessage;
import FileServerEntity.Message.ServerMessage.MessageNewFile;
import FileServerEntity.Server.ActiveFileServer;
import UI.FileProgressUpdate;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gus on 7/23/2015.
 */
public class ThreadUploadFile implements Runnable{
    private final static int NBBYTEPARMESSAGE = 2048;
    private final static int  NBBYTEMAXTHETHERYNG=10000000;

    private File fileUploaded;
    private String fileName;
    private ServerConnectionThread serverConnectedTo;
    private JProgressBar progressBar;

    public ThreadUploadFile(File fileUploaded, String path) {
        this.fileUploaded = fileUploaded;
        this.fileName = path+fileUploaded.getName();
        serverConnectedTo=ClientConnector.getInstance().getServerConnectedTo();
    }

    @Override
    public void run()
    {

        try {
            serverConnectedTo.sendMessage(new ClientAddFile(fileName,(int)fileUploaded.length()));

        }catch (Exception e){
            System.err.print(e);
            return;
        }





        boolean hack=true;
        while(hack){
            try{
                hack=false;
                int sfe=(int)fileUploaded.length();
                copy((int)fileUploaded.length());
            }catch(Exception e){
                try {
                    Thread.sleep(1000);
                    hack=true;
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }





        // si succ�s, retourne true

    }
    // >MaxValue



    private void copy(int fileSize){


        ArrayList<byte[]> listeBytes = new ArrayList<byte[]>();

        Path path = Paths.get(fileUploaded.getAbsolutePath());
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        int remaining=0;
        if(fileSize % NBBYTEPARMESSAGE!=0){
            remaining=1;
        }


        // split des bytes en array de NBBYTEPARMESSAGE
        for (int i = 0; i < (fileSize / NBBYTEPARMESSAGE)+remaining; i++) {
            byte[] bytesSplit = null;
            if(i == (fileSize / NBBYTEPARMESSAGE)+remaining-1){
                bytesSplit = new byte[fileSize % NBBYTEPARMESSAGE];
            }
            else{
                bytesSplit = new byte[NBBYTEPARMESSAGE];
            }


            for (int j = 0; j < bytesSplit.length; j++) {
                bytesSplit[j] = data[i*NBBYTEPARMESSAGE + j];
            }
            // ajout a la liste pour l'envoi
            listeBytes.add(bytesSplit);
        }

        // new



        // cr�ation des messages pour les diff�rents byte array(FileMessage)
        Date lastMesureTook=new Date();
        int numberPacketSent=0;
        for (int i = 0; i < listeBytes.size(); i++) {

            ClientUploadPartFile messageEnvoi=new ClientUploadPartFile(listeBytes.get(i),fileName,(i * NBBYTEPARMESSAGE));

            serverConnectedTo.sendMessage(messageEnvoi);
            FileProgressUpdate update=new FileProgressUpdate((int) ((double) i / (double)(listeBytes.size()-1)*100),fileName);

            ClientConnector.getInstance().updateProgressBar(update);


            numberPacketSent++;
            Date now=new Date();
            if(numberPacketSent*NBBYTEPARMESSAGE>NBBYTEMAXTHETHERYNG&&now.getTime()-lastMesureTook.getTime()<1000){
                try {
                    System.out.println("Hey, tethering the connection, NbByteParMessage: " + (numberPacketSent*NBBYTEPARMESSAGE) + " NbByteMaxTethering: " + NBBYTEMAXTHETHERYNG);
                    Thread.sleep(1100-(now.getTime() - lastMesureTook.getTime()));
                    numberPacketSent=0;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(now.getTime()-lastMesureTook.getTime()>1000){
                lastMesureTook=new Date();
                //numberPacketSent=0;
            }

        }
        //message final pour terminer la connexion

        ClientUploadPartFile messageFinal = new ClientUploadPartFile(null, fileName, 0);
        serverConnectedTo.sendMessage(messageFinal);


    }
}
