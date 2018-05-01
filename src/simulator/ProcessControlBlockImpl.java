/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tldlir001
 */
public class ProcessControlBlockImpl implements ProcessControlBlock
{
    private static int pID = 0;
    private static String pName;
    private static int priority = 0;
    private static Deque<Instruction> CPUInstructionQueue = new ArrayDeque<>();
    private static State state;
    
    
    public ProcessControlBlockImpl(String name, Deque<Instruction> IQueue)
    {
      pID = 0;
      pName = name;
      priority = 0;
      
      state = state.READY;
    }
    
    public static ProcessControlBlock loadProgram(String filename)
    {
        String name = "";
        Deque<Instruction> IQueue = new ArrayDeque<>();
        
        try
        {
            
            BufferedReader f = new BufferedReader(new FileReader(filename));
            String s = f.readLine();
            ArrayList<String> file = new ArrayList<>();
            while(s != null)
            {
              file.add(s);  
              s = f.readLine();
            }
            
            //PCB.pID++; 
            name = file.get(0).substring(file.get(0).indexOf(":")+2,file.get(0).length());
            
            //fetching instructions for queue
            for (int i = 2; i < file.size(); i++)
            {
                if(file.get(i).contains("CPU"))
                {
                    String [] ln = file.get(i).split(" ");
                    Instruction cpuIn = new CPUInstruction(Integer.parseInt(ln[1]));
                    IQueue.add(cpuIn);
                }
            }
    
        } 
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        
        ProcessControlBlockImpl PCB = new ProcessControlBlockImpl(name, IQueue);
        return PCB;

    }
    
    public int getPID()
    {
        return pID; 
    }

    public String getProgramName()
    {
        return pName;
    }
    
    public int getPriority()
    {
        return priority;
    }
    
    public int setPriority(int value)
    {
        int p = priority;
        priority = value;
        return p; //return old value
    }
    
    public Instruction getInstruction()
    {
        return CPUInstructionQueue.peek();
    }
    
    public boolean hasNextInstruction()
    {
        if(CPUInstructionQueue.peek() != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void nextInstruction()
    {
        CPUInstructionQueue.pop();
    }
    
    public State getState()
    {
        return state;
    }
    
    public void setState(State state)
    {
        this.state = state;
    }
   
    public String toString()
    {
        String s = "{pid(" + getPID() + "), state(" + getState() + "), name(" + getProgramName() + ")}";
        return s;
    }
}
