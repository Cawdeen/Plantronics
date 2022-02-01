/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package climates;

//import Model.RangeMethods;

import java.io.Serializable;


/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public abstract class Range implements RangeMethods, Serializable
{
    private int min;
    private int max;

    
    @Override
    public int getMin() {
        return min;
    }

    @Override
    public void setMin(int newMin) {
        min = newMin;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public void setMax(int newMax) {
        max = newMax;
    }
}
