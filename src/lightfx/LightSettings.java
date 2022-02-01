/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightfx;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class LightSettings {
    
    private float sunsetLengthSQR; //1-3
    private float whiteFadeSQR;    //1-3
    private int channelMaxW;       //0-100
    private int channelMaxR;       //0-100
    private int channelMaxG;       //0-100
    private int channelMaxB;       //0-100
    private int nightB = 3;        //for nightlight
    private int nightG = 2;        //for nightlight
    private int nightR = 1;        //for nightlight
    private int nightW = 0;        //for nightlight
    //private ArrayList<Float> timeValues; //storing various decimals to represent the percentage of noon. (sunset is 0.0, noon is 1.0)
    public enum DayTimes{
        NIGHT,
        SUNRISE,
        MORNING,
        NOON
    }
    private HashMap<DayTimes, Float> dayTimes = new HashMap<>();
    
    public LightSettings(float sunsetLength, float whiteFade, int w, int r, int g, int b)
    {
        dayTimes = new HashMap<>();
        dayTimes.put(DayTimes.NIGHT, 0f);
        dayTimes.put(DayTimes.SUNRISE, .05f);
        dayTimes.put(DayTimes.MORNING, .2f);
        dayTimes.put(DayTimes.NOON, .99f);
        this.sunsetLengthSQR = sunsetLength;
        this.whiteFadeSQR = whiteFade;
        this.channelMaxW = w;
        this.channelMaxR = r;
        this.channelMaxG = g;
        this.channelMaxB = b;
    }  
    
    public float getDayTimeValue(DayTimes dayTime)
    {
        float dayTimeValue = 0f;
        
        // Get the value for the key
        if (dayTimes.containsKey(dayTime)) {
            // Get the value for the key and create the letter
            dayTimeValue = dayTimes.get(dayTime);
        }
        // Return the output
        return dayTimeValue;
    }

    /**
     * @return the sunsetLengthSQR
     */
    public float getSunsetLengthSQR() {
        return sunsetLengthSQR;
    }

    /**
     * @param sunsetLengthSQR the sunsetLengthSQR to set
     */
    public void setSunsetLengthSQR(float sunsetLengthSQR) {
        this.sunsetLengthSQR = sunsetLengthSQR;
    }

    /**
     * @return the whiteFadeSQR
     */
    public float getWhiteFadeSQR() {
        return whiteFadeSQR;
    }

    /**
     * @param whiteFadeSQR the whiteFadeSQR to set
     */
    public void setWhiteFadeSQR(float whiteFadeSQR) {
        this.whiteFadeSQR = whiteFadeSQR;
    }

    /**
     * @return the channelMaxW
     */
    public int getChannelMaxW() {
        return channelMaxW;
    }

    /**
     * @param channelMaxW the channelMaxW to set
     */
    public void setChannelMaxW(int channelMaxW) {
        this.channelMaxW = channelMaxW;
    }

    /**
     * @return the channelMaxR
     */
    public int getChannelMaxR() {
        return channelMaxR;
    }

    /**
     * @param channelMaxR the channelMaxR to set
     */
    public void setChannelMaxR(int channelMaxR) {
        this.channelMaxR = channelMaxR;
    }

    /**
     * @return the channelMaxG
     */
    public int getChannelMaxG() {
        return channelMaxG;
    }

    /**
     * @param channelMaxG the channelMaxG to set
     */
    public void setChannelMaxG(int channelMaxG) {
        this.channelMaxG = channelMaxG;
    }

    /**
     * @return the channelMaxB
     */
    public int getChannelMaxB() {
        return channelMaxB;
    }

    /**
     * @param channelMaxB the channelMaxB to set
     */
    public void setChannelMaxB(int channelMaxB) {
        this.channelMaxB = channelMaxB;
    }

    /**
     * @return the nightB
     */
    public int getNightB() {
        return nightB;
    }

    /**
     * @param nightB the nightB to set
     */
    public void setNightB(int nightB) {
        this.nightB = nightB;
    }

    /**
     * @return the nightG
     */
    public int getNightG() {
        return nightG;
    }

    /**
     * @param nightG the nightG to set
     */
    public void setNightG(int nightG) {
        this.nightG = nightG;
    }

    /**
     * @return the nightR
     */
    public int getNightR() {
        return nightR;
    }

    /**
     * @param nightR the nightR to set
     */
    public void setNightR(int nightR) {
        this.nightR = nightR;
    }

    /**
     * @return the nightW
     */
    public int getNightW() {
        return nightW;
    }

    /**
     * @param nightW the nightW to set
     */
    public void setNightW(int nightW) {
        this.nightW = nightW;
    }
}
