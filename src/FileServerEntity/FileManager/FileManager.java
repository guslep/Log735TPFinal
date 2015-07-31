/******************************************************
 Cours : LOG735
 Session : �t� 2015
 Groupe : 01
 Projet : Laboratoire #3
 �tudiants : Guillaume L�pine
 Marc Plamondon
 Pier-Luc M�nard
 Code(s) perm. : LEPG14099201
 PLAM210908907
 MENP27019200

 Date cr�ation : 2015-07-2
 Date dern. modif. : 2015-05-07
 ******************************************************
la classe FileManager permet de monitorer les changements sur le dossier files, ainsi que s'updater si des 
changements sont apport�es manuellement, � l'aide de FileWatcher 
 ******************************************************/
package FileServerEntity.FileManager;

import FileServerEntity.Server.ActiveFileServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

	private ArrayList<File> listeFichiers;
	private HashMap<String, String> nomhashMap = new HashMap<String, String>();
	private HashMap<String, String> nomFichierDelete = new HashMap<String, String>();
	private static FileManager instance;
	private String localDirName = ActiveFileServer.getInstance()
			.getThisFileServer().getNom();
	private String localDir = System.getProperty("user.dir") + "\\files - "
			+ localDirName;
	private FileWatcher fw;

	/***
	 * constructeur par défaut, appelé lors du getInstance s'il est null
	 */
	private FileManager() {

		File test = new File(localDir);



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
			System.out
					.println("Serveur instancié, utilisation du répertoire "
							+ localDir);
			updatelisteFichiers();
		}

		// d�marrage du fileWatcher pour monitorer les changements du dossier,
		// le constructeur se charge de d�marrer un thread
		fw = new FileWatcher(localDir);

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
	public ArrayList<File> getListeFichiers() {
		// updatelisteFichiers();
		return listeFichiers;
	}

	/**
	 * permet d'obtenir les fichiers disponibles sur le serveur
	 */
	public void updatelisteFichiers() {
		Path rootDir = FileSystems.getDefault().getPath(localDir);
		ArrayList<File> list = new ArrayList<File>();
		getfileInDirectory(rootDir, list);
		this.listeFichiers = list;

	}
/**
 * permet de récupérer le fichier/dossier pour l'utiliser
 * @param directory
 * @param list
 */
	private void getfileInDirectory(Path directory, ArrayList<File> list) {

		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(directory);
			for (Path path : stream) {
				if (Files.isDirectory(path)) {
					getfileInDirectory(path, list);
				} else {
					list.add(path.toFile());
				}

			}
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * retourne le nombre de fichiers disponibles sur le serveur
	 * 
	 * @return nb de fichiers sur le serveur
	 */
	public int getNbFichiers() {
		updatelisteFichiers();
		if (listeFichiers != null)
			return listeFichiers.size();
		else
			return 0;
	}

	/**
	 * permet de supprimer un fichier en fonction de son nom
	 * 
	 * @param nomFichier
	 * @return true si le fichier est supprimé, false si une erreur
	 */
	public boolean supprimerFichier(String nomFichier) {
		boolean done = false;
		int cpt = 0;

		File directory = new File(localDir + "\\" + nomFichier);
		if (directory != null) {
			while (!done)
				try {
					File existBeforeDelete = new File(localDir + "\\"
							+ nomFichier);
					if (existBeforeDelete.exists()) {
						done = directory.delete();
					} else {
						done = true;
					}

				} catch (Exception e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

		}

		updatelisteFichiers();
		return done;
	}

	/**
	 *
	 * 
	 * @param fichier
	 *            (bytes?)
	 * @return true si créé, false si une erreur ou existe déjà
	 */
	public synchronized boolean creerFichier(byte[] fichier, String fileName) {
		return creerFichier(fichier, fileName, false);
	}

	/**
	 * permet de créer un fichier localement sur le dossier
	 * @param fichier fichier à écrire
	 * @param fileName nom du fichier
	 * @param updateSystem 
	 * @return
	 */
	public synchronized boolean creerFichier(byte[] fichier, String fileName,
			Boolean updateSystem) {
		if (!updateSystem) {
			fw.fileReceived(fileName);
		}

		String fullfilename = localDir + "\\" + fileName;
		File newFile = new File(fullfilename);
		if (!newFile.exists() && newFile.getParentFile() != null) {

			newFile.getParentFile().mkdirs();
			//sleep pour permettre au filewatcher de surveiller le dossier
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		try {
			FileOutputStream fos = new FileOutputStream(fullfilename, true);
			fos.write(fichier);
			fos.close();

		} catch (FileNotFoundException e) {

			return false;
		} catch (IOException e) {
			return false;
		}

		updatelisteFichiers();
		ActiveFileServer.getInstance().fileWritten(fileName);
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

		boolean done = false;
		int cpt = 0;

		while (done == false && cpt < listeFichiers.size()) {
			// String test=listeFichiers.get(cpt);
			if (filename.equals(listeFichiers.get(cpt).getAbsolutePath())) {
				fichier = listeFichiers.get(cpt);

			}
			cpt++;
		}
		return fichier;
	}

	public String getLocalDir() {
		return localDir;
	}
/**
 * le hashmap sert à savoir quels fichiers ne sera pas resynchroniser.
 * @return
 */
	public HashMap<String, String> getNomFichierDelete() {
		return nomFichierDelete;
	}
/**
 * retourne true si le fichier/dossier existe, pour prévenir les erreurs.
 * @param fileName nom du fichier
 * @return true si existe, false si non existant
 */
	public Boolean exist(String fileName) {

		File directory = new File(localDir + "\\" + fileName);
		if (directory.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
