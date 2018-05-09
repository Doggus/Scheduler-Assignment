
import java.util.Scanner;
import simulator.Config;
import simulator.FCFSKernel;
import simulator.Kernel;
import simulator.RRKernel;
import simulator.SystemTimer;
import simulator.TRACE;

public class SimulateRR
{
    
    public static void main(String[] args)
    {
        Scanner scn = new Scanner(System.in);
        
        System.out.println("*** RR Simulator ***");
        System.out.print("Enter configuration file name: ");
        String configFileName = scn.nextLine();
        System.out.print("Enter slice time: ");
        int sliceTime = scn.nextInt();
        System.out.print("Enter cost of system call: ");
        int syscallCost = scn.nextInt();
        System.out.print("Enter cost of context switch: ");
        int dispatchCost = scn.nextInt();
        System.out.print("Enter trace level: ");
        int level = scn.nextInt();
        
        TRACE.SET_TRACE_LEVEL(level);
        final Kernel kernel = new RRKernel(sliceTime);
        Config.init(kernel, dispatchCost, syscallCost);
        Config.buildConfiguration(configFileName);
        Config.run();
        SystemTimer timer = Config.getSystemTimer();
        System.out.println(timer);
        System.out.println("Context switches: " + Config.getCPU().getContextSwitches());
        System.out.printf("CPU utilization: %.2f\n",((double)timer.getUserTime())/timer.getSystemTime() * 100);
        
        /*
        Enter slice time: ?
        Enter cost of system call: 3
        Enter cost of context switch: 2
        Enter trace level: 5
        */
   }
    
    
    
    
    
  
}