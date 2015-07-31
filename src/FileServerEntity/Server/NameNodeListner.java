package FileServerEntity.Server;

import FileServerEntity.FileManager.InitFileSynchronizer;
import FileServerEntity.Message.Message;
import FileServerEntity.Message.ServerMessage.NewFileServerMessage;
import FileServerEntity.Message.ServerMessage.UpdateListFileServer;
import NameNode.FileServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Guillaume on 2015-06-19.
 */
public class NameNodeListner implements Runnable{

    private boolean finish=false;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    private boolean firstRun=true;
    Socket echoSocket = null;
    String succursaleName;
    public NameNodeListner(String serverHostname, String succursaleName, String portNumber) {


        System.out.println ("Essai de se connecter a l'hote " +
                serverHostname + " au port 10118.");
        this.succursaleName=succursaleName;




        try {
            echoSocket = new Socket(serverHostname, 10118);
            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ne pas se connecter au serveur: " + serverHostname);
            System.exit(1);
        }



        FileServer activeFile= ActiveFileServer.getInstance().getThisFileServer();
        NewFileServerMessage msg=new NewFileServerMessage(activeFile);
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //out.println(montant.toString()+","+succursaleName);

        msg=null;
        System.out.println("Liste des succursales");

    }

    /**
     * Ecoute pour les message envoyé par le serveur
     */
    @Override
    public void run()
    {
        while(!finish)
        {

            Message messageRecu=null;
            try
            {
                try
                {
                    while ((messageRecu=(Message) in.readObject())!=null){
                        if(UpdateListFileServer.class.isInstance(messageRecu)){
                           UpdateListFileServer update=(UpdateListFileServer)(messageRecu);
                            executeUpdate(update);


                        }
                    }



                } catch (SocketException e) {
                    finish=true;
                    in.close();
                    out.close();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }





    }


    private void createConnection(HashMap listeclient){


        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();
            FileServerClient currentClient=(FileServerClient)pair.getValue();

            if(currentClient.getConnectionThread()==null)
            {


                    FileServer thisSuccrusale= ActiveFileServer.getInstance().getThisFileServer();
                FileServerResponseThread newConnectionThread = new FileServerResponseThread(currentClient.getSuccursaleIPAdresse(), thisSuccrusale.getId(),Integer.parseInt(currentClient.getPort()));

                currentClient.setConnectionThread(newConnectionThread);

                new Thread(
                        newConnectionThread
                ).start();


            }



        }



    }

    private void executeUpdate(UpdateListFileServer update)
    {
//        TODO ici on créé un hashmap et on scrap celui du Active

      Iterator iter=update.getListFileServerInTheSystem().iterator();
        while (iter.hasNext()){
            FileServer fileServer=(FileServer) iter.next();
            if(!ActiveFileServer.getInstance().getListeSuccursale().containsKey(fileServer.getId())){
                String as=ActiveFileServer.getInstance().getThisFileServer().getNom();
                if(firstRun&&ActiveFileServer.getInstance().getThisFileServer().getNom().equals(update.getInitiator())&&fileServer.getNom().equals(update.getInitiator())){
                    ActiveFileServer.getInstance().setThisFileServer(fileServer);
                }else if(!fileServer.getNom().equals(ActiveFileServer.getInstance().getThisFileServer().getNom())) {
                    FileServerClient newSuccursale= new FileServerClient(fileServer.getSuccursaleIPAdresse(),fileServer.getNom(),fileServer.getPort(),fileServer.getClientPort());
                    newSuccursale.setId(fileServer.getId());
                    ActiveFileServer.getInstance().ajouterServeur(newSuccursale);

                }
            }

        }
        ActiveFileServer.getInstance().printSuccursale();
        if(firstRun){
            System.out.println("Creating Connection");

            createConnection(ActiveFileServer.getInstance().getListeSuccursale());
            new Thread(
                    new DistantFileServerConnectionListener()

            ).start();
            firstRun=false;

            new Thread(new InitFileSynchronizer()).start();



        }



    }
    
    public void sendMessage(Message currentMessage){
    	 try {
    		 out.writeObject(currentMessage);
         } catch (IOException e) {
            System.out.println("Socket is closed");
         }
    }

}
