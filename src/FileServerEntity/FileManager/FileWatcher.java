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
Cette classe est utilisée pour updater automatiquement les listes du FileManager,
advenant le cas ou des modifications sont faites manuellementdans le dossier
 ******************************************************/
package FileServerEntity.FileManager;

import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Message.ServerMessage.MessageDelete;
import FileServerEntity.Server.ActiveFileServer;
import FileServerEntity.Server.DifferedUpdate;
import FileServerEntity.Server.FileServerListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;


public class FileWatcher {


	HashMap<String, String> nomhashMap = new HashMap<String, String>();
	private final static HashMap<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
	static WatchService myWatcher;
	static String pathlocal;
	static Path toWatch;
	static DifferedUpdate du = null;


	/**
	 * constructeur avec le dossier à monitorer
	 * @param path chemin à surveiller
	 */
	public FileWatcher(String path) {
		this.pathlocal = path;
		this.toWatch = Paths.get(pathlocal);

		if (toWatch == null) {
			System.out.println("Directory not found");
		}

		System.out.println("directory " + pathlocal + " found");

		// Monitoring du dossier demandï¿½ en paramï¿½tre
		this.myWatcher = null;
		try {
			this.myWatcher = toWatch.getFileSystem().newWatchService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// start the file watcher thread below
		MyWatchQueueReader fileWatcher = new MyWatchQueueReader(myWatcher,
				nomhashMap);
		Thread th = new Thread(fileWatcher, "FileWatcher");
		th.start();

		// register folder to watch
		try {
			toWatch.register(myWatcher, ENTRY_CREATE, ENTRY_DELETE);
			registerAll(toWatch);
		} catch (IOException e) {
		}
		
		//add all existing folders to watcher
		
		

	}
/**
 * 
 * @param dir chemin à rajouter au monitoring
 * @throws IOException
 */
	private static void register(Path dir) throws IOException {
		WatchKey key = dir.register(myWatcher, ENTRY_CREATE, ENTRY_DELETE);
		Path prev = keys.get(key);
		if (prev == null) {
			System.out.format("register: %s\n", dir);
		} else {
			if (!dir.equals(prev)) {
				System.out.format("update: %s -> %s\n", prev, dir);
			}
		}
		keys.put(key, dir);
	}

	public void fileReceived(String filereceived) {
		nomhashMap.put(filereceived, filereceived);
	}

	/**
	 * Surveille le chemin et tous les sous-chemin du dossier
	 */
	private static void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
													 BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;

			}
		});
	}

	private static class MyWatchQueueReader implements Runnable {

		/**
		 * the watchService that is passed in from above
		 */
		private WatchService myWatcher;
		private HashMap<String, String> nomHashMap;

		public MyWatchQueueReader(WatchService myWatcher,
								  HashMap<String, String> nomHashMap) {
			this.myWatcher = myWatcher;
			this.nomHashMap = nomHashMap;
		}

		@Override
		public void run() {
			try {
				System.out.println("in thread");
				// on récupère la racine à monitorer
				WatchKey key = myWatcher.take();
				while (key != null) {
					// on récupère tous les états
					for (WatchEvent event : key.pollEvents()) {
						System.out.println("Received " + event.kind()
								+ " event for file: " + event.context());
						FileManager fm = FileManager.getInstance();
						fm.updatelisteFichiers();
						// remote create new files/folders
						Path name = (Path) key.watchable();
						Path child = name.resolve((Path) event.context());
						String HashMapPath = child.toString().replace(pathlocal + "\\", "");
						//si l'événement est la création d'un fichier.
						if (event.kind().toString().equals("ENTRY_CREATE")) {

							try {

								// if new folder, add to watch
								if (Files.isDirectory(child,
										LinkOption.NOFOLLOW_LINKS)) {
									registerAll(child);
								}
							} catch (IOException x) {
								x.printStackTrace();
							}
							// to prevent endless loops

							if(du == null){ 
								 new Thread(
							                du =new DifferedUpdate()
							        ).start();
								 
								}
								else if(du.getIsDone() == true){
									 new Thread(
								                du =new DifferedUpdate()
								        ).start();
								}

							// pour éviter de boucler à l'infini, on regarde si le fichier est dans le hashmap, permettant ainsi d'empêcher la copie 
							if (nomHashMap.containsKey(HashMapPath.toString())) {
								nomHashMap.remove(HashMapPath.toString());
							} else {
								fm = FileManager.getInstance();
								fm.updatelisteFichiers();
								File nouveauFichier = fm.getFichier(child.toString());
								if (nouveauFichier != null) {
									new Thread(new FileServerListener(
											nouveauFichier, HashMapPath)).start();

									System.out.println("envoie du fichier "
											+ child.toString());
								} else {
									// ajouter le dossier qui vient d'Ãªtre
									// créé au file watcher
								}

							}
							// gère les suppressions
						} else if (event.kind().toString()
								.equals("ENTRY_DELETE")) {
							fm = FileManager.getInstance();
							//boolean existe = fm.fichierExiste(filename);
							if (true) {

								FileManager.getInstance().getNomFichierDelete().put(HashMapPath, HashMapPath);
								//TODO: envoie d'une commande de delete
								ActiveFileServer.getInstance().pushToAllServer(new MessageDelete(HashMapPath));
                                InitSymchronizerMessage allFile = new InitSymchronizerMessage(FileManager.getInstance()
                                        .getListeFichiers(), false,
                                        FileManager.getInstance()
                                                .getLocalDir());

                                ActiveFileServer.getInstance()
                                        .pushToAllClient(allFile);


							}

						}

						key.reset();
						key = myWatcher.take();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Stopping thread");
		}
	}


}