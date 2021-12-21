package agh.ics.oop.gui;

import agh.ics.oop.RectangularMap;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.StartParameters;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.*;

public class App extends Application {
    private RectangularMap rectangularMap;
    private Stage stage;
    private EventHandler<ActionEvent> event;
    public StartParameters startParameters;
    private Thread engineThread;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = new Stage();
        parameterScene();
    }

    private void openAnimationWindow() {
        SimulationEngine engine = new SimulationEngine(startParameters, this);
        engineThread = new Thread(engine);
        engineThread.start();
    }

    private void parameterScene() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        String[] names = {
                "Map Height", //0
                "Map Width", //1
                "Jungle Height", //2
                "Jungle Width", //3
                "Energy Of every Plant", //4
                "Min. Energy to copulate", //5
                "Start animals energy", //6
                "Number of animals at start", //7
                "Max Energy", //8
                "Energy lost per day", //9
                "Refresh time [ms]", //10
                "Left Map mode (0-Normal/1-Magic)", //11
                "Right Map mode (0-Normal/1-Magic)" //12
        };
        TextField[] t = new TextField[13];
        int[] def = {20, 30, 6, 9, 15, 15, 1000, 30, 1000, 1, 100, 0, 0};
        for(int i = 0; i < names.length; i++) {
            gridPane.add(new Label(names[i]), 0, i);
            t[i] = new TextField((""+def[i]));
            gridPane.add(t[i], 1, i);
        }


        gridPane.setVgap(20);
        gridPane.setHgap(30);
        Button okButton = new Button("OK");
        gridPane.add(okButton, 3, names.length);




        okButton.setOnAction(value -> {
            for(int i = 0; i < t.length; i++) {
                def[i] = Integer.parseInt(t[i].getText());
                System.out.println(def[i]);
            }
            startParameters = new StartParameters(def);
            openAnimationWindow();
        });
        Scene scene = new Scene(gridPane, rgb(10, 100, 50));
        stage.setScene(scene);
        stage.setTitle("Set Parameters");
        stage.show();


    }

    public void showMap(GridPane grid1, GridPane grid2, Button exi, Button startL, Button startR, Button stopL, Button stopR, VBox lStats, VBox rStats) {
        Platform.runLater(() -> {
//            HBox h1 = new HBox(2);
//            HBox h2 = new HBox(3);
//            HBox h3 = new HBox(2);
//            h1.getChildren().addAll(grid1, grid2);
//            h1.setAlignment(Pos.CENTER);
//            h2.getChildren().addAll(sl, exi, sr);
//            h2.setAlignment(Pos.CENTER);
//            h3.getChildren().addAll(lStats, rStats);
//            h3.setAlignment(Pos.CENTER);
//            VBox v1 = new VBox(3);
//            v1.getChildren().addAll(h1, h2, h3);
//            v1.setAlignment(Pos.CENTER);
            HBox h1 = new HBox(3);
            HBox h2 = new HBox(2);
            HBox h3 = new HBox(2);
            VBox v1 = new VBox(3);
            VBox v2 = new VBox(3);
            v1.setAlignment(Pos.CENTER);
            v2.setAlignment(Pos.CENTER);
            h2.getChildren().addAll(startL, stopL);
            h3.getChildren().addAll(startR, stopR);
            v1.getChildren().addAll(grid1, h2, lStats);
            v2.getChildren().addAll(grid2, h3, rStats);
            h1.getChildren().addAll(v1, exi, v2);
            h1.setAlignment(Pos.CENTER);
            Scene scene = new Scene(h1, 1920, 900);
            stage.setScene(scene);
            //h1.getChildren().clear();
            stage.setTitle("And so it began");
            stage.show();
        });

    }

    public void closeMe() {
        stage.close();
    }
}
