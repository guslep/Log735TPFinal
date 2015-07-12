package FileServerEntity.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Gus on 6/11/2015.
 */
public class clientConnectionListener implements Runnable {
    public clientConnectionListener() {

    }



    @Override
    public void run() {
        ServerSocket serverSocket = null;

        boolean isRunning = true;

        try {
            serverSocket = new ServerSocket(Integer.parseInt( ActiveFileServer.getInstance().getPortNumber()));
        } catch (IOException e) {
            System.err.println("On ne peut pas ecouter au  port: 10119.");
            System.exit(1);
        }
        System.out.println("La succursale est en Attente de connexion.....");

        while (isRunning) {
            Socket succursaleSocket = null;
            try {
                succursaleSocket = serverSocket.accept();
                System.out.println("La succursale a accepte une connexion.....");

            } catch (IOException e) {
                System.err.println("Accept a echoue.");

            }


            //Create a new thread for each connection 1 client = 1 thread
            new Thread(
                    new ResponseClientThread(succursaleSocket)

            ).start();

        }
    }
}
