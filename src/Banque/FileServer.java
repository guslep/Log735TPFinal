package Banque;

import java.beans.Transient;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;

/**
 * Created by Gus on 6/4/2015.
 */
public class FileServer implements Serializable {

	private int id;

	private transient InetAddress succursaleIPAdresse;
	private int montant;
	private String nom;
	private String port;


	public FileServer(InetAddress succursaleIPAdresse, int montant, String nom,
			String portNumber) {
		this.succursaleIPAdresse = succursaleIPAdresse;
		this.montant = montant;
		this.nom = nom;
		this.port = portNumber;
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

	public int getMontant() {
		return montant;
	}

	public void setMontant(int montant) {
		this.montant = montant;
	}

	public String getNom() {
		return nom;
	}

	public String toString() {

		return id + "," + nom + "," + montant + ","
				+ succursaleIPAdresse.getHostAddress() + "," + port;

	}

	public synchronized void receiveDeposit(int depot) {

		try {
			montant += depot;

		} finally {

		}

	}

	public synchronized int doWHitdraw(int whitdraw) {
		if (whitdraw < 0) {
			return -1;
		}

		if (montant - whitdraw < 0) {
			return -1;// retourne -1 si le retrait a été impossible

		} else {
			System.out.println("Montant avant retrait " + montant);
			montant -= whitdraw;
		}

		return whitdraw;
	}
}
