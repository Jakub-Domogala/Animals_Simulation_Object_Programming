package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MapSimulator{

    public RectangularMap map;
    public ArrayList<Animal> animals = new ArrayList<>();
    private StartParameters params;
    private App app;
    private int day = 0;
    private int deadAnimals = 0;
    private int sumLifeOfDead = 0;


    public MapSimulator(StartParameters params, boolean isLeft, App application) {
        app = application;
        this.params = params;
        map = new RectangularMap(0,
                new Vector2d(params.mapWidth, params.mapHeight),
                new Vector2d(params.jgWidth, params.jgHeight), GameMode.Normal, params.energyPerPlant, params.energyToCopulate);
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
                if(map.isInJungle(new Vector2d(i, j))) stackPane.setBackground(new Background(new BackgroundFill(Color.rgb(80, 150, 50), CornerRadii.EMPTY, Insets.EMPTY)));
                else stackPane.setBackground(new Background(new BackgroundFill(Color.rgb(140, 180, 40), CornerRadii.EMPTY, Insets.EMPTY)));
                stackPane.getChildren().addAll(imgV);
                grid.add(stackPane, i, j);
            }
        }
        grid.setHgap(0);
        grid.setVgap(0);
        return grid;

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
        return stats;
    }

    public VBox sVbox() {
        Stats stats = getMyStats();
        VBox vBox = new VBox(6);
        Label label1 = new Label("Day number: " + stats.dayNumber);
        Label label2 = new Label("Animals alive: " + stats.numberOfAnimalsAlive);
        Label label3 = new Label("Average animal life lenght: " + stats.avgLifeLen);
        Label label4 = new Label("Dead animals counter: " + stats.numberOfDeadAnimals);
        Label label5 = new Label("Number of plants on map: " + stats.numberOfPlants);
        Label label6 = new Label("Average animal energy: " + stats.avgEnergy);

        vBox.getChildren().addAll(label1, label2, label3, label4, label5, label6);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

}
