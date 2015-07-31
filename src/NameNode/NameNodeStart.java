/******************************************************
 Cours : LOG735
 Session : Été 2015
 Groupe : 01
 Projet : Laboratoire #3
 Étudiants : Giullaume Lépine
 Marc Plamondon
 Pier-Luc Ménard
 Code(s) perm. : LEPG14099201
 PLAM210908907
 MENP27019200

 Date création : 2015-07-2
 Date dern. modif. : 2015-05-07
 ******************************************************
 Demarre une nameNode
 ******************************************************/

package NameNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NameNodeStart {
	public static void main(String[] args) throws IOException {
	NameNode nameNode =new NameNode();
		ServerSocket serverSocket = null;

		boolean isRunning=true;

		try {
			serverSocket = new ServerSocket(10118);
		}
		catch (IOException e)
		{
			System.err.println("On ne peut pas ecouter au  port: 10118.");
			System.exit(1);
		}
		System.out.println ("Le serveur est en marche, Attente de la connexion.....");

		new Thread(
				new ClientConnectionListener(nameNode)
		).start();

		while(isRunning){
			Socket succursaleSocket = null;
			try {
				succursaleSocket = serverSocket.accept();
			}
			catch (IOException e)
			{
				System.err.println("Accept a echoue.");
				System.exit(1);
			}


			//Create a new thread for each connection 1 client = 1 thread
			new Thread(
					new ResponseServerThread(succursaleSocket, nameNode)
			).start();



		}



	}
} 
