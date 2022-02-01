/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightfx;

import climates.Duration;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import weatherfx.ConfigModeData;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class LightData {
    
    private boolean isDay;
    private RGBCalc rgbCalc;
    private LightSettings lightSettings;
    
    private LightValues lightValues;
    
    public LightData()
    {
        isDay = false;
        lightSettings = new LightSettings(50, 50, 75,75,75,75);
        rgbCalc = new RGBCalc();
    }
    
    public LightValues getLightValues(Duration ld, LocalTime currentTime, ConfigModeData config)
    {
        LightValues lv = new LightValues(0, 0, 0, 0);
        if(getIsDay(ld.getStart1(),ld.getStart2(),ld.getEnd1(),ld.getEnd2(),currentTime.getHour(),currentTime.getMinute())==false && config.getConfigMode()==false)
        {
            return lv;
        }
        else
        {
            //get initial wrgb values
            RGBValues rgbV = rgbCalc.GetRGBValues(ld, currentTime, getLightSettings(), config);

            //multiply them by their max values
            double white = rgbV.getWIntensity() * (getLightSettings().getChannelMaxW()/100d);
            double red = (rgbV.getRed() * (getLightSettings().getChannelMaxR()/100d))*(rgbV.getRgbIntensity()/100d);
            double green = (rgbV.getGreen() * (getLightSettings().getChannelMaxG()/100d))*(rgbV.getRgbIntensity()/100d);
            double blue = (rgbV.getBlue() * (getLightSettings().getChannelMaxB()/100d))*(rgbV.getRgbIntensity()/100d);

            //take the x root of the rgb values to get the new rgb values based on sunset length(x). White gets this treatment and is altered again below for white cutout.
            //double sunsetLength = (double)(hs.getSunsetLengthSQR()*.01d)*3d;
            //sunsetLength = (sunsetLength-4d)*-1d;
            //double whiteFade = (double)(hs.getWhiteFadeSQR()*.01d)*3d;
            //whiteFade = (whiteFade-4d)*-1d;
            //white = (Math.pow(white,(1d/whiteFade+)))*100d;
            //red = (Math.pow(red, (1d/sunsetLength)))*100d;
            //green = (Math.pow(green, (1d/sunsetLength)))*100d;
            //blue = (Math.pow(blue, (1d/sunsetLength)))*100d;
            //raise white value to the power of the white cutout value
            //white = Math.pow(white, whiteFade);
            lv = new LightValues((int)white, (int)red, (int)green, (int)blue);
        }
        return lv;
    }
    
    public boolean getIsDay(int startHr, int startMn, int endHr, int endMn, int currentHr, int currentMn)
    {
        checkForDay(startHr,startMn,endHr,endMn,currentHr,currentMn);
        return isDay;
    }
    
    private void checkForDay(int startHr, int startMn, int endHr, int endMn, int currentHr, int currentMn)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn);
            String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn);
            String currentTime = String.valueOf(currentHr) + ":" + String.valueOf(currentMn);
            
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            Date d3 = sdf.parse(currentTime);
            if(d1.compareTo(d3)>0)        //start time is after current time
            {
                if(d2.compareTo(d3)>0)    //if end time is also after current time
                {
                    if(d2.compareTo(d1)>0)//if end time is after start time
                    {
                        isDay = false;    //lights would be off
                        return;
                    }
                    else                  //else if end time is the next time coming up
                    {
                        isDay = true;     //lights would be on
                        return;
                    }
                }
            }
            else                          //start time is before current time
            {
                if(d2.compareTo(d3)>0)    //end time is after current (current time is right in the middle)
                {
                    isDay = true;         //lights would be on
                    return;
                }
                else                      //both end and start times are before current time
                {
                    if(d2.compareTo(d1)>0)//if end time was after start time
                    {
                        isDay = false;    //lights would be off
                        return;
                    }
                    else                  //if the end time is before the start time, we'll hit that first the next morning
                    {
                        isDay = true;     //lights would be on
                        return;
                    }
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(LightData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the lightValues
     */
    public LightValues getLightValues() {
        return lightValues;
    }

    /**
     * @return the lightSettings
     */
    public LightSettings getLightSettings() {
        return lightSettings;
    }
}
