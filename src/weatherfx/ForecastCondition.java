/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherfx;

import java.time.LocalTime;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ForecastCondition 
{
    private int low;
    private int high;
    private int rainlvl; //0-3 0(none) 1(rain) 2(storm) 3(severe storm)
    private int cloudCover; //0-3
    private boolean fog;
    private LocalTime startTime;
        
    public ForecastCondition(float l, float h, int rlvl, int cc)
    {
        low = (int)l;
        high = (int)h;
        rainlvl = rlvl;
        cloudCover = cc;
        fog = false;
    }
    
    public ForecastCondition()
    {
        low = 0;
        high = 100;
        rainlvl = 0;
        cloudCover = 0;
        fog = false;
    }

    /**
     * @return the rainlvl
     */
    public int getRainlvl() {
        return rainlvl;
    }

    /**
     * @param rainlvl the rainlvl to set
     */
    public void setRainlvl(int rainlvl) {
        this.rainlvl = rainlvl;
    }

    /**
     * @return the cloudCover
     */
    public int getCloudCover() {
        return cloudCover;
    }

    /**
     * @param cloudCover the cloudCover to set
     */
    public void setCloudCover(int cloudCover) {
        this.cloudCover = cloudCover;
    }
    
    public String getConditionString()
    {
        String condition = "";
        switch(rainlvl)
        {
            case 0:
              switch(cloudCover)  
              {
                  case 0:
                      condition = "Clear";
                      break;
                  case 1:
                      condition = "Partly Cloudy";
                      break;
                  case 2:
                      condition = "Cloudy";
                      break;
                  case 3:
                      condition = "Thick Clouds";
                      break;
               }
              break;
            case 1:
                condition = "Rain";
                break;
            case 2:
                condition = "Scattered Thunderstorms";
                break;
            case 3:
                condition = "Severe Thunderstorms";
                break;
        }
        return condition;
    }

    /**
     * @return the startTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public String getStartTimeString()
    {
        String start = startTime.toString();
        return start;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the fog
     */
    public boolean getFog() {
        return fog;
    }

    /**
     * @param fog the fog to set
     */
    public void setFog(Boolean fog) {
        this.fog = fog;
    }
}
