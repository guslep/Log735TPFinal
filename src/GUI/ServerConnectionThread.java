package GUI;

import FileServerEntity.FileManager.ClientDownloadFile;
import FileServerEntity.Message.ClientMessage.ClientAcceptUpload;
import FileServerEntity.Message.ClientMessage.ClientListFile;
import FileServerEntity.Message.ClientMessage.ErrorUploading;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.FileMessage;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Message.ServerMessage.MessageNewFile;
import NameNode.FileServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by Gus on 7/16/2015.
 */
public class ServerConnectionThread extends Observable implements Runnable {
    private FileServer server;
    private ObjectOutputStream messageSender;
    private Socket echoSocket = null;
    private HashMap<String, ClientDownloadFile> fileBeingWritten = new HashMap<String, ClientDownloadFile>();

    private ObjectInputStream messageReader;


    public ServerConnectionThread(FileServer server) {
        this.server = server;

    }


    @Override
    public void run() {
        try {
            System.out.println("Connecting to "+ server.getSuccursaleIPAdresse().getHostAddress()+" "+server.getClientPort() );
            echoSocket = new Socket(server.getSuccursaleIPAdresse(),Integer.parseInt(server.getClientPort()) );
            messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
            messageReader = new ObjectInputStream(echoSocket.getInputStream());
            
            ClientConnector.getInstance().setServerName(server.getNom());
            ClientConnector.getInstance().setServerPort(server.getClientPort());
            messageSender.writeObject(new ClientListFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message messageReceived;

        Boolean exit=false;

        try
        {

            while (!exit&&(messageReceived =(Message)messageReader.readObject() ) != null)
            {
                System.out.println("message de type " + messageReceived);
                if(InitSymchronizerMessage.class.isInstance(messageReceived)){
                    InitSymchronizerMessage msgReceived= (InitSymchronizerMessage) messageReceived;

                    ClientConnector.getInstance().setListFileAvailaible(msgReceived.getListContainedFile(),msgReceived.getRootDirectory());
                }
                else if (MessageNewFile.class.isInstance(messageReceived)){

                   ClientDownloadFile clientFile = new ClientDownloadFile(this,((MessageNewFile)messageReceived).getFileName(),((MessageNewFile)messageReceived).getFileLength());
                   fileBeingWritten.put(clientFile.getNom(),clientFile);
                }
                else if(FileMessage.class.isInstance(messageReceived)){
                	FileMessage msg = (FileMessage) messageReceived;
                	fileBeingWritten.get(msg.getFileName()).addByte(msg.getByteArray(), msg.getPosition());
                }else if(ErrorUploading.class.isInstance(messageReceived)){

                    ErrorUploading msg=(ErrorUploading)messageReceived;
                    this.setChanged();
                    this.notifyObservers(msg);


                }
                else if(ClientAcceptUpload.class.isInstance(messageReceived)){

                    ClientAcceptUpload msg=(ClientAcceptUpload)messageReceived;
                    this.setChanged();
                    this.notifyObservers(messageReceived);


                }


            }
        } catch (SocketException e){

            System.out.println("erreur dans la connection");
            ClientConnector.getInstance().ConnectToFileSystem(ClientConnector.getInstance().getNameNodateSuccursaleIPAdresse(),ClientConnector.getInstance().getNameNodeportNumber());
            exit=true;
            try {
                messageSender.close();
                messageReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("erreur dans l'objet");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void fileWasWritten(String fileName) {
		fileBeingWritten.remove(fileName);
	}

    public synchronized void sendMessage(Message messageTosSend) {

        try {
                messageSender.writeObject(messageTosSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
