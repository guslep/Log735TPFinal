package FileManager;

import java.io.File;

public class testClass {
	public static void main (String[] args) {
		//test cr�ation fileManager
		FileManager fm = new FileManager();
		System.out.println(fm.getNbFichiers());
		
		//test �criture
		byte[] dafuck = new byte[1024];
		dafuck[0] = 0;
		fm.creerFichier(dafuck, "test.txt");
		
		//test lecture
		File fichier = fm.getFichier("test.txt");
		if(fichier != null)
		System.out.println("fichier " + fichier.getName() + " existe");
		else
			System.out.println("fichier test n'existe pas");
		
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
