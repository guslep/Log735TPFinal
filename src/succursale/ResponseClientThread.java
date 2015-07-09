package succursale;

import FileManager.TransitFile;
import succursale.Message.FileMessage;
import succursale.Message.Message;
import succursale.Message.MessageNewFile;
import succursale.Message.SynchMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Gus on 6/11/2015.
 */
public class ResponseClientThread implements Runnable {

	ObjectOutputStream messageSender;
	Socket echoSocket = null;

	ObjectInputStream messageReader;
	private HashMap<String, TransitFile> fileBeingWritten = new HashMap<String, TransitFile>();

	public ResponseClientThread(Socket sucursaleSocket) {

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
			HashMap a = ActiveFileServer.getInstance().getListeSuccursale();
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
	public ResponseClientThread(InetAddress succursaleIPAdresse,
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
		try {

			while ((messageReceived = (Message) messageReader.readObject()) != null) {
				System.out.println("message de type " + messageReceived);
				if (MessageNewFile.class.isInstance(messageReceived)) {
					TransitFile transit = new TransitFile(this,
							((MessageNewFile) messageReceived).getFileName(),
							((MessageNewFile) messageReceived).getFileLength());
					fileBeingWritten.put(transit.getNom(), transit);
				} else if (FileMessage.class.isInstance(messageReceived)) {
					FileMessage msg = (FileMessage) messageReceived;
					fileBeingWritten.get(msg.getFileName()).addByte(
							msg.getByteArray(), msg.getPosition());
				}

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
			messageSender.writeObject(messageTosSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileWasWritten(String fileName) {

		fileBeingWritten.remove(fileName);
	}

}
