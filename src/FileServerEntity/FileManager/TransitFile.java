package FileServerEntity.FileManager;

import FileServerEntity.Server.ResponseClientThread;

/**
 * Created by Gus on 7/9/2015.
 */
public class TransitFile {
    private String nom;
    private byte[] byteArray;
    private ResponseClientThread connectionThread;


    public TransitFile(ResponseClientThread responseClientThread, String nom, int byteSize) {
        this.connectionThread=responseClientThread;
        this.nom = nom;
        this.byteArray = new byte[byteSize];
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
        connectionThread.fileWasWritten(nom);

    }

    public String getNom() {
        return nom;
    }


}
