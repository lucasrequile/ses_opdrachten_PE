package be.kuleuven.candycrush.model;

public record NormalCandy(int color) implements Candy{
    public NormalCandy {
        if(color < 0 || color > 3){
            throw new IllegalArgumentException("Color must be between 0 and 3");
        }
    }
}
