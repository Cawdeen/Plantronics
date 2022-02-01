/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightfx;

import javafx.concurrent.Task;
import model.ModelData;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class LightUpdater extends Task<LightValues>{
    
    private final ModelData md;
    
    public LightUpdater(ModelData md)
    {
        this.md = md;
    }

    @Override
    protected LightValues call() throws Exception {
        //get initial lightvalues
        LightValues lv = md.getLightData().getLightValues(md.getClimateData().getCurrentSeason().getLightDuration()
                ,md.getCurrentConditionsData().getCurrentTime()
                ,md.getConfigModeData());
        
        //get the weather effects
        //skipping for now
        
        //here we do a check to see if any of the colors are off, and we keep it at the minimum value. Value is in LightSettings
        if(lv.getBlue() < md.getLightData().getLightSettings().getNightB())
        {
            lv.setBlue(md.getLightData().getLightSettings().getNightB());//keep it at min
        }
        if(lv.getRed()< md.getLightData().getLightSettings().getNightR())
        {
            lv.setRed(md.getLightData().getLightSettings().getNightR()); //keep it at min
        }
        if(lv.getGreen() < md.getLightData().getLightSettings().getNightG())
        {
            lv.setGreen(md.getLightData().getLightSettings().getNightG());
        }
        
        return lv;
    }
}
