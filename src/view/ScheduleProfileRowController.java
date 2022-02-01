package view;

import java.awt.MouseInfo;
import java.awt.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weatherfx.ScheduleProfile;

public class ScheduleProfileRowController {

    @FXML private Text scheduleName;
    @FXML private Button scheduleOptions;
    @FXML private MenuItem editName;
    @FXML private MenuItem deleteProfile;
    @FXML private MenuItem setAsActive;
    @FXML private Text functions;
    @FXML private ContextMenu contextMenu;

    private ScheduleProfilesController scheduleProfilesController;
    private ScheduleProfile profile;
    
    public ScheduleProfileRowController()
    {
        
    }

    public void injectScheduleProfilesController(ScheduleProfilesController c)
    {
        scheduleProfilesController = c;
    }
    
    @FXML
    void handleDeleteProfileAction(ActionEvent event)
    {
        scheduleProfilesController.deleteProfile(profile);
    }
    
    @FXML
    void handleEditProfileAction(ActionEvent event)
    {
        scheduleProfilesController.renameProfile(profile);
    }
    
    @FXML void setAsActiveAction(ActionEvent event)
    {
        scheduleProfilesController.setNewActiveProfile(profile);
    }

    void updateProfileDetails() {
        scheduleName.setText(profile.getName());
        functions.setText(String.valueOf(profile.getFunctions().size()));
    }

    void setProfile(ScheduleProfile profile) {
        this.profile = profile;
        updateProfileDetails();
    }
    
     @FXML
    void handleOptionsPoflleAction(ActionEvent event) {
        Stage stage = (Stage) scheduleOptions.getScene().getWindow();
        Point p = MouseInfo.getPointerInfo().getLocation();
        contextMenu.show(stage, p.x, p.y);
    }
}
