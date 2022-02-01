package view;

import climates.Climate;
import climates.Season;
import java.awt.MouseInfo;
import java.awt.Point;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SeasonRowController {

    @FXML private Text seasonName;
    @FXML private Button seasonOptions;
    @FXML private MenuItem editSeason;
    @FXML private Text dates;
    @FXML private ContextMenu contextMenu;

    private SeasonsPanelController settingsPanelController;
    private Season season;
    
    public SeasonRowController()
    {
        
    }

    public void injectSettingsPanelController(SeasonsPanelController c)
    {
        settingsPanelController = c;
    }
 
    @FXML
    void handleOptionsSeasonAction(ActionEvent event) {
        Stage stage = (Stage) seasonOptions.getScene().getWindow();
        Point p = MouseInfo.getPointerInfo().getLocation();
        contextMenu.show(stage, p.x, p.y);
    }
    
    @FXML
    void handleEditSeasonAction(ActionEvent event)
    {
        settingsPanelController.editSeason(event);
    }
    
    @FXML
    void handleDeleteSeasonAction(ActionEvent event)
    {
        settingsPanelController.deleteSeasonRequest(season);
    }
    
    public void updateSeasonDetails(Climate c, Season s)
    {
        seasonName.setText(s.getName());
        season = s;
        String dateRange = "";
        int startMonth = s.getStartDate().getMonthValue();
        int startDay = s.getStartDate().getDayOfMonth();
        ArrayList<Season> seasons = c.getSeasonOrder();
        Season s2 = new Season();
        for(int i = 0; i < seasons.size(); i++)
        {
            //if we found our season
            if(s == seasons.get(i)) 
            {
                if(i == seasons.size()-1) //if last seasons
                {
                    s2 = seasons.get(0);  //then next season is first in list
                }
                else
                {
                    s2 = seasons.get(i+1); //next season in list
                }
                break;
            }
        }
        LocalDate endDate = s2.getStartDate().minusDays(1);
        int endMonth = endDate.getMonthValue();
        int endDay = endDate.getDayOfMonth();
        dateRange += getMonth(startMonth);
        dateRange += (" "+startDay+" - ");
        dateRange += getMonth(endMonth);
        dateRange += (" "+endDay);
        dates.setText(dateRange);
        
    }
    String getMonth(int monthNum)
    {
        String mo = "";
        switch (monthNum){
            case 1: 
                mo = "Jan";
                break;
            case 2: 
                mo = "Feb";
                break;
            case 3: 
                mo = "Mar";
                break;
            case 4: 
                mo = "Apr";
                break;
            case 5: 
                mo = "May";
                break;
            case 6: 
                mo = "June";
                break;
            case 7: 
                mo = "July";
                break;
            case 8: 
                mo = "Aug";
                break;
            case 9: 
                mo = "Sept";
                break;
            case 10: 
                mo = "Oct";
                break;
            case 11: 
                mo = "Nov";
                break;
            case 12: 
                mo = "Dec";
                break;
        }
        return mo;
    }
    /**
     * @return the editSeason
     */
    public MenuItem getEditSeason() {
        return editSeason;
    }
}
