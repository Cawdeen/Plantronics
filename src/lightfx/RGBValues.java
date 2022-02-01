/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightfx;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class RGBValues 
{
    private int wIntensity; //value between 0-100 for light brightness.
    private int rgbIntensity; //value between 0-100 for light brightness.
    private int red;       //red value of lighting (0-100)
    private int green;     //green value of lighting (0-100)
    private int blue;      //blue value of lighting (0-100)

    public RGBValues(int newIntensity, int newRGBIntensity, int newRed, int newGreen, int newBlue)
    {
        wIntensity = newIntensity;
        rgbIntensity = newRGBIntensity;
        red = newRed;
        green = newGreen;
        blue = newBlue;
    }
    
    public RGBValues()
    {
        wIntensity = 0;
        red = 0;
        green = 0;
        blue = 0;
    }
    
    /**
     * @return the wIntensity
     */
    public int getWIntensity() {
        return wIntensity;
    }

    /**
     * @param intensity the wIntensity to set
     */
    public void setWIntensity(int intensity) {
        this.wIntensity = intensity;
    }

    /**
     * @return the red
     */
    public int getRed() {
        return red;
    }

    /**
     * @param red the red to set
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * @return the green
     */
    public int getGreen() {
        return green;
    }

    /**
     * @param green the green to set
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * @return the blue
     */
    public int getBlue() {
        return blue;
    }

    /**
     * @param blue the blue to set
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }

    /**
     * @return the rgbIntensity
     */
    public int getRgbIntensity() {
        return rgbIntensity;
    }

    /**
     * @param rgbIntensity the rgbIntensity to set
     */
    public void setRgbIntensity(int rgbIntensity) {
        this.rgbIntensity = rgbIntensity;
    }
}


