package succursale;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import succursale.Message.FileMessage;
import succursale.Message.Message;
import succursale.Message.MessageNewFile;
import succursale.Message.UpdateListFileServer;

/**
 * Classe permettant d'envoyer des messages d'exécution pour les autres serveurs
 * 
 * @author Marc
 *
 */
public class FileServerListener {

	private final static int NBBYTEPARMESSAGE = 2048;

	/**
	 * permet d'ajouter un fichier sur les nouveaux serveurs, et gère l'envoie
	 * des fichiers.
	 * 
	 * @param nouveauFichier
	 * @param filename
	 *            nom du fichier.
	 * @return true si succès, false si échec
	 */
	public static boolean ajoutFichier(File nouveauFichier, String filename) {

		
		
		
		// récupère les bytes d'un fichier
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
			int remaining=0;
			if(fileSize % NBBYTEPARMESSAGE!=0){
				remaining=1;
			}
				
			
			// split des bytes en array de NBBYTEPARMESSAGE
			for (int i = 0; i < (fileSize / NBBYTEPARMESSAGE)+remaining; i++) {
				byte[] bytesSplit = null;
				if(i == (fileSize / NBBYTEPARMESSAGE)+remaining-1){
					bytesSplit = new byte[fileSize % NBBYTEPARMESSAGE];
				}
				else{
					bytesSplit = new byte[NBBYTEPARMESSAGE];
				}
				

				for (int j = 0; j < bytesSplit.length; j++) {
					bytesSplit[j] = data[i*NBBYTEPARMESSAGE + j];
				}
				// ajout a la liste pour l'envoi
				listeBytes.add(bytesSplit);
			}

			// new
			MessageNewFile mnf = new MessageNewFile(fileSize, filename);
			ActiveFileServer afs = ActiveFileServer.getInstance();
			System.out.println("se rend jusqu'au push to all");
			afs.pushToAll(mnf);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			// création des messages pour les différents byte array(FileMessage)
			for (int i = 0; i < listeBytes.size(); i++) {

			
				FileMessage messageEnvoi = new FileMessage(listeBytes.get(i),
						filename, (i * NBBYTEPARMESSAGE));

				//System.out.println("envoie de partie  " + i+"de " + listeBytes.size());
				// ActiveFileServer.pushtoall()
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				ActiveFileServer.getInstance().pushToAll(messageEnvoi);

			}
			//message final pour terminer la connexion
			FileMessage messageFinal = new FileMessage(null, filename, 0);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			afs.getInstance().pushToAll(messageFinal);

			// si succès, retourne true
			return true;
		}
		// >MaxValue
		else {
			return false;
		}

	}
}
