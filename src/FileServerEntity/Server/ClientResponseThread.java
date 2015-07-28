package FileServerEntity.Server;

import FileServerEntity.FileManager.MissingFileSender;
import FileServerEntity.FileManager.TransitFile;
import FileServerEntity.Message.ClientMessage.MessageExecutor;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.FileMessage;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Message.ServerMessage.MessageNewFile;
import FileServerEntity.Message.ServerMessage.SynchMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientResponseThread implements Runnable {
    private boolean connectionDestroyed=false;

    ObjectOutputStream messageSender;
    Socket echoSocket = null;

    ObjectInputStream messageReader;
    private HashMap<String, TransitFile> fileBeingWritten = new HashMap<String, TransitFile>();

    public ClientResponseThread(Socket sucursaleSocket) {

        echoSocket = sucursaleSocket;
        try {
            messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
            messageReader = new ObjectInputStream((echoSocket.getInputStream()));


            ActiveFileServer.getInstance().ajouterClient(this);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void run() {
        Message messageReceived;



        try
        {

            while ((messageReceived =(Message)messageReader.readObject() ) != null)
            {
                System.out.println("message de type " + messageReceived);
                ((MessageExecutor)messageReceived).execute(this);



            }
        } catch (SocketException e){
            connectionDestroyed=true;
            System.out.println("erreur dans la connection");
            try {
                messageSender.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("erreur dans l'objet");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * envoie une message à une succursale
     *
     * @param messageTosSend
     */
    public synchronized void sendMessage(Message messageTosSend) {

        try {

            if(!connectionDestroyed){
                messageSender.writeObject(messageTosSend);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, TransitFile> getFileBeingWritten() {
        return fileBeingWritten;
    }

    public void fileWasWritten(String fileName) {

        fileBeingWritten.remove(fileName);
    }

    public boolean isConnectionDestroyed() {
        return connectionDestroyed;
    }
}

