package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.util.ArrayList;

public class SimulationEngine implements Runnable{
    private final StartParameters parameters;
    private App app;

    public SimulationEngine(StartParameters params, App application) {
        parameters = params;
        app = application;
    }

    @Override
    public void run() {
        MapSimulator lMap = new MapSimulator(parameters, true, app);
        MapSimulator rMap = new MapSimulator(parameters, false, app);
        final boolean[] lPlaying = {false};
        final boolean[] rPlaying = {false};
        final boolean[] keepWindow = {true};
        GridPane lGrid = lMap.getMyGrid();
        GridPane rGrid = rMap.getMyGrid();
        VBox rStats = rMap.sVbox();
        VBox lStats = lMap.sVbox();
        Button exitButton = new Button("Error Maker");
        Button startL = new Button("Start");
        Button startR = new Button("Start");
        Button stopL = new Button("Stop");
        Button stopR = new Button("Stop");
        exitButton.setOnAction(var -> {
            keepWindow[0] = false;
        });
        startL.setOnAction(var -> {
            lPlaying[0] = true;
        });
        startR.setOnAction(var -> {
            rPlaying[0] = true;
        });
        stopL.setOnAction(var -> {
            lPlaying[0] = false;
        });
        stopR.setOnAction(var -> {
            rPlaying[0] = false;
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
                    lGrid = lMap.getMyGrid();
                    lStats = lMap.sVbox();
                }
                if(rPlaying[0]) {
                    rMap.nextDayActions();
                    rGrid = rMap.getMyGrid();
                    rStats = rMap.sVbox();
                }

                app.showMap(lGrid, rGrid, exitButton, startL, startR, stopL, stopR, lStats, rStats);
            }
            app.closeMe();
            return;
    }
}
