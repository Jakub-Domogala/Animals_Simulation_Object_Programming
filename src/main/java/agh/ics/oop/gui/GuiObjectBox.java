package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class GuiObjectBox {
    public VBox vBox;
    public GuiObjectBox(IMapElement elem) {
        int imageSize = 20;
        vBox = new VBox(1);
        Image image = elem.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        vBox.getChildren().addAll(imageView);
        vBox.setAlignment(Pos.CENTER);
    }
}
