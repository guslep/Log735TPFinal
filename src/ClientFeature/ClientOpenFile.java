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
 Classe qui permet d'ouvrir un fichier avec le programme par défaut
 ******************************************************/


package ClientFeature;


import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ClientOpenFile {
    /**
     * MÃ©thode permettant d'ouvrir un fichier avec le programme par défaut
     * @param currentFile: Le fichier à ouvrir
     */
	public static void openFile(File currentFile){
		try {
			Desktop.getDesktop().open(currentFile);
		} catch (IOException e) {
			System.out.println("Unable to open the file!");
		}
	}
}
