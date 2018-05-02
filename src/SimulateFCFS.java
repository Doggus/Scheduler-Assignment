
import simulator.Config;
import simulator.FCFSKernel;
import simulator.Kernel;
import simulator.SystemTimer;
import simulator.TRACE;

public class SimulateFCFS
{
   
 public static void main(String[] args)
    {
        //int level = 0;
        int dispatchCost = 1;
        int syscallCost = 0;
        String configFileName = "ConfigFile.test";
        
        //TRACE.SET_TRACE_LEVEL(level);
        final Kernel kernel = new FCFSKernel();
        Config.init(kernel, dispatchCost, syscallCost);
        Config.buildConfiguration(configFileName);
        Config.run();
        SystemTimer timer = Config.getSystemTimer();
        System.out.println(timer);
        System.out.println("Context switches:" + Config.getCPU().getContextSwitches());
        System.out.printf("CPU utilization: %.2f\n",((double)timer.getUserTime())/timer.getSystemTime() * 100);
   }
   
}
