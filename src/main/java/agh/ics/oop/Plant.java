package agh.ics.oop;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Plant implements IMapElement{
    Vector2d position;
    Image image;

    public Plant(Vector2d position) {
        this.position = position;
        addImage();
    }

    public Vector2d getPosition() {
        return position;
    }

    private void addImage() {
        try {
            this.image = new Image(new FileInputStream("src/main/resources/plant.png"));
        } catch(FileNotFoundException exeption) {
            System.out.println(exeption.toString());
        }
    }

    public String toString() {
        return "Plant";
    }

    public Image getImage() {
        return image;
    }
}
