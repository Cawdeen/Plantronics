/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SuccessBoxController implements Initializable{

    @FXML private Button closeButton;
    @FXML private Text message;
    
    private SeasonSettings seasonSettingsController = null;

    @FXML
    void handleCloseButtonAction(ActionEvent event) {
        if(seasonSettingsController != null)
        {
            Stage s = (Stage) seasonSettingsController.getCloseButton().getScene().getWindow();
            seasonSettingsController.closeWindow(s);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
        else
        {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }
    
    public void injectSeasonSettingsController(SeasonSettings sc)
    {
        seasonSettingsController = sc;
    }
    
    public void injectMessage(String m)
    {
        message.setText(m);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
