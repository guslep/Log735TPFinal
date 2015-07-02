package succursale;

import Banque.FileServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DistantFileServer {



    FileServer thisSuccrusale;

    static HashMap<Integer, FileServerClient> listeSuccursale=new HashMap<Integer, FileServerClient>();
    private String portNumber;


    public HashMap<Integer, FileServerClient> getListeSuccursale() {
        return listeSuccursale;
    }

    static DistantFileServer instance;

    private DistantFileServer(){

    }

    public static DistantFileServer getInstance(){

        if (instance==null){
            instance= new DistantFileServer();
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
        DistantFileServer.listeSuccursale = listeSuccursale;
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
}

