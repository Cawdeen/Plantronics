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
public class IOData {
    
    private int failedSerialAttempts;
    private SerialPacket lastPacket;
    private SerialPacket lastGoodPacket;
    private long nanoTimePacketSuccess;
    private Pinout pinout;
    private KeyInput keyInput;
    
    public IOData(boolean pinoutActive, HardwareSettingsData hs)
    {
        failedSerialAttempts = 0;
        if(pinoutActive == false) //not using pinout
        {
            keyInput = new KeyInput();//create a key listener if there is no pinout (Windows mode)
            lastPacket = new SerialPacket(70,70,80,80,false); //instantiate fake packets
            lastGoodPacket = lastPacket;
        }else          //using pinout
        {
            lastPacket = new SerialPacket(-1,-1,-1,-1,false); //instantiate real packets
            lastGoodPacket = lastPacket;
        }
        
        pinout = new Pinout(pinoutActive, hs);
        
    }
    
    public void startHardwareTimer(int seconds)
    {
        
    }
    /**
     * @return the failedSerialAttempts
     */
    public int getFailedSerialAttempts() {
        return failedSerialAttempts;
    }

    /**
     * @return the lastPacket
     */
    public SerialPacket getLastPacket() {
        return lastPacket;
    }

    /**
     * @param lastPacket the lastPacket to set
     */
    public void setLastPacket(SerialPacket lastPacket) {
        if(pinout.pinout == true)//if we are using pinout (raspberry pi)
        {
            if(lastPacket != null)
            {
                this.lastPacket = lastPacket;
                if(lastPacket.isSuccess() == true) //successful reading
                {
                    lastGoodPacket = lastPacket;
                    failedSerialAttempts = 0;
                    nanoTimePacketSuccess = System.nanoTime();
                }
                else //not a successful reading
                {
                    failedSerialAttempts += 1;
                }
            }
            else{ //serial reading failed. Make a fake packet
                this.lastPacket = new SerialPacket(-1,-1,-1,-1,false); //instantiate failed packet
                failedSerialAttempts += 1;
            }
        }else//not using pinout. We are in Windows mode.
        {
            //set false temp and humidity and check KeyListener for inputs
            lastGoodPacket = this.lastPacket;
            failedSerialAttempts = 0;
            nanoTimePacketSuccess = System.nanoTime();
            float temp = lastGoodPacket.getTempSHT();
            float humid = lastGoodPacket.getHumiditySHT();
            temp += getKeyInput().getTemp(); //alter the temp by keyboard input
            humid += getKeyInput().getHumid();//alter the humidity by keyboard input
            this.lastPacket = new SerialPacket(temp,temp,humid, humid, false); //instantiate fake packet
        }
    }

    /**
     * @return the lastGoodPacket
     */
    public SerialPacket getLastGoodPacket() {
        return lastGoodPacket;
    }    

    /**
     * @return the nanoTimePacketSuccess
     */
    public long getSecondsSincePacketSuccess() {
        long elapsed = System.nanoTime()-nanoTimePacketSuccess; //get elapsed time since last successful packet
        elapsed = elapsed/1000000000; //convert to seconds
        return elapsed;
    }

    /**
     * @return the pinout
     */
    public Pinout getPinout() {
        return pinout;
    }

    /**
     * @return the keyListener
     */
    public KeyInput getKeyInput() {
        return keyInput;
    }
}
