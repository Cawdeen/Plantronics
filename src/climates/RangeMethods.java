/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package climates;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public interface RangeMethods 
{
    public abstract void setMin(int newMin);
    public abstract void setMax(int newMax);
    public abstract int getMin();
    public abstract int getMax();
}
