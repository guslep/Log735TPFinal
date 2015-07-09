package Banque;

import succursale.Transaction.Message;
import succursale.Transaction.NewFileServerMessage;

import java.io.*;
import java.net.Socket;

/**
 * Created by Gus on 5/7/2015.
 */
public class ResponseServerThread implements Runnable{

    private Socket sucursaleSocket;
    private NameNode nameNode;
    ObjectOutputStream out = null;

        // the thread will wait for client input and send it back in uppercase
    @Override
    public void run() {
        System.out.println ("connexion reussie");
        System.out.println ("Attente de l'entree.....");


        try {
            out = new ObjectOutputStream(sucursaleSocket.getOutputStream());

            ObjectInputStream in = new ObjectInputStream(( sucursaleSocket.getInputStream()));

            String inputLine;

            Message messageReceived;
            try {
                while ((messageReceived =(Message)in.readObject() ) != null)
                {
                    if(NewFileServerMessage.class.isInstance(messageReceived))
                    {
                        FileServer fileServer= ((NewFileServerMessage)messageReceived).getFileServer();
                      fileServer.setSuccursaleIPAdresse(sucursaleSocket.getInetAddress());
                        nameNode.addSucursale(fileServer);

                    }



                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }







            out.close();
            in.close();
            sucursaleSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void sendMessage(Message message){
        try {
            out.writeObject(message);
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
