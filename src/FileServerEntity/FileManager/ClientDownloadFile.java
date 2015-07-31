package FileServerEntity.FileManager;

import ClientFeature.ClientOpenFile;
import GUI.ServerConnectionThread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientDownloadFile {
	private static final String FOLDER_DESTINATION = "Downloads\\";
	 private String nom;
	 private ServerConnectionThread connectionThread;
	 private byte[] byteArray;
	 private String localDirName = FOLDER_DESTINATION;
	 private String localDir = System.getProperty("user.dir") + "\\files - " + localDirName ;	

	 public ClientDownloadFile(ServerConnectionThread connectionThread, String nom, int byteSize) {
	     this.connectionThread=connectionThread;
	     this.nom =  nom;
	     this.byteArray = new byte [byteSize];
	 }
	 
	 public void addByte(byte[] byteArrayReceived,int position){
	       
	    	if(byteArrayReceived==null){
	          writeFile();
	        }
	        else {
                System.arraycopy(byteArrayReceived, 0, byteArray, 0 + position, byteArrayReceived.length);
	        }
	    }

	 private void writeFile() {
		File currentFile= creerFichier(byteArray, nom);

		if(currentFile!=null){
			ClientOpenFile.openFile(currentFile);

		}

		 connectionThread.fileWasWritten(nom);
	 }
	 
	 public String getNom() {
	     return nom;
	 }
	 
	 public File creerFichier(byte[] fichier, String fileName) {
			System.out.println("maybe pls?");
			String fullfilename = localDir + fileName;
	        File newFile=new File(fullfilename);
	        
	        if(!newFile.exists()&&newFile.getParentFile()!=null){
	            newFile.getParentFile().mkdirs();
	        }
		 if(newFile.exists()){
			 try {
				 newFile.delete();
			 } catch (Exception e){
				 return newFile;
			 }
		 }

	        try {
				FileOutputStream fos = new FileOutputStream(fullfilename,true);
				fos.write(fichier);
				fos.close();

			} catch (FileNotFoundException e) {


				newFile=null;
			} catch (IOException e) {
				newFile =null;
			}
			return newFile;
		}
}
