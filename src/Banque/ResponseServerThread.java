package Banque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Gus on 5/7/2015.
 */
public class ResponseServerThread implements Runnable{

    private Socket sucursaleSocket;
    private NameNode nameNode;
    PrintWriter out = null;

        // the thread will wait for client input and send it back in uppercase
    @Override
    public void run() {
        System.out.println ("connexion reussie");
        System.out.println ("Attente de l'entree.....");


        try {
            out = new PrintWriter(sucursaleSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader( sucursaleSocket.getInputStream()));

            String inputLine;


            while ((inputLine = in.readLine()) != null)
            {
                System.out.println ("Serveur: " + inputLine);
               String succursaleValue[]=inputLine.split(",");
                nameNode.addSucursale(new FileServer(sucursaleSocket.getInetAddress(),Integer.parseInt(succursaleValue[0]),succursaleValue[1],succursaleValue[2]));


            }

            out.close();
            in.close();
            sucursaleSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void sendMessage(String message){
        try {
            out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ResponseServerThread(Socket sucursaleSocket, NameNode nameNode) {
        this.sucursaleSocket = sucursaleSocket;
        this.nameNode = nameNode;
        nameNode.addConnection(this);
    }
}
