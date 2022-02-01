/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import climates.ClimateData;
import io.HardwareSettingsData;
import io.IOData;
import lightfx.LightData;
import lightfx.LightSettings.DayTimes;
import weatherfx.ConfigModeData;
import weatherfx.CurrentConditionsData;
import weatherfx.ScheduledFunctionData;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ModelData {
    
    private ClimateData climateData;
    private CurrentConditionsData currentConditionsData;
    private ConfigModeData configModeData;
    private LightData lightData;
    private ScheduledFunctionData scheduledFunctionData;
    private IOData ioData;
    private HardwareSettingsData hardwareSettingsData;
    
    public ModelData(boolean pinout)
    {
        climateData = new ClimateData();
        hardwareSettingsData = new HardwareSettingsData();
        currentConditionsData = new CurrentConditionsData();
        configModeData = new ConfigModeData(false,0,0, DayTimes.MORNING);
        lightData = new LightData();
        scheduledFunctionData = new ScheduledFunctionData();
        ioData = new IOData(pinout, hardwareSettingsData);
    }

    /**
     * @return the climateData
     */
    public ClimateData getClimateData() {
        return climateData;
    }

    /**
     * @param climateData the climateData to set
     */
    public void setClimateData(ClimateData climateData) {
        this.climateData = climateData;
    }

    /**
     * @return the currentConditionsData
     */
    public CurrentConditionsData getCurrentConditionsData() {
        return currentConditionsData;
    }

    /**
     * @param currentConditionsData the currentConditionsData to set
     */
    public void setCurrentConditionsData(CurrentConditionsData currentConditionsData) {
        this.currentConditionsData = currentConditionsData;
    }

    /**
     * @return the configModeData
     */
    public ConfigModeData getConfigModeData() {
        return configModeData;
    }

    /**
     * @param configModeData the configModeData to set
     */
    public void setConfigModeData(ConfigModeData configModeData) {
        this.configModeData = configModeData;
    }

    /**
     * @return the scheduledFunctionData
     */
    public ScheduledFunctionData getScheduledFunctionData() {
        return scheduledFunctionData;
    }

    /**
     * @param scheduledFunctionData the scheduledFunctionData to set
     */
    public void setScheduledFunctionData(ScheduledFunctionData scheduledFunctionData) {
        this.scheduledFunctionData = scheduledFunctionData;
    }

    /**
     * @return the ioData
     */
    public IOData getIoData() {
        return ioData;
    }

    /**
     * @return the lightData
     */
    public LightData getLightData() {
        return lightData;
    }

    /**
     * @return the hardwareSettingsData
     */
    public HardwareSettingsData getHardwareSettingsData() {
        return hardwareSettingsData;
    }
    
}
