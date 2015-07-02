package Banque;

import sun.misc.Lock;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Gus on 6/4/2015.
 */
public class NameNode {
     private  int totalMoneyInThaBank=0;
    Lock montantLock=new Lock() {
    };

    private ArrayList<FileServer> listFileServer = new ArrayList<FileServer>();
    private ArrayList<ResponseServerThread>listConnection = new ArrayList<ResponseServerThread>();


     public synchronized Integer addSucursale(FileServer fileServer){
            fileServer.setId(listFileServer.size()+1);
         listFileServer.add(fileServer);

         try {
             montantLock.lock();
             try{
                 totalMoneyInThaBank+= fileServer.getMontant();

             }finally {

                 System.out.println(fileServer.getNom()+" "+" a ajouté "+ fileServer.getMontant());
                 System.out.println("Montant total de la banque est de "+ totalMoneyInThaBank);
                 pushToClient(totalMoneyInThaBank+";"+printSucursale());
                 montantLock.unlock();

             }
         } catch (InterruptedException e) {
             e.printStackTrace();
         }



         return fileServer.getId();
    }



    public synchronized void addConnection( ResponseServerThread response){
        listConnection.add(response);


    }

    private void pushToClient(String message){
        Iterator itr = listConnection.iterator();
        while (itr.hasNext()){
            ResponseServerThread current=(ResponseServerThread)itr.next();
        current.sendMessage(message);
        }
         
    }

    private  String printSucursale( ){
        String listSucursaleSTR="";
        Iterator itr= listFileServer.iterator();
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
