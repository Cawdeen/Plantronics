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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ModelData;
import plantronicfx.MainController;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class DetailsPanelController {
    
    @FXML private Button homePanelButton;
    @FXML private Button settingsMenuPanelButton;
    @FXML private Text minTemp;
    @FXML private Text maxTemp;
    @FXML private Text minHum;
    @FXML private Text maxHum;
    @FXML private Text failedPackets;
    @FXML private Text dayLength;
    @FXML private Text currentSeason;
    @FXML private Text seasonLength;
    @FXML private Text timeToNextSeason;
    @FXML private Text currentDateText;
    @FXML private Text currentTimeText;
    @FXML private Text currentTempText;
    @FXML private Text currentHumidityText;
    @FXML private Text heatingStatus;
    @FXML private Text coolingStatus;
    @FXML private Text humidifierStatus;
    @FXML private Text ventilationStatus;
    @FXML private Text lightStatus;
    @FXML private BarChart<?, ?> chartTemp;
    @FXML private BarChart<?, ?> chartDaylight;
    @FXML private AreaChart<?, ?> chartHumid;
    @FXML private BarChart<?, ?> chartRain;
    @FXML private CategoryAxis xTemp;
    @FXML private NumberAxis yTemp;
    @FXML private CategoryAxis xDaylight;
    @FXML private NumberAxis yDaylight;
    private ModelData modelData;
    //private Stage stage;
    
    public DetailsPanelController()
    {
         
    }
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        refreshGraphTab();
        updateTextFields();
    }
    /*
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    */
    //method to update text fields in this panel
    public void updateTextFields()
    {
        setCurrentSeason(modelData.getClimateData().currentSeasonToString());
        setSeasonLength(modelData.getClimateData().getCurrentSeasonLength());
        setTimeToNextSeason(modelData.getClimateData().getTimeToNextSeason());
        setMinTemp((Integer.toString(modelData.getClimateData().getCurrentSeason().getTempRange().getMin())));
        setMaxTemp((Integer.toString(modelData.getClimateData().getCurrentSeason().getTempRange().getMax())));
        setMinHum((Integer.toString(modelData.getClimateData().getCurrentSeason().getHumidRange().getMin())));
        setMaxHum((Integer.toString(modelData.getClimateData().getCurrentSeason().getHumidRange().getMax()))); 
        setDayLength(modelData.getClimateData().getDayLength());
        setFailedPackets(String.valueOf(modelData.getIoData().getFailedSerialAttempts()));
    }
    
    public void refreshGraphTab()
    {
        //Temperature
        chartTemp.getData().clear();
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Highs");
        ArrayList<Float> monthlyHighTemps = new ArrayList<>();
        monthlyHighTemps = modelData.getClimateData().getActiveClimate().getMonthlysForYear("temperature high");
        
        for(int i = 0; i < monthlyHighTemps.size(); i++)
        {
            dataSeries1.getData().add(new XYChart.Data(getMonthString(i), monthlyHighTemps.get(i))); 
        }      
        
        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Lows");
        ArrayList<Float> monthlyLowTemps = new ArrayList<>();
        monthlyLowTemps = modelData.getClimateData().getActiveClimate().getMonthlysForYear("temperature low");
        
        for(int i = 0; i < monthlyLowTemps.size(); i++)
        {
            dataSeries2.getData().add(new XYChart.Data(getMonthString(i), monthlyLowTemps.get(i))); 
        }
        chartTemp.getData().add(dataSeries1);
        chartTemp.getData().add(dataSeries2);
        chartTemp.setBarGap(.5);
        
        //Daylight
        chartDaylight.getData().clear();
        XYChart.Series dataSeries3 = new XYChart.Series();
        dataSeries3.setName("Daylight");
        ArrayList<Float> monthlyDaylights = new ArrayList<>();
        monthlyDaylights = modelData.getClimateData().getActiveClimate().getMonthlysForYear("daylight");
        
        for(int i = 0; i < monthlyDaylights.size(); i++)
        {
            dataSeries3.getData().add(new XYChart.Data(getMonthString(i), monthlyDaylights.get(i))); 
        }
        chartDaylight.getData().add(dataSeries3);
        chartDaylight.setBarGap(.5);
        
        //Humidity
        chartHumid.getData().clear();
        XYChart.Series dataSeries4 = new XYChart.Series();
        dataSeries4.setName("Highs");
        ArrayList<Float> monthlyHighHumid = new ArrayList<>();
        monthlyHighHumid = modelData.getClimateData().getActiveClimate().getMonthlysForYear("humidity high");
        
        for(int i = 0; i < monthlyHighHumid.size(); i++)
        {
            dataSeries4.getData().add(new XYChart.Data(getMonthString(i), monthlyHighHumid.get(i))); 
        }
        
        XYChart.Series dataSeries5 = new XYChart.Series();
        dataSeries5.setName("Lows");
        ArrayList<Float> monthlyLowHumid = new ArrayList<>();
        monthlyLowHumid = modelData.getClimateData().getActiveClimate().getMonthlysForYear("humidity low");
        
        for(int i = 0; i < monthlyLowHumid.size(); i++)
        {
            dataSeries5.getData().add(new XYChart.Data(getMonthString(i), monthlyLowHumid.get(i))); 
        }
        chartHumid.getData().add(dataSeries4);
        chartHumid.getData().add(dataSeries5);
        //chartHumid.setBarGap(.5);
        
        //RainFall
        chartRain.getData().clear();
        XYChart.Series dataSeries6 = new XYChart.Series();
        dataSeries6.setName("Rain");
        ArrayList<Float> monthlyRainDays = new ArrayList<>();
        monthlyRainDays = modelData.getClimateData().getActiveClimate().getMonthlysForYear("raindays");
        
        for(int i = 0; i < monthlyRainDays.size(); i++)
        {
            dataSeries6.getData().add(new XYChart.Data(getMonthString(i), monthlyRainDays.get(i))); 
        }
        
        chartRain.getData().add(dataSeries6);
        chartRain.setBarGap(.5);
    } 
    
    private String getMonthString(int i)
    {
        String month = "";
            switch (i)
            {
                case 0:
                    month = "Jan";
                    break;
                case 1:
                    month = "Feb";
                    break;
                case 2:
                    month = "Mar";
                    break;
                case 3:
                    month = "Apr";
                    break;
                case 4:
                    month = "May";
                    break;
                case 5:
                    month = "Jun";
                    break;
                case 6:
                    month = "Jul";
                    break;
                case 7:
                    month = "Aug";
                    break;
                case 8:
                    month = "Sep";
                    break;
                case 9:
                    month = "Oct";
                    break;
                case 10:
                    month = "Nov";
                    break;
                case 11:
                    month = "Dec";
                    break;
            }
        return month;
    }
    
    @FXML
    public void handleHomePanelButtonAction(ActionEvent event) 
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
    public void handleSettingsMenuPanelButtonAction(ActionEvent event)
    {
        try 
        {                     
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/SettingsMenuPanel.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Settings Menu");
            stage.setScene(scene);
            stage.show();
            SettingsMenuPanelController settingsMenuPanelController = fxmlLoader.getController();
            settingsMenuPanelController.setModel(modelData);
            
            //get and close the current stage
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @param minTemp the minTemp to set
     */
    public void setMinTemp(String minTemp) {
        this.minTemp.setText(minTemp);
    }

    /**
     * @param maxTemp the maxTemp to set
     */
    public void setMaxTemp(String maxTemp) {
        this.maxTemp.setText(maxTemp);
    }

    /**
     * @param minHum the minHum to set
     */
    public void setMinHum(String minHum) {
        this.minHum.setText(minHum);
    }

    /**
     * @param maxHum the maxHum to set
     */
    public void setMaxHum(String maxHum) {
        this.maxHum.setText(maxHum);
    }

    /**
     * @param dayLength the dayLength to set
     */
    public void setDayLength(String dayLength) {
        this.dayLength.setText(dayLength);
    }

    /**
     * @param currentSeason the currentSeason to set
     */
    public void setCurrentSeason(String currentSeason) {
        this.currentSeason.setText(currentSeason);
    }

    /**
     * @param seasonLength the seasonLength to set
     */
    public void setSeasonLength(String seasonLength) {
        this.seasonLength.setText(seasonLength);
    }

    /**
     * @param timeToNextSeason the timeToNextSeason to set
     */
    public void setTimeToNextSeason(String timeToNextSeason) {
        this.timeToNextSeason.setText(timeToNextSeason);
    }

    /**
     * @param currentDateText the currentDateText to set
     */
    public void setCurrentDateText(String currentDateText) {
        this.currentDateText.setText(currentDateText);
    }

    /**
     * @param currentTimeText the currentTimeText to set
     */
    public void setCurrentTimeText(String currentTimeText) {
        this.currentTimeText.setText(currentTimeText);
    }

    /**
     * @param currentTempText the currentTempText to set
     */
    public void setCurrentTempText(String currentTempText) {
        this.currentTempText.setText(currentTempText);
    }

    /**
     * @param currentHumidityText the currentHumidityText to set
     */
    public void setCurrentHumidityText(String currentHumidityText) {
        this.currentHumidityText.setText(currentHumidityText);
    }

    /**
     * @param heatingStatus the heatingStatus to set
     */
    public void setHeatingStatus(String heatingStatus) {
        this.heatingStatus.setText(heatingStatus);
    }

    /**
     * @param coolingStatus the coolingStatus to set
     */
    public void setCoolingStatus(String coolingStatus) {
        this.coolingStatus.setText(coolingStatus);
    }

    /**
     * @param humidifierStatus the humidifierStatus to set
     */
    public void setHumidifierStatus(String humidifierStatus) {
        this.humidifierStatus.setText(humidifierStatus);
    }

    /**
     * @param ventilationStatus the ventilationStatus to set
     */
    public void setVentilationStatus(String ventilationStatus) {
        this.ventilationStatus.setText(ventilationStatus);
    }

    /**
     * @param lightStatus the lightStatus to set
     */
    public void setLightStatus(String lightStatus) {
        this.lightStatus.setText(lightStatus);
    }

    /**
     * @return the failedPackets
     */
    public Text getFailedPackets() {
        return failedPackets;
    }

    /**
     * @param failedPackets the failedPackets to set
     */
    public void setFailedPackets(String failedPackets) {
        this.failedPackets.setText(failedPackets);
    }
    
    
    
}
