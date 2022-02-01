/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.ModelData;
import weatherfx.ScheduleProfile;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ScheduleProfilesController {
    
    @FXML private Button addProfileButton;
    @FXML private Button backArrow;
    @FXML private GridPane profileListGrid;
    
    private ModelData modelData;
    
    private ArrayList<ScheduleProfileRowController> scheduleProfileRows;
    private ArrayList<ScheduleProfile> profiles;
    private SuccessBoxController successBoxController;
    private ArrayList<TitledPane> titledPanes;
    private ScheduleNameBoxController scheduleNameBoxController;
    
    private Stage subStage;
    private Boolean renaming;
    
    public ScheduleProfilesController()
    {
        scheduleProfileRows = new ArrayList<ScheduleProfileRowController>();   
        profiles = new ArrayList<ScheduleProfile>();
        renaming = false;
        subStage = new Stage();
        subStage.initStyle(StageStyle.UNDECORATED);
        subStage.setTitle("");
        subStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) 
            {
                refreshProfiles();
            }
        });
    }
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        refreshProfiles();
    }
    
    public void setNewActiveProfile(ScheduleProfile profile)
    {
        modelData.getScheduledFunctionData().setActiveProfile(profile);
    }
    
    public void refreshProfiles()
    {
        profiles = modelData.getScheduledFunctionData().getProfiles();
        GridPane seasonGrid = profileListGrid;
        profileListGrid.getChildren().clear();
        scheduleProfileRows.clear();
        for(int i = 0; i < profiles.size(); i++)
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ScheduleProfileRow.fxml"));
                Pane newProfileRow = fxmlLoader.load();
                ScheduleProfileRowController newC = fxmlLoader.getController();
                scheduleProfileRows.add(newC);
                scheduleProfileRows.get(i).injectScheduleProfilesController(this);
                scheduleProfileRows.get(i).setProfile(modelData.getScheduledFunctionData().getProfiles().get(i));
                seasonGrid.addRow(i, newProfileRow);
            } catch (IOException ex) {
                Logger.getLogger(SeasonsPanelController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        seasonGrid.setVgap(0);
        seasonGrid.setPadding(new Insets(2, 0, 2, 0));
        profileListGrid = seasonGrid;
        
    }
    
    public void addScheduleRow(ScheduleProfile s)
    {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateRow.fxml"));
            TitledPane newTitledPane = fxmlLoader.load();
            titledPanes.add(newTitledPane);
            scheduleProfileRows.add(fxmlLoader.getController());
            int lastIndex = scheduleProfileRows.size()-1;
            scheduleProfileRows.get(lastIndex).setProfile(s);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleProfilesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //passes new climate name on to main panel controller
    public void acceptProfileName(ScheduleProfile s, String newProfileName)
    {
        if(renaming == true)//renaming an existing profile
        {
            for(int i = 0; i < modelData.getScheduledFunctionData().getProfiles().size(); i++)
            {
                if(modelData.getScheduledFunctionData().getProfiles().get(i).equals(s))
                {
                    modelData.getScheduledFunctionData().getProfiles().get(i).setName(newProfileName);
                    renaming = false;
                }
            }    
        }
        else{ //we arent renaming an existing profile, we are creating a new one
            modelData.getScheduledFunctionData().addNewProfile(newProfileName);
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
    public void handleAddProfileAction(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ScheduleNameBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.setScene(new Scene(root));
            subStage.show();
            scheduleNameBoxController = fxmlLoader.getController();
            scheduleNameBoxController.injectScheduleProfilesController(this);
            renaming = false;
        } catch (IOException ex) {
            Logger.getLogger(ScheduleProfilesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void renameProfile(ScheduleProfile p)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ScheduleNameBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.setScene(new Scene(root));
            subStage.show();
            scheduleNameBoxController = fxmlLoader.getController();
            scheduleNameBoxController.injectScheduleProfilesController(this);
            scheduleNameBoxController.injectProfile(p);
            renaming = true;
        } catch (IOException ex) {
            Logger.getLogger(ScheduleProfilesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteProfile(ScheduleProfile p)
    {     
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SuccessBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.setScene(new Scene(root));
            subStage.show();
            successBoxController = fxmlLoader.getController();
            modelData.getScheduledFunctionData().deleteProfile(p);
            successBoxController.injectMessage("Profile deleted.");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleProfilesController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    void editProfile(ScheduleProfile profile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
