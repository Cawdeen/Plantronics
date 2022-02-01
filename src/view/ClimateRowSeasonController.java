package view;


import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ClimateRowSeasonController {

    @FXML private GridPane seasonRowGrid;
    @FXML private Pane seasonRowPane;
    @FXML private GridPane seasonDetailGrid;
    @FXML private Text seasonName;
    @FXML private Text temp;
    @FXML private Text daylight;
    @FXML private Text rainfall;
    @FXML private Text humidity;

    public ClimateRowSeasonController()
    {
        
    }
    
    public void updateSeasonFields(String name, String tempSummary, String humiditySummary, String dayLight, String rFall)
    {
        setNameText(name);
        setTempText(tempSummary);
        setDayLightText(dayLight);
        setHumidText(humiditySummary);
        setRainFallText(rFall);
    }
    
    public void setNameText(String name) 
    {
        seasonName.setText(name);
    }
    
    public void setTempText(String ts)
    {
        temp.setText(ts);
    }
    
    public void setHumidText(String ts)
    {
        humidity.setText(ts);
    }
    
    public void setDayLightText(String dl)
    {
        daylight.setText(dl);
    }

    public void setRainFallText(String rf)
    {
        rainfall.setText(rf);
    }   
}

