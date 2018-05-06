package simulator;

import java.util.ArrayDeque;
import java.util.Deque;
import static simulator.InterruptHandler.TIME_OUT;
import static simulator.InterruptHandler.WAKE_UP;
import static simulator.SystemCall.EXECVE;
import static simulator.SystemCall.IO_REQUEST;
import static simulator.SystemCall.MAKE_DEVICE;
import static simulator.SystemCall.TERMINATE_PROCESS;

/**
 *
 * @author tldlir001
 */
public class RRKernel implements Kernel
{
    private Deque<ProcessControlBlock> readyQueue;
    int sliceTime;

    public RRKernel(int st)
    {
        // Set up the ready queue.
        readyQueue = new ArrayDeque<>();
        sliceTime = st;
        Config.init(this, 1, 1);
    }

    private ProcessControlBlock dispatch()
    {
       /*
        if(readyQueue.peek() != null && readyQueue.size() != 1 && !(readyQueue.peek().getInstruction() instanceof IOInstruction))
        {
            if (readyQueue.peek().getInstruction() instanceof IOInstruction)
            {
                readyQueue.add(readyQueue.pop());
                ProcessControlBlock pcb = Config.getCPU().contextSwitch(readyQueue.pop());

               return pcb;
            } 
            else
            {

                ProcessControlBlock pcb = Config.getCPU().contextSwitch(readyQueue.pop());
                Config.getCPU().getCurrentProcess().setState(ProcessControlBlock.State.RUNNING);

                if (pcb != null && pcb.hasNextInstruction())
                {
                    readyQueue.add(pcb);
                }

                if(((CPUInstruction)Config.getCPU().getCurrentProcess().getInstruction()).getBurstRemaining() > sliceTime || Config.getCPU().getCurrentProcess().hasNextInstruction())
                {
                    Config.getSimulationClock().scheduleInterrupt(sliceTime, this, Config.getCPU().getCurrentProcess().getPID());
                }

                return pcb;
            }
        }
        else
        {
            Config.getCPU().contextSwitch(null);
        }
        */
        
        if(!(Config.getCPU().isIdle()))
        {
           
            ProcessControlBlock pcb = Config.getCPU().getCurrentProcess();
            
            if(pcb.hasNextInstruction() || ((CPUInstruction)pcb.getInstruction()).getBurstRemaining() > 0)
            {
                readyQueue.add(pcb);
            }
            
            if(readyQueue.peek() instanceof IOInstruction && readyQueue.size() == 1)
            {
                return Config.getCPU().contextSwitch(null);
            }
           
        }
        
        if(readyQueue.peek() instanceof IOInstruction && readyQueue.size() == 1)
        {
            return Config.getCPU().contextSwitch(null);
        }
        else
        {
            if(((CPUInstruction)Config.getCPU().getCurrentProcess().getInstruction()).getBurstRemaining() > sliceTime)
        {
           Config.getSimulationClock().scheduleInterrupt(sliceTime, this, Config.getCPU().getCurrentProcess().getPID());
        }
        }
        
        //causing null pointer??
        
            
        return Config.getCPU().contextSwitch(readyQueue.poll());
        
      
    }

    public int syscall(int number, Object... varargs)
    {
        int result = 0;
        switch (number)
        {
            case MAKE_DEVICE:
            {
                IODevice device = new IODevice((Integer) varargs[0], (String) varargs[1]);
                Config.addDevice(device);
            }
            break;
            case EXECVE:
            {
                ProcessControlBlock pcb = this.loadProgram((String) varargs[0]);
                if (pcb != null)
                {
                  
                    readyQueue.add(pcb);

                    if (Config.getCPU().isIdle())
                    {
                        dispatch();
                    }

                } else
                {
                    result = -1;
                }
            }
            break;
            case IO_REQUEST:
            {
                IODevice dev = Config.getDevice((Integer) varargs[0]);
                dev.requestIO((Integer) varargs[1], Config.getCPU().getCurrentProcess(), this);
                Config.getCPU().getCurrentProcess().setState(ProcessControlBlock.State.WAITING);
                dispatch();
            }
            break;
            case TERMINATE_PROCESS:
            {
                Config.getCPU().getCurrentProcess().setState(ProcessControlBlock.State.TERMINATED);
                dispatch();
            }
            break;
            default:
                result = -1;
        }
        return result;
    }

    public void interrupt(int interruptType, Object... varargs)
    {
        switch (interruptType)
        {
            case TIME_OUT:
                dispatch();
                
            case WAKE_UP:

                if(varargs.length == 2)
                {
                    ProcessControlBlock pcb = (ProcessControlBlock)varargs[1];
                    pcb.setState(ProcessControlBlock.State.READY);
                }
                
                if (Config.getCPU().isIdle())
                {
                    dispatch();
                }

                break;
            default:
                throw new IllegalArgumentException("FCFSKernel:interrupt(" + interruptType + "...): unknown type.");
        }
    }

    private static ProcessControlBlock loadProgram(String filename)
    {
        try
        {
            return ProcessControlBlockImpl.loadProgram(filename);
        } 
        catch (Exception ex)
        {
            return null;
        }

        /*
        catch (FileNotFoundException fileExp)
        {
            return null;
        } 
        catch (IOException ioExp)
        {
            return null;
        }
         */
    }
}
