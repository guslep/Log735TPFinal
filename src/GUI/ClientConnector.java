package GUI;

import FileServerEntity.Message.ClientMessage.ClientAddFile;
import FileServerEntity.Message.ClientMessage.ClientDeleteFile;
import FileServerEntity.Message.ClientMessage.ClientReadFile;
import UI.FileProgressUpdate;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Gus on 7/16/2015.
 */
public class ClientConnector extends Observable {

	private static ClientConnector instance;
	private Inet4Address nameNodateSuccursaleIPAdresse;
	private Integer nameNodeportNumber;

	private ServerConnectionThread serverConnectedTo;
	private ArrayList<String> listFileAvailaible;
	
	private String serverName;
    private int serverPort;

	private ClientConnector() {
	}

	public static ClientConnector getInstance() {
		if (instance == null) {
			instance = new ClientConnector();
		}
		return instance;
	}

	public void addFile(File fichierOuDossier, String path) {
		// TODO lire et ajouter le fichier comme avec le serveur

		new Thread(new ThreadUploadFile(fichierOuDossier, path)).start();

	}

	public void deleteFile(String fileName) {

		serverConnectedTo.sendMessage(new ClientDeleteFile(fileName));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setChanged();
		this.notifyObservers();
	}

	public void readFile(String fileName) {
		serverConnectedTo.sendMessage(new ClientReadFile(fileName));
	}

	public void ConnectToFileSystem(Inet4Address nameNoderIpAdress,
			int portNumber) {
		new Thread(new SystemConnector(nameNoderIpAdress, portNumber)).start();
		this.nameNodateSuccursaleIPAdresse = nameNoderIpAdress;
		this.nameNodeportNumber = portNumber;
	}

	public ServerConnectionThread getServerConnectedTo() {
		return serverConnectedTo;
	}

	public void setServerConnectedTo(ServerConnectionThread serverConnectedTo) {
		this.serverConnectedTo = serverConnectedTo;
	}

	public static void setInstance(ClientConnector instance) {
		ClientConnector.instance = instance;
	}

	public ArrayList<String> getListFileAvailaible() {
		return listFileAvailaible;
	}

	public synchronized void setListFileAvailaible(
			ArrayList<File> listFileAvailaible, String rootDirectory) {

		ArrayList<String> listRelativeFilePath = new ArrayList<String>();
		if (listFileAvailaible != null) {
			for (File file : listFileAvailaible) {
				listRelativeFilePath.add(file.getPath().replace(
						rootDirectory + "\\", ""));
			}
		}
		this.listFileAvailaible = listRelativeFilePath;
		this.setChanged();
		this.notifyObservers();

	}

	public Inet4Address getNameNodateSuccursaleIPAdresse() {
		return nameNodateSuccursaleIPAdresse;
	}

	public Integer getNameNodeportNumber() {
		return nameNodeportNumber;
	}
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = Integer.parseInt(serverPort);
	}

	public void updateProgressBar(FileProgressUpdate update){
		this.setChanged();
		this.notifyObservers(update);

		System.out.println("pls update ome one");
	}
}
