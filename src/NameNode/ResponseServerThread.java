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
 Thread pour communiquer avec un file server
 ******************************************************/


package NameNode;

import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.MessageServerStatus;
import FileServerEntity.Message.ServerMessage.NewFileServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Gus on 5/7/2015.
 */
public class ResponseServerThread implements Runnable{

    private Socket sucursaleSocket;
    private NameNode nameNode;
    private boolean isDestroyed;
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

                    }else if (MessageServerStatus.class.isInstance(messageReceived)){
                    	MessageServerStatus currentServerStatus = (MessageServerStatus)messageReceived;
                        nameNode.updateServerStatus(currentServerStatus);
//                    	System.out.println("CPU AVAILABILITY: " + currentServerStatus.getIdServer());
//                    	System.out.println("RAM AVAILABILITY: " + currentServerStatus.getRamAvailability() + "GB");
                    }



                }
            }catch(SocketException e){
                isDestroyed=true;
                nameNode.removeSuccursale(this);

                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }







            out.close();
            in.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     * @param message envoie un message
     */
    public void sendMessage(Message message){
        try {
            if(!isDestroyed){
                out.writeObject(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * consturcteur
     * @param sucursaleSocket
     * @param nameNode
     */
    public ResponseServerThread(Socket sucursaleSocket, NameNode nameNode) {
        this.sucursaleSocket = sucursaleSocket;
        this.nameNode = nameNode;
        nameNode.addConnection(this);
    }

    /**
     *
     * @return permet de savoir si la connection a lache
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
