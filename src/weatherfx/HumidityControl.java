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
public class HumidityControl 
{
    private boolean humidifying;
    private boolean ventilating;

    public HumidityControl()
    {
        humidifying = false;
        ventilating = false;
    }

    public boolean getHumidifying(int minHumidity, int currentHumidity)
    {
        if(currentHumidity <= minHumidity && isValidCondition(currentHumidity) == true)
        {
            humidifying = true;
        }
        else 
        {
            humidifying = false;
        }
        return humidifying;
    }

    public boolean getVentilating(int maxHumidity, int currentHumidity)
    {
        if(currentHumidity >= maxHumidity && isValidCondition(currentHumidity) == true)
        {
            ventilating = true;
        }
        else
        {
            ventilating = false;
        }
        return ventilating;
    }
    
    public boolean isValidCondition(int currentHumidity)
    {
        if(currentHumidity > 10 && currentHumidity < 100)
        {
            return true;
        }else
            return false;
        
    }
}
