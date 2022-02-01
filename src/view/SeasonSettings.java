/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.ModelData;
import plantronicfx.MainController;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public abstract class SeasonSettings
{
    public abstract void setModel(ModelData modelData);
    public abstract void autofillSettings(int minTemp, int maxTemp, int minHumidity, int maxHumidity, int startHr, int startMin, int endHr, int endMin, int rainDays, LocalDate date, int newSeasonIndex, String seasonName);
    public abstract void handleCloseButtonAction(ActionEvent event);
    public abstract void handleSubmitButtonAction(ActionEvent event);
    public abstract void closeWindow(Stage s);
    public abstract Button getCloseButton();
}
