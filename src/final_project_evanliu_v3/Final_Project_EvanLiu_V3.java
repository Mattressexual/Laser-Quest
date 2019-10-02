package final_project_evanliu_v3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Final_Project_EvanLiu_V3 extends Application {
    
    private Scene menuScene;
    private Scene howToScene;
    
    @Override
    public void start(Stage stage) {
        
        // Main Menu Scene Layout
        
        StackPane pane = new StackPane();
        
        ImageView background = new ImageView(new Image(FileNames.BACKGROUND, Values.SCREEN_X, Values.SCREEN_Y, false, false));
        
        ImageView title = new ImageView(new Image(FileNames.MENU_TITLE, Values.MENU_TITLE_WIDTH, Values.MENU_TITLE_HEIGHT, false, false));
        
        ImageView playButton = new ImageView(new Image(FileNames.MENU_PLAY, Values.MENU_BUTTON_WIDTH, Values.MENU_BUTTON_HEIGHT, false, false));
        ImageView howToButton = new ImageView(new Image(FileNames.MENU_HOWTO, Values.MENU_BUTTON_WIDTH, Values.MENU_BUTTON_HEIGHT, false, false));
        ImageView quitButton = new ImageView(new Image(FileNames.MENU_QUIT, Values.MENU_BUTTON_WIDTH, Values.MENU_BUTTON_HEIGHT, false, false));
        
        VBox menuPane = new VBox(25);
        menuPane.getChildren().addAll(title, playButton, howToButton, quitButton);
        menuPane.setAlignment(Pos.CENTER);
        
        pane.getChildren().addAll(background, menuPane);
        menuPane.setAlignment(Pos.CENTER);
        
        menuScene = new Scene(pane, Values.SCREEN_X, Values.SCREEN_Y);
        
        ////////////////////////////////////////////////////////////////////////
        // How To Scene Layout
        
        ImageView howToBG = new ImageView(new Image(FileNames.BACKGROUND, Values.SCREEN_X, Values.SCREEN_Y, false, false));
        
        ImageView howToWASD = new ImageView(new Image(FileNames.HOWTO_WASD, Values.HOWTO_WASD_WIDTH, Values.HOWTO_WASD_HEIGHT, false, false));
        ImageView howToArrows = new ImageView(new Image(FileNames.HOWTO_ARROWS, Values.HOWTO_WASD_WIDTH, Values.HOWTO_WASD_HEIGHT, false, false));
        ImageView howToSpaceBar = new ImageView(new Image(FileNames.HOWTO_SPACEBAR, Values.HOWTO_SPACEBAR_WIDTH, Values.HOWTO_SPACEBAR_HEIGHT, false, false));
        
        ImageView howToMove = new ImageView(new Image(FileNames.HOWTO_MOVE_INSTRUCTIONS, Values.HOWTO_MOVE_INSTRUCTIONS_WIDTH, Values.HOWTO_MOVE_INSTRUCTIONS_HEIGHT, false, false));
        ImageView howToPew = new ImageView(new Image(FileNames.HOWTO_PEW_INSTRUCTIONS, Values.HOWTO_PEW_INSTRUCTIONS_WIDTH, Values.HOWTO_PEW_INSTRUCTIONS_HEIGHT, false, false));
        
        ImageView howToBack = new ImageView(new Image(FileNames.HOWTO_BACK, Values.HOWTO_BACK_WIDTH, Values.HOWTO_BACK_HEIGHT, false, false));
        
        howToBack.setOnMouseReleased(e -> {
            if (howToBack.contains(e.getX(), e.getY())) {
                stage.setScene(menuScene);
            }
        });
        
        HBox howToHBox = new HBox(50);
        howToHBox.getChildren().addAll(howToWASD, howToArrows);
        howToHBox.setAlignment(Pos.CENTER);
        
        StackPane howToStack = new StackPane();
        
        VBox howToVBox = new VBox(50);
        howToVBox.getChildren().addAll(howToHBox, howToMove, howToSpaceBar, howToPew, howToBack);
        howToVBox.setAlignment(Pos.CENTER);
        
        howToStack.getChildren().addAll(howToBG, howToVBox);
        
        howToScene = new Scene(howToStack, Values.SCREEN_X, Values.SCREEN_Y);
        
        ////////////////////////////////////////////////////////////////////////
        
        stage.setScene(menuScene);
        stage.show();
        
        playButton.setOnMouseReleased(e -> {
            if (playButton.contains(e.getX(), e.getY())) {
                Engine engine = new Engine();
                stage.setScene(engine.getScene());
            }
        });
        
        howToButton.setOnMouseReleased(e -> {
            if (howToButton.contains(e.getX(), e.getY())) {
                stage.setScene(howToScene);
            }
        });
        
        quitButton.setOnMouseReleased(e -> {
            if (quitButton.contains(e.getX(), e.getY())) {
                Platform.exit();
            }
        });
        
    }

    public static void main(String[] args) {
        launch();
    }
    
}
