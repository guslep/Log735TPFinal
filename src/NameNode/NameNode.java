package NameNode;


import FileServerEntity.Message.ServerMessage.MessageServerStatus;
import FileServerEntity.Message.ServerMessage.UpdateListFileServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Gus on 6/4/2015.
 */
public class NameNode {

    /**
     * liste de file server
     */
    private ArrayList<FileServer> listFileServer=new ArrayList<FileServer>();
    /**
     * liste des connections des serveurs
     */
    private ArrayList<ResponseServerThread>listConnection = new ArrayList<ResponseServerThread>();
    /**
     * liste du status de chaque serveur, contient l'etat de chaque serveur
     */
    private HashMap<Integer,MessageServerStatus> hashServerStatus=new HashMap<Integer, MessageServerStatus>();
    private final Object lockListeFileServer = new Object();
    private final Object lockListeConnection = new Object();


    /**
     *
     * @param fileServer ajout de d'un nouveau fileServer
     */
     public  void addSucursale(FileServer fileServer){



         synchronized(lockListeFileServer){

             fileServer.setId(listFileServer.size()+1);
             listFileServer.add(fileServer);

             pushToClient(fileServer);

         }




    }


    /**
     *
     * @param response ajout d'unde connection
     */
    public  void addConnection( ResponseServerThread response){
      synchronized (lockListeConnection){
          listConnection.add(response);

      }


    }

    /**
     *
     * @param fileServer envoie a tous les serveurs le nouveau serveurs qiu c'est connecte
     */
    private void pushToClient(FileServer fileServer){
        Iterator itr = listConnection.iterator();
        while (itr.hasNext()){
            ResponseServerThread current=(ResponseServerThread)itr.next();
            UpdateListFileServer update=new UpdateListFileServer(fileServer.getNom(),fileServer.getId(),this.listFileServer);


            System.out.println(printSucursale());
            if(current.isDestroyed()){
                listConnection.remove(current);
            }else{
                current.sendMessage(update);
            }

            System.out.println(update.getListFileServerInTheSystem().size());
        }
         
    }

    /**
     * utile pour le deubg
     * @return
     */
    private  String printSucursale( ){
        String listSucursaleSTR="";
        Iterator itr= this.listFileServer.iterator();
        while (itr.hasNext()){
            FileServer currentFileServer =(FileServer)itr.next();
            listSucursaleSTR+= currentFileServer.toString();
            if(itr.hasNext()){
                listSucursaleSTR+=";";
            }
        }
        
        return  listSucursaleSTR;
    }

    /**
     *
     * @param deadConnection delete un serveur
     */
    public  void removeSuccursale(ResponseServerThread deadConnection){
        int indexRemove=listConnection.indexOf(deadConnection);
        listConnection.remove(indexRemove);
        hashServerStatus.remove(listFileServer.get(indexRemove).getId());
        listFileServer.remove(indexRemove);



    }

    /**
     *
     * @param msg recoit une update du s'atus d'un serveur
     */
    public void updateServerStatus(MessageServerStatus msg){
        hashServerStatus.put(msg.getIdServer(),msg);

    }

    /**
     *
     * @return envoie un nouveaux clients vers le serveur le plus optimale
     */
    public FileServer dispatchToAvailaibleServer(){
        Iterator iter=hashServerStatus.entrySet().iterator();
        MessageServerStatus bestServer=null;
System.out.println("lel");
        while (iter.hasNext()){


            Map.Entry pair = (Map.Entry)iter.next();

            MessageServerStatus serverStatus=(MessageServerStatus)pair.getValue();
            if(bestServer==null){
                bestServer=serverStatus;
            }else if(serverStatus.isLessUsed(bestServer)){
                bestServer=serverStatus;

            }

        }

        FileServer bestFileServer=listFileServer.get(0);
        Iterator itr = listFileServer.iterator();
        while (itr.hasNext()){
            FileServer current=(FileServer)itr.next();


            if(bestServer==null){
                bestFileServer=current;
            }
            else if(current.getId()==bestServer.getIdServer()){
                bestFileServer=current;
            }


        }


        return bestFileServer ;

    }





}
