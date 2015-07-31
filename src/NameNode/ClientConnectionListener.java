/******************************************************
 Cours : LOG735
 Session : Été 2015
 Groupe : 01
 Projet : Laboratoire #3
 Étudiants : Giullaume Lépine
 Marc Plamondon
 Pier-Luc Ménard
 Code(s) perm. : LEPG14099201
 PLAM210908907
 MENP27019200

 Date création : 2015-07-2
 Date dern. modif. : 2015-05-07
 ******************************************************
 Classe qui ecoute pour les connections d'u client
 ******************************************************/



package NameNode;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Guillaume on 2015-07-12.
 * Ecoute sur le pourt 10111 pour des conenctions de clients puis cree un nouveau thread par client
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
        System.out.println ("Le serveur est en marche, Attente de la connexion d'un client.....");


        while(isRunning){
            Socket succursaleSocket = null;
            try {
                succursaleSocket = serverSocket.accept();
                System.out.println("Client connection Accepte");
            }
            catch (IOException e)
            {
                System.err.println("Accept a echoue.");
                System.exit(1);
            }


            //Create a new thread for each connection 1 client = 1 thread

            new Thread(
                    new ClientAnswerThread(succursaleSocket, nameNode)
            ).start();

        }


    }
}
