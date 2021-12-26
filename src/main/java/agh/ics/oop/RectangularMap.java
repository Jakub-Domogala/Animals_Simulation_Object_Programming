package agh.ics.oop;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RectangularMap extends AbstractWorldMap{
    public Vector2d lowerLeft; //lowerLeft of map
    public Vector2d upperRight; //upperRight of map
    public Vector2d jLowerLeft; //lowerLeft of Jungle
    public Vector2d jUpperRight; //upperRight of Jungle
    public HashMap<Vector2d, Plant> plants = new HashMap<>();
    public GameMode gameMode;
    public int energyPerPlant;
    public int copulateEnergy;

    public RectangularMap(int plantsAmount, Vector2d mapSize, Vector2d jungleSize, GameMode gameMode, int energyPerPlant, int copulateEnergy) {
        this.gameMode = gameMode;
        this.energyPerPlant = energyPerPlant;
        this.copulateEnergy = copulateEnergy;
        setBoundaries(mapSize, jungleSize);
        Random random = new Random();
        for(int i = 0; i < plantsAmount; i++) {
            int choice = random.nextInt( 2);
            if(choice == 0) placeJunglePlant();
            else placeStepPlant();
        }
    }

    private void setBoundaries(Vector2d mapSize, Vector2d jungleSize) {
        lowerLeft = new Vector2d(0,0);
        upperRight = new Vector2d(mapSize.subtract(new Vector2d(1,1)));
        Vector2d diff = new Vector2d(mapSize.subtract(jungleSize));
        diff = diff.divide(new Vector2d(2,2));
        jLowerLeft = diff;
        jUpperRight = diff.add(jungleSize.subtract(new Vector2d(1,1)));
    }

    public boolean isOccupied(Vector2d position) {
        if( animals.containsKey(position) || plants.containsKey(position)) return true;
        return false;
    }

    private boolean canPlantInJungle() {
        for(int i = jLowerLeft.x; i <= jUpperRight.x; i++) {
            for(int j = jLowerLeft.y; j<= jUpperRight.y; j++) {
                if(!plants.containsKey(new Vector2d(i, j)) && !animals.containsKey(new Vector2d(i, j))) return true;
            }
        }
        return false;
    }

    private boolean canPlantInStep() {
        for(int i = lowerLeft.x; i <= upperRight.x; i++) {
            for(int j = lowerLeft.y; j<= upperRight.y; j++) {
                if(!isInJungle(new Vector2d(i, j)))
                    if(!plants.containsKey(new Vector2d(i, j)) && !animals.containsKey(new Vector2d(i, j))) return true;
            }
        }
        return false;
    }


    public void placeJunglePlant() {
        if(!canPlantInJungle()) return;
        Vector2d position = randomCordsInRange(jLowerLeft, jUpperRight);
        if(isOccupied(position)) {
            placeJunglePlant();
            return;
        } else {
            plants.put(position, new Plant(position));
        }
    }

    public boolean isInJungle(Vector2d position) {
        if(position.follows(jLowerLeft) && position.precedes(jUpperRight)) return true;
        return false;
    }

    public void placeStepPlant() {
        if(!canPlantInStep()) return;
        Vector2d position = randomCordsInRange(lowerLeft, upperRight);
        if(isOccupied(position) || isInJungle(position)) {
            placeStepPlant();
        } else {
            plants.put(position, new Plant(position));
        }
    }

    public Vector2d randomCordsInRange(Vector2d lowerLeft, Vector2d upperRight) {
        Random random = new Random();
        int xDiff = upperRight.x - lowerLeft.x + 1;
        int yDiff = upperRight.y - lowerLeft.y + 1;
        int x = random.nextInt(xDiff) + lowerLeft.x;
        int y = random.nextInt(yDiff) + lowerLeft.y;
        return new Vector2d(x, y);
    }

    public ArrayList<Animal> strongestAnimalsAt(Vector2d position) {
        if(!animals.containsKey(position)) {
            return null;
        } else {
            ArrayList<Animal> list = animals.get(position);
            if(list.size() == 0) return null;
            int highestEnergy = 0;
            for(int i = 0; i < list.size(); i++) {
                if( list.get(i).energy > highestEnergy ) {
                    highestEnergy = list.get(i).energy;
                }
            }
            ArrayList<Animal> nList = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                if( list.get(i).energy == highestEnergy ) {
                    nList.add(list.get(i));
                }
            }
            return nList;
        }
    }

    public int[] showGenome(Vector2d position) {
        ArrayList<Animal> anim = strongestAnimalsAt(position);
        if(anim == null || anim.size() == 0) return null;
        return anim.get(0).getGenome();
    }

    public Image getImage(Vector2d position) {
        ArrayList<Animal> dominators = strongestAnimalsAt(position);
//        System.out.println();
        if(dominators != null && dominators.size() > 0) {
            return dominators.get(0).getImage();
        }
        if(plants.containsKey(position)) {
            if(upperRight.x < 10) {
                return plants.get(position).getImage();
            } else { return plants.get(position).getSmallImage();}
        }
        return null;
    }

    public void eatPlants() {
        for(int i = lowerLeft.x; i <= upperRight.x; i++) {
            for(int j = lowerLeft.y; j <= upperRight.y; j++) {
                ArrayList<Animal> list = strongestAnimalsAt(new Vector2d(i, j));
                if(plants.containsKey(new Vector2d(i, j)) && list != null && list.size() > 0) {
                    int energyPerAnimal = energyPerPlant/list.size();
                    for(int a = 0; a < list.size(); a++) {
                        list.get(a).energy += energyPerAnimal;
                    }
                    plants.remove(new Vector2d(i, j));
                }
            }
        }

    }

    public ArrayList<Animal> copulate() {
        ArrayList<Animal> arOut = new ArrayList<>();
        for(int i = lowerLeft.x; i <= upperRight.x; i++) {
            for(int j = lowerLeft.y; j <= upperRight.y; j++) {
                ArrayList<Animal> list = strongestAnimalsAt(new Vector2d(i, j));
                if(list != null) {
                    if(list.size() == 0) continue;
                    if(list.get(0).energy < copulateEnergy) continue;
                    if(list.size() >= 2) {
                        Random random = new Random();
                        int p1 = random.nextInt(list.size());
                        int p2 = p1;
                        while(p1 == p2) {
                            p2 = random.nextInt(list.size());
                        }
                        Animal animal = new Animal(this, list.get(p1), list.get(p2));
                        this.place(animal);
                        arOut.add(animal);

                    } else {
                        ArrayList<Animal> list2 = animals.get(new Vector2d(i, j));
                        int currEner = 0;
                        Animal animal = null;
                        for(int a = 0; a < list2.size(); a++) {
                            if(list2.get(a).energy > currEner && list2.get(a).energy < list.get(0).energy) {
                                animal = list2.get(a);
                            }
                        }
                        if(animal != null && animal.energy >= copulateEnergy) {
                            Animal nAnimal = new Animal(this, list.get(0), animal);
                            this.place(nAnimal);
                            arOut.add(nAnimal);
                        }
                    }
                }
            }
        }
        return arOut;
    }

    public ArrayList<Animal> magicEvent(ArrayList<Animal> animals) {
        ArrayList<Animal> animalArrayList = new ArrayList<>();
        for(int i = 0; i < animals.size(); i++) {
            Animal nAnimal = new Animal(this, animals.get(i), upperRight);
            place(nAnimal);
            animalArrayList.add(nAnimal);
        }
        return animalArrayList;
    }

}
