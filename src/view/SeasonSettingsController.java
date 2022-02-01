/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.transform.TransformerException;
import model.ModelData;
import plantronicfx.MainController;
import xml.ClimateReadWriter;

/**
 *
 * @author nimes
// */
public class SeasonSettingsController extends SeasonSettings implements Initializable {
    
    private ModelData modelData;
    private SuccessBoxController successBoxController;
    private int seasonIndex;
    
    @FXML private Button closeButton;
    @FXML private Button submitButton;
    @FXML private Button minTDown;
    @FXML private Button minTUp;
    @FXML private Button maxTDown;
    @FXML private Button maxTUp;
    @FXML private Button minHDown;
    @FXML private Button minHUp;
    @FXML private Button maxHDown;
    @FXML private Button maxHUp;
    @FXML private Button dayStartHrDown;
    @FXML private Button dayStartHrUp;
    @FXML private Button dayStartMinDown;
    @FXML private Button dayStartMinUp;
    @FXML private Button dayEndHrDown;
    @FXML private Button dayEndHrUp;
    @FXML private Button dayEndMinDown;
    @FXML private Button dayEndMinUp;
    @FXML private Button rainDaysDown;
    @FXML private Button rainDaysUp;
    @FXML private TextField minimumTemperature;
    @FXML private TextField maximumTemperature;
    @FXML private TextField minimumHumidity;
    @FXML private TextField maximumHumidity;
    @FXML private TextField dayStartHour;
    @FXML private TextField dayStartMin;
    @FXML private TextField dayEndHour;
    @FXML private TextField dayEndMin;
    @FXML private TextField seasonNameInput;
    @FXML private TextField rainDays;
    @FXML private DatePicker datePicker;
    @FXML private Text invalidEntryT;
    
    
    @Override
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
    }
    
    @Override
    public void autofillSettings(int minTemp, int maxTemp, int minHumidity, int maxHumidity, int startHr, int startMin, int endHr, int endMin, int rDays, LocalDate date, int newSeasonIndex, String seasonName)
    {
        seasonIndex = newSeasonIndex;
        seasonNameInput.setText(seasonName);
        minimumTemperature.setText(String.valueOf(minTemp));
        maximumTemperature.setText(String.valueOf(maxTemp));
        minimumHumidity.setText(String.valueOf(minHumidity));
        maximumHumidity.setText(String.valueOf(maxHumidity));
        dayStartHour.setText(String.valueOf(startHr));
        dayStartMin.setText(String.valueOf(startMin));
        dayEndHour.setText(String.valueOf(endHr));
        dayEndMin.setText(String.valueOf(endMin));
        rainDays.setText(String.valueOf(rDays));
        datePicker.setValue(date);
    }
    @FXML
    @Override
    public void handleCloseButtonAction(ActionEvent event) 
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    @Override
    public void handleSubmitButtonAction(ActionEvent event)
    {
        boolean savedSettings = saveSettings();
   
        if (savedSettings == true)
        {
            try {               
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SuccessBox.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("");
                stage.setScene(new Scene(root));
                stage.show();
                successBoxController = fxmlLoader.getController();
                successBoxController.injectSeasonSettingsController(this);
                successBoxController.injectMessage("Settings saved successfully");
                // Hide this current window (if this is what you want)
                //((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Stage stage = (Stage) closeButton.getScene().getWindow();
            invalidEntryT.setVisible(false);
        }
        else
        {
            invalidEntryT.setVisible(true);
        }
    }
    
    public boolean saveSettings()
    {
        //check if int inputs are actually ints
        if(isNumber(minimumTemperature.getText()) && isNumber(maximumTemperature.getText()) && isNumber(minimumHumidity.getText()) && isNumber(maximumHumidity.getText()) && isNumber(rainDays.getText()))
        {
            //check to make sure hour and minute inputs are valid
            if(isValidHour(dayStartHour.getText()) && isValidHour(dayEndHour.getText()) && isValidMinute(dayStartMin.getText()) && isValidMinute(dayEndMin.getText()))
            {    
                //set temp, humidity and raindays text inputs to ints
                int minT = Integer.parseInt(minimumTemperature.getText());
                int maxT = Integer.parseInt(maximumTemperature.getText());
                int minH = Integer.parseInt(minimumHumidity.getText());
                int maxH = Integer.parseInt(maximumHumidity.getText());
                int rDays = Integer.parseInt(rainDays.getText());
                LocalDate date = datePicker.getValue();
                String seasonName = seasonNameInput.getText();
                
                //if a minimum is greater than a maximum then show input error
                if(rangesOK(minT, maxT, minH, maxH, rDays)==false)
                {
                    return false;
                }
                
                //set time inputs to ints
                int startHr = Integer.parseInt(dayStartHour.getText());
                int startMn = Integer.parseInt(dayStartMin.getText());
                int endHr = Integer.parseInt(dayEndHour.getText());
                int endMn = Integer.parseInt(dayEndMin.getText());
                
                
         
               //set the values in the season class and update the xml file
                try {
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).setName(seasonName);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getTempRange().setMin(minT);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getTempRange().setMax(maxT);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getHumidRange().setMin(minH);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getHumidRange().setMax(maxH);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setStart1(startHr);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setStart2(startMn);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setEnd1(endHr);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setEnd2(endMn);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).setStartDate(date);
                    modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).setRainDays(rDays);
                    ClimateReadWriter climateReadWriter = new ClimateReadWriter();
                    climateReadWriter.writeClimateFile(modelData.getClimateData().getActiveClimate().getName(), modelData.getClimateData().getActiveClimate().getSeasonList());
                } catch (TransformerException ex) {
                    return false;
                }
                return true;    
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    //check make sure settings dont have minimums greater than maximums
    //also check to make sure numbers are within the acceptable range
    public boolean rangesOK(int minT, int maxT, int minH, int maxH, int rDays)
    {
        boolean numbersOK = false;
        if(minT < maxT && minH < maxH)
        {
            if(minT >= 35 && maxT <= 100 && minH >= 30 && maxH <= 100 && rDays >=0 && rDays <= 28)
            {
                numbersOK = true;
            }
        }
        return numbersOK;
    }
    //check if input is an int
    public boolean isNumber(String str)
    {
        try
        {
            Integer.parseInt(str);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    //check if input is 0-23 hours
    public boolean isValidHour(String str)
    {
        int hour;
        try
        {
            hour = Integer.parseInt(str);
        }
        catch (Exception e)
        {
            return false;
        }
        return hour >= 0 && hour < 24;
    }
    
    //check if input is 0-59 minutes
    public boolean isValidMinute(String str)
    {
        int minute;
        try
        {
            minute = Integer.parseInt(str);
        }
        catch (Exception e)
        {
            return false;
        }
        return minute >= 0 && minute < 60;
    }
    
    public void minTDownAction(ActionEvent event)
    {
        String minT = minimumTemperature.getText();
        int minTint = Integer.parseInt(minT);
        minTint = minTint -1;
        minimumTemperature.setText(String.valueOf(minTint));
    }
    
    public void minTUpAction(ActionEvent event)
    {
        String minT = minimumTemperature.getText();
        int minTint = Integer.parseInt(minT);
        minTint = minTint +1;
        minimumTemperature.setText(String.valueOf(minTint));
    }
    
    public void maxTDownAction(ActionEvent event)
    {
        String maxT = maximumTemperature.getText();
        int maxTint = Integer.parseInt(maxT);
        maxTint = maxTint -1;
        maximumTemperature.setText(String.valueOf(maxTint));
    }
    
    public void maxTUpAction(ActionEvent event)
    {
        String maxT = maximumTemperature.getText();
        int maxTint = Integer.parseInt(maxT);
        maxTint = maxTint +1;
        maximumTemperature.setText(String.valueOf(maxTint));
    }
    
    public void minHDownAction(ActionEvent event)
    {
        String minH = minimumHumidity.getText();
        int minHint = Integer.parseInt(minH);
        minHint = minHint -1;
        minimumHumidity.setText(String.valueOf(minHint));
    }
    
    public void minHUpAction(ActionEvent event)
    {
        String minH = minimumHumidity.getText();
        int minHint = Integer.parseInt(minH);
        minHint = minHint +1;
        minimumHumidity.setText(String.valueOf(minHint));
    }
    
    public void maxHDownAction(ActionEvent event)
    {
        String maxH = maximumHumidity.getText();
        int maxHint = Integer.parseInt(maxH);
        maxHint = maxHint -1;
        maximumHumidity.setText(String.valueOf(maxHint));
    }
    
    public void maxHUpAction(ActionEvent event)
    {
        String maxH = maximumHumidity.getText();
        int maxHint = Integer.parseInt(maxH);
        maxHint = maxHint +1;
        maximumHumidity.setText(String.valueOf(maxHint));
    }
    
    public void dayStartHrDownAction(ActionEvent event)
    {
        String dSHr = dayStartHour.getText();
        int dSHrint = Integer.parseInt(dSHr);
        dSHrint = dSHrint -1;
        dayStartHour.setText(String.valueOf(dSHrint));
    }
    
    public void dayStartHrUpAction(ActionEvent event)
    {
        String dSHr = dayStartHour.getText();
        int dSHrint = Integer.parseInt(dSHr);
        dSHrint = dSHrint +1;
        dayStartHour.setText(String.valueOf(dSHrint));
    }
    
    public void dayStartMinDownAction(ActionEvent event)
    {
        String dSMin = dayStartMin.getText();
        int dSMinint = Integer.parseInt(dSMin);
        dSMinint = dSMinint -1;
        dayStartMin.setText(String.valueOf(dSMinint));
    }
    
    public void dayStartMinUpAction(ActionEvent event)
    {
        String dSMin = dayStartMin.getText();
        int dSMinint = Integer.parseInt(dSMin);
        dSMinint = dSMinint +1;
        dayStartMin.setText(String.valueOf(dSMinint));
    }
    
    public void dayEndHrDownAction(ActionEvent event)
    {
        String dEHr = dayEndHour.getText();
        int dEHrint = Integer.parseInt(dEHr);
        dEHrint = dEHrint -1;
        dayEndHour.setText(String.valueOf(dEHrint));
    }
    
    public void dayEndHrUpAction(ActionEvent event)
    {
        String dEHr = dayEndHour.getText();
        int dEHrint = Integer.parseInt(dEHr);
        dEHrint = dEHrint +1;
        dayEndHour.setText(String.valueOf(dEHrint));
    }
    
    public void dayEndMinDownAction(ActionEvent event)
    {
        String dEMin = dayEndMin.getText();
        int dEMinint = Integer.parseInt(dEMin);
        dEMinint = dEMinint -1;
        dayEndMin.setText(String.valueOf(dEMinint));
    }
    
    public void dayEndMinUpAction(ActionEvent event)
    {
        String dEMin = dayEndMin.getText();
        int dEMinint = Integer.parseInt(dEMin);
        dEMinint = dEMinint +1;
        dayEndMin.setText(String.valueOf(dEMinint));
    }
    
    public void rainDaysDownAction(ActionEvent event)
    {
        String rDays = rainDays.getText();
        int rDaysint = Integer.parseInt(rDays);
        rDaysint = rDaysint -1;
        rainDays.setText(String.valueOf(rDaysint));
    }
    
    public void rainDaysUpAction(ActionEvent event)
    {
        String rDays = rainDays.getText();
        int rDaysint = Integer.parseInt(rDays);
        rDaysint = rDaysint +1;
        rainDays.setText(String.valueOf(rDaysint));
    }
    
    @Override
    public Button getCloseButton()
    {
        return closeButton;
    }
    
    @Override
    public void closeWindow(Stage s)
    {
        s.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
}

