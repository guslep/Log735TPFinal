package FileServerEntity.ServerStatus;

import FileServerEntity.Message.ServerMessage.MessageServerStatus;
import FileServerEntity.Server.ActiveFileServer;
import FileServerEntity.Server.NameNodeListner;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

public class AutoServerStatus implements Runnable{

	private static final int SLEEP_TIMER = 60;
	private static final int SECOND_TO_MILLISECOND = 1000;
	
	SystemInfo currentServerInfo;
	NameNodeListner nameNode;
	
    public AutoServerStatus(NameNodeListner nameNode) {
    	currentServerInfo = new SystemInfo();
    	this.nameNode = nameNode;
    	System.out.println("load watcher started");
	}
	
	@Override
	public void run() {
		System.out.println("run started");
			try {
				while (true){
					Thread.sleep(SLEEP_TIMER * SECOND_TO_MILLISECOND);

		            	try {
		            		MessageServerStatus messageStatus = new MessageServerStatus
		            				("message Server Status", 
		            				ActiveFileServer.getInstance().getThisFileServer().getId(), 
		            				 currentServerInfo.getProcessCpuAvailable(), 
		            				 currentServerInfo.getFreeRam(),currentServerInfo.getConnectionNumber());
		            		this.nameNode.sendMessage(messageStatus);
		            	} catch (MalformedObjectNameException e) {
		            		// TODO Auto-generated catch block
		            		e.printStackTrace();
		            	} catch (InstanceNotFoundException e) {
		            		// TODO Auto-generated catch block
		            		e.printStackTrace();
		            	} catch (ReflectionException e) {
		            		// TODO Auto-generated catch block
		            		e.printStackTrace();
		            	}

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}

}
