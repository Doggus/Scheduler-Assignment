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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tldlir001
 */
public class ProcessControlBlockImpl implements ProcessControlBlock
{
    private static int pID;
    private static int count = 0;
    private String pName;
    private int priority;
    private List<Instruction> Instructions;
    private int LineCounter;
    private State state;
    
    
    public ProcessControlBlockImpl(int id, String name, List<Instruction> Ins)
    {
      pID = id;
      pName = name;
      LineCounter = 0;
      priority = 0;
      Instructions = Ins;
      state = state.READY;
    }
    
    public static ProcessControlBlock loadProgram(String filename)
    {
        String name = "";
        List<Instruction> Ins = new ArrayList<>();
        
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
             
            name = filename;
            
            //fetching instructions for queue (start at 4th line in file)
            for (int i = 3; i < file.size(); i++) // CANT START FROM 3RD LINE BECAUSE CERTAIN PROGRAM FILES HAVE LESS THAN 3 LINES, LOOK AT TEST FILES FOR EXAMPLES
            {                                       //NEED TO JUST IGNORE LINES STARTING WITH #
                if(file.get(i).contains("CPU"))
                {
                    String [] Cln = file.get(i).split(" ");
                    Instruction cpuIn = new CPUInstruction(Integer.parseInt(Cln[1]));
                    Ins.add(cpuIn);
                }
                else
                {
                    String [] Iln = file.get(i).split(" ");
                    Instruction IOIn = new IOInstruction(Integer.parseInt(Iln[1]), Integer.parseInt(Iln[2]));
                    Ins.add(IOIn);
                }
            }
            System.out.println(Ins.size());
        } 
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        
        
        ProcessControlBlockImpl PCB = new ProcessControlBlockImpl(count, name, Ins);
        count++;
        
   
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
        //System.out.println(LineCounter);
        return Instructions.get(LineCounter);
    }
    
    public boolean hasNextInstruction()
    {
       if(LineCounter < Instructions.size()-1)
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
        //System.out.println(LineCounter);
        LineCounter++;
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
