package agh.ics.oop;


import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractWorldMap implements IWorldMap{
    public HashMap<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    public MapType mapType;

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        removeAnimal(oldPosition, animal);
        addAnimal(newPosition, animal);
    }


    public void removeAnimal(Vector2d oldPosition, Animal animal) {
        ArrayList<Animal> listin = animals.get(oldPosition);
        //Remove animal from list or whole list if'd  be left empty
        listin.remove(animal);
        if(listin.size() == 0) {
            animals.remove(oldPosition);
        }
    }

    private void addAnimal(Vector2d newPosition, Animal animal) {
        //Add animal to list or create list if no list for newPosition
        if(animals.containsKey(newPosition)) {
            ArrayList<Animal> listout = animals.get(newPosition);
            listout.add(animal);
            animals.put(newPosition, listout);
        } else {
            ArrayList<Animal> listout = new ArrayList<>();
            listout.add(animal);
            animals.put(newPosition, listout);
        }
    }

    public void place(Animal animal) {
        addAnimal(animal.getPosition(), animal);
    }


    //mapType = 0 means closed map, mapType != 0 means open
    public Vector2d moveToVec(Vector2d lowerLeft, Vector2d upperRight, Vector2d nPos, MapType mapType) {
        if(mapType == MapType.Closed)   // po to jest dziedziczenie, żeby nie robić takich if'ów
        {
            if(nPos.follows(lowerLeft) && nPos.precedes(upperRight))
            {
                return nPos;
            } else
            {
                return null;
            }
        } else //if open map
        {
            nPos = nPos.modulo(lowerLeft, upperRight);
            return nPos;
        }
    }

}
