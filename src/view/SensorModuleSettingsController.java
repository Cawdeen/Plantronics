/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author colli
 */

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ModelData;

public class SensorModuleSettingsController {
    
    @FXML private Button backArrow;
    @FXML private Text failedPackets;
    @FXML private Text lastPacket;
    @FXML private Text lastGoodPacket;
    @FXML private Text timeSinceLastGood;
    
    private ModelData modelData;
    
    private Timer secondTimer = new Timer();
    
    public SensorModuleSettingsController()
    {
        
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
                refreshView(); //refresh the fields on the screen
                });
            }
        },
        1000,   // run first occurrence immediately
        1000);  // run every second 
    }
    
    /**
     * Public method to set the modelData of the view.
     * @param modelData 
     */
    public void setModel(ModelData modelData)
    {
        this.setModelData(modelData);
        refreshView();
    }
    
    /**
     * Event handler for back arrow button. Closes the stage.
     * @param event 
     */
    @FXML
    void handleBackArrowAction(ActionEvent event) {
        //Close the current stage
        // get a handle to the stage
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        // close this stage
        stage.close();
    }
    
    private void refreshView()
    {
        setFailedPackets(Integer.toString(getModelData().getIoData().getFailedSerialAttempts()));
        setLastPacket(modelData.getIoData().getLastPacket().toString());
        setLastGoodPacket(modelData.getIoData().getLastGoodPacket().toString());
    }

    /**
     * @param failedPackets the failedPackets to set
     */
    public void setFailedPackets(String failedPackets) {
        this.failedPackets.setText(failedPackets);
    }

    /**
     * @return the modelData
     */
    public ModelData getModelData() {
        return modelData;
    }

    /**
     * @param modelData the modelData to set
     */
    public void setModelData(ModelData modelData) {
        this.modelData = modelData;
    }

    /**
     * @param lastPacket the lastPacket to set
     */
    public void setLastPacket(String lastPacket) {
        this.lastPacket.setText(lastPacket);
    }

    /**
     * @param lastGoodPacket the lastGoodPacket to set
     */
    public void setLastGoodPacket(String lastGoodPacket) {
        this.lastGoodPacket.setText(lastGoodPacket);
    }

    /**
     * @param timeSinceLastGood the timeSinceLastGood to set
     */
    public void setTimeSinceLastGood(LocalTime lastGoodTime) {
        //get difference between now and the time of the last packet in minutes
        String timeSinceLastGood = String.valueOf(LocalTime.now().until(lastGoodTime, ChronoUnit.MINUTES));
        timeSinceLastGood = timeSinceLastGood + " minutes.";
        this.timeSinceLastGood.setText(timeSinceLastGood);
    }
    
}
