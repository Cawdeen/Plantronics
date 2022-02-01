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
public class SeasonHumidity extends Range implements Serializable
{
    public SeasonHumidity(int minH, int maxH)
    {
        setMin(minH);
        setMax(maxH);
    }
    
    @Override
    public void setMin(int newMin) {
        super.setMin(newMin);
    }

    @Override
    public void setMax(int newMax) {
        super.setMax(newMax);
    }

    @Override
    public int getMin() {
        return super.getMin();
    }

    @Override
    public int getMax() {
        return super.getMax();
    }
}
