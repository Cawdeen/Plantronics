/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import climates.Climate;
import climates.Season;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.ModelData;
import plantronicfx.MainController;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class SeasonsPanelController {
    
    @FXML private Button addSeasonButton;
    @FXML private Button backArrow;
    @FXML private GridPane seasonListGrid;
    @FXML private Text climateTitle;
        
    private Stage subStage;
    
    private SettingsMenuPanelController settingsMenuPanelController;
    private SeasonSettings seasonSettingsController;
    private final ArrayList<SeasonRowController> seasonRowControllers;
    private SuccessBoxController successBoxController;
    private ArrayList<Season> seasons;
    
    private ModelData modelData;
    
    public SeasonsPanelController()
    {
        seasonRowControllers = new ArrayList<>();
        seasons = new ArrayList<>();
        subStage = new Stage();//to prevent opening more than one instance of season settings window.
        subStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) 
            {
                refreshSeasonList();
            }
        });
    }
    
    @FXML
    public void initialize()
    {
        
    }
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        updateClimate(modelData.getClimateData().getActiveClimate());
        refreshSeasonList();
    }
    
    
    public void refreshSeasonList()
    {
        seasons = modelData.getClimateData().getActiveClimate().getSeasonList();
        GridPane seasonGrid = seasonListGrid;
        seasonListGrid.getChildren().clear();
        seasonRowControllers.clear();
        for(int i = 0; i < seasons.size(); i++)
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SeasonRow.fxml"));
                Pane newSeasonRow = fxmlLoader.load();
                SeasonRowController newC = fxmlLoader.getController();
                seasonRowControllers.add(newC);
                seasonRowControllers.get(i).injectSettingsPanelController(this);
                seasonRowControllers.get(i).updateSeasonDetails(modelData.getClimateData().getActiveClimate(),seasons.get(i));
                seasonGrid.addRow(i, newSeasonRow);
            } catch (IOException ex) {
                Logger.getLogger(SeasonsPanelController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        seasonGrid.setVgap(0);
        seasonGrid.setPadding(new Insets(2, 0, 2, 0));
        seasonListGrid = seasonGrid;
    }
    
    public void updateClimate(Climate climate)
    {
        climateTitle.setText("Active Climate: "+climate.getName());
    }
    
    @FXML
    void handleAddSeasonAction(ActionEvent event)
    {
        modelData.getClimateData().newSeasonRequest();
        refreshSeasonList();
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
    
    public void deleteSeasonRequest(Season s)
    {
        try {               
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SuccessBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner((Stage) addSeasonButton.getScene().getWindow());
            subStage.setTitle("");
            subStage.setScene(new Scene(root));
            subStage.show();
            successBoxController = fxmlLoader.getController();
            if (modelData.getClimateData().deleteSeasonRequest(s) == true)
            {
                successBoxController.injectMessage("Season deleted successfully");
            }
            else
            {
                successBoxController.injectMessage("Cannot have zero seasons");
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshSeasonList();
    }
    
    //pressed edit season0 button
    public void editSeason(ActionEvent event){
            try { 
                int seasonIndex = 0;
                for(int i = 0; i < seasonRowControllers.size(); i++)
                {
                    //if the action button = the edit button in one of the seasonRowControllers (which season row did we click?)
                    if(event.getSource()==seasonRowControllers.get(i).getEditSeason())
                    {
                        seasonIndex = i;
                        break;
                    }
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SeasonSettings.fxml"));
                Parent root = fxmlLoader.load();
                subStage = new Stage();
                subStage.setOnHidden(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) 
                    {
                        refreshSeasonList();
                    }
                });
                subStage.setScene(new Scene(root));
                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner((Stage) addSeasonButton.getScene().getWindow());
                subStage.show();
                seasonSettingsController = fxmlLoader.getController();
                seasonSettingsController.setModel(modelData);
                //pull all the fields from the season
                String name = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getName();
                int minT = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getTempRange().getMin();
                int maxT = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getTempRange().getMax();
                int minH = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getHumidRange().getMin();
                int maxH = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getHumidRange().getMax();
                int startH = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().getStart1();
                int startM = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().getStart2();
                int endH = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().getEnd1();
                int endM = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().getEnd2();
                int rDays = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getRainDays();
                LocalDate d = modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getStartDate();
                seasonSettingsController.autofillSettings(minT, maxT, minH, maxH, startH, startM, endH, endM, rDays, d, seasonIndex, name);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
    } 
}
