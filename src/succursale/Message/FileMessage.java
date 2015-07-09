package succursale.Message;

import java.io.Serializable;

/***
 * message d'envoi entre les serveurs pour la copie des fichiers. permet de
 * savoir la taille du fichier, contient des bytes du fichier, ainsi que la
 * position d'écriture.
 * 
 * @author Marc
 *
 */
public class FileMessage extends Message implements Serializable {

	private static final long serialVersionUID = 6000639002033873833L;
	private byte[] byteArray;
	private String filename;
	private int position;

	public FileMessage(byte[] byteArray, String filename, int position) {
		this.byteArray = byteArray;
		this.filename = filename;
		this.position = position;
	}

	public String getFileName() {
		return filename;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public int getPosition() {
		return position;
	}

}
