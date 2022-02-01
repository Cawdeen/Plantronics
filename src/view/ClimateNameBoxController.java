/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import climates.Climate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ClimateNameBoxController {

    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private TextField climateNameInput;
    
    private Climate climate;
    
    private ClimateListPanelController climateSettingsPanelController;
    
    public ClimateNameBoxController()
    {
        
    }
    
    public void injectClimateSettingsPanelController(ClimateListPanelController c)
    {
        climateSettingsPanelController = c;
    }
    
    public void injectClimate(Climate c)
    {
        climate = c;
    }
    
    public void autoFillClimateName(String name)
    {
        climateNameInput.setText(name);
    }

    @FXML
    void handleCancelButtonAction(ActionEvent event) 
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSubmitButtonAction(ActionEvent event) 
    {
        String name = climateNameInput.getText();
        climateSettingsPanelController.acceptClimateName(climate, name);  
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

}
