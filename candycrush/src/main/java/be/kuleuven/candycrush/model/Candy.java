package be.kuleuven.candycrush.model;

import javafx.scene.paint.Color;

public sealed interface Candy permits NormalCandy, Licorice, JollyRancher, GummyBear, Dragibus {
    public static Candy generateNewCandy(){
        int r = (int) (Math.random() * 5);
        switch(r){
            case 0:
                return new NormalCandy(0);
            case 1:
                return new NormalCandy(1);
            case 2:
                return new NormalCandy(2);
            case 3:
                return new NormalCandy(3);
            case 4:
                int s = (int) (Math.random() * 4);
                switch(s){
                    case 0:
                        return new Licorice();
                    case 1:
                        return new JollyRancher();
                    case 2:
                        return new GummyBear();
                    case 3:
                        return new Dragibus();
                }
        }
        return null;
    }
    public default Color getColor() {
        switch ((Candy) this) {
            case Licorice l:
                return Color.BLACK;
            case JollyRancher j:
                return Color.PURPLE;
            case GummyBear g:
                return Color.YELLOW;
            case Dragibus d:
                return Color.PINK;
            case NormalCandy n:
                switch (((NormalCandy) this).color()) {
                    case 0:
                        return Color.BLUE;
                    case 1:
                        return Color.RED;
                    case 2:
                        return Color.GREEN;
                    case 3:
                        return Color.ORANGE;
                }
        }
        return null;
    }
    public default boolean isSpecial(){
        return this instanceof Licorice || this instanceof JollyRancher || this instanceof GummyBear || this instanceof Dragibus;
    }
}