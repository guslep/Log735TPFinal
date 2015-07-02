package succursale;

import Banque.FileServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ActiveFileServer {



    FileServer thisSuccrusale;
    static HashMap<Integer, FileServerClient> listeSuccursale=new HashMap<Integer, FileServerClient>();
    private String portNumber;
    private int montantBanque;


    public HashMap<Integer, FileServerClient> getListeSuccursale() {
        return listeSuccursale;
    }

    static ActiveFileServer instance;

    private ActiveFileServer(){
    }

    public static ActiveFileServer getInstance(){

        if (instance==null){
            instance= new ActiveFileServer();
        }
     return  instance;
    }


    /**
     * print la liste des clients
     */
    public void printSuccursale(){
        printHashMap(listeSuccursale);
    }


    /***
     * imprime un hashmap recu en parametre
     * @param listeclient
     */
    private static void printHashMap(HashMap listeclient){
        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            FileServerClient currentClient=(FileServerClient)pair.getValue();

            System.out.println("Id: " + currentClient.getId());
            System.out.print( "Nom succursale: "+currentClient.getNom());
            System.out.print( " Montant : "+currentClient.getMontant());
            System.out.println( " Adresse IP: "+currentClient.getSuccursaleIPAdresse().getHostAddress()+"\n");

        }

    }




    public FileServer getThisSuccrusale() {
        return thisSuccrusale;
    }

    public static void setListeSuccursale(HashMap<Integer, FileServerClient> listeSuccursale) {
        ActiveFileServer.listeSuccursale = listeSuccursale;
    }

    public void setThisSuccrusale(FileServer thisSuccrusale) {
        this.thisSuccrusale = thisSuccrusale;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public int getMontantBanque() {
        return montantBanque;
    }

    public void setMontantBanque(int montantBanque) {
        this.montantBanque = montantBanque;
    }

    //TODO retirer les system.out et les envoyer dans les logs à la places

//TODO banque envoie sont montant lorsqu'une nouvelle sucursale join
    //TODO ajouter les erreurs aka perte d'argent
    //TODO ajouter commande de snapshot


}

