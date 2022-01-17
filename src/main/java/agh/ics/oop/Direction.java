package agh.ics.oop;

public enum Direction {
    Forward,    // wartości enuma to stałe, więc raczej piszemy wielkimi literami
    ForwardRight,
    Right,
    BackwardRight,
    Backward,
    BackwardLeft,
    Left,
    ForwardLeft;

    private static final String[] directionNames = {"F", "FR", "R", "BR", "B", "BL", "L", "FL"};

    public String toString() {return directionNames[this.ordinal()];}

}
