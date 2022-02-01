/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import weatherfx.ScheduleProfile;


public class ScheduleNameBoxController {

    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private TextField profileNameInput;
    
    private ScheduleProfile profile;
    
    private ScheduleProfilesController scheduleProfilesController;
    
    public ScheduleNameBoxController()
    {
        
    }
    
    public void injectScheduleProfilesController(ScheduleProfilesController c)
    {
        scheduleProfilesController = c;
    }
    
    public void injectProfile(ScheduleProfile p)
    {
        profile = p;
        profileNameInput.setText(profile.getName());
    }
    
    public void autoFillProfileName(String name)
    {
        
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
        String name = profileNameInput.getText();
        scheduleProfilesController.acceptProfileName(profile, name);  
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

}
