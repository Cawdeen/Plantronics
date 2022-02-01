/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherfx;

import java.util.ArrayList;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ScheduleProfile {
    
    private ArrayList<ScheduledFunction> functions;
    private String name;
    
    public ScheduleProfile()
    {
        functions = new ArrayList<ScheduledFunction>();
    }
    
    public ScheduleProfile(ArrayList<ScheduledFunction> functions)
    {
        this.functions = functions;
    }

    /**
     * @return the functions
     */
    public ArrayList<ScheduledFunction> getFunctions() {
        return functions;
    }

    /**
     * @param functions the functions to set
     */
    public void setFunctions(ArrayList<ScheduledFunction> functions) {
        this.functions = functions;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
        
}
