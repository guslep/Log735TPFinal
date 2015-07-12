package Banque;

import serverStatus.MessageServerStatus;
import succursale.Message.Message;
import succursale.Message.NewFileServerMessage;
import sun.net.ConnectionResetException;

import java.io.*;
import java.net.Socket;

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
                    	System.out.println("CPU AVAILABILITY: " + currentServerStatus.getCpuAvailabilty() + "%");
                    	System.out.println("RAM AVAILABILITY: " + currentServerStatus.getRamAvailability() + "GB");
                    }



                }
            }catch(ConnectionResetException e){

                out.close();
            }
            catch (IOException e) {
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
            if(!isDestroyed){
                out.writeObject(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ResponseServerThread(Socket sucursaleSocket, NameNode nameNode) {
        this.sucursaleSocket = sucursaleSocket;
        this.nameNode = nameNode;
        nameNode.addConnection(this);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
