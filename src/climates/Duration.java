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
public abstract class Duration implements DurationMethods, Serializable
{
    private int start1;
    private int end1;
    private int start2;
    private int end2;
    
    @Override
    public void setStart1(int newStart1) {
        start1 = newStart1;
    }

    @Override
    public void setEnd1(int newEnd1) {
        end1 = newEnd1;
    }

    @Override
    public void setStart2(int newStart2) {
        start2 = newStart2;
    }

    @Override
    public void setEnd2(int newEnd2) {
        end2 = newEnd2;
    }

    @Override
    public int getStart1() {
        return start1;
    }

    @Override
    public int getEnd1() {
        return end1;
    }

    @Override
    public int getStart2() {
        return start2;
    }

    @Override
    public int getEnd2() {
        return end2;
    }
    
}
