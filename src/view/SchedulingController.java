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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ModelData;
import weatherfx.ScheduledFunction;
import static weatherfx.ScheduledFunction.HardwareType.*;


public class SchedulingController {

    
    @FXML private ComboBox indexCombo;
    @FXML private Button indexAdd;
    @FXML private Button indexDelete;
    @FXML private Button editFunction;
    @FXML private TextField indexTextField;
    @FXML private ScatterChart<?,?> scheduleChart;
    
    private Stage subStage;

    private ModelData modelData;
    
    public void setModel(ModelData modelData)
    {
        this.modelData = modelData;
        subStage = new Stage();
        updateDropdown();
        updateChart();
    }
    
    @FXML
    public void initialize()
    {
        //x axis
        //NumberAxis timeAxis = (NumberAxis) scheduleChart.getXAxis();
        //timeAxis.setLabel("Time");         
        //y axis
        CategoryAxis yAxis = (CategoryAxis) scheduleChart.getYAxis();
        yAxis.getCategories().setAll(
            "Mist", 
            "Fog",
            "Heat",
            "Cooling",
            "Circulation",
            "Ventilation"
        );
        
        indexTextField.textProperty().addListener((obj, oldVal, newVal) -> {
            if(indexCombo.getValue()!=null) //if function is selected
            {
                //get the index of the selected function
                int index = (int) indexCombo.getValue()-1;
                //set the name of the function in modelData to the text field
                modelData.getScheduledFunctionData().getFunctions().get(index).setName(indexTextField.getText()); 
            }
        });

        
        //Creating the Scatter chart 
        //scheduleChart = new ScatterChart(timeAxis, yAxis); 
    }
    
