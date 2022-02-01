/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class SerialPacket {
    
    private float tempDHT;
    private float tempSHT;
    private float humidityDHT;
    private float humiditySHT;
    private boolean success;
    private LocalTime createOnTime;
    private LocalDate createOnDate;
    
    public SerialPacket(float tempDHT, float tempSHT, float humidityDHT, float humiditySHT, boolean success)
    {
        this.tempDHT = tempDHT;
        this.tempSHT = tempSHT;
        this.humidityDHT = humidityDHT;
        this.humiditySHT = humiditySHT;
        this.success = success;
        createOnDate = LocalDate.now();
        createOnTime = LocalTime.now();
    }    

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return the tempDHT
     */
    public float getTempDHT() {
        return tempDHT;
    }

    /**
     * @return the humidityDHT
     */
    public float getHumidityDHT() {
        return humidityDHT;
    }

    /**
     * @return the tempSHT
     */
    public float getTempSHT() {
        return tempSHT;
    }

    /**
     * @return the humiditySHT
     */
    public float getHumiditySHT() {
        return humiditySHT;
    }
    
    public String toString()
    {
        return tempDHT + "," + " " + tempSHT + "," + " " + humidityDHT + "," + " " + humiditySHT + "," + " " + success;
    }

    /**
     * @return the createOnTime
     */
    public LocalTime getCreateOnTime() {
        return createOnTime;
    }

    /**
     * @return the createOnDate
     */
    public LocalDate getCreateOnDate() {
        return createOnDate;
    }
}
