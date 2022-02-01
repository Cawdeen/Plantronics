/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import climates.Climate;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.ModelData;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ClimateListPanelController {
    
    @FXML private Button backArrow;
    @FXML private Accordion accordion;
    @FXML private ScrollPane scrollPane;
    
    private ModelData modelData;
    
    private ArrayList<ClimateRowController> climateRows;
    private SuccessBoxController successBoxController;
    private ArrayList<TitledPane> titledPanes;
    private ClimateNameBoxController climateNameBoxController;
    
    private Stage subStage;
    private Boolean renaming;
    
    public ClimateListPanelController()
    {
        titledPanes = new ArrayList<TitledPane>();
        climateRows = new ArrayList<ClimateRowController>();   
        renaming = false;
        subStage = new Stage();
        subStage.initStyle(StageStyle.UNDECORATED);
        subStage.setTitle("");
        subStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) 
            {
                refreshClimates();
            }
        });
    }
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        refreshClimates();
    }
    
    public void setNewActiveClimate(Climate climate)
    {
        modelData.getClimateData().setActiveClimate(climate);
    }
    
    public void refreshClimates()
    {
        ArrayList<Climate> climates = modelData.getClimateData().getListOfClimates();
        //first remove the contents of the accordion, clear the titledpanes, clear the controllers
        accordion.getPanes().removeAll(titledPanes);
        titledPanes.clear();
        climateRows.clear();
        for(int i = 0; i < climates.size(); i++)
        {
            try 
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateRow.fxml"));
                TitledPane newTitledPane = fxmlLoader.load();
                titledPanes.add(newTitledPane);
                climateRows.add(fxmlLoader.getController());
                climateRows.get(i).setClimate(climates.get(i));
                climateRows.get(i).injectClimateSettingsPanelController(this);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ClimateListPanelController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        addSeasons(climates);
        accordion.getPanes().addAll(titledPanes);
        //accordion.setExpandedPane(titledPanes.get(0));
        scrollPane.autosize();
        accordion.autosize();
        
    }
    
    public void addClimateRow(Climate c)
    {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateRow.fxml"));
            TitledPane newTitledPane = fxmlLoader.load();
            titledPanes.add(newTitledPane);
            climateRows.add(fxmlLoader.getController());
            int lastIndex = climateRows.size()-1;
            climateRows.get(lastIndex).setClimate(c);
        } catch (IOException ex) {
            Logger.getLogger(ClimateListPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //accordion stuff
        //accordion stuff
        //accordion stuff
        //accordion stuff
    }
    
    public void addSeasons(ArrayList<Climate> climates)
    {
        
        for(int i = 0; i < climates.size(); i++)
        {
            int numSeasons = climates.get(i).getNumberOfSeasons();
            GridPane seasonGrid = climateRows.get(i).getSeasonGrid();
            GridPane headerGrid = climateRows.get(i).getSeasonHeader();
            GridPane containerGrid = climateRows.get(i).getContainerGrid();
            ArrayList<ClimateRowSeasonController> sCList = new ArrayList<ClimateRowSeasonController>();
            for(int j = 0; j < numSeasons; j++)
            {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateRowSeason.fxml"));
                    GridPane newSeasonRow = fxmlLoader.load();
                    sCList.add(fxmlLoader.getController());
                    seasonGrid.addRow(j, newSeasonRow);
                    //seasonGrid.add(newSeasonRow, 0, j);
                } catch (IOException ex) {
                    Logger.getLogger(ClimateListPanelController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            seasonGrid.setVgap(8);
            seasonGrid.setPadding(new Insets(10, 10, 20, 10));
            climateRows.get(i).setSeasonGrid(seasonGrid);
            containerGrid.getChildren().clear();
            containerGrid.addRow(0, headerGrid);
            containerGrid.addRow(1, seasonGrid);
            climateRows.get(i).setContainerGrid(containerGrid);
            climateRows.get(i).injectSeasonControllers(sCList);
            titledPanes.get(i).setContent(climateRows.get(i).getContainerGrid());
            climateRows.get(i).refreshSeasonLabels();
        }
    }
    
    //passes new climate name on to main panel controller
    public void acceptClimateName(Climate c, String newClimateName)
    {
        if(renaming == true)
        {
            modelData.getClimateData().renameClimate(c, newClimateName);
            renaming = false;
        }
        else
            modelData.getClimateData().newClimateRequest(newClimateName);
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
    public void handleCreateClimateAction(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateNameBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.setScene(new Scene(root));
            subStage.show();
            climateNameBoxController = fxmlLoader.getController();
            climateNameBoxController.injectClimateSettingsPanelController(this);
        } catch (IOException ex) {
            Logger.getLogger(ClimateListPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void renameClimate(Climate c)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ClimateNameBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.setScene(new Scene(root));
            subStage.show();
            climateNameBoxController = fxmlLoader.getController();
            climateNameBoxController.injectClimateSettingsPanelController(this);
            climateNameBoxController.injectClimate(c);
            climateNameBoxController.autoFillClimateName(c.getName());
            renaming = true;
        } catch (IOException ex) {
            Logger.getLogger(ClimateListPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteClimate(Climate c)
    {     
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SuccessBox.fxml"));
            Parent root = fxmlLoader.load();
            subStage.setScene(new Scene(root));
            subStage.show();
            successBoxController = fxmlLoader.getController();
            if (modelData.getClimateData().deleteClimate(c) == true)
            {
                successBoxController.injectMessage("Climate deleted successfully");
            }
            else
            {
                successBoxController.injectMessage("Cannot have zero climates");
            }
        } catch (IOException ex) {
            Logger.getLogger(ClimateListPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
