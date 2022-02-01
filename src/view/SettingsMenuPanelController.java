package view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ModelData;
import plantronicfx.MainController;

public class SettingsMenuPanelController {

    @FXML private Button climateProfiles;
    @FXML private Button scheduleProfiles;
    @FXML private Button seasonOptions;
    @FXML private Button lightingSettings;
    @FXML private Button scheduling;
    @FXML private Button sensorModuleSettings;
    @FXML private ImageView homeImageOn;
    @FXML private ImageView detailsImageOff;
    @FXML private ImageView settingsImageOff;
    @FXML private Button homePanelButton;
    @FXML private Button detailsPanelButton;
    
    public SettingsMenuPanelController()
    {
        
    }
    
    private ModelData modelData;
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
    }

    @FXML
    void handleHomePanelButtonAction(ActionEvent event)
    {
        try 
        {                              
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/HomePanel.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root); 
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
            HomePanelController homePanelController = fxmlLoader.getController();
            homePanelController.setModel(modelData);
            //homePanelController.setStage(stage);
            //method to call to kill timers in home panel when closed
            stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) 
            {
                System.out.println("home panel hidden");
                homePanelController.shutdown();
            }
            });
            
            //get and close the current stage
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleDetailsPanelButtonAction(ActionEvent event)
    {
        try 
        {                     
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/DetailsPanel.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Details");
            stage.setScene(scene);
            stage.show();
            DetailsPanelController detailsPanelController = fxmlLoader.getController();
            detailsPanelController.setModel(modelData);
            //detailsPanelController.setStage(stage);
            
            //get the current stage
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void climateProfilesAction(ActionEvent event) {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateListPanel.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Climates");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) climateProfiles.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            ClimateListPanelController climateListPanelController = fxmlLoader.getController();
            climateListPanelController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void seasonOptionsAction(ActionEvent event) 
    {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SeasonsPanel.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Seasons");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) seasonOptions.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            SeasonsPanelController seasonsPanelController = fxmlLoader.getController();
            seasonsPanelController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void lightingSettingsAction(ActionEvent event) {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/LightingSettings.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Lighting Settings");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) lightingSettings.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            LightingSettingsController lightingSettingsController = fxmlLoader.getController();
            lightingSettingsController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void fanSettingsAction(ActionEvent event) {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/FanSettings.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Fan Settings");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) lightingSettings.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            FanSettingsController fanSettingsController = fxmlLoader.getController();
            fanSettingsController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void schedulingAction(ActionEvent event) {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/Scheduling.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Scheduling");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) scheduling.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            SchedulingController schedulingController = fxmlLoader.getController();
            schedulingController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void scheduleProfilesAction(ActionEvent event)
    {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ScheduleProfiles.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Schedule Profiles");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) scheduleProfiles.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            ScheduleProfilesController scheduleProfilesController = fxmlLoader.getController();
            scheduleProfilesController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void sensorModuleSettingsAction(ActionEvent event) 
    {
        try 
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SensorModuleSettings.fxml"));
            Parent root = fxmlLoader.load();
            Stage subStage = new Stage();
            subStage.setTitle("Sensor Module");
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) sensorModuleSettings.getScene().getWindow());
            subStage.setScene(new Scene(root));
            subStage.show();
            SensorModuleSettingsController sensorModuleSettingsController = fxmlLoader.getController();
            sensorModuleSettingsController.setModel(modelData);
        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}