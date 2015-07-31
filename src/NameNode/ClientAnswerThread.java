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

        // the thread will wait for client input and send it back in uppercase
    @Override
    public void run() {
        System.out.println ("connexion reussie pour le client");



        try {
            out = new ObjectOutputStream(sucursaleSocket.getOutputStream());

            ObjectInputStream in = new ObjectInputStream(( sucursaleSocket.getInputStream()));

            String inputLine;

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
    public void sendMessage(Message message){
        try {
            if(!isFinished){
                out.writeObject(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ClientAnswerThread(Socket sucursaleSocket, NameNode nameNode) {
        this.sucursaleSocket = sucursaleSocket;
        this.nameNode = nameNode;

    }


}
