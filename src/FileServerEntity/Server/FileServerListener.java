package FileServerEntity.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.Message.ServerMessage.FileMessage;
import FileServerEntity.Message.ServerMessage.MessageNewFile;

/**
 * Classe permettant d'envoyer des messages d'ex�cution pour les autres serveurs
 * 
 * @author Marc
 *
 */
public class FileServerListener implements Runnable{

	private final static int NBBYTEPARMESSAGE = 2048;
    private final static int  NBBYTEMAXTHETHERYNG=100000000;
	private File nouveauFichier;
    private FileServerResponseThread caller;
	private ClientResponseThread clientConnection;
	String filename;

	public FileServerListener(File nouveauFichier, String filename, FileServerResponseThread caller) {
		this.nouveauFichier = nouveauFichier;
		this.filename = filename;
        this.caller=caller;
	}
    public FileServerListener(File nouveauFichier, String filename) {
        this.nouveauFichier = nouveauFichier;
        this.filename = filename;
        this.caller=null;

    }

	public FileServerListener(File sentFile, String fileName, ClientResponseThread caller) {
		this.nouveauFichier = sentFile;
		this.filename = fileName;
		this.caller=null;
		this.clientConnection=caller;

	}

	@Override
	public void run() {

		int fileSize;
		// r�cup�re les bytes d'un fichier


		if (nouveauFichier.length() <= Integer.MAX_VALUE) {
			
			int oldlength = 0;
			boolean proceed = false;
			while(!proceed){
				System.out.println(nouveauFichier.length());
				if( oldlength != (int) nouveauFichier.length()){
					oldlength = (int) nouveauFichier.length();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					proceed = true;
				}
			}
			fileSize = (int) nouveauFichier.length();
			//forgive us father we have sinned !!
			boolean hack=true;
			while(hack){
				try{
					hack=false;
					copy(fileSize);
				}catch(Exception e){
					try {
						Thread.sleep(1000);
						hack=true;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			
			
			
			
			// si succ�s, retourne true

		}
		// >MaxValue

	}
	
	private void copy(int fileSize){


		ArrayList<byte[]> listeBytes = new ArrayList<byte[]>();

		Path path = Paths.get(nouveauFichier.getAbsolutePath());
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			System.out.print(e.getMessage());
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
		MessageNewFile mnf = new MessageNewFile(fileSize, filename.replace(FileManager.getInstance().getLocalDir()+"\\",""));
        ActiveFileServer afs = ActiveFileServer.getInstance();
		if(clientConnection!=null){
			clientConnection.sendMessage(mnf);
		}
		else if(caller==null){


		afs.pushToAllServer(mnf);}

        else{
            caller.sendMessage(mnf);
        }



		// cr�ation des messages pour les diff�rents byte array(FileMessage)
        Date lastMesureTook=new Date();
        int numberPacketSent=0;
		for (int i = 0; i < listeBytes.size(); i++) {


			FileMessage messageEnvoi = new FileMessage(listeBytes.get(i),
					filename, (i * NBBYTEPARMESSAGE));
			if(clientConnection!=null){
				clientConnection.sendMessage(messageEnvoi);
			}
			else if(caller==null){
                ActiveFileServer.getInstance().pushToAllServer(messageEnvoi);

            } else{
                caller.sendMessage(messageEnvoi);
            }
            numberPacketSent++;
            Date now=new Date();
             if(numberPacketSent*NBBYTEPARMESSAGE>NBBYTEMAXTHETHERYNG&&now.getTime()-lastMesureTook.getTime()<1000){
                 try {
                	 System.out.println("Hey, tethering the connection, NbByteParMessage: " + (numberPacketSent*NBBYTEPARMESSAGE) + " NbByteMaxTethering: " + NBBYTEMAXTHETHERYNG);
                     Thread.sleep(1100-(now.getTime() - lastMesureTook.getTime()));
                     numberPacketSent=0;
                     
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }else if(now.getTime()-lastMesureTook.getTime()>1000){
                 lastMesureTook=new Date();
                 //numberPacketSent=0;
             }

		}
		//message final pour terminer la connexion
		FileMessage messageFinal = new FileMessage(null, filename, 0);
		if(clientConnection!=null){
			clientConnection.sendMessage(messageFinal);
		} else if(caller==null){
            afs.getInstance().pushToAllServer(messageFinal);
        }
		else{
            caller.sendMessage(messageFinal);
        }


	}
}
