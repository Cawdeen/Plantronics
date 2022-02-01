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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;
import weatherfx.ScheduledFunction;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ScheduleSettingsController {
    
    @FXML private RadioButton radialHWFog;
    @FXML private RadioButton radialHWMist;
    @FXML private RadioButton radialHWHeat;
    @FXML private RadioButton radialHWCool;
    @FXML private RadioButton radialHWCirc;
    @FXML private RadioButton radialHWVent;
    @FXML private Button hwStartHrDown;
    @FXML private Button hwStartHrUp;
    @FXML private TextField hwStartHour;
    @FXML private Button hwStartMinDown;
    @FXML private Button hwStartMinUp;
    @FXML private TextField hwStartMin;
    @FXML private TextField hwTime;
    @FXML private Button hwTimeDown;
    @FXML private Button hwTimeUp;
    
    private ScheduledFunction function;
    
    
    public void setFunction(ScheduledFunction function)
    {
        this.function = function;
        updateFields();
    }
    
    private void updateFields()
    {
        hwStartHour.setText(Integer.toString(function.getStartHr()));
        hwStartMin.setText(Integer.toString(function.getStartMn()));
        hwTime.setText(Integer.toString(function.getDurationSec()));
        String type = function.getTypeString();
        updateRadio(type);
    }
    
    private void updateRadio(String type)
    {
        switch (type)
        {
            case "Mist":
                radialHWMist.setSelected(true);
                radialHWFog.setSelected(false);
                radialHWHeat.setSelected(false);
                radialHWCool.setSelected(false);
                radialHWCirc.setSelected(false);
                radialHWVent.setSelected(false);
                break;
            case "Fog":
                radialHWMist.setSelected(false);
                radialHWFog.setSelected(true);
                radialHWHeat.setSelected(false);
                radialHWCool.setSelected(false);
                radialHWCirc.setSelected(false);
                radialHWVent.setSelected(false);
                break;
            case "Heat":
                radialHWMist.setSelected(false);
                radialHWFog.setSelected(false);
                radialHWHeat.setSelected(true);
                radialHWCool.setSelected(false);
                radialHWCirc.setSelected(false);
                radialHWVent.setSelected(false);
                break;
            case "Cooling":
                radialHWMist.setSelected(false);
                radialHWFog.setSelected(false);
                radialHWHeat.setSelected(false);
                radialHWCool.setSelected(true);
                radialHWCirc.setSelected(false);
                radialHWVent.setSelected(false);
                break;
            case "Circulation":
                radialHWMist.setSelected(false);
                radialHWFog.setSelected(false);
                radialHWHeat.setSelected(false);
                radialHWCool.setSelected(false);
                radialHWCirc.setSelected(true);
                radialHWVent.setSelected(false);
                break;
            case "Ventilation":
                radialHWMist.setSelected(false);
                radialHWFog.setSelected(false);
                radialHWHeat.setSelected(false);
                radialHWCool.setSelected(false);
                radialHWCirc.setSelected(false);
                radialHWVent.setSelected(true);
                break;
        }   
    }
    
    @FXML
    void handleBackArrowAction(ActionEvent event)
    {
        //Close the current stage
        // get a handle to the stage
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        // close this stage
        stage.close();
    }
    
    @FXML
    void dayStartHrDownAction(ActionEvent event) {
        int hr = Integer.parseInt(hwStartHour.getText());
        if(hr==0)
        {
            hwStartHour.setText(Integer.toString(23));
            function.setStartHr(23);
        }
        else if(hr>0)
        {
            hwStartHour.setText(Integer.toString(hr-1));
            function.setStartHr(hr-1);
        }
    }

    @FXML
    void dayStartHrUpAction(ActionEvent event) {
        int hr = Integer.parseInt(hwStartHour.getText());
        if(hr==23)
        {
            hwStartHour.setText(Integer.toString(0));
            function.setStartHr(0);
        }
        else if(hr<23)
        {
            hwStartHour.setText(Integer.toString(hr+1));
            function.setStartHr(hr+1);
        }
        
    }

    @FXML
    void dayStartMinDownAction(ActionEvent event) {
        int mn = Integer.parseInt(hwStartMin.getText());
        if(mn==0)
        {
            hwStartMin.setText(Integer.toString(59));
            function.setStartMn(59);
        }
        else if(mn>0)
        {
            hwStartMin.setText(Integer.toString(mn-1));
            function.setStartMn(mn-1);
        }
    }

    @FXML
    void dayStartMinUpAction(ActionEvent event) {
        int mn = Integer.parseInt(hwStartMin.getText());
        if(mn==59)
        {
            hwStartMin.setText(Integer.toString(0));
            function.setStartMn(0);
        }
        else if(mn<59)
        {
            hwStartMin.setText(Integer.toString(mn+1));
            function.setStartMn(mn+1);
        }
    }

    @FXML
    void handleRadioHWCircAction(ActionEvent event) {
        updateRadio("Circulation");
        function.setType(ScheduledFunction.HardwareType.CIRCULATION);
    }

    @FXML
    void handleRadioHWCoolAction(ActionEvent event) {
        updateRadio("Cooling");
        function.setType(ScheduledFunction.HardwareType.COOLING);
    }

    @FXML
    void handleRadioHWFogAction(ActionEvent event) {
        updateRadio("Fog");
        function.setType(ScheduledFunction.HardwareType.FOG);
    }

    @FXML
    void handleRadioHWHeatAction(ActionEvent event) {
        updateRadio("Heat");
        function.setType(ScheduledFunction.HardwareType.HEAT);
    }

    @FXML
    void handleRadioHWMistAction(ActionEvent event) {
        updateRadio("Mist");
        function.setType(ScheduledFunction.HardwareType.MIST);
    }

    @FXML
    void handleRadioHWVentAction(ActionEvent event) {
        updateRadio("Ventilation");
        function.setType(ScheduledFunction.HardwareType.VENTILATION);
    }

    @FXML
    void hwtimeAction(InputMethodEvent event) {
        int time = Integer.parseInt(hwTime.getText());
        if(time<0) //not zero or greater, set back to zero
        {
            hwTime.setText(Integer.toString(0));
            function.setStartHr(0);
        }
        else
        {
            function.setStartHr(time);
        }
    }

    @FXML
    void hwStartHourAction(InputMethodEvent event) {
        int hr = Integer.parseInt(hwStartHour.getText());
        if(hr>59)
        {
            hwStartHour.setText(Integer.toString(59));
            function.setStartHr(59);
        }
        else if(hr<0)
        {
            hwStartHour.setText(Integer.toString(0));
            function.setStartHr(0);
        }
        else
        {
            function.setStartHr(hr);
        }
    }

    @FXML
    void hwStartMinAction(InputMethodEvent event) {
        int mn = Integer.parseInt(hwStartMin.getText());
        if(mn>59)
        {
            hwStartMin.setText(Integer.toString(59));
            function.setStartMn(59);
        }
        else if(mn<0)
        {
            hwStartMin.setText(Integer.toString(0));
            function.setStartMn(0);
        }
        else
        {
            function.setStartMn(mn);
        }
    }
    
    @FXML
    void timeDownAction(ActionEvent event) {
        int time = Integer.parseInt(hwTime.getText());
        if(time==0)
        {
            hwTime.setText(Integer.toString(0));
            function.setDurationSec(0);
        }
        else if(time>0)
        {
            hwTime.setText(Integer.toString(time-1));
            function.setDurationSec(time-1);
        }
    }

    @FXML
    void timeUpAction(ActionEvent event) {
        int time = Integer.parseInt(hwTime.getText());
        if(time < 86398)
        {
            hwTime.setText(Integer.toString(time+1));
            function.setDurationSec(time+1);
        }
    }
    
    
}
