/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherfx;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import xml.ScheduleReadWriter;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ScheduledFunctionData {
        
    private ArrayList<ScheduleProfile> profiles = new ArrayList<ScheduleProfile>();
    private ArrayList<String> listOfScheduleFileNames = new ArrayList<String>();
    private ScheduleProfile activeProfile;
    
    private ScheduleReadWriter scheduleReadWriter = new ScheduleReadWriter();
    
    public ScheduledFunctionData()
    {
        //see if there are climate profiles
        ArrayList<String> newFileNames = scheduleReadWriter.getScheduleFileNames();
        
        if(newFileNames == null || newFileNames.size()<1)
        {
            System.out.println("There are no schedule files.");
        }
        else
        {
            //save the list of filenames
            System.out.println("Schedule files found:");
            listOfScheduleFileNames = newFileNames;
        }
        
        //if there is no climates then create a default climate
        if(listOfScheduleFileNames.isEmpty() || listOfScheduleFileNames == null)
        {
            createProfile(); 
            activeProfile = profiles.get(0);
        }
        else
        {
            for(int i=0; i<listOfScheduleFileNames.size(); i++)
            {
                ScheduleProfile profile = scheduleReadWriter.readScheduleFile(listOfScheduleFileNames.get(i));
                profiles.add(profile);
            }
            activeProfile = profiles.get(0);
        } 
        updateScheduleFile(activeProfile);
    }
    
    /**
     * Method which checks if a scheduled function will be compatible with current hardware states. 
     * Example: A scheduled function to mist will return false if the humidity is above the maximum allowed level and ventilation is running.
     * @return 
     */
    public boolean checkFunctionCompatibility(ScheduledFunction sf, boolean highTemp, boolean lowHumid, boolean highHumid)
    {
        boolean compatible = true;
        switch(sf.getType())
            {
                case MIST:
                    if(highHumid)compatible = false;
                    break;
                case FOG:
                    if(highHumid)compatible = false;
                    break;
                case HEAT:
                    break;
                case COOLING:
                    break;
                case VENTILATION:
                    if(lowHumid)compatible = false;
                    break;
                case CIRCULATION:
                    break;
            }
        return compatible;
    }

    /**
     * @return the functions
     */
    public ArrayList<ScheduledFunction> getFunctions() {
        return getActiveProfile().getFunctions();
    }

    /**
     * @param functions the functions to set
     */
    public void setFunctions(ArrayList<ScheduledFunction> functions) {
        this.getActiveProfile().setFunctions(functions);
    }
    
    /**
     * Public method to add a new scheduled hardware function to the list
     */
    public void addScheduledFunction()
    {
        ScheduledFunction function = new ScheduledFunction();
        function.setName("Hardware Function");
        getActiveProfile().getFunctions().add(function);
        updateScheduleFile(activeProfile);
    }
    
    /**
     * public method to delete a scheduled function of the specified index from the list. 
     * @param index 
     */
    public void deleteScheduledFunction(int index)
    {
        if(getActiveProfile().getFunctions().get(index)!=null)//if not null
        {
            getActiveProfile().getFunctions().remove(index);//remove the item from the list
            updateScheduleFile(activeProfile);
        }
    }
    
    
    
    public ArrayList<Float> getScheduledFunctionTimes()
    {
        ArrayList<Float> times = new ArrayList<Float>();
        //cycle through all the scheduled functions
        for(int i = 0; i < getActiveProfile().getFunctions().size(); i++)
        {
            //get time as a float
            float time = getActiveProfile().getFunctions().get(i).getStartHr() + (getActiveProfile().getFunctions().get(i).getStartMn()/60);
            //add to list
            times.add(time);
        }
        return times;
    }
    
    public void deleteProfile(ScheduleProfile profile)
    {
        if(profiles.contains(profile))
        {
            profiles.remove(profile);
            scheduleReadWriter.deleteScheduleFile(profile.getName());
        }
    }

    /**
     * @return the profiles
     */
    public ArrayList<ScheduleProfile> getProfiles() {
        return profiles;
    }

    /**
     * @param profiles the profiles to set
     */
    public void setProfiles(ArrayList<ScheduleProfile> profiles) {
        this.profiles = profiles;
    }

    /**
     * @return the activeProfile
     */
    public ScheduleProfile getActiveProfile() {
        return activeProfile;
    }

    /**
     * @param activeProfile the activeProfile to set
     */
    public void setActiveProfile(ScheduleProfile activeProfile) {
        this.activeProfile = activeProfile;
    }

    private void createProfile() {
        ScheduledFunction newFunction = new ScheduledFunction();//create default function (Mist)
        ArrayList<ScheduledFunction> newFunctionList = new ArrayList<ScheduledFunction>();
        newFunctionList.add(newFunction);
        ScheduleProfile newProfile = new ScheduleProfile(newFunctionList);//creat new profile
        newProfile.setName("New Schedule Profile");
        profiles.add(newProfile);     //add profile to list
        updateScheduleFile(newProfile);
    }
    
    public void addNewProfile()
    {
        ScheduleProfile newProfile = new ScheduleProfile();
        profiles.add(newProfile);
        updateScheduleFile(newProfile);
    }

    public void addNewProfile(String newProfileName) {
        ScheduleProfile newProfile = new ScheduleProfile();
        newProfile.setName(newProfileName);
        profiles.add(newProfile);
        updateScheduleFile(newProfile);
    }
    
    public void updateScheduleFile(ScheduleProfile p)
    {
        try {
            scheduleReadWriter.writeScheduleFile(p.getName(), p.getFunctions());
        } catch (TransformerException ex) {
            System.out.println("Failure updating schedule profile file.");
        }
    }
    
    public void updateActiveScheduleFile()
    {
        try {
            if(activeProfile!=null){
                scheduleReadWriter.writeScheduleFile(activeProfile.getName(), activeProfile.getFunctions());
            }
        } catch (TransformerException ex) {
            System.out.println("Failure updating schedule profile file.");
        }
    }
}
