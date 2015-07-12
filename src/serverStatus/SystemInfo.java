package serverStatus;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

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

    public String OSname() {
        return System.getProperty("os.name");
    }

    public String OSversion() {
        return System.getProperty("os.version");
    }

    public String OsArch() {
        return System.getProperty("os.arch");
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
     
    	return  Double.parseDouble(df.format(freeRam / 1024 / 1024 / 1024));
    }
    
    public double getProcessCpuAvailable() throws MalformedObjectNameException, ReflectionException, InstanceNotFoundException {

	   
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty())     return Double.NaN;

	    Attribute att = (Attribute)list.get(0);
	   // double value  = Double.catt.getValue();

	    //if (value == -1.0)      return Double.NaN;  // usually takes a couple of seconds before we get real values

	    return 0.00;//((int)(100 - (value * 1000) / 10.0));        // returns a percentage value with 1 decimal point precision
	}
}