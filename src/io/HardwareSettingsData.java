/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class HardwareSettingsData {
    
    private int circFanConstSpeed;  //speed of circulation fan when on constantly
    private boolean circFanConstant;//toggle for constant circulation
    private int ventFanConstSpeed;  //speed of ventilation fan when on constantly
    private boolean ventFanConstant;//toggle for constant ventilation
    private int rgbFanLow;          //low cutoff for rgb cooling fans
    private int wFanLow;            //low cutoff for white cooling fans
    private int rgbFanHigh;         //max for rgb cooling fans
    private int wFanHigh;           //max for white cooling fans
    private int circFanHigh;        //max for circulation fan
    private int ventFanHigh;        //max for ventilation fan
    private int correctionDelayHumidity; //the amount of time in seconds between attempting to correct humidity.
    private int correctionDelayTemp;     //the amount of time in seconds between attemping to correct temperature.
    private int correctionTimeHumidity;  //the amount of time in seconds that the correction for humidity runs before shutting off.
    private int correctionTimeTemp;      //the amount of time in seconds that the correction for temperature runs before shutting off.
    
    public HardwareSettingsData()
    {
        this.ventFanHigh = 8;
        this.circFanHigh = 8;
        this.wFanHigh = 100;
        this.wFanLow = 10;
        this.rgbFanHigh = 100;
        this.rgbFanLow = 10;
        this.ventFanConstSpeed = 2;
        this.circFanConstSpeed = 2;
        this.circFanConstant = false;
        this.ventFanConstant = false;
        this.correctionDelayHumidity = 120;
        this.correctionDelayTemp = 120;
        this.correctionTimeHumidity = 60;
        this.correctionTimeTemp = 60;
    }

    /**
     * @return the circFanConstSpeed
     */
    public int getCircFanConstSpeed() {
        return circFanConstSpeed;
    }

    /**
     * @param circFanConstSpeed the circFanConstSpeed to set
     */
    public void setCircFanConstSpeed(int circFanConstSpeed) {
        this.circFanConstSpeed = circFanConstSpeed;
    }

    /**
     * @return the circFanConstant
     */
    public boolean isCircFanConstant() {
        return circFanConstant;
    }

    /**
     * @param circFanConstant the circFanConstant to set
     */
    public void setCircFanConstant(boolean circFanConstant) {
        this.circFanConstant = circFanConstant;
    }

    /**
     * @return the ventFanConstSpeed
     */
    public int getVentFanConstSpeed() {
        return ventFanConstSpeed;
    }

    /**
     * @param ventFanConstSpeed the ventFanConstSpeed to set
     */
    public void setVentFanConstSpeed(int ventFanConstSpeed) {
        this.ventFanConstSpeed = ventFanConstSpeed;
    }

    /**
     * @return the ventFanConstant
     */
    public boolean isVentFanConstant() {
        return ventFanConstant;
    }

    /**
     * @param ventFanConstant the ventFanConstant to set
     */
    public void setVentFanConstant(boolean ventFanConstant) {
        this.ventFanConstant = ventFanConstant;
    }

    /**
     * @return the rgbFanLow
     */
    public int getRgbFanLow() {
        return rgbFanLow;
    }

    /**
     * @param rgbFanLow the rgbFanLow to set
     */
    public void setRgbFanLow(int rgbFanLow) {
        this.rgbFanLow = rgbFanLow;
    }

    /**
     * @return the wFanLow
     */
    public int getwFanLow() {
        return wFanLow;
    }

    /**
     * @param wFanLow the wFanLow to set
     */
    public void setwFanLow(int wFanLow) {
        this.wFanLow = wFanLow;
    }

    /**
     * @return the rgbFanHigh
     */
    public int getRgbFanHigh() {
        return rgbFanHigh;
    }

    /**
     * @param rgbFanHigh the rgbFanHigh to set
     */
    public void setRgbFanHigh(int rgbFanHigh) {
        this.rgbFanHigh = rgbFanHigh;
    }

    /**
     * @return the wFanHigh
     */
    public int getwFanHigh() {
        return wFanHigh;
    }

    /**
     * @param wFanHigh the wFanHigh to set
     */
    public void setwFanHigh(int wFanHigh) {
        this.wFanHigh = wFanHigh;
    }

    /**
     * @return the circFanHigh
     */
    public int getCircFanHigh() {
        return circFanHigh;
    }

    /**
     * @param circFanHigh the circFanHigh to set
     */
    public void setCircFanHigh(int circFanHigh) {
        this.circFanHigh = circFanHigh;
    }

    /**
     * @return the ventFanHigh
     */
    public int getVentFanHigh() {
        return ventFanHigh;
    }

    /**
     * @param ventFanHigh the ventFanHigh to set
     */
    public void setVentFanHigh(int ventFanHigh) {
        this.ventFanHigh = ventFanHigh;
    }

    /**
     * Returns the seconds that the system waits between attempts to correct humidity.
     * @return the correctionDelayHumidity
     */
    public int getCorrectionDelayHumidity() {
        return correctionDelayHumidity;
    }

    /**
     * Set the amount of time in seconds that the system waits between attempting to correct humidity. This prevents overcompensation of humidity correction.
     * @param correctionDelayHumidity the correctionDelayHumidity to set
     */
    public void setCorrectionDelayHumidity(int correctionDelayHumidity) {
        this.correctionDelayHumidity = correctionDelayHumidity;
    }

    /**
     * Returns the seconds that the system waits between attempts to correct temperature.
     * @return the correctionDelayTemp
     */
    public int getCorrectionDelayTemp() {
        return correctionDelayTemp;
    }

    /**
     * Set the amount of time in seconds that the system waits between attempting to correct temperature. This prevents overcompensation of temperature correction.
     * @param correctionDelayTemp the correctionDelayTemp to set
     */
    public void setCorrectionDelayTemp(int correctionDelayTemp) {
        this.correctionDelayTemp = correctionDelayTemp;
    }

    /**
     * Returns the amount of time in seconds that a humidity correction task runs.
     * @return the correctionTimeHumidity
     */
    public int getCorrectionTimeHumidity() {
        return correctionTimeHumidity;
    }

    /**
     * Set the amount of time in seconds that a humidity correction task runs before shutting off.
     * @param correctionTimeHumidity the correctionTimeHumidity to set
     */
    public void setCorrectionTimeHumidity(int correctionTimeHumidity) {
        this.correctionTimeHumidity = correctionTimeHumidity;
    }

    /**
     * Returns the amount of time in seconds that a temperature correction task runs.
     * @return the correctionTimeTemp
     */
    public int getCorrectionTimeTemp() {
        return correctionTimeTemp;
    }

    /**
     * Set the amount of time in seconds that a temperature correction task runs before shutting off.
     * @param correctionTimeTemp the correctionTimeTemp to set
     */
    public void setCorrectionTimeTemp(int correctionTimeTemp) {
        this.correctionTimeTemp = correctionTimeTemp;
    }
}
