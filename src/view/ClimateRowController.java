package view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import climates.Climate;

public class ClimateRowController {

    @FXML private GridPane seasonHeaderGrid;
    @FXML private Button climateOpen;
    @FXML private GridPane seasonRowGrid;
    @FXML private Pane seasonRowPane;
    @FXML private GridPane seasonDetailGrid;
    @FXML private Text climateName;
    @FXML private Text climateSeasonsNum;
    @FXML private GridPane seasonGrid;
    @FXML private GridPane seasonHeader;
    @FXML private GridPane containerGrid;
    @FXML private ContextMenu contextMenu;
    
    private ArrayList<ClimateRowSeasonController> sControllers;
    private ClimateListPanelController climateSettingsPanelController;
    
    private Climate climate;
    
     public ClimateRowController()
    {
            sControllers = new ArrayList<ClimateRowSeasonController>();
    }    
    
    @FXML void handleClimateOpenAction(ActionEvent event) 
    {
         Stage stage = (Stage) climateOpen.getScene().getWindow();
         Point p = MouseInfo.getPointerInfo().getLocation();
         contextMenu.show(stage, p.x, p.y);
    }
    
    public void injectClimateSettingsPanelController(ClimateListPanelController c)
    {
        climateSettingsPanelController = c;
    }
    
    @FXML void handleSetClimateAction(ActionEvent event)
    {
        climateSettingsPanelController.setNewActiveClimate(climate);
    }
    
    @FXML void handleRenameClimateAction(ActionEvent event)
    {
        climateSettingsPanelController.renameClimate(climate);
    }
    
    @FXML void handleDeleteClimateAction(ActionEvent event)
    {
        climateSettingsPanelController.deleteClimate(climate);
    }
    
    public void injectSeasonControllers(ArrayList<ClimateRowSeasonController> sC)
    {
        for(int i = 0; i < sC.size(); i++)
        {
            
        }
        sControllers = sC;
    }
    
    public void setClimate(Climate newClimate)
    {
        climate = newClimate;
        setClimateName(climate.getName());
        setClimateSeasonsNum(climate.getNumberOfSeasons());
    }
    
    public void refreshSeasonLabels()
    {
        for(int i = 0; i < sControllers.size(); i++)
        {
            String name = climate.getSeason(i).getName();
            String tempSummary = climate.getSeason(i).getTempsSummary();
            String dayLight = climate.getSeason(i).getDayLightSummary();
            String humidSummary = climate.getSeason(i).getHumiditySummary();
            String rainDays = Integer.toString(climate.getSeason(i).getRainDays());
            sControllers.get(i).updateSeasonFields(name, tempSummary, humidSummary, dayLight, rainDays);
        }
    }
    
    public GridPane getSeasonGrid(){
        return seasonGrid;
    }
    
    public void setSeasonGrid(GridPane newSeasonGrid)
    {
        seasonGrid = newSeasonGrid;
    }

    /**
     * @param ClimateOpen the ClimateOpen to set
     */
    public void setClimateOpen(Button climateOpen) {
        this.climateOpen = climateOpen;
    }

    /**
     * @param climateName the climateName to set
     */
    public void setClimateName(String climateName) {
        this.climateName.setText(climateName);
    }

    /**
     * @param climateSeasonsNum the climateSeasonsNum to set
     */
    public void setClimateSeasonsNum(int climateSeasonsNum) {
        this.climateSeasonsNum.setText(Integer.toString(climateSeasonsNum));
    }

    /**
     * @return the seasonHeader
     */
    public GridPane getSeasonHeader() {
        return seasonHeader;
    }

    /**
     * @return the containerGrid
     */
    public GridPane getContainerGrid() {
        return containerGrid;
    }

    /**
     * @param containerGrid the containerGrid to set
     */
    public void setContainerGrid(GridPane containerGrid) {
        this.containerGrid = containerGrid;
    }
    
}
