/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package climates;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class Season implements Serializable{
    
    //new
    private String name;
    
    private Range tempRange;
    private Range humidRange;
    private int rainDays;
    private Duration lightDuration;
    private LocalDate startDate;
    private int daysToMid;
    
    public Season(String newName, int minTemp, int maxTemp, int minHum, int maxHum, int startHr, int startMn, int endHr, int endMn, int rDays)
    {
        name = newName;
        tempRange = new SeasonTemp(minTemp, maxTemp);
        humidRange = new SeasonHumidity(minHum, maxHum);
        rainDays = rDays;
        lightDuration = new LightDuration(startHr, startMn, endHr, endMn);
        startDate = LocalDate.now();
    }
    
    public Season()
    {
        
    }
    /*
    public Season(int minTemp, int maxTemp, int minHumidity, int maxHumidity, int startMonth, int startDay, int lightOnHour, int lightOnMin, int lightOffHour, int lightOffMin)
    {
        tempRange = new SeasonTemp(minTemp, maxTemp);
        //humidRange = new SeasonHumidity(int minHumidity, int maxHumidity);
    }
    */
    /**
     * @return the tempRange
     */
    public Range getTempRange() {
        return tempRange;
    }

    /**
     * @param tempRange the tempRange to set
     */
    public void setTempRange(Range tempRange) {
        this.tempRange = tempRange;
    }

    /**
     * @return the humidRange
     */
    public Range getHumidRange() {
        return humidRange;
    }

    /**
     * @param humidRange the humidRange to set
     */
    public void setHumidRange(Range humidRange) {
        this.humidRange = humidRange;
    }

    /**
     * @return the lightDuration
     */
    public Duration getLightDuration() {
        return lightDuration;
    }

    /**
     * @param lightDuration the lightDuration to set
     */
    public void setLightDuration(Duration lightDuration) {
        this.lightDuration = lightDuration;
    }
    
    public String getTempsSummary()
    {
        String summary = Integer.toString(getTempRange().getMin())+"°/"+Integer.toString(getTempRange().getMax())+"°";
        return summary;
    }
    
    public String getHumiditySummary()
    {
        String summary = Integer.toString(getHumidRange().getMin())+"%/"+Integer.toString(getHumidRange().getMax())+"%";
        return summary;
    }
    
    public String getDayLightSummary()
    {
        String dayLength = "";
        try {
            
            int startHr = getLightDuration().getStart1();
            int startMn = getLightDuration().getStart2();
            int endHr = getLightDuration().getEnd1();
            int endMn = getLightDuration().getEnd2();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn);
            String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn);
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 86400000 + elapsed;
            }
            float hours = (int) Math.floor(elapsed / 3600000);
            float minutes = (int) Math.floor((elapsed - hours * 3600000) / 60000);
            float flength = hours + (minutes/60);
            //dayLength = String.valueOf(hours  + minutes/60);
            dayLength = String.format("%2.1f",(float)flength);
            
        } catch (ParseException ex) {
            Logger.getLogger(Season.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dayLength;
    }
    
    public float getDayLight()
    {
        float daylight = 0;
        try {
            
            int startHr = getLightDuration().getStart1();
            int startMn = getLightDuration().getStart2();
            int endHr = getLightDuration().getEnd1();
            int endMn = getLightDuration().getEnd2();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn);
            String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn);
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 86400000 + elapsed;
            }
            float hours = (int) Math.floor(elapsed / 3600000);
            float minutes = (int) Math.floor((elapsed - hours * 3600000) / 60000);
            daylight = hours + (minutes/60);
            //dayLength = String.valueOf(hours  + minutes/60);
            
            
        } catch (ParseException ex) {
            Logger.getLogger(Season.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daylight;
    }

    /**
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public String getName()
    {
        return name;
    }

    /**
     * @return the rainDays
     */
    public int getRainDays() {
        return rainDays;
    }

    /**
     * @param rainDays the rainDays to set
     */
    public void setRainDays(int rainDays) {
        this.rainDays = rainDays;
    }

    /**
     * @return the daysToMid
     */
    public int getDaysToMid() {
        return daysToMid;
    }

    /**
     * @param daysToMid the daysToMid to set
     */
    public void setDaysToMid(int daysToMid) {
        this.daysToMid = daysToMid;
    }

   public LocalDate getMidDate()
   {
       LocalDate midDate = startDate.plusDays(daysToMid);
       return midDate;
   }
    
}
