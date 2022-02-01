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
public class LightValues {
    
    private int white;     //white value of lighting (0-100)
    private int red;       //red value of lighting (0-100)
    private int green;     //green value of lighting (0-100)
    private int blue;      //blue value of lighting (0-100)
    
    public LightValues()
    {
        white = 0;
        red = 0;
        green = 0;
        blue = 0;
    }
    
    public LightValues(int w, int r, int g, int b)
    {
        white = w;
        red = r;
        green = g;
        blue = b;
    }
    
    public void updateLightValues(int w, int r, int g, int b)
    {
        setWhite(w);
        setRed(r);
        setGreen(g);
        setBlue(b);
    }

    /**
     * @return the white
     */
    public int getWhite() {
        return white;
    }

    /**
     * @return the red
     */
    public int getRed() {
        return red;
    }

    /**
     * @return the green
     */
    public int getGreen() {
        return green;
    }

    /**
     * @return the blue
     */
    public int getBlue() {
        return blue;
    }

    /**
     * @param white the white to set
     */
    public void setWhite(int white) {
        this.white = white;
    }

    /**
     * @param red the red to set
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * @param green the green to set
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * @param blue the blue to set
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }
    
}
