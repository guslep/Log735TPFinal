package GUI;

import FileServerEntity.Message.ClientMessage.ClientAcceptUpload;
import FileServerEntity.Message.ClientMessage.ClientAddFile;
import FileServerEntity.Message.ClientMessage.ClientUploadPartFile;
import FileServerEntity.Message.ClientMessage.ErrorUploading;
import UI.FileProgressUpdate;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Gus on 7/23/2015.
 */
public class ThreadUploadFile implements Runnable, Observer {
    private final static int NBBYTEPARMESSAGE = 2048;
    private final static int  NBBYTEMAXTHETHERYNG=10000000;

    private File fileUploaded;
    private String fileName;
    private ServerConnectionThread serverConnectedTo;
    private JProgressBar progressBar;
    private  boolean stopUploading=false;
    private boolean uploadAccepted=false;

    public ThreadUploadFile(File fileUploaded, String path) {
        this.fileUploaded = fileUploaded;
        this.fileName = path+fileUploaded.getName();
        serverConnectedTo=ClientConnector.getInstance().getServerConnectedTo();
        serverConnectedTo.addObserver(this);
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
        while(hack&&!stopUploading){
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

        serverConnectedTo.deleteObserver(this);

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

        while (!uploadAccepted){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // cr�ation des messages pour les diff�rents byte array(FileMessage)
        Date lastMesureTook=new Date();
        int numberPacketSent=0;
        for (int i = 0; i < listeBytes.size(); i++) {
            if(stopUploading){
                return;
            }

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

    @Override
    public void update(Observable o, Object arg) {
        if(arg!=null&& ErrorUploading.class.isInstance(arg)){
            ErrorUploading msg=(ErrorUploading)arg;
            if(fileName.equals(msg.getFilename())){
                this.stopUploading=true;
            }
        }
        else if(arg!=null&& ClientAcceptUpload.class.isInstance(arg)){
            ClientAcceptUpload message=(ClientAcceptUpload)arg;
            if (message.getFilename().equals(fileName)){
                this.uploadAccepted=true;
            }

        }
    }
}
