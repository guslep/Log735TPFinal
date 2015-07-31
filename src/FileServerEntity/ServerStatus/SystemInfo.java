package FileServerEntity.ServerStatus;

import FileServerEntity.Server.ActiveFileServer;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class SystemInfo {

    private Runtime runtime;
    private DecimalFormat df = new DecimalFormat("#.00");
    MBeanServer mbs;
    ObjectName name;
    
    public SystemInfo(){
    	runtime = Runtime.getRuntime();
    	  mbs    = ManagementFactory.getPlatformMBeanServer();
 	     try {
			name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

  public int getConnectionNumber(){
        return ActiveFileServer.getInstance().getConnectionClient().size();
    }

    public long totalMem() {
        return Runtime.getRuntime().totalMemory();
    }

    public long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
    public double getFreeRam (){
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        double freeRam = (freeMemory + (maxMemory - allocatedMemory));
     
    	return  Double.parseDouble(df.format(freeRam / 1024 / 1024 / 1024).replace(',','.'));
    }
    
    public double getProcessCpuAvailable() throws MalformedObjectNameException, ReflectionException, InstanceNotFoundException {

	   
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty())     return Double.NaN;

	    Attribute att = (Attribute)list.get(0);
	    double value  = ((Double) att.getValue());

	    if (value == -1.0)      return Double.NaN;  // usually takes a couple of seconds before we get real values

	    return 0.00;//((int)(100 - (value * 1000) / 10.0));        // returns a percentage value with 1 decimal point precision
	}
}