    private void updateDropdown()
    {
        //new empty list of indexes for the drop down
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        //for loop through the number of scheduled functions
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i ++)
        {
            indexes.add(i+1); //numbers range from 1 - n
        }
        indexCombo.getItems().clear();
        indexCombo.getItems().addAll(indexes);
    }
    
    private void updateChart()
    {
        scheduleChart.getData().clear(); //clear the data
        
        //Mist series
        XYChart.Series mistSeries = new XYChart.Series();
        mistSeries.setName("Mist");
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i++)
        {
            if(modelData.getScheduledFunctionData().getFunctions().get(i).getType()==MIST)//if mist
            {
                //get the start time as a decimal of the hour. (7:30 = 7.5)
                float time = (float)modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()/60f);
                //add the type of the function and the time of the function to the chart
                mistSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(i).getTypeString())); 
            }
        }
        scheduleChart.getData().add(mistSeries);
        //resize the MIST icons
        //take all series
        for (XYChart.Series<?, ?> series : scheduleChart.getData()) {
          //for all series, take date, each data has Node (symbol) for representing point
          for (XYChart.Data<?, ?> data : series.getData()) {
            // this node is StackPane
            StackPane stackPane =  (StackPane) data.getNode();
            stackPane.setPrefWidth(15);
            stackPane.setPrefHeight(15);
          }
        }
        
        
        //Fog series
        XYChart.Series fogSeries = new XYChart.Series();
        fogSeries.setName("Fog");
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i++)
        {
            if(modelData.getScheduledFunctionData().getFunctions().get(i).getType()==FOG)//if fog
            {
                //get the start time as a decimal of the hour. (7:30 = 7.5)
                float time = (float)modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()/60f);
                //add the type of the function and the time of the function to the chart
                fogSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(i).getTypeString())); 
            }
        }
        //Heating series
        XYChart.Series heatSeries = new XYChart.Series();
        heatSeries.setName("Heat");
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i++)
        {
            if(modelData.getScheduledFunctionData().getFunctions().get(i).getType()==HEAT)//if heat
            {
                //get the start time as a decimal of the hour. (7:30 = 7.5)
                float time = (float)modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()/60f);
                //add the type of the function and the time of the function to the chart
                heatSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(i).getTypeString())); 
            }
        }
        //Cooling series
        XYChart.Series coolSeries = new XYChart.Series();
        coolSeries.setName("Cool");
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i++)
        {
            if(modelData.getScheduledFunctionData().getFunctions().get(i).getType()==COOLING)//if cooling
            {
                //get the start time as a decimal of the hour. (7:30 = 7.5)
                float time = (float)modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()/60f);
                //add the type of the function and the time of the function to the chart
                coolSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(i).getTypeString())); 
            }
        }
        //Circulation series
        XYChart.Series circSeries = new XYChart.Series();
        circSeries.setName("Circulation");
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i++)
        {
            if(modelData.getScheduledFunctionData().getFunctions().get(i).getType()==CIRCULATION)//if circulation
            {
                //get the start time as a decimal of the hour. (7:30 = 7.5)
                float time = (float)modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()/60f);
                //add the type of the function and the time of the function to the chart
                circSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(i).getTypeString())); 
            }
        }
        //Ventilation series
        XYChart.Series ventSeries = new XYChart.Series();
        ventSeries.setName("Ventilation");
        for(int i = 0; i < modelData.getScheduledFunctionData().getFunctions().size(); i++)
        {
            if(modelData.getScheduledFunctionData().getFunctions().get(i).getType()==VENTILATION)//if ventilation
            {
                //get the start time as a decimal of the hour. (7:30 = 7.5)
                float time = (float)modelData.getScheduledFunctionData().getFunctions().get(i).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(i).getStartMn()/60f);
                //add the type of the function and the time of the function to the chart
                ventSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(i).getTypeString())); 
            }
        }
        //Selected (Single selected functin)
        XYChart.Series selectedSeries = new XYChart.Series();
        selectedSeries.setName("Selected");
        
        if(indexCombo.getValue()!=null)//something selected
        {
            //get the index of the selected function
            int index = (int) indexCombo.getValue()-1;
            //set the name field of the selected function
            
            //get the start time as a decimal of the hour. (7:30 = 7.5)
            float time = (float)modelData.getScheduledFunctionData().getFunctions().get(index).getStartHr() + (float)(modelData.getScheduledFunctionData().getFunctions().get(index).getStartMn()/60f);
            selectedSeries.getData().add(new XYChart.Data(time, modelData.getScheduledFunctionData().getFunctions().get(index).getTypeString())); 
        }
        
        
        scheduleChart.getData().add(fogSeries);
        scheduleChart.getData().add(heatSeries);
        scheduleChart.getData().add(coolSeries);
        scheduleChart.getData().add(circSeries);
        scheduleChart.getData().add(ventSeries);
        scheduleChart.getData().add(selectedSeries);
    }
    
    @FXML
    void handleBackArrowAction(ActionEvent event)
    {
        //update the schedule file
        modelData.getScheduledFunctionData().updateActiveScheduleFile();
        //Close the current stage
        // get a handle to the stage
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        // close this stage
        stage.close();
    }
    
    @FXML 
    void indexAddAction(ActionEvent event)
    {
        modelData.getScheduledFunctionData().addScheduledFunction();
        updateDropdown();
        updateChart();
    }
    
    @FXML
    void indexDeleteAction(ActionEvent event)
    {
        if(indexCombo.getValue()!=null)//something is selected
        {
            modelData.getScheduledFunctionData().deleteScheduledFunction((int)indexCombo.getValue() -1);//remove the index (-1 to account for list showing 1-n indexes instead of 0-n)
            updateDropdown();
            updateChart();
        }
    }
    
    @FXML
    void selectIndexAction(ActionEvent event)
    {
        if(indexCombo.getValue()!=null)
        {
            //get the index of the selected function
            int index = (int) indexCombo.getValue()-1;
            //set the name field of the selected function
            indexTextField.setText(modelData.getScheduledFunctionData().getFunctions().get(index).getName());
            updateChart();
        }
        else indexTextField.setText(null);
    }
    /*
    @FXML
    void indexTextFieldAction(InputMethodEvent event) {
        if(indexCombo.getValue()!=null) //if function is selected
        {
            //get the index of the selected function
            int index = (int) indexCombo.getValue()-1;
            //set the name of the function in modelData to the text field
            modelData.getScheduledFunctionData().getFunctions().get(index).setName(indexTextField.getText()); 
        }
    }
*/
    /*
    @FXML
    void indexTextFieldAction(KeyEvent event) {
        if(indexCombo.getValue()!=null) //if function is selected
        {
            //get the index of the selected function
            int index = (int) indexCombo.getValue()-1;
            //set the name of the function in modelData to the text field
            modelData.getScheduledFunctionData().getFunctions().get(index).setName(indexTextField.getText()); 
        }
    }
    */
    @FXML
    void editFunctionAction(ActionEvent event)
    {
        if(indexCombo.getValue()!=null) //if function is selected
        {
            try {
                //get the index of the selected function
                int index = (int) indexCombo.getValue()-1;
                //get the scheduled fucntion
                ScheduledFunction function = modelData.getScheduledFunctionData().getFunctions().get(index);
                
                //open the new scene
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ScheduleSettings.fxml"));
                Parent root = fxmlLoader.load();
                subStage = new Stage();
                subStage.setOnHidden(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) 
                    {
                        System.out.println("Update Chart");
                        updateChart();
                        //update the schedule file
                        modelData.getScheduledFunctionData().updateActiveScheduleFile();
                    }
                });
                subStage.setScene(new Scene(root));
                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner((Stage) editFunction.getScene().getWindow());
                subStage.show();
                ScheduleSettingsController scheduleSettingsController = fxmlLoader.getController();
                scheduleSettingsController.setFunction(function);
                
                //set the function to edit
            } catch (IOException ex) {
                Logger.getLogger(SchedulingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

