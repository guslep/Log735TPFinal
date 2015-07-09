package FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import succursale.FileServerListener;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/***
 * Cette classe est utilisée pour updater automatiquement les listes du
 * FileManager, advenant le cas ou des modifications sont faites manuellement
 * dans le dossier
 * 
 * exemple tiré de http://www.thecoderscorner.com/team-blog/java-and-jvm/java-nio/36-watching-files-in-java-7-with-watchservice
 * 
 * @author Marc
 *
 */
public class FileWatcher  {
	
	public FileWatcher(String path){
		Path toWatch = Paths.get(path);
	      if(toWatch == null) {
	          System.out.println("Directory not found");
	      }
	      
	      System.out.println("directory " + path + " found");

	      //Monitoring du dossier demandé en paramètre
	      WatchService myWatcher = null;
		try {
			myWatcher = toWatch.getFileSystem().newWatchService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      // start the file watcher thread below
	      MyWatchQueueReader fileWatcher = new MyWatchQueueReader(myWatcher);
	      Thread th = new Thread(fileWatcher, "FileWatcher");
	      th.start();
	      
	      //register folder to watch
	        try {
				toWatch.register(myWatcher, ENTRY_CREATE, ENTRY_DELETE);
			} catch (IOException e) {}
	      
	}
	  
	
	private static class MyWatchQueueReader implements Runnable {
		 
        /** the watchService that is passed in from above */
        private WatchService myWatcher;
        public MyWatchQueueReader(WatchService myWatcher) {
            this.myWatcher = myWatcher;
        }
 
        /**
         * In order to implement a file watcher, we loop forever 
         * ensuring requesting to take the next item from the file 
         * watchers queue.
         */
        @Override
        public void run() {
            try {
            	System.out.println("in thread");
                // get the first event before looping
                WatchKey key = myWatcher.take();
                while(key != null) {
                    // we have a polled event, now we traverse it and 
                    // receive all the states from it
                    for (WatchEvent event : key.pollEvents()) {
                        System.out.println("Received " + event.kind() + " event for file: " + event.context() );
                        FileManager fm = FileManager.getInstance();
        				fm.updatelisteFichiers();
        				//remote create new files/folders
        				
        				if(event.kind().toString().equals("ENTRY_CREATE")){
        					String filename = event.context().toString();
        					fm = FileManager.getInstance();
        					File nouveauFichier = fm.getFichier(filename); 
        					FileServerListener.ajoutFichier(nouveauFichier, filename);
        					System.out.println("envoie du fichier " + filename);
        				}
        				else if(event.kind().toString().equals("ENTRY_DELETE")){
        					// to do, remote delete
        				}
        				
                    }
                    
                    key.reset();
                    key = myWatcher.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Stopping thread");
        }
    }
	
}
