/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static lightfx.LightSettings.DayTimes.NIGHT;
import model.ModelData;
import plantronicfx.MainController;
import weatherfx.ForecastCondition;

public class HomePanelController {

    @FXML private Text tempText;
    @FXML private Text humidText;
    @FXML private ImageView currentConImage;
    @FXML private Button detailsPanelButton;
    @FXML private Button settingsMenuPanelButton;
    @FXML private ImageView lightImageOff;
    @FXML private ImageView heatImageOff;
    @FXML private ImageView coolImageOff;
    @FXML private ImageView humidImageOff;
    @FXML private ImageView ventImageOff;
    @FXML private ImageView lightImageOn;
    @FXML private ImageView heatImageOn;
    @FXML private ImageView coolImageOn;
    @FXML private ImageView humidImageOn;
    @FXML private ImageView ventImageOn;
    
    @FXML private ImageView forecast0;
    @FXML private ImageView forecast1;
    @FXML private ImageView forecast2;
    @FXML private ImageView forecast3;
    @FXML private ImageView forecast4;
    @FXML private ImageView forecast5;
    @FXML private ImageView forecast6;
    private ArrayList<ImageView> images;
    private Image clearDay = new Image("view/icon_sunny_black.png");
    private Image clearNight = new Image("view/icon_moon_black.png");
    private Image partlyCloudy = new Image("view/icon_partly_cloudy_black.png");
    private Image partlyCloudyNight = new Image("view/icon_partly_cloudy_night_black.png");
    private Image cloud = new Image("view/icon_cloudy_black.png");
    private Image rain = new Image("view/icon_rain_black.png");
    private Image storms = new Image("view/icon_storms_black.png");
    private Image severeStorms = new Image("view/icon_severe_storms_black.png");
    
    @FXML private Text fcText0;
    @FXML private Text fcText1;
    @FXML private Text fcText2;
    @FXML private Text fcText3;
    @FXML private Text fcText4;
    @FXML private Text fcText5;
    @FXML private Text fcText6;
    
    private Timer secondTimer = new Timer();
    
    private Stage stage;
    
    private ModelData modelData;
    
    public HomePanelController()
    {
        /*
        forecast0 = new ImageView();
        forecast1 = new ImageView();
        forecast2 = new ImageView();
        forecast3 = new ImageView();
        forecast4 = new ImageView();
        forecast5 = new ImageView();
        forecast6 = new ImageView();
        
        images.add(forecast0);
        images.add(forecast1);
        images.add(forecast2);
        images.add(forecast3);
        images.add(forecast4);
        images.add(forecast5);
        images.add(forecast6);
        */
        //stop the timer process in the mainPanelController when the window is closed.
           

    }
    
