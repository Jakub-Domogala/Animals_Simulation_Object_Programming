package agh.ics.oop;


import java.util.ArrayList;

/**  The interface responsible for interacting with the map of the world.
  Assumes that Vector2d and MoveDirection classes are defined.
 */
public interface IWorldMap {

    Vector2d moveToVec(Vector2d lowerLeft, Vector2d upperRight, Vector2d nPos, MapType mapType);

    void place(Animal animal);

    boolean isOccupied(Vector2d position);

    ArrayList<Animal> strongestAnimalsAt(Vector2d position);
}