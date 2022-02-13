/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plantronicfx;

import climates.Climate;
import climates.ClimateData;
import climates.Season;
import com.fazecast.jSerialComm.SerialPort;
import io.HardwareTimer;
import io.SensorModule;
import io.SerialPacket;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lightfx.LightUpdater;
import lightfx.LightValues;
import model.ModelData;
import weatherfx.ScheduledFunction;

/**
 *Main controller of the application. Contains a timer task that executes once a second.
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class MainController {
    
    private Stage stage;
    private Parent root;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private final Timer secondTimer = new Timer();
    private int timerIndex = 0;
    private LocalTime lastScheduleTime = LocalTime.now();
    
    private ModelData modelData;
    
    ExecutorService executorService;
    ExecutorService executorServiceHardware;
    private boolean ventilating;
    private boolean misting;
    private boolean highTemp;
    private boolean highHumid;
    private boolean lowHumid;
    private boolean correctionDelay = false;
    
    //ClimateReadWriter climateReadWrite;
    
    boolean pinout = false;
    
    public MainController()
    {        
        modelData = new ModelData(pinout);  
        executorService = Executors.newFixedThreadPool(2);
        executorServiceHardware = Executors.newFixedThreadPool(6);

        //create timer for updating clock, current status etc.
        secondTimer.scheduleAtFixedRate(
        new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(() -> {    
                //the repeated task
                modelData.getIoData().getPinout().PWMOutput();
                switch(timerIndex)
                {
                    case 0:                           
                        //SensorModule reading start
                        
                        if(modelData.getIoData().getLastPacket().isSuccess() == false){ //if last packet was not a success
                            performSensorTask();
                        }
                        //last one was success and greater than 5 seconds ago
                        else if(modelData.getIoData().getLastPacket().isSuccess() == true && modelData.getIoData().getSecondsSincePacketSuccess()>5)
                        {
                            performSensorTask();
                        }
                        
                        timerIndex ++;
                        break;
                    case 1:
                        //Lighting
                        
                        updateLighting();
                        timerIndex ++;
                        break;
                    case 2:
                        if(correctionDelay == false)
                        {
                            createCorrectionDelayTimer();
                        }
                        timerIndex ++;
                        break;
                    case 3:
                        //check for scheduled tasks
                        checkSchedule();
                        timerIndex = 0;
                        break;
                }
                });
            }
        },
        2000,   // run first occurrence 
        1000);  // run every second    
    }
    
    private void createCorrectionDelayTimer()
    {
        //create a task that waits for the proper delay. On completion it will check for necessary correction for temp and humidity.
        Task<Void> correctionTimer = new HardwareTimer(modelData.getHardwareSettingsData().getCorrectionDelayHumidity());
            
            correctionTimer.setOnRunning((succeededEvent) -> {
                correctionDelay = true;
            });
            correctionTimer.setOnSucceeded((succeededEvent) -> {
            //check for corrections when complete
                checkForConditionsCorrection();
                correctionDelay = false;
            });
            executorServiceHardware.execute(correctionTimer);
    }
    
    private void performSensorTask()
    {
        Task<SerialPacket> sensorModule = new SensorModule();
        
        //success of reading serial data from sensor module
        sensorModule.setOnSucceeded((succeededEvent) -> {
            SerialPacket sp = sensorModule.getValue();
            if(sp.isSuccess())
            {
                modelData.getCurrentConditionsData().setCurrentTemp((int)sp.getTempSHT(), (int)sp.getTempDHT()); //set temp (convert to int)
                modelData.getCurrentConditionsData().setCurrentHumidity((int)sp.getHumiditySHT(), (int)sp.getHumidityDHT()); //set humidity (convert to int)
                modelData.getIoData().setLastPacket(sp);
            }
        });

        sensorModule.setOnRunning((succeededEvent) -> {
            SerialPacket sp = sensorModule.getValue();
            
        });

        sensorModule.setOnFailed(e -> {
            SerialPacket sp = sensorModule.getValue();
            //Throwable problem = sensorModule.getException();
            modelData.getIoData().setLastPacket(sp);
            //set current conditions to last good packet (needed for Windows mode or when not using pinout)
            modelData.getCurrentConditionsData().setCurrentTemp((int)modelData.getIoData().getLastGoodPacket().getTempSHT(), (int)modelData.getIoData().getLastGoodPacket().getTempDHT()); //set temp (convert to int)
            modelData.getCurrentConditionsData().setCurrentHumidity((int)modelData.getIoData().getLastGoodPacket().getHumiditySHT(), (int)modelData.getIoData().getLastGoodPacket().getHumidityDHT()); //set humidity (convert to int)
        });
        executorService.execute(sensorModule);
    }
    
    public void updateLighting() 
    {
        Task<LightValues> lightUpdater = new LightUpdater(modelData);

        lightUpdater.setOnSucceeded((succeededEvent) -> {
            LightValues lv = lightUpdater.getValue();
            //set white, R,G,B, light values in the pinout object
            modelData.getIoData().getPinout().setPwmW(lv.getWhite());
            modelData.getIoData().getPinout().setPwmR(lv.getRed());
            modelData.getIoData().getPinout().setPwmG(lv.getGreen());
            modelData.getIoData().getPinout().setPwmB(lv.getBlue());
            
            modelData.getIoData().getPinout().setPwmWFan(lv.getWhite());//set white channel fan pwm same as white light pwm
            int rgbAvg = (lv.getRed() + lv.getGreen() + lv.getBlue())/3; //average the three rgb light channels
            modelData.getIoData().getPinout().setPwmRGBFan(rgbAvg); //set the rgb fan value to the average     
        });
        
        lightUpdater.setOnFailed(e -> {
            
        });
        
        lightUpdater.setOnRunning((succeededEvent) -> {
            
        });
        executorService.execute(lightUpdater);
        //modelData.getIoData().getPinout().PWMOutput();
    }
    
    private void checkForConditionsCorrection()
    {
        //ventilation is checking for high temps first, then high humidity if the first check was false.
        highTemp = modelData.getCurrentConditionsData().getTempControl().getCooling(
                modelData.getClimateData().getCurrentSeason().getTempRange().getMax(), modelData.getCurrentConditionsData().getCurrentTemp());
               
        //ventilation is also checking for high humidity
        highHumid = modelData.getCurrentConditionsData().getHumidityControl().getVentilating(
                modelData.getClimateData().getCurrentSeason().getHumidRange().getMax(),modelData.getCurrentConditionsData().getCurrentHumidity());

        //misting is checking for low humidity
        lowHumid = modelData.getCurrentConditionsData().getHumidityControl().getHumidifying(
                    modelData.getClimateData().getCurrentSeason().getHumidRange().getMin(),modelData.getCurrentConditionsData().getCurrentHumidity());

        createCorrectionTask();//create the timed tasks
    }
    
    private void createCorrectionTask()
    {
        if(lowHumid)
        {
            Task<Void> hardwareTimer = new HardwareTimer(modelData.getHardwareSettingsData().getCorrectionTimeHumidity());
            
            hardwareTimer.setOnRunning((succeededEvent) -> {
            //turn onthe hardware 
                modelData.getIoData().getPinout().setMisting(true);
            });
            hardwareTimer.setOnSucceeded((succeededEvent) -> {
            //turn off the hardware 
                modelData.getIoData().getPinout().setMisting(false);
                lowHumid = false;
            });
            executorServiceHardware.execute(hardwareTimer);
        }
        //ventilating because of high temps
        if(highTemp)//ventilating because of high temps
        {
            Task<Void> hardwareTimer = new HardwareTimer(modelData.getHardwareSettingsData().getCorrectionTimeTemp());
            
            hardwareTimer.setOnRunning((succeededEvent) -> {
            //turn onthe hardware 
                modelData.getIoData().getPinout().setVentToHigh();
            });
            hardwareTimer.setOnSucceeded((succeededEvent) -> {
            //turn off the hardware 
               
                modelData.getIoData().getPinout().setVentToLow();
                highTemp = false;
            });
            executorServiceHardware.execute(hardwareTimer);
        }
        //ventilating because of high humidity
        else if(highHumid)//ventilating because of high humidity
        {
            Task<Void> hardwareTimer = new HardwareTimer(modelData.getHardwareSettingsData().getCorrectionTimeHumidity());
            
            hardwareTimer.setOnRunning((succeededEvent) -> {
            //turn onthe hardware 
                modelData.getIoData().getPinout().setVentToHigh();
            });
            hardwareTimer.setOnSucceeded((succeededEvent) -> {
            //turn off the hardware 
                modelData.getIoData().getPinout().setVentToLow();
                highHumid = false;
            });
            executorServiceHardware.execute(hardwareTimer);
        }
    }
    
    private void checkSchedule()
    {
        //the last scheduled function time wasnt this minute and there are functions
        if(lastScheduleTime.getMinute()!=LocalTime.now().getMinute() && modelData.getScheduledFunctionData().getFunctions().size()>0)
        {
            //iterate through all the functions
            for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i ++)
            {
                //matches this hour
                if(modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr()==LocalTime.now().getHour())
                {
                    //matches this minute
                    if(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()==LocalTime.now().getMinute())
                    {
                        if(modelData.getScheduledFunctionData().checkFunctionCompatibility(modelData.getScheduledFunctionData().getFunctions().get(i),highTemp,lowHumid,highHumid))
                        {
                            //create the scheduleTask thread
                            createScheduleTask(modelData.getScheduledFunctionData().getFunctions().get(i));
                            lastScheduleTime = LocalTime.now();
                        }
                    }
                }
            }
        }
    }
    
    
    
    private void createScheduleTask(ScheduledFunction sf)
    {
        Task<Void> runningHardware = new HardwareTimer(sf.getDurationSec());
        
        runningHardware.setOnRunning((succeededEvent) -> {
            //turn onthe hardware 
            switch(sf.getType())
            {
                case MIST:
                    modelData.getIoData().getPinout().setMisting(true);
                    break;
                case FOG:
                    modelData.getIoData().getPinout().setFogging(true);
                    break;
                case HEAT:
                    break;
                case COOLING:
                    break;
                case VENTILATION:
                    modelData.getIoData().getPinout().setVentToHigh();
                    break;
                case CIRCULATION:
                    modelData.getIoData().getPinout().setCircToHigh();
                    break;
            }
        });
        runningHardware.setOnSucceeded((succeededEvent) -> {
            //turn off the hardware 
            switch(sf.getType())
            {
                case MIST:
                    modelData.getIoData().getPinout().setMisting(false);
                    break;
                case FOG:
                    modelData.getIoData().getPinout().setFogging(false);
                    break;
                case HEAT:
                    break;
                case COOLING:
                    break;
                case VENTILATION:
                    modelData.getIoData().getPinout().setVentToLow();
                    break;
                case CIRCULATION:
                    modelData.getIoData().getPinout().setCircToLow();
                    break;
            }
        });
        executorServiceHardware.execute(runningHardware);
    }

   
    
/*    
    public void updateHeatCooling()
    {
        if(cooling)
        {
            model.getEvControl().getPinIOOutput().setPwmCool(100);
        }
        else
        {
            model.getEvControl().getPinIOOutput().setPwmCool(0);
        } 
    }
    
    public void updateSettingsPanel()
    {
        settingsPanelController.addCLimates(model.getEvControl().getListOfClimates());
        settingsPanelController.insertHardwareSettings(model.getEvControl().getHardwareSettings());
        settingsPanelController.insertWeatherSettings(model.getEvControl().getWeatherController().getWeatherSettings());
    }
*/   
    
