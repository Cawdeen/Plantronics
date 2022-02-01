/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import model.ModelData;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class FanSettingsController {
    
    @FXML private ToggleButton configMode;
    @FXML private ToggleButton circFanToggle;
    @FXML private TextField circSpeedConst;
    @FXML private Button circStrengthConstDown;
    @FXML private Button circStrengthConstUp;
    @FXML private TextField circSpeedMax;
    @FXML private Button circStrengthMaxDown;
    @FXML private Button circStrengthMaxUp;
    @FXML private ToggleButton ventFanToggle;
    @FXML private TextField ventSpeedConst;
    @FXML private Button ventSpeedConstDown;
    @FXML private Button ventSpeedConstUp;
    @FXML private TextField ventSpeedMax;
    @FXML private Button ventSpeedMaxDown;
    @FXML private Button ventSpeedMaxUp;
    @FXML private TextField wSpeedMin;
    @FXML private Button wMinDown;
    @FXML private Button wMinUp;
    @FXML private TextField wSpeedMax;
    @FXML private Button wMaxUp;
    @FXML private TextField rgbSpeedMin;
    @FXML private Button rgbMinDown;
    @FXML private Button rgbMinUp;
    @FXML private TextField rgbSpeedMax;
    @FXML private Button rgbMaxDown;
    @FXML private Button rgbMaxUp;
    @FXML private Button backArrow;

    private ModelData modelData;
    
    public FanSettingsController()
    {
        
    }
    
    @FXML
    public void initialize()
    {
        
    }
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        updateFields();
    }
    
    private void updateFields()
    {
        //text fields
        circSpeedConst.setText(String.valueOf(modelData.getHardwareSettingsData().getCircFanConstSpeed()));
        circSpeedMax.setText(String.valueOf(modelData.getHardwareSettingsData().getCircFanHigh()));
        ventSpeedConst.setText(String.valueOf(modelData.getHardwareSettingsData().getVentFanConstSpeed()));
        ventSpeedMax.setText(String.valueOf(modelData.getHardwareSettingsData().getVentFanHigh()));
        wSpeedMin.setText(String.valueOf(modelData.getHardwareSettingsData().getwFanLow()));
        wSpeedMax.setText(String.valueOf(modelData.getHardwareSettingsData().getwFanHigh()));
        rgbSpeedMin.setText(String.valueOf(modelData.getHardwareSettingsData().getRgbFanLow()));
        rgbSpeedMax.setText(String.valueOf(modelData.getHardwareSettingsData().getRgbFanHigh()));
        //configMode button
        configMode.setSelected(modelData.getConfigModeData().getConfigMode());
        //constant fan toggles
        circFanToggle.setSelected(modelData.getHardwareSettingsData().isCircFanConstant());
        //set the On/Off text
        if(circFanToggle.isSelected()){
            circFanToggle.setText("On");}
        else{circFanToggle.setText("Off");}
        ventFanToggle.setSelected(modelData.getHardwareSettingsData().isVentFanConstant());
        //set the On/Off text
        if(ventFanToggle.isSelected()){
            ventFanToggle.setText("On");}
        else{ventFanToggle.setText("Off");}
    }
    
    @FXML
    void circFanToggleAction(ActionEvent event) {
        if(circFanToggle.isSelected())
        {
            modelData.getHardwareSettingsData().setCircFanConstant(true);
            circFanToggle.setText("On");
        }
        else
        {
            modelData.getHardwareSettingsData().setCircFanConstant(false);
            circFanToggle.setText("Off");
        }
    }

    @FXML
    void circStrengthConstDownAction(ActionEvent event) {
        int speed = Integer.parseInt(circSpeedConst.getText());
        //only do something if its greater than zero
        if(speed>0)
        {
            circSpeedConst.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setCircFanConstSpeed(speed-1);
        }
    }

    @FXML
    void circStrengthConstUpAction(ActionEvent event) {
        int speed = Integer.parseInt(circSpeedConst.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            circSpeedConst.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setCircFanConstSpeed(speed+1);
        }
    }

    @FXML
    void circStrengthMaxDownAction(ActionEvent event) {
        int speed = Integer.parseInt(circSpeedMax.getText());
        //only do something if its greater than zero
        if(speed>0)
        {
            circSpeedMax.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setCircFanHigh(speed-1);
        }
    }

    @FXML
    void circStrengthMaxUpAction(ActionEvent event) {
        int speed = Integer.parseInt(circSpeedMax.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            circSpeedMax.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setCircFanHigh(speed+1);
        }
    }

    @FXML
    void handleBackArrowAction(ActionEvent event) {
        //Close the current stage
        // get a handle to the stage
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        // close this stage
        stage.close();
    }

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
    void rgbMaxDownAction(ActionEvent event) {
        int speed = Integer.parseInt(rgbSpeedMax.getText());
        //only do something if its greater than 0
        if(speed>0)
        {
            rgbSpeedMax.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setRgbFanHigh(speed-1);
        }
    }

    @FXML
    void rgbMaxUpAction(ActionEvent event) {
        int speed = Integer.parseInt(rgbSpeedMax.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            rgbSpeedMax.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setRgbFanHigh(speed+1);
        }
    }

    @FXML
    void rgbMinDownAction(ActionEvent event) {
        int speed = Integer.parseInt(rgbSpeedMin.getText());
        //only do something if its greater than 0
        if(speed>0)
        {
            rgbSpeedMin.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setRgbFanLow(speed-1);
        }
    }

    @FXML
    void rgbMinUpAction(ActionEvent event) {
        int speed = Integer.parseInt(rgbSpeedMin.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            rgbSpeedMin.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setRgbFanLow(speed+1);
        }
    }

    @FXML
    void ventFanToggleAction(ActionEvent event) {
        if(ventFanToggle.isSelected())
        {
            modelData.getHardwareSettingsData().setVentFanConstant(true);
            ventFanToggle.setText("On");
        }
        else
        {
            modelData.getHardwareSettingsData().setVentFanConstant(false);
            ventFanToggle.setText("Off");
        }
    }

    @FXML
    void ventSpeedConstDownAction(ActionEvent event) {
        int speed = Integer.parseInt(ventSpeedConst.getText());
        //only do something if its greater than zero
        if(speed>0)
        {
            ventSpeedConst.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setVentFanConstSpeed(speed-1);
        }
    }

    @FXML
    void ventSpeedConstUpAction(ActionEvent event) {
        int speed = Integer.parseInt(ventSpeedConst.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            ventSpeedConst.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setVentFanConstSpeed(speed+1);
        }
    }

    @FXML
    void ventSpeedMaxDownAction(ActionEvent event) {
        int speed = Integer.parseInt(ventSpeedMax.getText());
        //only do something if its greater than zero
        if(speed>0)
        {
            ventSpeedMax.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setVentFanHigh(speed-1);
        }
    }

    @FXML
    void ventSpeedMaxUpAction(ActionEvent event) {
        int speed = Integer.parseInt(ventSpeedMax.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            ventSpeedMax.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setVentFanHigh(speed+1);
        }
    }

    @FXML
    void wMaxUpAction(ActionEvent event) {
        int speed = Integer.parseInt(wSpeedMax.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            wSpeedMax.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setwFanHigh(speed+1);
        }
    }

    @FXML
    void wMaxDownAction(ActionEvent event)
    {
        int speed = Integer.parseInt(wSpeedMax.getText());
        //only do something if its greater than 0
        if(speed>0)
        {
            wSpeedMax.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setwFanHigh(speed-1);
        }
    }
    
    @FXML
    void wMinDownAction(ActionEvent event) {
        int speed = Integer.parseInt(wSpeedMin.getText());
        //only do something if its greater than 0
        if(speed>0)
        {
            wSpeedMin.setText(Integer.toString(speed-1));
            modelData.getHardwareSettingsData().setwFanLow(speed-1);
        }
    }

    @FXML
    void wMinUpAction(ActionEvent event) {
        int speed = Integer.parseInt(wSpeedMin.getText());
        //only do something if its less than 100
        if(speed<100)
        {
            wSpeedMin.setText(Integer.toString(speed+1));
            modelData.getHardwareSettingsData().setwFanLow(speed+1);
        }
    }
}
