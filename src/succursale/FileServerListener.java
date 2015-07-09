package succursale;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import succursale.Transaction.FileMessage;
import succursale.Transaction.Message;
import succursale.Transaction.UpdateListFileServer;

/**
 * Classe permettant d'envoyer des messages d'ex�cution pour les autres serveurs
 * 
 * @author Marc
 *
 */
public class FileServerListener {

	private final static int NBBYTEPARMESSAGE = 2048;

	/**
	 * permet d'ajouter un fichier sur les nouveaux serveurs, et g�re l'envoie
	 * des fichiers.
	 * 
	 * @param nouveauFichier
	 * @param filename
	 *            nom du fichier.
	 * @return true si succ�s, false si �chec
	 */
	public static boolean ajoutFichier(File nouveauFichier, String filename) {

		// r�cup�re les bytes d'un fichier
		if (nouveauFichier.length() <= Integer.MAX_VALUE) {
			int fileSize = (int) nouveauFichier.length();

			ArrayList<byte[]> listeBytes = new ArrayList<byte[]>();

			Path path = Paths.get(nouveauFichier.getAbsolutePath());
			byte[] data = null;
			try {
				data = Files.readAllBytes(path);
			} catch (IOException e) {
				return false;
			}
			// split des bytes en array de NBBYTEPARMESSAGE
			for (int i = 0; i < (fileSize / NBBYTEPARMESSAGE); i++) {
				byte[] bytesSplit = new byte[NBBYTEPARMESSAGE];
				
				for(int j = 0; j < NBBYTEPARMESSAGE; j++){
					bytesSplit[j] = data[i+j];
				}
				//ajout a la liste pour l'envoi
				listeBytes.add(bytesSplit);
			}
			
			// cr�ation des messages pour les diff�rents byte array(FileMessage)
			for(int i = 0; i< listeBytes.size(); i++){
				
				FileMessage messageEnvoi = new FileMessage(listeBytes.get(i), filename, fileSize, (i*NBBYTEPARMESSAGE));
				
				// ActiveFileServer.pushtoall()
				ActiveFileServer afs = ActiveFileServer.getInstance();
				afs.pushToAll(messageEnvoi);
				
			}
						


			// si succ�s, retourne true
			return true;
		}
		// >MaxValue
		else {
			return false;
		}

	}
}
