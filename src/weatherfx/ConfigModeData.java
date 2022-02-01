/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherfx;

import lightfx.LightSettings.DayTimes;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ConfigModeData {
    
    private boolean configMode;
    private ForecastCondition condition;
    private DayTimes configTime;
    
    
    public ConfigModeData(boolean configMode, int rainlvl, int cloudCover, DayTimes configTime)
    {
        this.configMode = configMode;
        this.condition = new ForecastCondition(0f,0f,rainlvl,cloudCover);
        this.configTime = configTime;
    }

    /**
     * @return the configMode
     */
    public boolean getConfigMode() {
        return configMode;
    }

    /**
     * @param configMode the configMode to set
     */
    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
    }

    /**
     * @return the condition
     */
    public ForecastCondition getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(ForecastCondition condition) {
        this.condition = condition;
    }

    /**
     * @return the configTime
     */
    public DayTimes getConfigTime() {
        return configTime;
    }

    /**
     * @param configTime the configTime to set
     */
    public void setConfigTime(DayTimes configTime) {
        this.configTime = configTime;
    }
}
