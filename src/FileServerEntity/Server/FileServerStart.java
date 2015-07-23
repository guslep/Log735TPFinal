package FileServerEntity.Server;



import NameNode.FileServer;
import FileServerEntity.FileManager.FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import FileServerEntity.ServerStatus.AutoServerStatus;

/**
 * Created by Gus on 6/11/2015.
 */
    public class FileServerStart {
    /*main d'une succursale demande les infos à l.utulisateur, contacte le serveur, part le menu et le banqueListner


     */
    public static void main(String[] args) throws IOException {
        //ip du serveur
        String serverHostname;
//        TODO une fois que ca marche mettre client en singleton et sortir le while immence et le mettre dans un autre thread
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String portNumber;
        //nom de la succursale
        String succursaleName;
        //Montant
        Integer montant;

        System.out.print ("Entree l'adresse du NameNode: ");
        serverHostname=stdIn.readLine();
        if(serverHostname==null||serverHostname.equals("")){
            serverHostname="127.0.0.1";
        }
        System.out.print ("Entree le port du file server: ");

        portNumber=stdIn.readLine();
        if(portNumber==null||portNumber.equals("")){

            portNumber=Integer.toString(10119+new Random().nextInt(20));
        }
        System.out.print ("Entree le nom du FileServer: ");
        succursaleName=stdIn.readLine();
        if(succursaleName==null||succursaleName.equals("")){
            Random rand=new Random();

            succursaleName="Test "+(rand.nextInt(9)+1);
        }
        Random rand=new Random();

        montant=rand.nextInt(1000000)+1000;

        System.out.print ("Entree le port d'écoute pour les clients: ");
        String clientPortNumber=stdIn.readLine();
        if(clientPortNumber==null||clientPortNumber.equals("")){

            clientPortNumber=Integer.toString(10219+new Random().nextInt(20));
        }


        ActiveFileServer.getInstance().setPortNumber(portNumber);
        ActiveFileServer.getInstance().setThisFileServer(new FileServer(null,  succursaleName, portNumber, clientPortNumber));

        	NameNodeListner nameNode;
        new Thread(
                nameNode=new NameNodeListner(serverHostname,montant,succursaleName,portNumber)
        ).start();

        new Thread(new ClientConnectionListener()).start();

        // really important
        FileManager fm = FileManager.getInstance();




        // pour d�bug �a crash <3
       new Thread(new AutoServerStatus(nameNode)).start() ;
		







    }
}
