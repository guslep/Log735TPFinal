package NameNode;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Guillaume on 2015-07-12.
 */
public class ClientConnectionListener implements Runnable{

    private NameNode nameNode;

    public ClientConnectionListener(NameNode nameNode) {
        this.nameNode = nameNode;
    }

    @Override
    public void run() {

        ServerSocket serverSocket = null;

        boolean isRunning=true;

        try {
            serverSocket = new ServerSocket(10111);
        }
        catch (IOException e)
        {
            System.err.println("On ne peut pas ecouter au  port: 10111.");
            System.exit(1);
        }
        System.out.println ("Le serveur est en marche, Attente de la connexion.....");


        while(isRunning){
            Socket succursaleSocket = null;
            try {
                succursaleSocket = serverSocket.accept();
            }
            catch (IOException e)
            {
                System.err.println("Accept a echoue.");
                System.exit(1);
            }


            //Create a new thread for each connection 1 client = 1 thread
            new Thread(
                    new ResponseServerThread(succursaleSocket, nameNode)
            ).start();
            new Thread(
                    new ClientAnswerThread(succursaleSocket, nameNode)
            ).start();

        }


    }
}