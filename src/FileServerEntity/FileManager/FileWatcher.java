package FileServerEntity.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.nio.file.attribute.*;

import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import FileServerEntity.Message.ServerMessage.MessageDelete;
import FileServerEntity.Server.ActiveFileServer;
import FileServerEntity.Server.DifferedUpdate;
import FileServerEntity.Server.FileServerListener;
import FileServerEntity.Server.NameNodeListner;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/***
 * Cette classe est utilis�e pour updater automatiquement les listes du
 * FileManager, advenant le cas ou des modifications sont faites manuellement
 * dans le dossier
 * 
 * @author Marc
 *
 */
public class FileWatcher {


	HashMap<String, String> nomhashMap = new HashMap<String, String>();
	private final static HashMap<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
	static WatchService myWatcher;
	static String pathlocal;
	static Path toWatch;
	static DifferedUpdate du= null;


	public FileWatcher(String path) {
		this.pathlocal = path;
		this.toWatch = Paths.get(pathlocal);

		if (toWatch == null) {
			System.out.println("Directory not found");
		}

		System.out.println("directory " + pathlocal + " found");

		// Monitoring du dossier demand� en param�tre
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
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
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

		/**
		 * In order to implement a file watcher, we loop forever ensuring
		 * requesting to take the next item from the file watchers queue.
		 */
		@Override
		public void run() {
			try {
				System.out.println("in thread");
				// get the first event before looping
				WatchKey key = myWatcher.take();
				while (key != null) {
					// we have a polled event, now we traverse it and
					// receive all the states from it
					for (WatchEvent event : key.pollEvents()) {
						System.out.println("Received " + event.kind()
								+ " event for file: " + event.context());
						FileManager fm = FileManager.getInstance();
						fm.updatelisteFichiers();
						// remote create new files/folders
						Path name = (Path) key.watchable();
						Path child = name.resolve((Path) event.context());
						String HashMapPath = child.toString().replace(pathlocal + "\\", "");
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
									// ajouter le dossier qui vient d'être
									// créé au file watcher
								}

							}
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