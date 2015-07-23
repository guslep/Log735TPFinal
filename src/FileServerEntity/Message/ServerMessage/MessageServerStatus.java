package FileServerEntity.Message.ServerMessage;
import java.io.Serializable;

import FileServerEntity.Message.Message;

public class MessageServerStatus extends Message implements Serializable{

	private int idServer;
	private double cpuAvailabilty;
	private double ramAvailability;
    private  int connectionNumber;

    public MessageServerStatus(String type, int idServer, double cpuAvailabilty, double ramAvailability,int connectionNumber) {
        super(type);
        this.setCpuAvailabilty(cpuAvailabilty);
        this.setRamAvailability(ramAvailability);
        this.idServer = idServer;
        this.connectionNumber=connectionNumber;
    }

    public int getIdServer(){
		return this.idServer;
	}
    
    public void setIdServer(int idServer){
		this.idServer = idServer;
	}

	public double getCpuAvailabilty() {
		return cpuAvailabilty;
	}

	public void setCpuAvailabilty(double cpuAvailabilty) {
		this.cpuAvailabilty = cpuAvailabilty;
	}

	public double getRamAvailability() {
		return ramAvailability;
	}

	public void setRamAvailability(double ramAvailability) {
		this.ramAvailability = ramAvailability;
	}

    public int getConnectionNumber() {
        return connectionNumber;
    }

    public void setConnectionNumber(int connectionNumber) {
        this.connectionNumber = connectionNumber;
    }
        /*
        Compare quel des serveurs est le mieux
         */
    public Boolean isLessUsed(MessageServerStatus otherServer){
        return this.getRamAvailability()*5-this.connectionNumber*10<otherServer.getRamAvailability()*5-otherServer.getConnectionNumber()*10;

    }
}
