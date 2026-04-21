package com.wecib;

import com.wecib.audio.GameMusic;
import com.wecib.ui.SceneManager;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    /** Design baseline: 16:9 widescreen; scaled down to fit inside monitor margins. */
    private static final double PREF_SCENE_WIDTH = 1600;
    private static final double PREF_SCENE_HEIGHT = 900;

    @Override
    public void start(Stage primaryStage) {
        GameMusic.start();

        StackPane root = new StackPane();
        root.getStyleClass().add("root-pane");

        SceneManager sceneManager = new SceneManager(root);

        Rectangle2D vis = Screen.getPrimary().getVisualBounds();
        // Keep the window away from screen edges (dock, menu bar, rounded corners)
        double marginH = 80;
        double marginV = 72;
        double maxW = Math.max(800, vis.getWidth() - 2 * marginH);
        double maxH = Math.max(540, vis.getHeight() - 2 * marginV);
        double scale = Math.min(1.0, Math.min(maxW / PREF_SCENE_WIDTH, maxH / PREF_SCENE_HEIGHT));
        double sceneW = PREF_SCENE_WIDTH * scale;
        double sceneH = PREF_SCENE_HEIGHT * scale;

        Scene scene = new Scene(root, sceneW, sceneH);
        scene.getStylesheets().add(
                getClass().getResource("/com/wecib/style.css").toExternalForm()
        );

        primaryStage.setTitle("The WECIB Card Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(540);

        primaryStage.setX(vis.getMinX() + (vis.getWidth() - sceneW) / 2);
        primaryStage.setY(vis.getMinY() + (vis.getHeight() - sceneH) / 2);

        primaryStage.show();

        sceneManager.showTitleScreen();
    }

    @Override
    public void stop() {
        GameMusic.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
