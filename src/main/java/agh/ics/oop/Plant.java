package agh.ics.oop;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Plant implements IMapElement{
    Vector2d position;  // czy to nie powinno być prywatne?
    Image image;    // idealnie by było to przenieść gdzieś do GUI
    Image smallImage;

    public Plant(Vector2d position) {
        this.position = position;
        addImage();
    }

    public Vector2d getPosition() {
        return position;
    }

    private void addImage() {
        try {
            this.image = new Image(new FileInputStream("src/main/resources/plant.png"));    // czy każdy kwiatek musi osobno wczytywać obrazek?
            this.smallImage = new Image(new FileInputStream("src/main/resources/smallPlant.png"));
        } catch(FileNotFoundException exception) {
            System.out.println(exception.toString());   // zamiana wyjątku na komunikat jest mało opłacalna, zwłaszcza jak komunikat jest w konsoli, a aplikacja ma GUI
        }   // czy możemy dalej sensownie działać bez tego obrazka?
    }

    public String toString() {
        return "Plant";
    }

    public Image getImage() {
        return image;
    }

    public Image getSmallImage() {return smallImage;}
}
