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
    
    public IOData(boolean pinoutActive, HardwareSettingsData hs)
    {
        failedSerialAttempts = 0;
        lastPacket = new SerialPacket(-1,-1,-1,-1,false); //instantiate packets
        lastGoodPacket = lastPacket;
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
        if(pinout.pinout)//if we are using pinout (raspberry pi)
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
}
