package FileServerEntity.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ClientFeature.ClientOpenFile;
import FileServerEntity.Server.ActiveFileServer;
import GUI.ServerConnectionThread;

public class ClientDownloadFile {
	private static final String FOLDER_DESTINATION = "\\Downloads\\";
	 private String nom;
	 private ServerConnectionThread connectionThread;
	 private byte[] byteArray;
	 private String localDirName = ActiveFileServer.getInstance().getThisFileServer().getNom();
	 private String localDir = System.getProperty("user.dir") + "\\files - " + localDirName ;	

	 public ClientDownloadFile(ServerConnectionThread connectionThread, String nom, int byteSize) {
	     this.connectionThread=connectionThread;
	     this.nom = FOLDER_DESTINATION + nom;
	     this.byteArray = new byte [byteSize];
	 }
	 
	 public void addByte(byte[] byteArrayReceived,int position){
	       
	    	if(byteArrayReceived==null){
	          writeFile();
	        }
	        else {
	            for (int i=0;i<byteArrayReceived.length;i++){
	            byteArray[i+position]=byteArrayReceived[i];
	            }
	        }
	    }

	 private void writeFile() {
		 creerFichier(byteArray, nom);
		 File currentFile = new File(nom);
		 ClientOpenFile.openFile(currentFile);
		 connectionThread.fileWasWritten(nom);
	 }
	 
	 public String getNom() {
	     return nom;
	 }
	 
	 public boolean creerFichier(byte[] fichier, String fileName) {
			System.out.println("maybe pls?");
			String fullfilename = localDir + fileName;
	        File newFile=new File(fullfilename);
	        
	        if(!newFile.exists()&&newFile.getParentFile()!=null){
	            newFile.getParentFile().mkdirs();
	        }

	        try {
				FileOutputStream fos = new FileOutputStream(fullfilename,true);
				fos.write(fichier);
				fos.close();

			} catch (FileNotFoundException e) {


				return false;
			} catch (IOException e) {
				return false;
			}
			return true;
		}
}
