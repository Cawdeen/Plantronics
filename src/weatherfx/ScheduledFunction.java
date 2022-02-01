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
public class ScheduledFunction {
    
    public enum HardwareType{
        MIST,
        FOG,
        HEAT,
        COOLING,
        CIRCULATION,
        VENTILATION
    }
    
    private String name;
    private int startHr;
    private int startMn;
    private int durationSec;
    private HardwareType type;
    
    
    public ScheduledFunction()
    {
        type = HardwareType.MIST;
        name = "Default Mist Function";
        startHr = 1;
        startMn = 0;
        durationSec = 5;
    }
    
    public ScheduledFunction(String name, String type, int startHr, int startMn, int duration)
    {
        
        this.type = getTypeFromString(type);
        this.name = name;
        this.startHr = startHr;
        this.startMn = startMn;
        this.durationSec = duration;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the startHr
     */
    public int getStartHr() {
        return startHr;
    }

    /**
     * @param startHr the startHr to set
     */
    public void setStartHr(int startHr) {
        this.startHr = startHr;
    }

    /**
     * @return the startMn
     */
    public int getStartMn() {
        return startMn;
    }

    /**
     * @param startMn the startMn to set
     */
    public void setStartMn(int startMn) {
        this.startMn = startMn;
    }

    /**
     * @return the durationSec
     */
    public int getDurationSec() {
        return durationSec;
    }

    /**
     * @param durationSec the durationSec to set
     */
    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    /**
     * @return the type
     */
    public HardwareType getType() {
        return type;
    }
    
    public HardwareType getTypeFromString(String type)
    {
        HardwareType hwType = HardwareType.MIST;
        switch (type)
        {
            case "Mist":
                hwType = HardwareType.MIST;
                break;
            case "Fog":
                hwType = HardwareType.FOG;
                break;
            case "Heat":
                hwType = HardwareType.HEAT;
                break;
            case "Cooling":
                hwType = HardwareType.COOLING;
                break;
            case "Circulation":
                hwType = HardwareType.CIRCULATION;
                break;
            case "Ventilation":
                hwType = HardwareType.VENTILATION;
                break;
        }
        return hwType;
    }
    
    public String getTypeString()
    {
        String string = "";
        switch (type)
        {
            case MIST:
                string = "Mist";
                break;
            case FOG:
                string = "Fog";
                break;
            case HEAT:
                string = "Heat";
                break;
            case COOLING:
                string = "Cooling";
                break;
            case CIRCULATION:
                string = "Circulation";
                break;
            case VENTILATION:
                string = "Ventilation";
                break;
        }
        return string;
    }

    /**
     * @param type the type to set
     */
    public void setType(HardwareType type) {
        this.type = type;
    }
}
