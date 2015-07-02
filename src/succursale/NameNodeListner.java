package succursale;

import Banque.FileServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Guillaume on 2015-06-19.
 */
public class NameNodeListner implements Runnable{

    private boolean finish=false;
    PrintWriter out = null;
    BufferedReader in = null;
    private boolean firstRun=true;
    Socket echoSocket = null;
    String succursaleName;
    public NameNodeListner(String serverHostname, Integer montant, String succursaleName, String portNumber) {


        System.out.println ("Essai de se connecter a l'hote " +
                serverHostname + " au port 10118.");
        this.succursaleName=succursaleName;




        try {
            echoSocket = new Socket(serverHostname, 10118);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ne pas se connecter au serveur: " + serverHostname);
            System.exit(1);
        }



        out.println(montant.toString() + "," + succursaleName + "," + portNumber);


        //out.println(montant.toString()+","+succursaleName);

        System.out.println("Liste des succursales");

    }

    /**
     * Ecoute pour les message envoyé par le serveur
     */
    @Override
    public void run()
    { HashMap<Integer, FileServerClient> listeSuccursale;
        while(!finish)
        {

            String recuTest= null;
            try {
                recuTest = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(recuTest);
            String recu[]=recuTest.split(";");
            int banuqeMontant=Integer.parseInt(recu[0]);
            ActiveFileServer.getInstance().setMontantBanque(banuqeMontant);
            listeSuccursale= ActiveFileServer.getInstance().getListeSuccursale();
            for(int index=1;index<recu.length;index++){
                String[] splitSuccursale=recu[index].split(",");

                if(!listeSuccursale.containsKey(Integer.parseInt(splitSuccursale[0]))){
                    if(splitSuccursale[1].equals(succursaleName)){

                        FileServer thisSuccrusale= null;
                        try {
                            thisSuccrusale = new FileServer(InetAddress.getByName(splitSuccursale[3]),
                                    Integer.parseInt(splitSuccursale[2]),splitSuccursale[1],splitSuccursale[4]);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        thisSuccrusale.setId(Integer.parseInt(splitSuccursale[0]));
                        ActiveFileServer.getInstance().setThisSuccrusale(thisSuccrusale);

                    }
                    else{
                        FileServerClient newSuccursale= null;
                        try {
                            newSuccursale = new FileServerClient(InetAddress.getByName(splitSuccursale[3]),
                                    Integer.parseInt(splitSuccursale[2]),splitSuccursale[1],splitSuccursale[4]);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        newSuccursale.setId(Integer.parseInt(splitSuccursale[0]));

                        listeSuccursale.put(Integer.parseInt(splitSuccursale[0]),newSuccursale);


                    }}



            }
            ActiveFileServer.getInstance().setListeSuccursale(listeSuccursale);

            ActiveFileServer.getInstance().printSuccursale();
            if(firstRun){
                System.out.println("Creating Connection");

                createConnection(listeSuccursale);
                new Thread(
                        new clientConnectionListener()

                ).start();
                firstRun=false;
                System.out.println("Starting transactionDispatcher");
/*
                ActiveFileServer.getInstance().setTransactionDispatcher(transactionDispatcher);
                new Thread(
                        transactionDispatcher
                ).start();
                */

            }


        }


        try {
            in.close();
            out.close();
            echoSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void createConnection(HashMap listeclient){


        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();
            FileServerClient currentClient=(FileServerClient)pair.getValue();

            if(currentClient.getConnectionThread()==null)
            {


                    FileServer thisSuccrusale= ActiveFileServer.getInstance().getThisSuccrusale();
                ResponseClientThread newConnectionThread = new ResponseClientThread(currentClient.getSuccursaleIPAdresse(), thisSuccrusale.getId(),Integer.parseInt(currentClient.getPort()));

                currentClient.setConnectionThread(newConnectionThread);

                new Thread(
                        newConnectionThread
                ).start();


            }



        }



    }
}
