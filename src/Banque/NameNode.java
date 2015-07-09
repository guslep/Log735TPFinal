package Banque;

import succursale.Transaction.UpdateListFileServer;
import sun.misc.Lock;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Gus on 6/4/2015.
 */
public class NameNode {


    private ArrayList<FileServer> listFileServer=new ArrayList<FileServer>();
    private ArrayList<ResponseServerThread>listConnection = new ArrayList<ResponseServerThread>();



     public  void addSucursale(FileServer fileServer){



         synchronized(listFileServer){

             fileServer.setId(listFileServer.size()+1);
             listFileServer.add(fileServer);

             pushToClient(fileServer);

         }




    }



    public  void addConnection( ResponseServerThread response){
      synchronized (listConnection){
          listConnection.add(response);

      }


    }

    private void pushToClient(FileServer fileServer){
        Iterator itr = listConnection.iterator();
        while (itr.hasNext()){
            ResponseServerThread current=(ResponseServerThread)itr.next();
            UpdateListFileServer update=new UpdateListFileServer(fileServer.getNom(),fileServer.getId(),this.listFileServer);


            System.out.println(printSucursale());
            current.sendMessage(update);
            System.out.println(update.getListFileServerInTheSystem().size());
        }
         
    }

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




}
