package NameNode;
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
 Represente un fileserver dans le reseau
 ******************************************************/


import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Gus on 6/4/2015.
 */
public class FileServer implements Serializable {
    /**
     * id du serveur
     */
	private int id;
    /**
     * ip du serveur
     */
	private InetAddress succursaleIPAdresse;
    /**
     * nom du serveur
     */
	private String nom;
    /**
     * port sur lequel il ecoute pour les autres serveurs
     */
	private String port;
    /**
     * port surlesquels les clients se connectent
     */
    private String clientPort;

    /**
     *
     * @param succursaleIPAdresse adresse ip du serveur
     * @param nom nom du serveur
     * @param portNumber port du serveur
     * @param clientPort port surlesquel les clients
     */
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



}
