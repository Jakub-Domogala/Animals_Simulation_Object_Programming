package agh.ics.oop;

import agh.ics.oop.gui.App;
import com.sun.javafx.geom.Shape;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class MapSimulator{

    public RectangularMap map;
    public ArrayList<Animal> animals = new ArrayList<>();
    private StartParameters params;
    private App app;
    private int day = 0;
    private int deadAnimals = 0;
    private int sumLifeOfDead = 0;
    public boolean isPlaying = false;
    public boolean isLeft;
    public GameMode gameMode;
    private SimulationEngine simulationEngine;
    private int magicEventCounter = 0;


    public MapSimulator(StartParameters params, boolean isLeft, App application, int gm, SimulationEngine engine) {
        this.isLeft = isLeft;
        simulationEngine = engine;
        if(gm == 0) gameMode = GameMode.Normal;
        else gameMode = GameMode.Magic;
        app = application;
        this.params = params;
        map = new RectangularMap(0,
                new Vector2d(params.mapWidth, params.mapHeight),
                new Vector2d(params.jgWidth, params.jgHeight), gameMode, params.energyPerPlant, params.energyToCopulate);
        if(isLeft) map.mapType = MapType.Open;
        else map.mapType = MapType.Closed;
        for(int i = 0; i < params.animalsAtStart; i++) {
            Animal nAnimal = new Animal(map, map.upperRight, this.params.startEnergy, this.params.maxEnergy);
            map.place(nAnimal);
            animals.add(nAnimal);
        }
    }

    public void nextDayActions() {
        deleteDead();
        moveAnimals();
        eatPlants();
        copulate();
        newPlants();
        day++;
    }

    private void deleteDead() {
        for(int i = 0; i < animals.size(); i++) {
            if(animals.get(i).energy <= 0) {
                sumLifeOfDead += animals.get(i).lifeLen;
                map.removeAnimal(animals.get(i).getPosition(), animals.get(i));
                animals.remove(i);
                deadAnimals++;
                i--;
            }
        }
    }

    private void moveAnimals() {
        for(int i = 0; i < animals.size(); i++) {
            animals.get(i).move();
            animals.get(i).energy -= params.moveEnergy;
        }
    }

    private void eatPlants() {
        map.eatPlants();
    }

    private void copulate() {
        ArrayList<Animal> ar = map.copulate();
        for(int i = 0; i < ar.size(); i++) animals.add(ar.get(i));
        if(gameMode == GameMode.Magic && animals.size() == 5 && magicEventCounter < 3) {
            ArrayList<Animal> mAr = map.magicEvent(animals);
            for(int i = 0; i < mAr.size(); i++) animals.add(mAr.get(i));
            isPlaying = false;
            magicEventCounter += 1;
            simulationEngine.stopPlaying(isLeft);
            simulationEngine.showMagicEvent(isLeft);
        }
    }

    private void newPlants() {
        map.placeStepPlant();
        map.placeJunglePlant();
    }


    public GridPane getMyGrid() {
        int size = 400/map.upperRight.y;
        GridPane grid = new GridPane();
        for(int i = map.lowerLeft.x; i <= map.upperRight.x; i++) {
            for(int j = map.lowerLeft.y; j <= map.upperRight.y; j++) {
                Image img = map.getImage(new Vector2d(i, j));
                ImageView imgV = new ImageView(img);
                imgV.setFitHeight(size);
                imgV.setFitWidth(size);
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(size, size);
                int finalI = i;
                int finalJ = j;
                stackPane.setOnMouseClicked(event -> {
                    if(!isPlaying) {
                        app.showGenome(getGenomeVBox(new Vector2d(finalI, finalJ)), isLeft);
                    }

                });
                if(map.isInJungle(new Vector2d(i, j))) stackPane.setBackground(new Background(new BackgroundFill(Color.rgb(80, 150, 50), CornerRadii.EMPTY, Insets.EMPTY)));
                else stackPane.setBackground(new Background(new BackgroundFill(Color.rgb(140, 180, 40), CornerRadii.EMPTY, Insets.EMPTY)));
                stackPane.getChildren().addAll(imgV);
                //
                grid.add(stackPane, i, j);
            }
        }
        grid.setHgap(0);
        grid.setVgap(0);
        //grid.setAlignment(Pos.CENTER);
        return grid;
    }

    public VBox getGenomeVBox(Vector2d position) {
        int[] gen = map.showGenome(position);
        VBox vBox = new VBox(8);
        vBox.getChildren().add(new Label("Presenting genome of chosen animal"));
        for(int i = 0; i < 4; i++) {
            String str = "";
            for(int j = 0; j < 8; j++) {
                str += gen[j+i*8] + "  ";
            }
            Label label = new Label(str);
            label.setFont(new Font(66));
            vBox.getChildren().add(label);
        }
        vBox.getChildren().addAll(new Label("Press \"Start\" to continue"));
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 220, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        return vBox;
    }
    private int sumEnergies() {
        int sum = 0;
        for(int i = 0; i < animals.size(); i++) sum += animals.get(i).energy;
        return sum;
    }



    public Stats getMyStats() {
        Stats stats = new Stats();
        stats.dayNumber = day;
        stats.numberOfAnimalsAlive = animals.size();
        stats.numberOfDeadAnimals = deadAnimals;
        if(deadAnimals == 0) {stats.avgLifeLen = 0;}
        else stats.avgLifeLen = sumLifeOfDead/deadAnimals;
        stats.numberOfPlants = map.plants.size();
        if(animals.size() > 0) stats.avgEnergy = sumEnergies()/animals.size();
        else stats.avgEnergy = 0;
        stats.avgKids = 0;
        for(int i = 0; i < animals.size(); i++) stats.avgKids += animals.get(i).kidsCounter;
        if(animals.size() > 0) stats.avgKids = stats.avgKids/animals.size();
        else stats.avgKids = 0;

        //Getting dominant gene

        if(animals.size() > 0) {
            int[] commonGenes = {0,0,0,0,0,0,0,0};
            for (int i = 0; i < animals.size(); i++) {
                int[] singleDomi = {0, 0, 0, 0, 0, 0, 0, 0};
                int[] genome = animals.get(i).getGenome();
                for (int j = 0; j < 32; j++) singleDomi[genome[j]]++;
                int bestAmountOfGene = 0;
                int domiGene = -1;
                for (int j = 0; j < 8; j++) {
                    if (bestAmountOfGene < singleDomi[j]) {
                        bestAmountOfGene = singleDomi[j];
                        domiGene = j;
                    }
                }
                commonGenes[domiGene]++;
            }
            stats.dominantGen = 0;
            for(int i = 0; i < 8; i++) {
                if(commonGenes[i] > commonGenes[stats.dominantGen]) stats.dominantGen = i;
            }
        } else stats.dominantGen = -1;
        return stats;
    }

    public VBox sVbox() {
        Stats stats = getMyStats();
        VBox vBox = new VBox(6);
        Label title;
        if(isLeft) title = new Label("Open Map");
        else title = new Label("Closed Map");
        Label label1 = new Label("Day number: " + stats.dayNumber);
        Label label2 = new Label("Animals alive: " + stats.numberOfAnimalsAlive);
        Label label3 = new Label("Average animal life lenght: " + stats.avgLifeLen);
        Label label4 = new Label("Dead animals counter: " + stats.numberOfDeadAnimals);
        Label label5 = new Label("Number of plants on map: " + stats.numberOfPlants);
        Label label6 = new Label("Average animal energy: " + stats.avgEnergy);
        Label label7 = new Label("Average amount of kids: " + stats.avgKids);
        Label label8 = new Label("Dominant genome: " + stats.dominantGen);

        title.setFont(new Font(20));
        label1.setFont(new Font(24));
        label2.setFont(new Font(19));
        label3.setFont(new Font(18));
        label4.setFont(new Font(17));
        label5.setFont(new Font(16));
        label6.setFont(new Font(15));
        label7.setFont(new Font(15));
        label8.setFont(new Font(15));


        vBox.getChildren().addAll(title, label1, label2, label3, label4, label5, label6, label7, label8);
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 120, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        return vBox;
    }

}