    @FXML
    public void initialize()
    {
        secondTimer.scheduleAtFixedRate(
        new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(() -> {    
                //the repeated task once a second             
                updateStatus(); //refresh the fields on the screen
                });
            }
        },
        1000,   // run first occurrence immediately
        1000);  // run every second 
    }
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        
    }
    
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    
    public void shutdown()
    {
        secondTimer.cancel();
    }
    
    @FXML
    void detailsPanelButtonAction(ActionEvent event) {
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
            
            //get and close the current stage
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            shutdown();
            thisStage.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void settingsMenuPanelButtonAction(ActionEvent event) {
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
            shutdown();
            thisStage.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateStatus()
    {
        //temp
        tempText.setText(Integer.toString(modelData.getCurrentConditionsData().getCurrentTemp()) + "°");
        //humidity
        humidText.setText(Integer.toString(modelData.getCurrentConditionsData().getCurrentHumidity()) + "%");
        //light
        boolean light = false;
        if(modelData.getConfigModeData().getConfigMode() != true)
        {
        light = (modelData.getLightData().getIsDay(
                modelData.getClimateData().getCurrentSeason().getLightDuration().getStart1(),
                modelData.getClimateData().getCurrentSeason().getLightDuration().getStart2(),
                modelData.getClimateData().getCurrentSeason().getLightDuration().getEnd1(),
                modelData.getClimateData().getCurrentSeason().getLightDuration().getEnd2(),
                modelData.getCurrentConditionsData().getCurrentTime().getHour(),
                modelData.getCurrentConditionsData().getCurrentTime().getMinute()));
        }
        else if(modelData.getConfigModeData().getConfigTime() != NIGHT){
            light = true;
        }
        lightImageOn.setVisible(light);
        lightImageOff.setVisible(!light);
        //heat
        //cooling
        //humidity
        //ventilation        
    }
    
    public void updateStatus(boolean light, boolean heat, boolean cool, boolean humidify, boolean ventilate, String temp, ForecastCondition currentCon, LocalTime sunrise, LocalTime sunset)
    {        
        lightImageOn.setVisible(light);
        lightImageOff.setVisible(!light);       
        heatImageOn.setVisible(heat);
        heatImageOff.setVisible(!heat);
        coolImageOn.setVisible(cool);
        coolImageOff.setVisible(!cool);
        humidImageOn.setVisible(humidify);
        humidImageOff.setVisible(!humidify);
        ventImageOn.setVisible(ventilate);
        ventImageOff.setVisible(!ventilate);
        tempText.setText(temp + "°");
        /*
        fcText0.setText(forecasts.get(0).getStartTimeString());
        fcText1.setText(forecasts.get(1).getStartTimeString());
        fcText2.setText(forecasts.get(2).getStartTimeString());
        fcText3.setText(forecasts.get(3).getStartTimeString());
        fcText4.setText(forecasts.get(4).getStartTimeString());
        fcText5.setText(forecasts.get(5).getStartTimeString());
        fcText6.setText(forecasts.get(6).getStartTimeString());
        */
        int currentRainLvl = currentCon.getRainlvl();
        int currentCloudLvl = currentCon.getCloudCover();
        Image currConditionImage = new Image("view/icon_sunny_black.png");
        switch(currentRainLvl)
        {
            case 0:
                switch(currentCloudLvl)
                {
                    case 0:
                        if(light==true)
                        {
                            currConditionImage = clearDay;
                        }
                        else
                        {
                            currConditionImage = clearNight;
                        }
                        break;
                    case 1:
                        currConditionImage = partlyCloudy;
                        break;
                    case 2:
                        currConditionImage = cloud;
                        break;
                    case 3:
                        currConditionImage = cloud;
                        break;
                }
                break;
            case 1:
                currConditionImage = rain;
                break;
            case 2:
                currConditionImage = storms;
                break;
            case 3:
                currConditionImage = severeStorms;
                break;           
        }
        currentConImage.setImage(currConditionImage);
        /*
        //for updating forecasts on home screen
        ArrayList<Image> images = new ArrayList<Image>();
        for(int i = 0; i < 7; i++)
        {
            int cloudCover = forecasts.get(i).getCloudCover();
            int rainlvl = forecasts.get(i).getRainlvl();
            Image condition = new Image("view/icon_sunny_black.png");
            switch(rainlvl)
            {
                case 0:
                  switch(cloudCover)  
                  {
                      case 0:
                          if(forecasts.get(i).getStartTime().isBefore(sunset))      //before sunset
                          {
                              if(forecasts.get(i).getStartTime().isBefore(sunrise)) //if it's also before sunrise
                              {
                                  condition = clearNight;                           //then it's night (morning)
                              }
                              else
                              {
                                  condition = clearDay;                             //otherwise it's daytime
                              }                             
                          }
                          else                                                      //after sunset
                          {
                              condition = clearNight;                               //night
                          }
                          break;
                      case 1:
                          if(forecasts.get(i).getStartTime().isBefore(sunset))      //before sunset
                          {
                              if(forecasts.get(i).getStartTime().isBefore(sunrise)) //if it's also before sunrise
                              {
                                  condition = partlyCloudyNight;                           //then it's night (morning)
                              }
                              else
                              {
                                  condition = partlyCloudy;                             //otherwise it's daytime
                              }                             
                          }
                          else                                                      //after sunset
                          {
                              condition = partlyCloudyNight;                                //night
                          }
                          break;
                      case 2:
                          condition = cloud;
                          break;
                      case 3:
                          condition = cloud;
                          break;
                   }
                  break;
                case 1:
                    condition = rain;
                    break;
                case 2:
                    condition = storms;
                    break;
                case 3:
                    condition = severeStorms;
                    break;
            }
            images.add(condition);
        }
        forecast0.setImage(images.get(0));
        forecast1.setImage(images.get(1));
        forecast2.setImage(images.get(2));
        forecast3.setImage(images.get(3));
        forecast4.setImage(images.get(4));
        forecast5.setImage(images.get(5));
        forecast6.setImage(images.get(6));
        */
    }
    
/* OLD
    public void updateStatus(boolean light, boolean heat, boolean cool, boolean humidify, boolean ventilate, String temp, ArrayList<ForecastCondition> forecasts, ForecastCondition currentCon, LocalTime sunrise, LocalTime sunset)
    {        
        lightImageOn.setVisible(light);
        lightImageOff.setVisible(!light);       
        heatImageOn.setVisible(heat);
        heatImageOff.setVisible(!heat);
        coolImageOn.setVisible(cool);
        coolImageOff.setVisible(!cool);
        humidImageOn.setVisible(humidify);
        humidImageOff.setVisible(!humidify);
        ventImageOn.setVisible(ventilate);
        ventImageOff.setVisible(!ventilate);
        tempText.setText(temp + "°");
        
        fcText0.setText(forecasts.get(0).getStartTimeString());
        fcText1.setText(forecasts.get(1).getStartTimeString());
        fcText2.setText(forecasts.get(2).getStartTimeString());
        fcText3.setText(forecasts.get(3).getStartTimeString());
        fcText4.setText(forecasts.get(4).getStartTimeString());
        fcText5.setText(forecasts.get(5).getStartTimeString());
        fcText6.setText(forecasts.get(6).getStartTimeString());
        int currentRainLvl = currentCon.getRainlvl();
        int currentCloudLvl = currentCon.getCloudCover();
        Image currConditionImage = new Image("view/icon_sunny_black.png");
        switch(currentRainLvl)
        {
            case 0:
                switch(currentCloudLvl)
                {
                    case 0:
                        if(light==true)
                        {
                            currConditionImage = clearDay;
                        }
                        else
                        {
                            currConditionImage = clearNight;
                        }
                        break;
                    case 1:
                        currConditionImage = partlyCloudy;
                        break;
                    case 2:
                        currConditionImage = cloud;
                        break;
                    case 3:
                        currConditionImage = cloud;
                        break;
                }
                break;
            case 1:
                currConditionImage = rain;
                break;
            case 2:
                currConditionImage = storms;
                break;
            case 3:
                currConditionImage = severeStorms;
                break;           
        }
        currentConImage.setImage(currConditionImage);
        
        ArrayList<Image> images = new ArrayList<Image>();
        for(int i = 0; i < 7; i++)
        {
            int cloudCover = forecasts.get(i).getCloudCover();
            int rainlvl = forecasts.get(i).getRainlvl();
            Image condition = new Image("view/icon_sunny_black.png");
            switch(rainlvl)
            {
                case 0:
                  switch(cloudCover)  
                  {
                      case 0:
                          if(forecasts.get(i).getStartTime().isBefore(sunset))      //before sunset
                          {
                              if(forecasts.get(i).getStartTime().isBefore(sunrise)) //if it's also before sunrise
                              {
                                  condition = clearNight;                           //then it's night (morning)
                              }
                              else
                              {
                                  condition = clearDay;                             //otherwise it's daytime
                              }                             
                          }
                          else                                                      //after sunset
                          {
                              condition = clearNight;                               //night
                          }
                          break;
                      case 1:
                          if(forecasts.get(i).getStartTime().isBefore(sunset))      //before sunset
                          {
                              if(forecasts.get(i).getStartTime().isBefore(sunrise)) //if it's also before sunrise
                              {
                                  condition = partlyCloudyNight;                           //then it's night (morning)
                              }
                              else
                              {
                                  condition = partlyCloudy;                             //otherwise it's daytime
                              }                             
                          }
                          else                                                      //after sunset
                          {
                              condition = partlyCloudyNight;                                //night
                          }
                          break;
                      case 2:
                          condition = cloud;
                          break;
                      case 3:
                          condition = cloud;
                          break;
                   }
                  break;
                case 1:
                    condition = rain;
                    break;
                case 2:
                    condition = storms;
                    break;
                case 3:
                    condition = severeStorms;
                    break;
            }
            images.add(condition);
        }
        forecast0.setImage(images.get(0));
        forecast1.setImage(images.get(1));
        forecast2.setImage(images.get(2));
        forecast3.setImage(images.get(3));
        forecast4.setImage(images.get(4));
        forecast5.setImage(images.get(5));
        forecast6.setImage(images.get(6));
        
    }
*/
}
