package GUI;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.FileManager.MissingFileSender;
import FileServerEntity.FileManager.TransitFile;
import FileServerEntity.Message.ClientMessage.ClientListFile;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.FileMessage;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Message.ServerMessage.MessageDelete;
import FileServerEntity.Message.ServerMessage.MessageNewFile;
import NameNode.FileServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Gus on 7/16/2015.
 */
public class ServerConnectionThread implements Runnable {
    private FileServer server;
    private ObjectOutputStream messageSender;
    private Socket echoSocket = null;

    private ObjectInputStream messageReader;


    public ServerConnectionThread(FileServer server) {
        this.server = server;

    }


    @Override
    public void run() {
        try {
            System.out.println("Connecting to "+ server.getSuccursaleIPAdresse().getHostAddress()+" "+server.getClientPort() );
            echoSocket = new Socket(server.getSuccursaleIPAdresse(),Integer.parseInt(server.getClientPort()) );
            messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
            messageReader = new ObjectInputStream(echoSocket.getInputStream());
            System.out.println("ClientConnected To "+server.getNom()+":"+server.getClientPort() );
            messageSender.writeObject(new ClientListFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message messageReceived;



        try
        {

            while ((messageReceived =(Message)messageReader.readObject() ) != null)
            {
                System.out.println("message de type " + messageReceived);
                if(MessageNewFile.class.isInstance(messageReceived)){


                }
                else if(FileMessage.class.isInstance(messageReceived)){

                }
                else if(InitSymchronizerMessage.class.isInstance(messageReceived)){

                    ClientConnector.getInstance().setListFileAvailaible(((InitSymchronizerMessage) messageReceived).getListContainedFile());
                }


            }
        } catch (SocketException e){

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


    public synchronized void sendMessage(Message messageTosSend) {

        try {


                messageSender.writeObject(messageTosSend);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
