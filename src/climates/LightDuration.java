/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package climates;

import java.io.Serializable;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class LightDuration extends Duration implements Serializable
{
    public LightDuration(int startHour, int startMin, int endHour, int endMin)
    {
        setStart1(startHour);
        setStart2(startMin);
        setEnd1(endHour);
        setEnd2(endMin);
    }
    
    public void setStart1(int newStart1)
    {
        super.setStart1(newStart1);
    }
    
    public void setStart2(int newStart2)
    {
        super.setStart2(newStart2);
    }
    
    public void setEnd1(int newEnd1)
    {
        super.setEnd1(newEnd1);
    }
    
    public void setEnd2(int newEnd2)
    {
        super.setEnd2(newEnd2);
    }
    
    public int getStart1()
    {
        return super.getStart1();
    }
    
    public int getStart2()
    {
        return super.getStart2();
    }
    
    public int getEnd1()
    {
        return super.getEnd1();
    }
    
    public int getEnd2()
    {
        return super.getEnd2();
    }
}
