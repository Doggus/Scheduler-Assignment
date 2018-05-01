/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tldlir001
 */
public class ProcessControlBlockImpl implements ProcessControlBlock
{
    private int pID;
    private String pName;
    private int priority;
    private Instruction instruction;
    private State state;
    
    public ProcessControlBlockImpl(int id, String name, int pr, Instruction i, State s)
    {
        pID = id;
        pName = name;
        priority = pr;
        instruction = i;
        state = s;
    }
    
    public static ProcessControlBlock loadProgram(String filename)
    {
        try
        {
            BufferedReader f = new BufferedReader(new FileReader("testone.txt"));
            String s = f.readLine();
            while(s != null)
            {
                
            }
            
        } 
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        
        ProcessControlBlockImpl pcbi = new ProcessControlBlockImpl(pID, pName, pID, instruction, state);
        return pcbi; 
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
        return instruction;
    }
    
    public boolean hasNextInstruction()
    {
        
    }
    
    public void nextInstruction()
    {
        
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
