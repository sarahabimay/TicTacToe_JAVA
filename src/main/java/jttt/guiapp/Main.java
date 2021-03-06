package jttt.guiapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import jttt.guiapp.app.GUIApp;
import jttt.guiapp.gamemaker.GUIGameMaker;
import jttt.guiapp.javafxcomponents.GUIView;

public class Main extends Application {
    private final int GUI_WINDOW_HEIGHT = 700;
    private final int GUI_WINDOW_WIDTH = 675;
    private final String TTT_TITLE = "TicTacToe";
    private final String CSS_SCRIPT = "javafxgui.css";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TTT_TITLE);
        Scene scene = new Scene(new StackPane(), GUI_WINDOW_HEIGHT, GUI_WINDOW_WIDTH);
        scene.getStylesheets().add(Main.class.getResource(CSS_SCRIPT).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        GUIApp guiApp = new GUIApp(new GUIGameMaker(), new GUIView(scene));
        guiApp.displayGameOptions();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
