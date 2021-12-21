package agh.ics.oop;

import agh.ics.oop.gui.App;

import javafx.application.Application;

import java.util.ArrayList;
import java.util.Random;

public class World {
    public static void main(String[] args) {
        //Genome g = new Genome();
//        Random random = new Random();
//        Genome a = new Genome();
//        Genome b = new Genome();
//        for(int i = 0; i < 10; i++) {
//            Genome c = new Genome(a.genes, b.genes , 90, 7);
//        }
//
//        Genome c = new Genome(a.genes, b.genes , 90, 7);
//        System.out.println(a.genes);
//        System.out.println(b.genes);
//        System.out.println(c.genes);

//        Vector2d a = new Vector2d(6, 2);
//        Vector2d lowerLeft = new Vector2d(7, 3);
//        Vector2d upperRight = new Vector2d(13, 7);
//        System.out.println(a.modulo(lowerLeft, upperRight));

        try {
            Application.launch(App.class);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }


    }
}
