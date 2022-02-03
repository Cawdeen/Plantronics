/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import javafx.scene.input.KeyCode;

/**
 *
 * @author collin
 * This class gives a number to alter the current temp or humidity. May return positive or negative numbers based on input of arrow keys. 
 * The IOData class then uses this to change the temp or humidity by that much.
 */
public class KeyInput {
   
    private int humid;
    private int temp;
    
    public KeyInput()
    {
        humid = 0;
        temp = 0;
    }

    /**
     * @return the humid. Then set back to zero.
     */
    public int getHumid() {
        int humidOut = humid;
        setHumid(0);
        return humidOut;
    }

    /**
     * @return the temp. Then set back to zero.
     */
    public int getTemp() {
        int tempOut = temp;
        setTemp(0);
        return tempOut;
    }

    /**
     * @param humid the humid to set
     */
    public void setHumid(int humid) {
        this.humid = humid;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(int temp) {
        this.temp = temp;
    }
    
    public void handle(KeyCode code) {

        switch (code) {
            case UP:    
                temp += 1; 
                break;
            case DOWN:  
                temp -= 1; 
                break;
            case LEFT:  
                humid -= 1; 
                break;
            case RIGHT: 
                humid += 1; 
                break;
            default:
                break;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
