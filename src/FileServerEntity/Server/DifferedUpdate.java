package FileServerEntity.Server;

import FileServerEntity.FileManager.FileManager;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;

public class DifferedUpdate implements Runnable{

	boolean isdone;
	public void run() {
		isdone = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InitSymchronizerMessage allFile = new InitSymchronizerMessage(FileManager.getInstance()
				.getListeFichiers(), false,
				FileManager.getInstance()
						.getLocalDir());
		ActiveFileServer.getInstance()
				.pushToAllClient(allFile);
		isdone = true;
		
	}
	public boolean getIsDone(){
		return isdone;
	}

}
