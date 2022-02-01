/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;


import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import java.util.Timer;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class Pinout {
    
    
    //*********************
    //For raspberry pi or windows. 
    //0=pi, 1=windows
    boolean pinout;
    //*********************
    
    private Timer timer;
    
    //Pinout numbers
    //pinout numbers for light channels
    //private static int LEDChannelW = 4;
    private static int ledChannelR = 0;
    private static int ledChannelG = 2;
    private static int ledChannelB = 3;
    //pinout numbers for lighting cooling fans
    private static int ledChannelWFan = 4;
    private static int ledChannelRGBFan = 5;
    //pinout numbers for Mist and Fog
    private static int mistPin = 12;
    private static int fogPin = 13;
    
    //pinout numbers for heating, cooling, ventilation, circulation
    private static int heatPin = 21;
    private static int coolPin = 22;
    private static int circPin = 23;
    private static int ventPin = 24;
    
    //PWM values
    //pwm output values for light channels
    private int pwmW;
    private int pwmR;
    private int pwmG;
    private int pwmB;
    private GpioPinPwmOutput gpwmW;
    private GpioPinDigitalOutput mistChannelPin;
    private GpioPinDigitalOutput fogChannelPin;
    private boolean misting;
    private boolean fogging;
    //pwm output values for lighting cooling fans
    private int pwmWFan;
    private int pwmRGBFan;   
    private int currentVentLevel;
    private int currentCircLevel;
    private boolean circHigh;
    private boolean ventHigh;
    
    HardwareSettingsData hs;
    
    public Pinout(boolean pinout, HardwareSettingsData hs)
    {
        this.pinout = pinout;
        this.hs = hs;
        misting = false;
        
        if(pinout) //for windows or raspberry pi
        {           
            // initialize wiringPi library, this is needed for PWM
            Gpio.wiringPiSetup();
            // softPwmCreate(int pin, int value, int range)
            // the range is set like (min=0 ; max=100)
            //SoftPwm.softPwmCreate(LEDChannelW, 0, 100);
            SoftPwm.softPwmCreate(ledChannelR, 0, 100);
            SoftPwm.softPwmCreate(ledChannelG, 0, 100);
            SoftPwm.softPwmCreate(ledChannelB, 0, 100);
            SoftPwm.softPwmCreate(ledChannelWFan, 0, 100);
            SoftPwm.softPwmCreate(ledChannelRGBFan, 0, 100);
            SoftPwm.softPwmCreate(ventPin, 0, 10);
            SoftPwm.softPwmCreate(circPin, 0, 10);
            
            GpioController gpio = GpioFactory.getInstance();            
            // All Raspberry Pi models support a hardware PWM pin on GPIO_01.
            // Raspberry Pi models A+, B+, 2B, 3B also support hardware PWM pins: GPIO_23, GPIO_24, GPIO_26
            //
            // by default we will use gpio pin #01; however, if an argument
            // has been provided, then lookup the pin by address
            //White channel led pin
            Pin ledChannelW = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01  // default pin if no pin argument found
                );               // argument array to search in
            gpwmW = gpio.provisionPwmOutputPin(ledChannelW);
            // you can optionally use these wiringPi methods to further customize the PWM generator
            // see: http://wiringpi.com/reference/raspberry-pi-specifics/
            com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
            //com.pi4j.wiringpi.Gpio.pwmSetRange(1024);
            //com.pi4j.wiringpi.Gpio.pwmSetClock(500);
            
            
            //create mist pin
            mistChannelPin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(mistPin), "mistChannelPin", PinState.HIGH);  
            fogChannelPin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(fogPin), "fogChannelPin", PinState.HIGH);  
        }
    }
    
    public void PWMOutput()
    {        
        if(pinout)
        {
            gpwmW.setPwm(getPwmW()*10);
            //SoftPwm.softPwmWrite(getLEDChannelW(), getPwmW());
            SoftPwm.softPwmWrite(getLEDChannelR(), getPwmR());
            SoftPwm.softPwmWrite(getLEDChannelG(), getPwmG());
            SoftPwm.softPwmWrite(getLEDChannelB(), getPwmB());            

            if(misting == true)
            {
                mistChannelPin.low();
            }
            else
            {
                mistChannelPin.high();
            }
            if(fogging == true)
            {
                fogChannelPin.low();
            }
            else
            {
                fogChannelPin.high();
            }

            SoftPwm.softPwmWrite(getLEDChannelWFan(), getPwmWFan());
            SoftPwm.softPwmWrite(getLEDChannelRGBFan(), getPwmRGBFan());
            SoftPwm.softPwmWrite(ventPin, getCurrentVentLevel());
            SoftPwm.softPwmWrite(circPin, getCurrentCircLevel());
        }
        else
        {
            System.out.println("current circ level: "+getCurrentCircLevel());
        }
    }

    /**
     * @return the ledChannelR
     */
    public static int getLEDChannelR() {
        return ledChannelR;
    }

    /**
     * @param aLEDChannelR the ledChannelR to set
     */
    public static void setLEDChannelR(int aLEDChannelR) {
        ledChannelR = aLEDChannelR;
        if(ledChannelR>100)
        {
            ledChannelR=100;
        }
    }

    /**
     * @return the ledChannelG
     */
    public static int getLEDChannelG() {
        return ledChannelG;
    }

    /**
     * @param aLEDChannelG the ledChannelG to set
     */
    public static void setLEDChannelG(int aLEDChannelG) {
        ledChannelG = aLEDChannelG;
        if(ledChannelG>100)
        {
            ledChannelG=100;
        }
    }

    /**
     * @return the ledChannelB
     */
    public static int getLEDChannelB() {
        return ledChannelB;
    }

    /**
     * @param aLEDChannelB the ledChannelB to set
     */
    public static void setLEDChannelB(int aLEDChannelB) {
        ledChannelB = aLEDChannelB;
        if(ledChannelB>100)
        {
            ledChannelB=100;
        }
    }

    /**
     * @return the ledChannelWFan
     */
    public static int getLEDChannelWFan() {
        return ledChannelWFan;
    }

    /**
     * @param aLEDChannelWFan the ledChannelWFan to set
     */
    public static void setLEDChannelWFan(int aLEDChannelWFan) {
        ledChannelWFan = aLEDChannelWFan;
    }

    /**
     * @return the ledChannelRGBFan
     */
    public static int getLEDChannelRGBFan() {
        return ledChannelRGBFan;
    }

    /**
     * @param aLEDChannelRGBFan the ledChannelRGBFan to set
     */
    public static void setLEDChannelRGBFan(int aLEDChannelRGBFan) {
        ledChannelRGBFan = aLEDChannelRGBFan;
    }

    /**
     * @return the pwmW
     */
    public int getPwmW() {
        return pwmW;
    }

    /**
     * @param pwmW the pwmW to set
     */
    public void setPwmW(int pwmW) {
        this.pwmW = pwmW;
    }

    /**
     * @return the pwmR
     */
    public int getPwmR() {
        return pwmR;
    }

    /**
     * @param pwmR the pwmR to set
     */
    public void setPwmR(int pwmR) {
        this.pwmR = pwmR;
    }

    /**
     * @return the pwmG
     */
    public int getPwmG() {
        return pwmG;
    }

    /**
     * @param pwmG the pwmG to set
     */
    public void setPwmG(int pwmG) {
        this.pwmG = pwmG;
    }

    /**
     * @return the pwmB
     */
    public int getPwmB() {
        return pwmB;
    }

    /**
     * @param pwmB the pwmB to set
     */
    public void setPwmB(int pwmB) {
        this.pwmB = pwmB;
    }

    /**
     * @return the pwmWFan
     */
    public int getPwmWFan() {
        if(pwmWFan > 20)
        {
            return pwmWFan;
        }
        else
        {
            return 0;
        }
    }

    /**
     * @param pwmWFan the pwmWFan to set
     */
    public void setPwmWFan(int pwmWFan) {
        this.pwmWFan = pwmWFan;
    }

    /**
     * @return the pwmRGBFan
     */
    public int getPwmRGBFan() {
        if(pwmRGBFan > 20)
        {
            return pwmRGBFan;
        }
        else
        {
            return 0;
        }
    }

    /**
     * @param pwmRGBFan the pwmRGBFan to set
     */
    public void setPwmRGBFan(int pwmRGBFan) {
        this.pwmRGBFan = pwmRGBFan;
    }

    /**
     * @param misting the misting to set
     */
    public void setMisting(Boolean misting) {
        this.misting = misting;
    }
    
    public void setVentToHigh()
    {
        ventHigh = true;
    }
    
    public void setVentToLow()
    {
        ventHigh = false;
    }
    
    public void setCircToHigh()
    {
        circHigh = true;
    }
    
    public void setCircToLow()
    {
        circHigh = false;
    }

    private int getCurrentVentLevel() {
        if(ventHigh){
            currentVentLevel = hs.getVentFanHigh();
        }
        else if(hs.isVentFanConstant()){
            currentVentLevel = hs.getVentFanConstSpeed();
        }
        else{
            currentVentLevel = 0;
        }
        return currentVentLevel;
    }

    private int getCurrentCircLevel() {
        if(circHigh){
            currentCircLevel = hs.getCircFanHigh();
        }
        else if(hs.isCircFanConstant()){
            currentCircLevel = hs.getCircFanConstSpeed();
        }
        else{
            currentCircLevel = 0;
        }
        return currentCircLevel;
    }

    public void setFogging(boolean f) {
        this.fogging = f;
    }
}
