package view;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LoadingController {

    @FXML
    private AnchorPane loadingScreen;
    
    @FXML
    private ImageView img;

    /**
     * @return the loadingScreen
     */
    public AnchorPane getLoadingScreen() {
        return loadingScreen;
    }

    /**
     * @return the img
     */
    public ImageView getImg() {
        return img;
    }
    
    
}

