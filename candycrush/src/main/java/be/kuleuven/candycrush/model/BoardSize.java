package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public record BoardSize(int height, int width) {
    public BoardSize {
        if (height <= 0) throw new IllegalArgumentException("height must be non-negative");
        if (width <= 0) throw new IllegalArgumentException("width must be non-negative");
    }

    public Iterable<Position> positions() {
        ArrayList<Position> positionsList = new ArrayList<Position>();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                positionsList.add(new Position(i, j, this));
            }
        }
        return (Iterable<Position>)positionsList;
    }

    public int size() {
        return height * width;
    }
}
