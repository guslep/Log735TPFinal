package FileServerEntity.Server;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.FileManager.MissingFileSender;
import FileServerEntity.FileManager.TransitFile;

import FileServerEntity.Message.*;
import FileServerEntity.Message.ServerMessage.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Gus on 6/11/2015.
 */
public class FileServerResponseThread implements Runnable {

    private boolean connectionDestroyed=false;

	ObjectOutputStream messageSender;
	Socket echoSocket = null;

	ObjectInputStream messageReader;
	private HashMap<String, TransitFile> fileBeingWritten = new HashMap<String, TransitFile>();

	public FileServerResponseThread(Socket sucursaleSocket) {

		echoSocket = sucursaleSocket;
		try {
			messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
			messageReader = new ObjectInputStream((echoSocket.getInputStream()));

			SynchMessage message = null;

			try {
				message = (SynchMessage) messageReader.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			ActiveFileServer.getInstance().getListeSuccursale()
					.get(message.getIdSuccursale()).setConnectionThread(this);
			ActiveFileServer.getInstance().printSuccursale();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * @param succursaleIPAdresse
	 *            adresse ip ;a qui on ce connecte
	 * @param idSuccursale
	 *            id de la succursale qu'on contacte
	 * @param portNumber
	 *            port sur lequel on contacte les succursales
	 */
	public FileServerResponseThread(InetAddress succursaleIPAdresse,
									Integer idSuccursale, Integer portNumber) {

		try {
			echoSocket = new Socket(succursaleIPAdresse, portNumber);

			messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
			messageReader = new ObjectInputStream(echoSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Hote inconnu: " + succursaleIPAdresse);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Ne pas se connecter au serveur: "
					+ succursaleIPAdresse);
			System.exit(1);
		}
		try {
			messageSender.writeObject(new SynchMessage(idSuccursale));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("La succursale vient d'initier une connexion.....");
		// BufferedReader stdIn = new BufferedReader(new
		// InputStreamReader(System.messageReader));

	}

	@Override
	public void run() {

		System.out.println("connexion reussie");
		System.out.println("Attente de l'entree.....");

		Message messageReceived;



        try
        {

            while ((messageReceived =(Message)messageReader.readObject() ) != null)
            {
            	System.out.println("message de type " + messageReceived);
                if(MessageNewFile.class.isInstance(messageReceived)){

                    TransitFile transit=new TransitFile(this,((MessageNewFile)messageReceived).getFileName(),((MessageNewFile)messageReceived).getFileLength());
                    fileBeingWritten.put(transit.getNom(),transit);
                }
                else if(FileMessage.class.isInstance(messageReceived)){
                	FileMessage msg = (FileMessage) messageReceived;
                	fileBeingWritten.get(msg.getFileName()).addByte(msg.getByteArray(), msg.getPosition());
                }
                else if(InitSymchronizerMessage.class.isInstance(messageReceived)){

                    new Thread(new MissingFileSender((InitSymchronizerMessage)messageReceived,this)).start();
                }
				else if(MessageDelete.class.isInstance(messageReceived)){
					FileManager.getInstance().supprimerFichier(((MessageDelete)messageReceived).getFileName());
				}
				else if(MessageFileWriteAttempt.class.isInstance(messageReceived)){
					MessageFileWriteAttempt msg=(MessageFileWriteAttempt) messageReceived;
					ActiveFileServer.getInstance().reserveFileName(msg.getFileName());

				}else if(MessageFileWriteFail.class.isInstance((messageReceived))){

					MessageFileWriteFail msg=(MessageFileWriteFail) messageReceived;
					ActiveFileServer.getInstance().reserveFileFailed(msg.getFileName());

				}

            }
        } catch (SocketException e){
            connectionDestroyed=true;
            System.out.println("erreur dans la connection");
            try {
                messageSender.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("erreur dans l'objet");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


	}

	/**
	 * envoie une message à une succursale
	 * 
	 * @param messageTosSend
	 */
	public synchronized void sendMessage(Message messageTosSend) {

		try {

            if(!connectionDestroyed){
                messageSender.writeObject(messageTosSend);
            }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileWasWritten(String fileName) {

		fileBeingWritten.remove(fileName);
	}

    public boolean isConnectionDestroyed() {
        return connectionDestroyed;
    }
}
