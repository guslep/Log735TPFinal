/******************************************************
 Cours : LOG735
 Session : Été 2015
 Groupe : 01
 Projet : Laboratoire #3
 Étudiants : Guillaume Lépine
 Marc Plamondon
 Pier-Luc Ménard
 Code(s) perm. : LEPG14099201
 PLAM210908907
 MENP27019200

 Date création : 2015-07-2
 Date dern. modif. : 2015-05-07
 ******************************************************
 Classe qui permet d'ouvrir un fichier avec le programme par défaut
 ******************************************************/
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
	 
	 
	 /**
	  * Ajout des octets du fichier à céer dans un tableau d'octets temporaire
	  * Si le tableau recu est vide, créé le fichier avec la méthode writeFile
	  * Sinon, on ajoute les octets dans le tableau d'octets temporaire
	  * 
	  * @param byteArrayReceived
	  * @param position
	  */
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
	 
	 /**
	  * Création d'un fichier à ouvrir dans le dossier Downloads local du client
	  * 
	  * @author Pier-Luc
	  * @param fichier
	  * @param fileName
	  * @return Le fichier créé
	  */
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
