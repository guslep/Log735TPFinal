package NameNode;


import FileServerEntity.Message.ServerMessage.UpdateListFileServer;
import FileServerEntity.Message.ServerMessage.MessageServerStatus;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Gus on 6/4/2015.
 */
public class NameNode {


    private ArrayList<FileServer> listFileServer=new ArrayList<FileServer>();
    private ArrayList<ResponseServerThread>listConnection = new ArrayList<ResponseServerThread>();
    private HashMap<Integer,MessageServerStatus> hashServerStatus=new HashMap<Integer, MessageServerStatus>();



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
            if(current.isDestroyed()){
                listConnection.remove(current);
            }else{
                current.sendMessage(update);
            }

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

    public  void removeSuccursale(ResponseServerThread deadConnection){
        int indexRemove=listConnection.indexOf(deadConnection);
        listConnection.remove(indexRemove);
        hashServerStatus.remove(listFileServer.get(indexRemove).getId());
        listFileServer.remove(indexRemove);



    }
    public void updateServerStatus(MessageServerStatus msg){
        hashServerStatus.put(msg.getIdServer(),msg);

    }
    public FileServer dispatchToAvailaibleServer(){
        Iterator iter=hashServerStatus.entrySet().iterator();
        MessageServerStatus bestServer=null;

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



            if(current.getId()==bestServer.getIdServer()){
                bestFileServer=current;
            }


        }


        return bestFileServer ;

    }





}
