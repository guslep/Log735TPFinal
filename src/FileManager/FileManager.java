package FileManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import succursale.ActiveFileServer;

/***
 * la classe FileManager permet de monitorer les changements sur le dossier
 * files, ainsi que s'updater si des changements sont apportés manuellement, à
 * l'aide de FileWatcher
 * 
 * @author Marc
 *
 */
public class FileManager {

	private File[] listeFichiers;
	private static FileManager instance;
	String localDirName = ActiveFileServer.getInstance().getThisFileServer().getNom();
	String localDir = System.getProperty("user.dir") + "\\files - " + localDirName ;

	/***
	 * constructeur par défaut, appelé lors du getInstance s'il est null
	 */
	private FileManager() {

		File test = new File(localDir);
		
		System.out.println(" not stuck");
		if (!test.exists()) {
			boolean success;
			try {
				success = new File(localDir).mkdirs();
				if (!success) {
					System.out
							.println("Erreur lors de la création du dossier pour les fichiers,");
					listeFichiers = null;
				} else {
					System.out
							.println("Serveur instancié, utilisation du répertoire "
									+ localDir);
				}
			} catch (SecurityException se) {
				System.out.println("erreur de permissions");
			}

		} else {
			System.out.println("Serveur instancié, utilisation du répertoire "
					+ localDir);
			updatelisteFichiers();
		}
		// démarrage du fileWatcher pour monitorer les changements du dossier, le constructeur se charge de démarrer un thread
				FileWatcher fw = new FileWatcher(localDir);
	}

	/**
	 * singleton pour s'approprier l'instance du FileManager (puisqu'elle peut
	 * etre rapellée par FileWatcher
	 * 
	 * @return
	 */
	public static FileManager getInstance() {
		if (instance == null) {
			synchronized (FileManager.class) {
				if (instance == null) {
					instance = new FileManager();
				}
			}
		}
		return instance;
	}

	/**
	 * constructeur par défaut, va se construire en listant les items
	 * disponibles
	 */

	/**
	 * méthode pour synchronizer les fichiers du répertoire local avec les
	 * autres serveurs.
	 * 
	 * @return
	 */
	public boolean SynchronizeFiles() {
		updatelisteFichiers();
		// ActiveFileServer listeConnexions = ActiveFileServer.getInstance();
		return true;
	}

	/**
	 * retourne la liste des fichiers du serveur
	 * 
	 * @return liste des fichiers du serveur
	 */
	public File[] getListeFichiers() {
		updatelisteFichiers();
		return listeFichiers;
	}

	/**
	 * permet d'obtenir les fichiers disponibles sur le serveur
	 */
	public void updatelisteFichiers() {
		this.listeFichiers = new File(localDir).listFiles();
	}

	/**
	 * retourne le nombre de fichiers disponibles sur le serveur
	 * 
	 * @return nb de fichiers sur le serveur
	 */
	public int getNbFichiers() {
		updatelisteFichiers();
		if (listeFichiers != null)
			return listeFichiers.length;
		else
			return 0;
	}

	/**
	 * retourne si le fichier existe localement sur le serveur
	 * 
	 * @param nomFichier
	 * @return true si le fichier existe, false sinon
	 */
	public boolean fichierExiste(String nomFichier) {
		updatelisteFichiers();
		boolean existe = false;
		int cpt = 0;
		while (existe == false && cpt < listeFichiers.length) {
			if (nomFichier == listeFichiers[cpt].getName()) {
				existe = true;
			}
			cpt++;
		}

		return existe;
	}

	/**
	 * permet de supprimer un fichier en fonction de son nom
	 * 
	 * @param nomFichier
	 * @return true si le fichier est supprimé, false si une erreur
	 */
	public boolean supprimerFichier(String nomFichier) {
		updatelisteFichiers();
		boolean done = false;
		int cpt = 0;
		while (done == false && cpt < listeFichiers.length) {
			if (nomFichier == listeFichiers[cpt].getName()) {
				done = listeFichiers[cpt].delete();
			}
			cpt++;
		}
		updatelisteFichiers();
		return done;
	}

	/*
	 * int filesize=1022386; int bytesRead; int currentTot = 0; Socket socket =
	 * new Socket("127.0.0.1",15123); byte [] bytearray = new byte [filesize];
	 * InputStream is = socket.getInputStream(); FileOutputStream fos = new
	 * FileOutputStream("copy.doc"); BufferedOutputStream bos = new
	 * BufferedOutputStream(fos); bytesRead =
	 * is.read(bytearray,0,bytearray.length); currentTot = bytesRead; do {
	 * bytesRead = is.read(bytearray, currentTot,
	 * (bytearray.length-currentTot)); if(bytesRead >= 0) currentTot +=
	 * bytesRead; } while(bytesRead > -1); bos.write(bytearray, 0 , currentTot);
	 * bos.flush(); bos.close(); socket.close(); }
	 * 
	 * BufferedInputStream buffIn = new BufferedInputStream(iStream);
	 * 
	 * 2e iStream is the InputStream object
	 * 
	 * BufferedOutputStream buffOut=new BufferedOutputStream(new
	 * FileOutputStream(file)); byte []arr = new byte [1024 * 1024]; int
	 * available = -1; while((available = buffIn.read(arr)) > 0) {
	 * buffOut.write(arr, 0, available); } buffOut.flush(); buffOut.close();
	 */
	/**
	 * TODO
	 * 
	 * @param fichier
	 *            (bytes?)
	 * @return true si créé, false si une erreur ou existe déjà
	 */
	public synchronized boolean creerFichier(byte[] fichier, String fileName) {

		System.out.println("maybe pls?");
		String fullfilename = localDir + "\\" + fileName;
		try {
			FileOutputStream fos = new FileOutputStream(fullfilename);
			fos.write(fichier);
			fos.close();

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		updatelisteFichiers();
		return true;
	}

	/**
	 * permet de récupérer un fichier local selon son nom
	 * 
	 * @param filename
	 *            nom du fichier à récupérer
	 * @return File du fichier, null si le fichier est inexistant
	 */
	public File getFichier(String filename) {
		File fichier = null;
		updatelisteFichiers();
		boolean done = false;
		int cpt = 0;
		while (done == false && cpt < listeFichiers.length) {
			if (filename.equals(listeFichiers[cpt].getName())) {
				fichier = listeFichiers[cpt];
			}
			cpt++;
		}
		return fichier;
	}

}
