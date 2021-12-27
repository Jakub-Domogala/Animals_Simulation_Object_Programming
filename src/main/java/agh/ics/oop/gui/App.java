package agh.ics.oop.gui;

import agh.ics.oop.RectangularMap;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.StartParameters;
import agh.ics.oop.Stats;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static javafx.scene.paint.Color.*;

public class App extends Application {
    private RectangularMap rectangularMap;
    public Stage stage;
    private Scene scene;
    private EventHandler<ActionEvent> event;
    public StartParameters startParameters;
    private Thread engineThread;
    public BorderPane borderPane;
    public SimulationEngine engine;
    private Image animationBackground;
    private Image parametersImage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            animationBackground = new Image(new FileInputStream("src/main/resources/background2.png"));
            parametersImage = new Image(new FileInputStream("src/main/resources/monkey.png"));
        } catch (FileNotFoundException exception) {
            System.out.println(exception);
        }
        stage = primaryStage;
        parameterScene();
    }

    private void openAnimationWindow() {
        engine = new SimulationEngine(startParameters, this);
        engineThread = new Thread(engine);
        engineThread.start();
    }

    private void parameterScene() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(200, 120, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        String[] names = {
                "Map Height", //0
                "Map Width", //1
                "Jungle Ratio Numerator", //2
                "Jungle Ratio Denominator", //3
                "Energy Of every Plant", //4
                "Min. Energy to copulate", //5
                "Start animals energy", //6
                "Number of animals at start", //7
                "Max Energy (Only for energy visualisation)", //8
                "Energy lost per day", //9
                "Refresh time [ms]", //10
                "Left Map mode (0-Normal/1-Magic)", //11
                "Right Map mode (0-Normal/1-Magic)" //12
        };
        TextField[] t = new TextField[13];
        int[] def = {20, 30, 1, 3, 15, 15, 1000, 30, 1000, 1, 100, 0, 0};
        for(int i = 0; i < names.length; i++) {
            Label label = new Label(names[i]);
            label.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
            gridPane.add(label, 0, i);
            t[i] = new TextField((""+def[i]));
            t[i].setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
            gridPane.add(t[i], 1, i);
        }


        gridPane.setVgap(20);
        gridPane.setHgap(30);
        Button okButton = new Button("OK");
        okButton.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.add(okButton, 3, names.length);




        okButton.setOnAction(value -> {
            for(int i = 0; i < t.length; i++) {
                def[i] = Integer.parseInt(t[i].getText());
                System.out.println(def[i]);
            }
            startParameters = new StartParameters(def);
            openAnimationWindow();
        });
        HBox hBox = new HBox(2);
        hBox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 120, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView imgV = new ImageView(parametersImage);
        imgV.setPreserveRatio(true);
        imgV.setFitHeight(400);
        hBox.getChildren().addAll(gridPane, imgV);
        hBox.setMaxSize(900, 400);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, rgb(10, 100, 50));
        Stage paramStage = new Stage();
        paramStage.setScene(scene);
        paramStage.setTitle("Set Parameters");
        paramStage.show();
    }

    public HBox buttons(Button b1, Button b2, Button b3, Button b4, Button b5) {
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(b1, b2, b3, b4, b5);
        hBox.setSpacing(140);
        return hBox;
    }

    public HBox statHbox() {
        HBox hBox = new HBox(3);
        hBox.getChildren().addAll(engine.lMap.sVbox(), engine.rMap.sVbox());
        hBox.setSpacing(700);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    public void openSimulationWindow(Button startL, Button startR, Button stopL, Button stopR, Button exit) {
        Platform.runLater(() -> {
            borderPane = new BorderPane();
            borderPane.setLeft(engine.lMap.getMyGrid());
            borderPane.setRight(engine.rMap.getMyGrid());
            HBox but = buttons(startL, stopL, exit, startR, stopR);
            but.setAlignment(Pos.CENTER);
            borderPane.setTop(but);
            borderPane.setBottom(statHbox());
            borderPane.setCenterShape(true);



            BackgroundImage backgroundImage = new BackgroundImage(animationBackground,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);


            borderPane.setBackground(new Background(backgroundImage));
            scene = new Scene(borderPane,1400, 777);
            stage.setScene(scene);
            stage.setTitle("And so it began...");
            stage.show();
        });



    }


    public void refresh(boolean l, boolean r) {
        Platform.runLater(() -> {
            if(l) borderPane.setLeft(engine.lMap.getMyGrid());
            if(r) borderPane.setRight(engine.rMap.getMyGrid());
            borderPane.setBottom(statHbox());
            borderPane.setCenter(getMyChart(engine.lMap.getMyStats(), engine.rMap.getMyStats()));

        });

    }
    //isLeft == true means left | isLeft == false means right
    public void showGenome(VBox vBox, boolean isLeft) {
        if(isLeft) {
            borderPane.setLeft(vBox);
        } else {
            borderPane.setRight(vBox);
        }
    }

    public void showMagicEvent(boolean isLeft) {
        Platform.runLater(() -> {
            VBox vBox = getMagicVBox();
            if(isLeft) {
                borderPane.setLeft(vBox);
            } else {
                borderPane.setRight(vBox);
            }
        });

    }

    private VBox getMagicVBox() {
        Label label = new Label("Magic Event ocurred on this map");
        label.setFont(new Font(35));
        Label label1 = new Label("Press \"Start\" to continue simulation on this map");
        label1.setFont(new Font(25));
        VBox vBox = new VBox(2);
        vBox.getChildren().addAll(label, label1);
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        return vBox;
    }

    public VBox getMyChart(Stats stats1, Stats stats2) {
        VBox vBox = new VBox();
//        final String animalsAlive = "Animals";
//        final String avgLife = "Average life lenght";
//        final String deadAnimals = "Deaths counter";
//        final String plants = "Amount of Plants";
//        final String avgEnergy = "Average energy";
//        final String avgKids = "Average kids amount";

        final String lMap = "Left Map";
        final String rMap = "Right Map";

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);

        bc.setTitle("Simulations Summary");
        xAxis.setLabel("Categories");
        yAxis.setLabel("Value");

        XYChart.Series sAnimals = new XYChart.Series();
        XYChart.Series sAvgLife = new XYChart.Series();
        XYChart.Series sDeaths = new XYChart.Series();
        XYChart.Series sPlants = new XYChart.Series();
        XYChart.Series sEnergy = new XYChart.Series();
        XYChart.Series sKids = new XYChart.Series();

        sAnimals.setName("Animals");
        sAvgLife.setName("Avg Life Len");
        sDeaths.setName("Deaths");
        sPlants.setName("Plants");
        sEnergy.setName("Avg Energy");
        sKids.setName("Avg Kids");

        sAnimals.getData().add(new XYChart.Data(lMap, stats1.numberOfAnimalsAlive));
        sAnimals.getData().add(new XYChart.Data(rMap, stats2.numberOfAnimalsAlive));

        sAvgLife.getData().add(new XYChart.Data(lMap, stats1.avgLifeLen));
        sAvgLife.getData().add(new XYChart.Data(rMap, stats2.avgLifeLen));

        sDeaths.getData().add(new XYChart.Data(lMap, stats1.numberOfDeadAnimals));
        sDeaths.getData().add(new XYChart.Data(rMap, stats2.numberOfDeadAnimals));

        sPlants.getData().add(new XYChart.Data(lMap, stats1.numberOfPlants));
        sPlants.getData().add(new XYChart.Data(rMap, stats2.numberOfPlants));

        sEnergy.getData().add(new XYChart.Data(lMap, stats1.avgEnergy));
        sEnergy.getData().add(new XYChart.Data(rMap, stats2.avgEnergy));

        sKids.getData().add(new XYChart.Data(lMap, stats1.avgKids));
        sKids.getData().add(new XYChart.Data(rMap, stats2.avgKids));


        bc.getData().addAll(sAnimals, sAvgLife, sDeaths, sPlants, sEnergy, sKids);
        bc.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        bc.lookup(".chart-vertical-grid-lines").setStyle("-fx-stroke: transparent;");
        bc.lookup(".chart-horizontal-grid-lines").setStyle("-fx-stroke: E77710;");



//        Node fill = sAnimals.getNode().lookup(".chart-sAnimals-bar-fill");
//        Color color = Color.RED; // or any other color
//
//        String rgb = String.format("%d, %d, %d",
//                (int) (color.getRed() * 255),
//                (int) (color.getGreen() * 255),
//                (int) (color.getBlue() * 255));
//
//        fill.setStyle("-fx-fill: rgba(" + rgb + ", 0.15);");




        vBox.getChildren().add(bc);

        bc.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40, 0.1), CornerRadii.EMPTY, Insets.EMPTY)));
        return vBox;
    }

    public void closeMe() {
        stage.close();
    }
}
