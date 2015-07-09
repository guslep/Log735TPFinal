package succursale.Transaction;

import java.io.Serializable;
/***
 * message d'envoi entre les serveurs pour la copie des fichiers. permet de savoir la taille du fichier, contient des bytes du fichier, ainsi que la position d'écriture.
 * @author Marc
 *
 */
public class FileMessage extends Message implements Serializable{
	private byte[] byteArray;
	private String filename; 
	private int fileSize;
	private int position;
	
	
	public FileMessage(byte[] byteArray, String filename, int fileSize, int position){
		this.byteArray = byteArray;
		this.filename = filename;
		this.fileSize = fileSize;
		this.position = position;
	}
	
	
	public String getFileName(){
		return filename;
	}
	public byte[] getByteArray(){
		return byteArray;
	}
	public int getFileSize() {
		return fileSize;
	}
	public int getPosition() {
		return position;
	}
	
}
