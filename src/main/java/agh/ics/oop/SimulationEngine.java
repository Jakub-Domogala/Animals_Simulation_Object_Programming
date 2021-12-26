package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.ArrayList;

public class SimulationEngine implements Runnable{
    private final StartParameters parameters;
    private App app;
    public MapSimulator lMap;
    public MapSimulator rMap;
    public final boolean[] lPlaying = {false};
    public final boolean[] rPlaying = {false};

    public SimulationEngine(StartParameters params, App application) {
        parameters = params;
        app = application;
    }

    @Override
    public void run() {
        lMap = new MapSimulator(parameters, true, app, parameters.lMode, this);
        rMap = new MapSimulator(parameters, false, app, parameters.rMode, this);

        final boolean[] keepWindow = {true};
        GridPane lGrid = lMap.getMyGrid();
        GridPane rGrid = rMap.getMyGrid();
        VBox rStats = rMap.sVbox();
        VBox lStats = lMap.sVbox();
        Button exitButton = new Button("Error Maker");
        exitButton.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Button startL = new Button("Start");
        startL.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Button startR = new Button("Start");
        startR.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Button stopL = new Button("Stop");
        stopL.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Button stopR = new Button("Stop");
        stopR.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        app.openSimulationWindow(startL, startR, stopL, stopR, exitButton);


        exitButton.setOnAction(var -> {
            keepWindow[0] = false;
        });
        startL.setOnAction(var -> {
            lPlaying[0] = true;
            lMap.isPlaying = true;
        });
        startR.setOnAction(var -> {
            rPlaying[0] = true;
            rMap.isPlaying = true;
        });
        stopL.setOnAction(var -> {
            lPlaying[0] = false;
            lMap.isPlaying = false;
        });
        stopR.setOnAction(var -> {
            rPlaying[0] = false;
            rMap.isPlaying = false;
        });


            while(keepWindow[0]) {
//                if(lMap.animals.size() < 1) lPlaying = false;
//                if(rMap.animals.size() < 1) rPlaying = false;

                try {
                    Thread.sleep(parameters.refreshTime);
                } catch (InterruptedException exception) {
                    System.out.println(exception.toString());
                }
                if(lPlaying[0]) {
                    lMap.nextDayActions();
                }
                if(rPlaying[0]) {
                    rMap.nextDayActions();
                }

                app.refresh(lPlaying[0], rPlaying[0]);
                if(!app.stage.isShowing()) return;
            }
            app.closeMe();
            return;
    }

    public void stopPlaying(boolean isLeft) {
        if(isLeft) lPlaying[0] = false;
        else rPlaying[0] = true;
    }

    public void showMagicEvent(boolean isLeft) {
        app.showMagicEvent(isLeft);
    }


}
