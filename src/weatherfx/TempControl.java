/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherfx;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class TempControl 
{
    private boolean heating;
    private boolean cooling;

    public TempControl()
    {
        heating = false;
        cooling = false;
    }

    public boolean getHeating(int minTemp, int currentTemp)
    {
        if(currentTemp <= minTemp && isValidCondition(currentTemp) == true)
        {
            heating = true;
        }
        else 
        {
            heating = false;
        }
        return heating;
    }

    public boolean getCooling(int maxTemp, int currentTemp)
    {
        if(currentTemp >= maxTemp && isValidCondition(currentTemp) == true)
        {
            cooling = true;
        }
        else
        {
            cooling = false;
        }
        return cooling;
    }
    
    public boolean isValidCondition(int currentTemp)
    {
        if(currentTemp > 50 && currentTemp < 100)
        {
            return true;
        }else
            return false;
        
    }
}
