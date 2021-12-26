package agh.ics.oop;

import javafx.scene.image.Image;

import javax.security.auth.login.CredentialException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Animal implements  IMapElement{
    private int maxEnergy;
    private final int amountOfImages = 8;

    private Vector2d position;
    private MapDirection mapDirection;
    private final RectangularMap map;
    private final Image [] images = new Image[amountOfImages];
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private final Genome genome;
    public int energy;
    public int lifeLen = 0;
    public int kidsCounter = 0;

    //Create random Animal
    public Animal(RectangularMap map, Vector2d upperRight, int energy, int maxEnergy) {
        this.map = map;
        mapDirection = randomMapDirection();
        genome = new Genome();
        position = randomCords(upperRight);
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        addImages();
    }

    //Create child of p1 and p2
    public Animal(RectangularMap map, Animal p1, Animal p2) {
        p1.energy *= 0.75;
        p2.energy *= 0.75;
        p1.kidsCounter++;
        p2.kidsCounter++;
        this.map = map;
        mapDirection = randomMapDirection();
        genome = new Genome(p1.genome.genes, p2.genome.genes, p1.energy, p2.energy);
        position = p1.getPosition();
        this.energy = (int) (p1.energy*0.3 + p2.energy*0.3);
        addImages();
    }

    //Create duplicate on random Cords
    public Animal(RectangularMap map, Animal p, Vector2d upperRight) {
        this.map = map;
        mapDirection = randomMapDirection();
        genome = p.genome;
        position = randomCords(upperRight);
        energy = p.energy;
        addImages();
    }

    void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }


    //Create random Vector2d in range of map
    private Vector2d randomCords(Vector2d upperRight) {
        Random random = new Random();
        int x = random.nextInt(upperRight.x);
        int y = random.nextInt(upperRight.y);
        return new Vector2d(x, y);
    }

    public int[] getGenome() {
        int[] gen = new int[genome.genes.size()];
        for(int i = 0; i < genome.genes.size(); i++) {
            gen[i] = genome.genes.get(i);
        }
        return gen;
    }

    public MapDirection randomMapDirection() {
        Random random = new Random();
        int f = random.nextInt(8);
        MapDirection mapDirection = MapDirection.North;
        mapDirection = mapDirection.numToDirection(f);
        return mapDirection;
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }

    public Vector2d getPosition() {
        return position;
    }

    public Image getImage() {
        int index = (int) ((float) (energy) / maxEnergy * amountOfImages);
        if(index >= amountOfImages) return images[amountOfImages-1];
        //System.out.println(index + " index of image energy: " + energy);
        return images[index];
    }

    private void addImages() {
        if(map.upperRight.x < 10) {
            for (int i = 0; i < amountOfImages; i++) {
                try {
                    images[i] = new Image(new FileInputStream("src/main/resources/animal" + i + ".png"));
                } catch (FileNotFoundException exception) {
                    System.out.println(exception.toString() + "animal");
                }
            }
        } else {
            for (int i = 1; i <= amountOfImages; i++) {
                try {
                    images[i-1] = new Image(new FileInputStream("src/main/resources/smallAnimal000" + i + ".png"));
                } catch (FileNotFoundException exception) {
                    System.out.println(exception.toString() + "smallAnimal");
                }
            }
        }
    }

    public void move() {
        lifeLen++;
        Random random = new Random();
        int choice = random.nextInt(32);
        choice = genome.genes.get(choice);
        if(choice == 0) {
            if(null != map.moveToVec(map.lowerLeft, map.upperRight, position.add(mapDirection.toUnitVector()), map.mapType)) {
                Vector2d oldPosition = position;
                position = map.moveToVec(map.lowerLeft, map.upperRight, position.add(mapDirection.toUnitVector()), map.mapType);
                map.positionChanged(oldPosition, position, this);
            }
        } else {
            if(choice == 4) {
                if(null != map.moveToVec(map.lowerLeft, map.upperRight, position.subtract(mapDirection.toUnitVector()), map.mapType)) {
                    Vector2d oldPosition = position;
                    position = map.moveToVec(map.lowerLeft, map.upperRight, position.subtract(mapDirection.toUnitVector()), map.mapType);
                    map.positionChanged(oldPosition, position, this);
                    positionChanged(oldPosition, position);
                }
            } else {
                for(int i = 0; i <= choice; i++) {
                    mapDirection.next();
                }
            }
        }
        energy--;

    }

}
