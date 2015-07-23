package GUI;

import FileServerEntity.Message.ClientMessage.ClientDispatchAnswer;
import FileServerEntity.Message.ClientMessage.ClientDispatchRequest;
import FileServerEntity.Message.ClientMessage.MessageExecutor;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.SynchMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Gus on 7/16/2015.
 */
public class SystemConnector implements Runnable {

    private boolean connectionDestroyed=false;

    private ObjectOutputStream messageSender;
    private Socket echoSocket = null;

    private ObjectInputStream messageReader;


    private InetAddress succursaleIPAdresse;
     private Integer portNumber;

    public SystemConnector(InetAddress succursaleIPAdresse, Integer portNumber) {
        this.succursaleIPAdresse = succursaleIPAdresse;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {


        try {
            echoSocket = new Socket(succursaleIPAdresse, portNumber);

            messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
            messageReader = new ObjectInputStream(echoSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + succursaleIPAdresse);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ne pas se connecter au serveur: "
                    + succursaleIPAdresse);
            System.exit(1);
        }
        try {
            messageSender.writeObject(new ClientDispatchRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Le client vient de se connecter");









            try
            {      Message messageReceived =(Message)messageReader.readObject();

                System.out.println("message de type " + messageReceived);
                    if(ClientDispatchAnswer.class.isInstance(messageReceived)){
                        ClientDispatchAnswer msg=(ClientDispatchAnswer) messageReceived;
                        ServerConnectionThread connector=new ServerConnectionThread(msg.getServerAvailaible());
                        new Thread(connector).start();
                        ClientConnector.getInstance().setServerConnectedTo(connector);



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
}