/*
FROM BEFORE REFACTOR
    public void updateSeasonsPanel()
    {
        seasonsPanelController.updateClimate(climateData.getActiveClimate());
        seasonsPanelController.refreshSeasonList();
    }
*/

    
    //called from SeasonSettings controllers. Passes all user input values back to this controller to put in model classes and 
    //then pulls from model classes (updateCCPanel();) to update the main panel with the new settings.
    /*
    public boolean insertSeasonSettings(int seasonIndex, String newSeasonName, String minTText, String maxTText, String minHText, String maxHText, String startHrText, String startMnText, String endHrText, String endMnText, String rainDays, LocalDate date)
    {
        //check if int inputs are actually ints
        if(isNumber(minTText) && isNumber(maxTText) && isNumber(minHText) && isNumber(maxHText) && isNumber(rainDays))
        {
            //check to make sure hour and minute inputs are valid
            if(isValidHour(startHrText) && isValidHour(endHrText) && isValidMinute(startMnText) && isValidMinute(endMnText))
            {    
                //set temp, humidity and raindays text inputs to ints
                int minT = Integer.parseInt(minTText);
                int maxT = Integer.parseInt(maxTText);
                int minH = Integer.parseInt(minHText);
                int maxH = Integer.parseInt(maxHText);
                int rDays = Integer.parseInt(rainDays);
                
                //if a minimum is greater than a maximum then show input error
                if(rangesOK(minT, maxT, minH, maxH, rDays)==false)
                {
                    return false;
                }
                
                //set time inputs to ints
                int startHr = Integer.parseInt(startHrText);
                int startMn = Integer.parseInt(startMnText);
                int endHr = Integer.parseInt(endHrText);
                int endMn = Integer.parseInt(endMnText);
                
                //set the values in the season class
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).setName(newSeasonName);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getTempRange().setMin(minT);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getTempRange().setMax(maxT);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getHumidRange().setMin(minH);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getHumidRange().setMax(maxH);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setStart1(startHr);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setStart2(startMn);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setEnd1(endHr);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).getLightDuration().setEnd2(endMn);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).setStartDate(date);
                modelData.getClimateData().getActiveClimate().getSeason(seasonIndex).setRainDays(rDays);
               
                closedSeasonSettings();
                try {
                    climateReadWrite.writeClimateFile(modelData.getClimateData().getActiveClimate().getName(), modelData.getClimateData().getActiveClimate().getSeasonList());
                } catch (TransformerException ex) {
                    return false;
                }
                
                //Commented out temporarily for refactor
                //updateCCPanel();

//TEMP FOR REFACTOR                
//                updateSeasonsPanel();
//                updateSettingsPanel();
                return true;    
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
 */
    
    /*Temp commented until refactor. Was called every second
    //method to update the pane that shows current settings
    public void updateCCPanel()
    {
        detailsPanelController.setCurrentSeason(getModel().getEvControl().currentSeasonToString());
        detailsPanelController.setSeasonLength(getModel().getEvControl().getCurrentSeasonLength());
        detailsPanelController.setTimeToNextSeason(getModel().getEvControl().getTimeToNextSeason());
        detailsPanelController.setMinTemp((Integer.toString(getModel().getEvControl().getCurrentSeason().getTempRange().getMin())));
        detailsPanelController.setMaxTemp((Integer.toString(getModel().getEvControl().getCurrentSeason().getTempRange().getMax())));
        detailsPanelController.setMinHum((Integer.toString(getModel().getEvControl().getCurrentSeason().getHumidRange().getMin())));
        detailsPanelController.setMaxHum((Integer.toString(getModel().getEvControl().getCurrentSeason().getHumidRange().getMax()))); 
        detailsPanelController.setDayLength(calculateDayLength());
        detailsPanelController.setCurrentTempText(Integer.toString(getModel().getEvControl().getCurrentConditions().getCurrentTemp()));
        detailsPanelController.setCurrentHumidityText(Integer.toString(getModel().getEvControl().getCurrentConditions().getCurrentHumidity()));
        getStage().show();
    }
    */
    
    /*  REMOVED AND PLACED IN CLIMATE
    //calculate the length of daylight for current settings pane
    public String calculateDayLength() //String
    {
        String dayLength = "";
        int startHr = getModel().getEvControl().getCurrentSeason().getLightDuration().getStart1();
        int startMn = getModel().getEvControl().getCurrentSeason().getLightDuration().getStart2();
        int endHr = getModel().getEvControl().getCurrentSeason().getLightDuration().getEnd1();
        int endMn = getModel().getEvControl().getCurrentSeason().getLightDuration().getEnd2();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String startTime = String.valueOf(startHr) + ":" + String.valueOf(startMn);
        String endTime = String.valueOf(endHr) + ":" + String.valueOf(endMn);
        try {
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            //if the end time is less than the start time, then the end time must be the next day. Subtract the difference from a full day length to get true elapsed.
            if (elapsed < 0)
            {
                elapsed = 86400000 + elapsed;
            }
            int hours = (int) Math.floor(elapsed / 3600000);
            int minutes = (int) Math.floor((elapsed - hours * 3600000) / 60000);
            dayLength = String.valueOf(hours + " hours, " + minutes + " minutes");
          
        } catch (ParseException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dayLength;
    }
    */
    
    //kill the timer. Called when the window is closed. Called from EnvironmentControlSprint2FXML.java
    public void killTimer()
    {
        secondTimer.cancel();
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
     public void insertStage(Stage s)
    {
        stage = s;
    }
/*     
    public void updateHardwareSettings(HardwareSettings hs)
    {
        model.getEvControl().setHardwareSettings(hs);
    }
    
    public void updateWeatherSettings(WeatherSettings ws)
    {
        model.getEvControl().getWeatherController().setWeatherSettings(ws);
    }
    
    public HardwareSettings getHardwareSettings()
    {
        return model.getEvControl().getHardwareSettings();
    }
*/     
    public void newSeasonRequest()
    {
        getClimateData().newSeasonRequest();
//        updateSeasonsPanel();
//        updateSettingsPanel();
    }
    
    public Boolean deleteSeasonRequest(Season s)
    {
        Boolean result = getModelData().getClimateData().deleteSeasonRequest(s);
//TEMP FOR REFACTOR
//        updateSeasonsPanel();
//        updateSettingsPanel();
        return result;
    }
    
    //new climate request takes in name and creates new climate
    public void newClimateRequest(String name)
    {
        getModelData().getClimateData().newClimateRequest(name);
//TEMP FOR REFACTOR
//        settingsPanelController.addCLimates(model.getEvControl().getListOfClimates());
    }
    
    public Boolean deleteClimate(Climate c)
    {
        Boolean result = getModelData().getClimateData().deleteClimate(c);
//TEMP FOR REFACTOR
//        settingsPanelController.addCLimates(model.getEvControl().getListOfClimates());
        return result;
    }
    
    public void renameClimate(Climate c, String name)
    {
        getModelData().getClimateData().renameClimate(c, name);
//TEMP FOR REFACTOR        
//        settingsPanelController.addCLimates(model.getEvControl().getListOfClimates());
    }
    
    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param activeClimate the activeClimate to set
     */
    public void setActiveClimate(Climate activeClimate) {
        getModelData().getClimateData().setActiveClimate(activeClimate);
    }

    /**
     * @return the climateData
     */
    public ClimateData getClimateData() {
        return getModelData().getClimateData();
    }

    /**
     * @return the modelData
     */
    public ModelData getModelData() {
        return modelData;
    }

}