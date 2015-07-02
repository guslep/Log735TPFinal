package Banque;

import java.net.*;
import java.io.*;

public class NameNodeConnectorConnector {
	public static void main(String[] args) throws IOException {
	NameNode nameNode =new NameNode();
		ServerSocket serverSocket = null;
		 int totalMoneyInTheBank=0;//$$$$$
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
