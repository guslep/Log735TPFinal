package GUI;

import FileServerEntity.Message.ClientMessage.ClientDeleteFile;

import java.io.File;
import java.net.Inet4Address;
import java.util.ArrayList;

/**
 * Created by Gus on 7/16/2015.
 */
public class ClientConnector {

    private static ClientConnector instance;
    private  ServerConnectionThread serverConnectedTo;
    private ArrayList<File> listFileAvailaible;
    private ClientConnector() {
    }

    public static ClientConnector getInstance() {
        if(instance==null){
            instance=new ClientConnector();
        }
        return instance;
    }



    public void addFile(){
        //TODO lire et ajouter le fichier comme avec le serveur


    }

    public void deleteFille(String fileName){

        serverConnectedTo.sendMessage(new ClientDeleteFile(fileName));

    }
    public void readFile(){

        //TODO recevoir un fichier

    }

    public void ConnectToFileSystem(Inet4Address nameNoderIpAdress,int portNumber){
        new Thread(new SystemConnector(nameNoderIpAdress,portNumber));
    }

    public ServerConnectionThread getServerConnectedTo() {
        return serverConnectedTo;
    }

    public void setServerConnectedTo(ServerConnectionThread serverConnectedTo) {
        this.serverConnectedTo = serverConnectedTo;
    }

    public static void setInstance(ClientConnector instance) {
        ClientConnector.instance = instance;
    }

    public ArrayList<File> getListFileAvailaible() {
        return listFileAvailaible;
    }

    public void setListFileAvailaible(ArrayList<File> listFileAvailaible) {
        this.listFileAvailaible = listFileAvailaible;
    }
}
