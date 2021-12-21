package agh.ics.oop;

public enum MapDirection {
    North,
    NorthEast,
    East,
    SouthEast,
    South,
    SouthWest,
    West,
    NorthWest;

    private static MapDirection[] values = values();

    private static final String[] directionNames = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

    private static final Vector2d[] unitVectors =
            {
                    new Vector2d(0,1),
                    new Vector2d(1,1),
                    new Vector2d(1,0),
                    new Vector2d(1,-1),
                    new Vector2d(0,-1),
                    new Vector2d(-1,-1),
                    new Vector2d(-1, 0),
                    new Vector2d(-1,1)
            };

    public MapDirection next() {return values[(this.ordinal()+1) % values.length];}

    public Vector2d toUnitVector() { return new Vector2d(unitVectors[this.ordinal()]); }

    public MapDirection numToDirection(int num) { return values[num]; }
}
