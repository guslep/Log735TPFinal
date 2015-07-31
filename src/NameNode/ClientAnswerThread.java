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
 Classe qui permet de renvoyer au client le serveur qui doit lui répondre

 ******************************************************/

package NameNode;

import FileServerEntity.Message.ClientMessage.ClientDispatchAnswer;
import FileServerEntity.Message.ClientMessage.ClientDispatchRequest;
import FileServerEntity.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Gus on 5/7/2015.
 */
public class ClientAnswerThread implements Runnable{

    private Socket sucursaleSocket;
    private NameNode nameNode;
    private boolean isFinished=false;
    ObjectOutputStream out = null;


    /**
     * recoit une connection  d'un client
     */
    @Override
    public void run() {
        System.out.println ("connexion reussie pour le client");



        try {
            out = new ObjectOutputStream(sucursaleSocket.getOutputStream());

            ObjectInputStream in = new ObjectInputStream(( sucursaleSocket.getInputStream()));



            Message messageReceived;
            try {
                while (!isFinished&&(messageReceived =(Message)in.readObject() ) != null)
                {
                    if(ClientDispatchRequest.class.isInstance(messageReceived))

                    {
                        ClientDispatchAnswer message=new ClientDispatchAnswer(nameNode.dispatchToAvailaibleServer());
                        sendMessage(message);
                        isFinished=true;



                    }



                }
            }catch(SocketException e){
                isFinished=true;


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
     * envoie un message
     * @param message message à envoyer
     *
     */
    public void sendMessage(Message message){
        try {
            if(!isFinished){
                out.writeObject(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param sucursaleSocket socket connecte
     * @param nameNode namenode
     */
    public ClientAnswerThread(Socket sucursaleSocket, NameNode nameNode) {
        this.sucursaleSocket = sucursaleSocket;
        this.nameNode = nameNode;

    }


}
