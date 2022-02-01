/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightfx;

import climates.Duration;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import static java.util.Calendar.*;
import model.ModelData;
import weatherfx.ConfigModeData;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class RGBCalc {
    
    private int kelvin;
    private float wIntensity;
    private float rgbIntensity;
    private float fractionOfNoon;
    private RGBValues rgbv;
    private float sunsetFactor = 2; 
    private float whiteFadeFactor = 2; 
    
    public RGBCalc()
    {
        fractionOfNoon = 0f;
        kelvin = 0;     //needed?
        wIntensity = 0f; //needed?
        rgbIntensity = 0f; //needed?
        rgbv = new RGBValues();
    }
    
    public RGBValues GetRGBValues(Duration ld, LocalTime currentTime, LightSettings ls, ConfigModeData config) 
    { 
        float sunset = ls.getSunsetLengthSQR();
        float whiteFade = ls.getWhiteFadeSQR();
        int startHr = ld.getStart1();
        int startMn = ld.getStart2();
        int endHr = ld.getEnd1();
        int endMn = ld.getEnd2();
        int currentHr = currentTime.getHour();
        int currentMn = currentTime.getMinute();
        int currentSc = currentTime.getSecond();
        
        //get fraction of midday. Either config mode or normal daytime calculations.
        if(config.getConfigMode() == false)
        {
            //calculate fraction of noon from actual times
            fractionOfNoon = calculateFractionOfNoon(startHr, startMn, endHr, endMn, currentHr, currentMn, currentSc);
        }
        else
        {
            //pull the fraction of noon from preset values for sunrise, noon, night
            fractionOfNoon = ls.getDayTimeValue(config.getConfigTime());
        }
        //prepare slider (0-100) to be converted to numbers from 4-1(4 is short/early, 1 is late/long).
        sunset = (sunset*.01f)*(sunsetFactor-1f);  //convert range to: 0 - max-1
        sunset = (sunset-sunsetFactor)*-1f;   //convert to: max - min
        whiteFade = (whiteFade*.01f)*(whiteFadeFactor-1f); //convert range to: 0 - max-1
        whiteFade = (whiteFade-whiteFadeFactor)*-1f;  //convert to: max - min        

        //take the x root of the rgb values to get the new rgb values based on sunset length(x). White gets this treatment and is altered again below for white cutout.
        float wFractionOfNoon = (float)Math.pow(fractionOfNoon,(1f/sunset));
        float rgbFractionOfNoon = (float)Math.pow(fractionOfNoon,(1f/sunset));
        
        wFractionOfNoon = (float)Math.pow(wFractionOfNoon, whiteFade);
        
        //use fractin of midday to set the intensity of the lightvalues
        float wIntensity = setIntensity(wFractionOfNoon)*100f;
        float rgbIntensity = setIntensity(rgbFractionOfNoon)*100f;
        rgbv.setWIntensity((int)wIntensity);
        rgbv.setRgbIntensity((int)rgbIntensity);
        kelvin = setKelvin(rgbFractionOfNoon);
        float red = RedFromKelvin(kelvin)/255f;
        float green = GreenFromKelvin(kelvin)/255f;
        float blue = BlueFromKelvin(kelvin)/255f;
        red = red*100f;
        green  = green*100f;
        blue = blue*100f;
        rgbv.setRed((int)red);
        rgbv.setGreen((int)green);
        rgbv.setBlue((int)blue);
        
        return rgbv;
    }
    
    //find Kelvin of daylight
    private int setKelvin(float fraction)
    {
        float newKelvin = 0;
        newKelvin = (float) (7500f * (Math.sqrt(fraction)));
        return (int)newKelvin;
    }
    
    //find brightness of the lights (0-100)
    private float setIntensity(float fraction)
    {
        float newIntensity = 0;
        newIntensity = (float) Math.pow(fraction, 1f/1f);
        return newIntensity;
    }
    
    //calculate the fraction of time between mid day and the end of the day. Mid day is 1.0 and nightfall is 0.0.
    public float calculateFractionOfNoon(int startHr, int startMn, int endHr, int endMn, int currentHr, int currentMn, int currentSc) //String
    {
        double fractionOfNoon = 0L;        
        //get total daylength
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn) + ":00";
        String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn) + ":00";
        String currentTime = String.valueOf(currentHr) + ":" + String.valueOf(currentMn) + ":" + String.valueOf(currentSc);
        
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();

        c1.set(YEAR, MONTH, DAY_OF_MONTH, startHr, startMn, 0);
        c2.set(YEAR, MONTH, DAY_OF_MONTH, endHr, endMn, 0);
        c3.set(YEAR, MONTH, DAY_OF_MONTH, currentHr, currentMn, currentSc);
        //*********
        //Date d1 = sdf.parse(startTime);
        //Date d2 = sdf.parse(endTime);
        //Date d3 = sdf.parse(currentTime);

        //get elapsed time between start and end (may be negative for next day)
        long elapsed = c2.getTimeInMillis() - c1.getTimeInMillis();
        //***********
        //long elapsed = d2.getTime() - d1.getTime();

        //if the end time is less than the start time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
        if (elapsed < 0)
        {
            elapsed = 86400000 + elapsed;
        }
        //get half the daylength
        elapsed = elapsed/2;

        //create date for noon
        Calendar c4 = Calendar.getInstance();
        c4.set(YEAR, MONTH, DAY_OF_MONTH, startHr, startMn);
        c4.add(Calendar.SECOND, (int)elapsed);                                     //CHANGED FROM SECOND TO MILLISECOND
        //check if noon is before our current time
        if(c4.before(c3))
        {

        }
        //get elapsed time from now until daylight ends.

        long elapsed2 = c2.getTimeInMillis() - c3.getTimeInMillis();
        //if the end time is less than the current time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
        if (elapsed2 < 0)
        {
            elapsed2 = 86400000 + elapsed2;
        }

        //calculate initial fraction
        fractionOfNoon = (double)elapsed2/(double)elapsed;
        //check if before or after noon. Will be greater than 1 if before noon. 
        if (fractionOfNoon > 1)
        {
            fractionOfNoon = ((fractionOfNoon - 1)*-1)+1;
            //fractionOfNoon = fractionOfNoon*-1;
        }
        return (float)fractionOfNoon;
    }
    
    public int RedFromKelvin(int kelvinTemp)
    {
        float rColor = 0;
        //Set Temperature = Temperature \ 100
        float temp = kelvinTemp/100;

        //If Temperature <= 66 Then
        if(temp <= 66)
        {
                //Red = 255
                rColor = 255;
        }
        //Else
        else
        {
                //Red = Temperature - 60
                rColor = temp - 60;
                //Red = 329.698727446 * (Red ^ -0.1332047592)
                rColor = (float) (329.698727446f * Math.pow(rColor,-0.1332047592f));
                //If Red < 0 Then Red = 0
                if(rColor < 0)
                {
                        rColor = 0;
                }
                //If Red > 255 Then Red = 255
                if(rColor > 255)
                {
                        rColor = 255;
                }
        }
        //End If
        return (int)rColor;
    }
    
    public int GreenFromKelvin(int kelvinTemp)
    {
        float gColor = 0;
        //Set Temperature = Temperature \ 100
        float temp = kelvinTemp/100;

        //If Temperature <= 66 Then
        if(temp <= 66)
        {
                //Green = Temperature
                gColor = temp;
                //Green = 99.4708025861 * Ln(Green) - 161.1195681661
                gColor = (float) (99.4708025861f * Math.log(temp) - 161.1195681661f);
                //If Green < 0 Then Green = 0
                if(gColor < 0)
                {
                        gColor = 0;
                }
                //If Green > 255 Then Green = 255
                if(gColor > 255)
                {
                        gColor = 255;
                }

        }
        //Else
        else
        {
                //Green = Temperature - 60
                gColor = temp - 60;
                //Green = 288.1221695283 * (Green ^ -0.0755148492)
                gColor = (float) (288.1221695283f * Math.pow(gColor,-0.0755148492f));
                //If Green < 0 Then Green = 0
                if(gColor < 0)
                {
                        gColor = 0;
                }
                //If Green > 255 Then Green = 255
                if(gColor > 255)
                {
                        gColor = 255;
                }
        }
        //End If
        return (int)gColor;
    }
    
    public int BlueFromKelvin(int kelvinTemp)
    {
        float bColor = 0;
        //Set Temperature = Temperature \ 100
        float temp = kelvinTemp/100;

        //If Temperature <= 66 Then
        if(temp >= 66)
        {
                //Red = 255
                bColor = 255;
        }
        //Else
        else
        {
                //If Temperature <= 19 Then
                if(temp <= 19)
                {
                        //Blue = 0
                        bColor = 0;
                }
                else
                {
                        //Blue = Temperature - 10
                        bColor = temp - 10;
                        //Blue = 138.5177312231 * Ln(Blue) - 305.0447927307
                        bColor = (float) (138.5177312231f * Math.log(bColor) - 305.0447927307f);
                        //If Blue < 0 Then Blue = 0
                        if(bColor < 0)
                        {
                                bColor = 0;
                        }
                        //If Blue > 255 Then Blue = 255
                        if(bColor > 255)
                        {
                                bColor = 255;
                        }
                }

        }
        //End If
        return (int)bColor;
    }

    /**
     * @return the fractionOfNoon
     */
    public float getFractionOfNoon() {
        return fractionOfNoon;
    }
}
