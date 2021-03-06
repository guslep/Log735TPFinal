package FileServerEntity.Server;

import NameNode.FileServer;


import FileServerEntity.Message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ActiveFileServer {

	FileServer thisFileServer;
	private final Object lockListeClient = new Object();
	private final Object lockListeServer = new Object();
	static HashMap<Integer, FileServerClient> listeSuccursale = new HashMap<Integer, FileServerClient>();
    private ArrayList<ClientResponseThread> connectionClient=new ArrayList<ClientResponseThread>();
	private String portNumber;

	public HashMap<Integer, FileServerClient> getListeSuccursale() {

		synchronized (lockListeServer){
		return listeSuccursale;}
	}

	static ActiveFileServer instance;

	private ActiveFileServer() {
	}

	public static ActiveFileServer getInstance() {

		if (instance == null) {
			instance = new ActiveFileServer();
		}
		return instance;
	}

	/**
	 * print la liste des clients
	 */
	public void printSuccursale() {
		printHashMap(listeSuccursale);
	}

	/***
	 * imprime un hashmap recu en parametre
	 * 
	 * @param listeclient
	 */
	private static void printHashMap(HashMap listeclient) {
		Iterator it = listeclient.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			FileServerClient currentClient = (FileServerClient) pair.getValue();

			System.out.println("Id: " + currentClient.getId());
			System.out.print("Nom succursale: " + currentClient.getNom());

			System.out.println(" Adresse IP: "
					+ currentClient.getSuccursaleIPAdresse().getHostAddress()
					+ "\n");

		}

	}

	public FileServer getThisFileServer() {
		return thisFileServer;
	}

	public static void setListeSuccursale(
			HashMap<Integer, FileServerClient> listeSuccursale) {
		ActiveFileServer.listeSuccursale = listeSuccursale;
	}

	public void setThisFileServer(FileServer thisFileServer) {
		this.thisFileServer = thisFileServer;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

		public void ajouterClient(ClientResponseThread client){
			synchronized (lockListeClient){
				connectionClient.add(client);
			}

		}

	public void ajouterServeur(FileServerClient nouveauServeut){
		synchronized (lockListeServer){
			listeSuccursale.put(nouveauServeut.getId(),nouveauServeut);
		}

	}

	public  void pushToAllServer(Message message) {
		synchronized (lockListeServer){
		Iterator iter = listeSuccursale.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			FileServerClient currentClient = (FileServerClient) pair.getValue();
			if(currentClient.getConnectionThread().isConnectionDestroyed()){
                listeSuccursale.remove(pair.getKey());
            }else{
                currentClient.getConnectionThread().sendMessage(message);

            }



		}}
	}


    public  void pushToAllClient(Message message){
		synchronized (lockListeClient) {
			Iterator iter = connectionClient.iterator();


			while (iter.hasNext()) {
				ClientResponseThread client = (ClientResponseThread) iter.next();

				if (client == null || client.isConnectionDestroyed()) {
					iter.remove();
				} else {
					client.sendMessage(message);

				}


			}
		}
    }

    public ArrayList getConnectionClient() {
		synchronized (lockListeClient){
        return connectionClient;}
    }
}
