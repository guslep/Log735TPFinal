package FileServerEntity.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Gus on 6/11/2015.
 */
public class ClientConnectionListener implements Runnable {
    public ClientConnectionListener() {

    }



    @Override
    public void run() {
        ServerSocket serverSocket = null;

        boolean isRunning = true;

        try {
            serverSocket = new ServerSocket(Integer.parseInt( ActiveFileServer.getInstance().getThisFileServer().getClientPort()));
        } catch (IOException e) {
            System.err.println("On ne peut pas ecouter au  port "+ActiveFileServer.getInstance().getThisFileServer().getClientPort());
            System.exit(1);
        }
        System.out.println("Le serveur est en Attente de connexion de client.....");

        while (isRunning) {
            Socket succursaleSocket = null;
            try {
                succursaleSocket = serverSocket.accept();
                System.out.println("La succursale a accepte une connexion de client.....");

            } catch (IOException e) {
                System.err.println("Accept a echoue.");

            }


            //Create a new thread for each connection 1 client = 1 thread
            new Thread(
                    new ClientResponseThread(succursaleSocket)

            ).start();

        }
    }
}
