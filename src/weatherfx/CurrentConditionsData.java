/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherfx;

import java.time.LocalDate;
import java.time.LocalTime;
/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class CurrentConditionsData 
{
    private int currentTemp;
    private int currentHumidity;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private TempControl tempControl;
    private HumidityControl humidityControl;
    
    public CurrentConditionsData(int newTemp, int newHumidity)
    {
        tempControl = new TempControl();
        humidityControl = new HumidityControl();
        currentTemp = newTemp;
        currentHumidity = newHumidity;
        setCurrentDate();
    }
    
    public CurrentConditionsData()
    {
        tempControl = new TempControl();
        humidityControl = new HumidityControl();
        currentTemp = -1;
        currentHumidity = -1;
    }

    private void setCurrentDate() {
        currentDate = LocalDate.now();
    }

    /**
     * @return the currentTemp
     */
    public int getCurrentTemp() {
        return currentTemp;
    }

    /**
     * @param currentTemp the currentTemp to set
     */
    public void setCurrentTemp(int dhtTemp, int shtTemp) {
        boolean sht = false;
        boolean dht = false;
        if (shtTemp > 1 && shtTemp < 150)
        {
            sht = true;
        }
        if (dhtTemp > 1 && dhtTemp < 150)
        {
            dht = true;
        }
        if(sht)
        {
            this.currentTemp = shtTemp;
        }
        else if(dht)
        {
            this.currentTemp = dhtTemp;
        }
        else
        {
            this.currentTemp = -1;
        }
    }

    /**
     * @return the currentHumidity
     */
    public int getCurrentHumidity() {
        return currentHumidity;
    }

    /**
     * @param currentHumidity the currentHumidity to set
     */
    public void setCurrentHumidity(int shtHumid, int dhtHumid) {
        boolean sht = false;
        boolean dht = false;
        if (shtHumid > 1 && shtHumid < 100)
        {
            sht = true;
        }
        if (dhtHumid > 1 && dhtHumid < 100)
        {
            dht = true;
        }
        if(sht)
        {
            this.currentHumidity = shtHumid;
        }
        else if(dht)
        {
            this.currentHumidity = shtHumid;
        }
        else
        {
            this.currentHumidity = -1;
        }
    }

    /**
     * @return the currentDate
     */
    public LocalDate getCurrentDate() {
        setCurrentDate();
        return currentDate;
    }

    /**
     * @return the currentTime
     */
    public LocalTime getCurrentTime() {
        setCurrentTime();
        return currentTime;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime() {
        currentTime = LocalTime.now();
    }

    /**
     * @return the tempControl
     */
    public TempControl getTempControl() {
        return tempControl;
    }

    /**
     * @return the humidityControl
     */
    public HumidityControl getHumidityControl() {
        return humidityControl;
    }
}




