package succursale;



import Banque.FileServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

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

        System.out.print ("Entree l'adresse de la banque: ");
        serverHostname=stdIn.readLine();
        if(serverHostname==null||serverHostname.equals("")){
            serverHostname="127.0.0.1";
        }
        System.out.print ("Entree e port de la succursale: ");

        portNumber=stdIn.readLine();
        if(portNumber==null||portNumber.equals("")){
            portNumber="10119";
        }
        System.out.print ("Entree le nom de la succursale: ");
        succursaleName=stdIn.readLine();
        if(succursaleName==null||succursaleName.equals("")){
            Random rand=new Random();

            succursaleName="Test "+(rand.nextInt(9)+1);
        }
        System.out.print ("Entree le montant de la succursale: ");
        String strMontant=stdIn.readLine();


        if(strMontant==null||strMontant.equals("")){

            Random rand=new Random();
            montant=rand.nextInt(1000000)+1000;

        }else{
            montant=Integer.parseInt(strMontant);
        }

        ActiveFileServer.getInstance().setPortNumber(portNumber);
        ActiveFileServer.getInstance().setThisSuccrusale(new FileServer(null,montant,succursaleName,portNumber));


        new Thread(
                new NameNodeListner(serverHostname,montant,succursaleName,portNumber)
        ).start();






        //TODO Deplacer ce code la dans le BanqueCOnnector

//TODO ajouter le menu ici et l'interaction avec l'usager, idealement creer une classe affichage ce serait pas mal plus clean
        //stdIn.close();





    }
}
