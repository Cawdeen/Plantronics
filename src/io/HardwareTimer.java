/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import javafx.concurrent.Task;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class HardwareTimer extends Task<Void>{
    
    private final int duration;

    public HardwareTimer(int duration)
    {
        this.duration = duration;
    }
    
    @Override
    protected Void call() throws Exception {
        //sleep for the duration
        Thread.sleep(1000L*duration);
        return null;
    }
    
}
