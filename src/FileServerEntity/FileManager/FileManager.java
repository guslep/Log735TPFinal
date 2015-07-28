package FileServerEntity.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;

import FileServerEntity.Server.ActiveFileServer;

/***
 * la classe FileManager permet de monitorer les changements sur le dossier
 * files, ainsi que s'updater si des changements sont apport�s manuellement, �
 * l'aide de FileWatcher
 * 
 * @author Marc
 *
 */
public class FileManager {

	private ArrayList<File> listeFichiers;
	private HashMap<String, String> nomhashMap = new HashMap<String, String>();
	private HashMap<String, String> nomFichierDelete = new HashMap<String, String>();
	private static FileManager instance;
	private String localDirName = ActiveFileServer.getInstance().getThisFileServer().getNom();
	private String localDir = System.getProperty("user.dir") + "\\files - " + localDirName ;
	private FileWatcher fw;

	/***
	 * constructeur par d�faut, appel� lors du getInstance s'il est null
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
							.println("Erreur lors de la cr�ation du dossier pour les fichiers,");
					listeFichiers = null;
				} else {
					System.out
							.println("Serveur instanci�, utilisation du r�pertoire "
									+ localDir);
				}
			} catch (SecurityException se) {
				System.out.println("erreur de permissions");
			}

		} else {
			System.out.println("Serveur instanci�, utilisation du r�pertoire "
					+ localDir);
			updatelisteFichiers();
		}

		// d�marrage du fileWatcher pour monitorer les changements du dossier, le constructeur se charge de d�marrer un thread
				fw = new FileWatcher(localDir);

	}

	/**
	 * singleton pour s'approprier l'instance du FileManager (puisqu'elle peut
	 * etre rapell�e par FileWatcher
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
	 * constructeur par d�faut, va se construire en listant les items
	 * disponibles
	 */

	/**
	 * m�thode pour synchronizer les fichiers du r�pertoire local avec les
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
		//updatelisteFichiers();
		return listeFichiers;
	}

	
	/**
	 * permet d'obtenir les fichiers disponibles sur le serveur
	 */
	public void updatelisteFichiers() {
        Path rootDir=FileSystems.getDefault().getPath(localDir);
        ArrayList<File> list=new ArrayList<File>();
        getfileInDirectory(rootDir,list);
        this.listeFichiers=list;

    }
    private void getfileInDirectory(Path directory,   ArrayList<File> list){

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory);
            for(Path path:stream){
                if(Files.isDirectory(path)){
                    getfileInDirectory(path,list);
                }
                else{
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
	 * @return true si le fichier est supprim�, false si une erreur
	 */
	public boolean supprimerFichier(String nomFichier) {
		boolean done = false;
		int cpt = 0;

//		while (done == false && cpt < listeFichiers.size()) {
//				///A Optimiser pas mal sur que le code du bas marche toujours
//
//			if ((localDir+"\\"+nomFichier).equals(listeFichiers.get(cpt).getAbsolutePath())  ) {
//				done = listeFichiers.get(cpt).delete();
//				listeFichiers.remove(cpt);
//			}
//			cpt++;
//		}
			
			File directory=new File(localDir+"\\"+nomFichier);
			if(directory!=null){
				while(!done)
				try{
					File existBeforeDelete=new File(localDir+"\\"+nomFichier);
					if(existBeforeDelete.exists()){
				done = directory.delete();}
					else{
						done=true;
					}

				
				}catch(Exception e){
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
	 *
	 * 
	 * @param fichier
	 *            (bytes?)
	 * @return true si cr��, false si une erreur ou existe d�j�
	 */
	public synchronized boolean creerFichier(byte[] fichier, String fileName) {
		return creerFichier(fichier,fileName,false);
	}

		public synchronized boolean creerFichier(byte[] fichier, String fileName,Boolean updateSystem) {
		if(!updateSystem){
		fw.fileReceived(fileName);
		}
		System.out.println("maybe pls?");
		String fullfilename = localDir + "\\" + fileName;
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

		updatelisteFichiers();
		return true;
	}

	/**
	 * permet de r�cup�rer un fichier local selon son nom
	 * 
	 * @param filename
	 *            nom du fichier � r�cup�rer
	 * @return File du fichier, null si le fichier est inexistant
	 */
	public File getFichier(String filename) {
		File fichier = null;
		//updatelisteFichiers();
		boolean done = false;
		int cpt = 0;



        while (done == false && cpt < listeFichiers.size()) {
//            String test=listeFichiers.get(cpt);
            if (filename.equals(listeFichiers.get(cpt).getAbsolutePath()) ) {
                fichier = listeFichiers.get(cpt);

            }
            cpt++;
        }
        return fichier;
	}

    public String getLocalDir() {
        return localDir;
    }

	public HashMap<String, String> getNomFichierDelete() {
		return nomFichierDelete;
	}
}
