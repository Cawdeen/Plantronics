/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package climates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import xml.ClimateReadWriter;

/**
 *This class will hold all data regarding climates, seasons, active climate etc.
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ClimateData {
    
    private Climate activeClimate;
    private ArrayList<Climate> listOfClimates = new ArrayList<Climate>();
    private ArrayList<String> listOfClimateFileNames = new ArrayList<String>();
    
    ClimateReadWriter climateReadWriter = new ClimateReadWriter();
    
    /**
     * Default constructor for ClimateData. The constructor will use the climate read writer to see if there are saved climates and open them.
     * If there are none, then a default climate will be created in the /climates folder.
     */
    public ClimateData()
    {
        //see if there are climate profiles
        ArrayList<String> newFileNames = climateReadWriter.getClimateFileNames();
        if(newFileNames == null || newFileNames.size()<1)
        {
            System.out.println("There are no climate files.");
        }
        else
        {
            //save the list of filenames
            System.out.println("Climate files found:");
            listOfClimateFileNames = newFileNames;
        }
        
        //if there is no climates then create a default climate
        if(listOfClimateFileNames.isEmpty() || listOfClimateFileNames == null)
        {
            createClimate(); 
            activeClimate = listOfClimates.get(0);
        }
        else
        {
            for(int i=0; i<listOfClimateFileNames.size(); i++)
            {
                Climate climate = climateReadWriter.readClimateFile(listOfClimateFileNames.get(i));
                listOfClimates.add(climate);
            }
            activeClimate = listOfClimates.get(0);
        }
    }
    
    /**
     * Method for creating default climate if there is none.
     */
    public void createClimate()
    {
        Season newSeason = new Season("Default Season",70,80,65,95,7,0,19,0,15);//create default season
        ArrayList<Season> newSeasonList = new ArrayList<Season>();
        newSeasonList.add(newSeason);
        Climate newClimate = new Climate(newSeasonList);//creat new climate
        newClimate.setName("DefaultClimate");
        getListOfClimates().add(newClimate);     //add climate to list
    }
    
    /**
     * Public method to try to create a new season.
     */
    public void newSeasonRequest()
    {
        Season newSeason = new Season("New Season",70,80,65,95,7,0,19,0,15);//create default season
        getActiveClimate().addSeason(newSeason);
        //update the climate file using the read writer
        climateReadWriter.updateClimateFile(getActiveClimate().getName(), getActiveClimate().getName(), getActiveClimate().getSeasonList());
    }
    
    /**
     * Method to delete a season from the Climate.
     * @param s
     * @return Boolean
     */
    public Boolean deleteSeasonRequest(Season s)
    {
        Boolean result = false;
        ArrayList<Season> seasons = activeClimate.getSeasonList();
        if(seasons.size() < 2)
        {
            return result;
        }
        else
        {
            for(int i = 0; i < seasons.size(); i++)
            {
                if(seasons.get(i) == s)
                {
                    seasons.remove(i);
                    climateReadWriter.updateClimateFile(activeClimate.getName(), activeClimate.getName(), seasons);
                    result = true;
                }
            }
        }
        return result;
    }
    
    //get order or dates
    public ArrayList<Season> getSeasonOrder()
    {
        //blank list of start dates
        ArrayList<Season> orderedSeasons = new ArrayList<Season>();
        if(getActiveClimate().getNumberOfSeasons()>1)   
        {
            //copy the list
            for(int i = 0; i<getActiveClimate().getNumberOfSeasons(); i++)
            {
                orderedSeasons.add(getActiveClimate().getSeason(i));
            }
            //started checking at next index
            Season temp; //for swap
            for (int i = 0; i < getActiveClimate().getNumberOfSeasons()-1; i++) 
            {
                for (int j = i + 1; j < getActiveClimate().getNumberOfSeasons(); j++) { 
                    LocalDate d1 = orderedSeasons.get(i).getStartDate();
                    LocalDate d2 = orderedSeasons.get(j).getStartDate();
                    if (d1.getDayOfYear() > d2.getDayOfYear()) 
                    {
                        //perform swap
                        temp = orderedSeasons.get(i);
                        orderedSeasons.set(i, orderedSeasons.get(j));
                        //orderedSeasons.add(i, orderedSeasons.get(j));
                        orderedSeasons.set(j, temp);
                        //orderedSeasons.add(j, temp);
                    }
                }
            }
        }
        else if(getActiveClimate().getNumberOfSeasons()==1)
        {
            orderedSeasons.add(getActiveClimate().getSeason(0));
        }
        else
        {
            System.out.println("There are no seasons in the climate.");
        }
        return orderedSeasons;
    }
    
    //return the current season
    public Season getCurrentSeason()
    {
        //get seasons in correct order
        ArrayList<Season> orderedSeasons = getSeasonOrder();
        //default to the first season
        Season currentSeason = getActiveClimate().getSeason(0);
        Season currentSeasonOrdered = orderedSeasons.get(0);
        //set date variables
        LocalDate cDate = LocalDate.now();
        LocalDate firstDate = orderedSeasons.get(0).getStartDate();
        //if the first season start date is after today, then the current season is the last season of the year
        if(firstDate.getDayOfYear() > cDate.getDayOfYear())
        {
            currentSeasonOrdered = orderedSeasons.get(orderedSeasons.size()-1);
        }
        else
        {
            for(int i=0; i<orderedSeasons.size(); i++)
            {
                LocalDate d = orderedSeasons.get(i).getStartDate();
                //if the season being checked is before today, thats the current season (so far)
                if(d.getDayOfYear() < cDate.getDayOfYear())
                {
                    currentSeasonOrdered = orderedSeasons.get(i);
                }
            }
        }
        for(int i=0; i< activeClimate.getNumberOfSeasons(); i++)
        {
            if(getActiveClimate().getSeason(i) == currentSeasonOrdered)
            {
                currentSeason = getActiveClimate().getSeason(i);
                break;
            }
        }
        return currentSeason;
    }
    /**
     * Get the current season name as a String
     * @return 
     */
    //get current season to string
    public String currentSeasonToString()
    {
        String seasonString = getCurrentSeason().getName();
        return seasonString;
    }
    
    //get length of current season
    public String getCurrentSeasonLength()
    {
        String length = "";
        Season currentSeason = getCurrentSeason();
        Season nextSeason = getNextSeason();
        
        int startMonth = getCurrentSeason().getStartDate().getMonthValue();
        int startDay = getCurrentSeason().getStartDate().getDayOfMonth();
        int endMonth = nextSeason.getStartDate().getMonthValue();
        int endDay = nextSeason.getStartDate().getDayOfMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("MM:dd");
        String seasonStart = String.valueOf(startMonth) + ":" + String.valueOf(startDay);
        String seasonEnd = String.valueOf(endMonth) + ":" + String.valueOf(endDay);
        try {
            Date d1 = sdf.parse(seasonStart);
            Date d2 = sdf.parse(seasonEnd);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next year. Subtract the difference from a full year length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 31536000000L + elapsed;
            }
            long months = (long) Math.floor(elapsed / 2629800000L);
            long days = (long) Math.floor((elapsed - months * 2629800000L) / 86400000);
            length = String.valueOf(months + " months, " + days + " days");
        } catch (ParseException ex) {
            System.out.println("Error parsing dates in method getCurrentSeasonLength()");;
        }
        return length;
    }
    
    /**
     * Get the time to next season as a String.
     * @return 
     */
    public String getTimeToNextSeason()
    {
        String length = "";
        Season nextSeason = getNextSeason();
        
        int startMonth = LocalDate.now().getMonthValue();
        int startDay = LocalDate.now().getDayOfMonth();
        int endMonth = nextSeason.getStartDate().getMonthValue();
        int endDay = nextSeason.getStartDate().getDayOfMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("MM:dd");
        String now = String.valueOf(startMonth) + ":" + String.valueOf(startDay);
        String seasonEnd = String.valueOf(endMonth) + ":" + String.valueOf(endDay);
        try {
            Date d1 = sdf.parse(now);
            Date d2 = sdf.parse(seasonEnd);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next year. Subtract the difference from a full year length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 31536000000L + elapsed;
            }
            long months = (long) Math.floor(elapsed / 2629800000L);
            long days = (long) Math.floor((elapsed - months * 2629800000L) / 86400000);
            length = String.valueOf(months + " months, " + days + " days");
        } catch (ParseException ex) {
            System.out.println("Error parsing dates in getTimeToNextSeason()");
        }
        return length;
    }
    
    /**
     * Return the next season.
     * @return 
     */
     public Season getNextSeason()
    {
        Season nextSeason = null;
        ArrayList<Season> seasons = getSeasonOrder();
        Season currentSeason = getCurrentSeason();
        for(int i = 0; i < seasons.size(); i++)
        {
            if(seasons.get(i) == currentSeason)
            {
                if(i != seasons.size()-1)
                {
                    nextSeason = seasons.get(i+1);
                }
                else
                {
                    nextSeason = seasons.get(0);
                }
            }
        }
        return nextSeason;
    }
     
     /**
      * Returns the current days length as a String.
      * @return 
      */
     public String getDayLength() //String
    {
        String dayLength = "";
        int startHr = getCurrentSeason().getLightDuration().getStart1();
        int startMn = getCurrentSeason().getLightDuration().getStart2();
        int endHr = getCurrentSeason().getLightDuration().getEnd1();
        int endMn = getCurrentSeason().getLightDuration().getEnd2();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn);
        String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn);
        try {
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 86400000 + elapsed;
            }
            int hours = (int) Math.floor(elapsed / 3600000);
            int minutes = (int) Math.floor((elapsed - hours * 3600000) / 60000);
            dayLength = String.valueOf(hours + " hours, " + minutes + " minutes");
          
        } catch (ParseException ex) {
            System.out.println("Error parsing dates in getDayLength()");
        }
        return dayLength;
    }

    /**
     * @return the activeClimate
     */
    public Climate getActiveClimate() {
        return activeClimate;
    }

    /**
     * @param activeClimate the activeClimate to set
     */
    public void setActiveClimate(Climate activeClimate) {
        this.activeClimate = activeClimate;
    }

    /**
     * @return the listOfClimateFileNames
     */
    public ArrayList<String> getListOfClimateFileNames() {
        return listOfClimateFileNames;
    }

    /**
     * @param listOfClimateFileNames the listOfClimateFileNames to set
     */
    public void setListOfClimateFileNames(ArrayList<String> listOfClimateFileNames) {
        this.listOfClimateFileNames = listOfClimateFileNames;
    }
    
    /**
     * Method to rename a climate. Takes the climate to rename as a parameter as well as the new name as a string.
     * @param c
     * @param name 
     */
    public void renameClimate(Climate c, String name)
    {
        String oldName = null;
        for(int i = 0; i < getListOfClimates().size(); i++)
        {
            if(getListOfClimates().get(i) == c)
            {
                oldName = getListOfClimates().get(i).getName();
                getListOfClimates().get(i).setName(name);
                climateReadWriter.updateClimateFile(c.getName(), oldName, getActiveClimate().getSeasonList());
                break;
            }
        }
    }
    /**
     * Method to request new climate(manual climate creation)
     * @param name 
     * @return Boolean
     */
    
    public Boolean newClimateRequest(String name)
    {
        Season newSeason = new Season("Default Season",70,80,65,95,7,0,19,0,15);//create default season
        ArrayList<Season> newSeasonList = new ArrayList<Season>();
        newSeasonList.add(newSeason);
        Climate newClimate = new Climate(newSeasonList);//creat new climate
        newClimate.setName(name);
        getListOfClimates().add(newClimate);     //add climate to list
        try {
            climateReadWriter.writeClimateFile(newClimate.getName(), newClimate.getSeasonList()); //write the new climate to the file
            return true;
        } catch (TransformerException ex) {
            return false;
        }
    }
    
    /**
     * Method used to delete a climate and return a True value if successful.
     * @param c
     * @return Boolean
     */
    public Boolean deleteClimate(Climate c)
    {
        Boolean result = false;
        if(getListOfClimates().size() < 2)
        {
            return result;
        }
        else
        {
            for(int i = 0; i < getListOfClimates().size(); i++)
            {
                if(getListOfClimates().get(i) == c)
                {
                    if(activeClimate == c)
                    {
                        getListOfClimates().remove(i);
                        climateReadWriter.deleteClimateFile(c.getName());
                        result = true;
                        if(getListOfClimates().get(0)!=null)
                        {
                            activeClimate = getListOfClimates().get(0);
                        }
                        else
                        {
                           System.out.println("index error with setting active climate!");
                        }
                    }
                    else
                    {
                        getListOfClimates().remove(i);
                        climateReadWriter.deleteClimateFile(c.getName());
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @return the listOfClimates
     */
    public ArrayList<Climate> getListOfClimates() {
        return listOfClimates;
    }

    /**
     * @param listOfClimates the listOfClimates to set
     */
    public void setListOfClimates(ArrayList<Climate> listOfClimates) {
        this.listOfClimates = listOfClimates;
    }
    
}
