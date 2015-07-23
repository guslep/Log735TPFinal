package FileServerEntity.FileManager;

import FileServerEntity.Server.ClientResponseThread;
import FileServerEntity.Server.ResponseClientThread;

/**
 * Created by Gus on 7/9/2015.
 */
public class TransitFile {
    private String nom;
    private byte[] byteArray;
    private ResponseClientThread connectionThread;
    private ClientResponseThread clientConnectionThread;


    public TransitFile(ResponseClientThread responseClientThread, String nom, int byteSize) {
        this.connectionThread=responseClientThread;
        this.nom = nom;
        this.byteArray = new byte[byteSize];
    }

    public TransitFile(ClientResponseThread clientConnectionThread,String nom,int byteSize ){
        this.nom = nom;
        this.byteArray = new byte[byteSize];
        this.clientConnectionThread = clientConnectionThread;
    }

    public void addByte(byte[] byteArrayReceived,int position){
       
    	if(byteArrayReceived==null){
          writeFile();
        }
        else {
            for (int i=0;i<byteArrayReceived.length;i++){
            byteArray[i+position]=byteArrayReceived[i];

        }}
    }

    private void writeFile() {
        FileManager.getInstance().creerFichier(byteArray, nom);
        if(connectionThread==null){
            clientConnectionThread.fileWasWritten(nom);
        }else{
            connectionThread.fileWasWritten(nom);

        }



    }

    public String getNom() {
        return nom;
    }


}
