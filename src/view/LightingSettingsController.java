package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static lightfx.LightSettings.DayTimes.*;
import model.ModelData;

public class LightingSettingsController {

    @FXML private Slider gSlider;
    @FXML private Slider bSlider;
    @FXML private Slider rSlider;
    @FXML private Slider wSlider;
    @FXML private RadioButton radioSunset;
    @FXML private RadioButton radioMorning;
    @FXML private RadioButton radioMidDay;
    @FXML private RadioButton radioNight;
    @FXML private ToggleButton configMode;
    @FXML private Slider wFadeSlider;
    @FXML private Slider sunsetSlider;
    @FXML private Button backArrow;
    
    private ModelData modelData;
    
    public LightingSettingsController()
    {
        wSlider = new Slider(0, 100, 100);
        rSlider = new Slider(0, 100, 100);
        gSlider = new Slider(0, 100, 100);
        bSlider = new Slider(0, 100, 100);  
        radioSunset = new RadioButton();
        radioMorning = new RadioButton();
        radioMidDay = new RadioButton();
        radioNight = new RadioButton();
        //sunset sliders use negative values that get converted to positive values when used. Due to the fact that higher number make the sunset length and white channel cutoff shorter.
        wFadeSlider = new Slider(0, 100, 100);
        sunsetSlider = new Slider(0, 100, 100);
    }
    
    @FXML
    public void initialize()
    {
        
    }
    
    /**
     * Public method to set the modelData of the view.
     * @param modelData 
     */
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        refreshView();
    }
    
    private void refreshView()
    {
        wSlider.setValue(modelData.getLightData().getLightSettings().getChannelMaxW());
        rSlider.setValue(modelData.getLightData().getLightSettings().getChannelMaxR());
        gSlider.setValue(modelData.getLightData().getLightSettings().getChannelMaxG());
        bSlider.setValue(modelData.getLightData().getLightSettings().getChannelMaxB());
        wFadeSlider.setValue(modelData.getLightData().getLightSettings().getWhiteFadeSQR());
        sunsetSlider.setValue(modelData.getLightData().getLightSettings().getSunsetLengthSQR());
        configMode.setSelected(modelData.getConfigModeData().getConfigMode());
        switch(modelData.getConfigModeData().getConfigTime())
        {
            case NIGHT:
                radioSunset.setSelected(false);
                radioMorning.setSelected(false);
                radioMidDay.setSelected(false);
                radioNight.setSelected(true);
                break;
            case SUNRISE:
                radioSunset.setSelected(true);
                radioMorning.setSelected(false);
                radioMidDay.setSelected(false);
                radioNight.setSelected(false);
                break;
            case MORNING:
                radioSunset.setSelected(false);
                radioMorning.setSelected(true);
                radioMidDay.setSelected(false);
                radioNight.setSelected(false);
                break;
            case NOON:
                radioSunset.setSelected(false);
                radioMorning.setSelected(false);
                radioMidDay.setSelected(true);
                radioNight.setSelected(false);
                break;
        }
    }
    
    /**
     * Event handler for back arrow button. Closes the stage.
     * @param event 
     */
    @FXML
    void handleBackArrowAction(ActionEvent event) {
        //Close the current stage
        // get a handle to the stage
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        // close this stage
        stage.close();
    }

    /**
     * Event Handler for ConfigMode toggle button.
     * @param event 
     */
    @FXML
    void handleConfigModeAction(ActionEvent event) {
        if(configMode.isSelected())
        {
            modelData.getConfigModeData().setConfigMode(true);
        }
        else
        {
            modelData.getConfigModeData().setConfigMode(false);
        }
    }
    
    @FXML
    public void handleRadioSunsetAction(ActionEvent event)
    {
        radioSunset.setSelected(true);
        radioMorning.setSelected(false);
        radioMidDay.setSelected(false);
        radioNight.setSelected(false);
        modelData.getConfigModeData().setConfigTime(SUNRISE);
    }
    
    @FXML
    public void handleRadioMorningAction(ActionEvent event)
    {
        radioSunset.setSelected(false);
        radioMorning.setSelected(true);
        radioMidDay.setSelected(false);
        radioNight.setSelected(false);
        modelData.getConfigModeData().setConfigTime(MORNING);
    }
    
    @FXML
    public void handleRadioMidDayAction(ActionEvent event)
    {
        radioSunset.setSelected(false);
        radioMorning.setSelected(false);
        radioMidDay.setSelected(true);
        radioNight.setSelected(false);
        modelData.getConfigModeData().setConfigTime(NOON);
    }
    
    @FXML
    public void handleRadioNightAction(ActionEvent event)
    {
        radioSunset.setSelected(false);
        radioMorning.setSelected(false);
        radioMidDay.setSelected(false);
        radioNight.setSelected(true);
        modelData.getConfigModeData().setConfigTime(NIGHT);
    }
    
    @FXML
    void sliderActionW(MouseEvent event)
    {
        System.out.println(wSlider.getValue());
        modelData.getLightData().getLightSettings().setChannelMaxW((int)wSlider.getValue());
    }
    @FXML
    void sliderActionR(MouseEvent event)
    {
        System.out.println(rSlider.getValue());
        modelData.getLightData().getLightSettings().setChannelMaxR((int)rSlider.getValue());
    }
    @FXML
    void sliderActionG(MouseEvent event)
    {
        System.out.println(gSlider.getValue());
        modelData.getLightData().getLightSettings().setChannelMaxG((int)gSlider.getValue());
    }
    @FXML
    void sliderActionB(MouseEvent event)
    {
        System.out.println(bSlider.getValue());
        modelData.getLightData().getLightSettings().setChannelMaxB((int)bSlider.getValue());
    }
    @FXML
    void sliderActionWFade(MouseEvent event)
    {
        System.out.println(wFadeSlider.getValue());
        modelData.getLightData().getLightSettings().setWhiteFadeSQR((int)wFadeSlider.getValue());
    }
    @FXML
    void sliderActionSunset(MouseEvent event)
    {
        System.out.println(sunsetSlider.getValue());
        modelData.getLightData().getLightSettings().setSunsetLengthSQR((int)sunsetSlider.getValue());
    }
   
}
