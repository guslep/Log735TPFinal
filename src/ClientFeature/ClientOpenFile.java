package ClientFeature;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class ClientOpenFile {

	public static void openFile(File currentFile){
		try {
			Desktop.getDesktop().open(currentFile);
		} catch (IOException e) {
			System.out.println("Unable to open the file!");
		}
	}
}
