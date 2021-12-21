package agh.ics.oop;

import java.util.Objects;

public class Vector2d {


    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d Nv) {
        this.x = Nv.x;
        this.y = Nv.y;
    }

    public String toString() {
        return ("(" + this.x + "," + this.y + ")");
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other) { return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y)); }

    public Vector2d lowerLeft(Vector2d other) { return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y)); }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d divide(Vector2d other) {return new Vector2d(this.x / other.x, this.y / other.y); }

    public boolean equals(Object other) {
        return other.getClass().equals(Vector2d.class) && this.x == ((Vector2d) other).x && this.y == ((Vector2d) other).y;
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }



    //Returns modulo of this Vector2d based on rectangle with given lowerLeft and UpperRight
    public Vector2d modulo(Vector2d lowerLeft, Vector2d upperRight) {
        int xDiff = upperRight.x - lowerLeft.x + 1;
        int yDiff = upperRight.y - lowerLeft.y + 1;

        int nx = this.x; //take this Vector2d
        int ny = this.y;

        nx -= lowerLeft.x; //move lowerLeft of this Rectangle to (0,0)
        ny -= lowerLeft.y;

        nx += xDiff; //add Rectangle size to new Vector2d
        ny += yDiff;

        nx = nx % xDiff; //make modulo by size of Rectangle
        ny = ny % yDiff;

        nx += lowerLeft.x; //move rectangle back on its position
        ny += lowerLeft.y;

        return new Vector2d(nx, ny); //return created Vector2d
    }





    //Compare by Vector2D X value, Vector 2D Y value, Object
    public int compareX(Vector2d v2) {
        Vector2d v1 = this;
        if (v1.x > v2.x) return 1;
        if (v1.x < v2.x) return -1; //if X value is diffrent

        if (v1.y > v2.y) return 1;
        if (v1.y < v2.y) return -1; //if Y value is diffrent
        return 0; //if all the same (shouldn't be possible)
    }

    //Compare by Vector2D Y value, Vector 2D X value, Object
    public int compareY(Vector2d v2) {
        Vector2d v1 = this;
        if (v1.y > v2.y) return 1;
        if (v1.y < v2.y) return -1; //if Y value is diffrent

        if (v1.x > v2.x) return 1;
        if (v1.x < v2.x) return -1; //if X value is diffrent
        return 0; //if all the same (shouldn't be possible)
    }


    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


}
