package NameNode;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Gus on 6/4/2015.
 */
public class FileServer implements Serializable {

	private int id;

	private InetAddress succursaleIPAdresse;

	private String nom;
	private String port;
    private String clientPort;

	public FileServer(InetAddress succursaleIPAdresse, String nom,
                      String portNumber, String clientPort) {
		this.succursaleIPAdresse = succursaleIPAdresse;

		this.nom = nom;
		this.port = portNumber;
        this.clientPort = clientPort;
    }

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public InetAddress getSuccursaleIPAdresse() {
		return succursaleIPAdresse;
	}



	public String getNom() {
		return nom;
	}

	public void setSuccursaleIPAdresse(InetAddress succursaleIPAdresse) {
		this.succursaleIPAdresse = succursaleIPAdresse;
	}

	public String toString() {

		return id + "," + nom + "," +  succursaleIPAdresse.getHostAddress() + "," + port;

	}

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }



    // public synchronized int doWHitdraw(int whitdraw){
	// if(whitdraw<0){
	// return -1;
	// }
	//
	// try {
	// montantLock.lock();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// try{
	// if(montant-whitdraw<0)
	// {
	// return -1;//retourne -1 si le retrait a été impossible
	//
	// }else{
	// System.out.println("Montant avant retrait "+montant);
	// montant-=whitdraw;}
	//
	// }finally {
	//
	// montantLock.unlock();
	// System.out.println("Montant total de la succursale est de "+
	// montant+" "+whitdraw +" ont été retiré");
	//
	// }
	//
	//
	//
	// return whitdraw;
	// }
}
