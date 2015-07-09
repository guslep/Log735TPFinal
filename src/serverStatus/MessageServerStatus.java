package serverStatus;
import java.io.Serializable;

import succursale.Message.Message;

public class MessageServerStatus extends Message implements Serializable{

	private int idServer;
	private double cpuAvailabilty;
	private double ramAvailability;

    public MessageServerStatus(String type, int idServer, double cpuAvailabilty, double ramAvailability) {
        super(type);
        this.setCpuAvailabilty(cpuAvailabilty);
        this.setRamAvailability(ramAvailability);
        this.idServer = idServer;
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
}
