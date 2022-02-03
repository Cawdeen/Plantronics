/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plantronicfx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import view.HomePanelController;
import view.LoadingController;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class PlantronicFX extends Application {
    
    private static MainController mainController;
    @FXML private AnchorPane anchorPane;
    
    @Override
    public void start(Stage stage) throws Exception {
        //load the loading screen
        FXMLLoader loaderLoad = new FXMLLoader(getClass().getClassLoader().getResource("view/Loading.fxml"));
        
        final Stage loadStage = new Stage();
        loadStage.initStyle(StageStyle.UNDECORATED);
        loadStage.setAlwaysOnTop(true);
        Parent rootLoad = loaderLoad.load();
        Scene sceneLoad = new Scene(rootLoad);
        loadStage.setTitle("");
        loadStage.initModality(Modality.WINDOW_MODAL);
        loadStage.initOwner(stage);
        loadStage.setScene(sceneLoad);
        loadStage.show();
        LoadingController loadController = loaderLoad.getController();
        anchorPane = loadController.getLoadingScreen();
        
        Thread taskThread = new Thread(new Runnable() {
        @Override
        public void run() {
            
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("load home screeen");
                        loadHomeScreen(stage);
                        loadStage.toFront();
                    } catch (IOException ex) {
                        Logger.getLogger(PlantronicFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            //loop
            for(int i = 100; i > 0; i --)
            {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PlantronicFX.class.getName()).log(Level.SEVERE, null, ex);
                }
                final float opacity = i/100f;
                System.out.println("opacity: "+opacity);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadStage.toFront();
                        loadStage.setOpacity(opacity);
                    }
                });
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    loadStage.close();
                }
            });
        }
      });
      taskThread.start();
    }
    
    @Override
    public void stop(){
        System.out.println("Application closing.");
        mainController.killTimer();
        Platform.exit();
    }
    
    private void loadHomeScreen(Stage stage) throws IOException
    {
        mainController = new MainController();
        //load the home panel
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/HomePanel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Environment Controller");
        stage.setScene(scene);
        stage.show();
        HomePanelController homePanelController = loader.getController();
        //homePanelController.setStage(stage);
        homePanelController.setModel(mainController.getModelData());
        //mainPanelController.goToDetailsScene();
        //mainPanelController.openSeasonsScene();
        //homePanelController.openHomeScene();
        //mainPanelController.openSettingsScene();
        //stop the timer process in the homePanelController when the window is closed.
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) 
            {
                //mainController.killTimer();
                System.out.println("homepanel stage closed");
                homePanelController.shutdown();
            }
        });
        
        //KeyEvent Listener
        stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                mainController.getModelData().getIoData().getKeyInput().handle(code);
            }
        });
    }    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);     
    }
    
}
