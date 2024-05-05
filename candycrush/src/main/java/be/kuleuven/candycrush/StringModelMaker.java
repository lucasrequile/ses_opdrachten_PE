package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.*;

public class StringModelMaker {
    public StringModelMaker() {
        CandyCrushModel model1 = createBoardFromString("""
   @@o#
   o*#o
   @@**
   *#@@""");

        CandyCrushModel model2 = createBoardFromString("""
   #oo##
   #@o@@
   *##o@
   @@*@o
   **#*o""");

        CandyCrushModel model3 = createBoardFromString("""
   #@#oo@
   @**@**
   o##@#o
   @#oo#@
   @*@**@
   *#@##*""");
        /*System.out.println("Model 1:");
        model1.maximizeScore();
        System.out.println("Model 1 klaar");
        System.out.println("Model 2:");
        model2.maximizeScore();
        System.out.println("Model 2 klaar");
        System.out.println("Model 3:");
        model3.maximizeScore();
        System.out.println("Model 3 klaar");*/

    }

    public static CandyCrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        var model = new CandyCrushModel("a", size); // deze moet je zelf voorzien
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                model.setCandyAt(new Position(row, col, size), characterToCandy(line.charAt(col)));
            }
        }
        return model;
    }

    private static Candy characterToCandy(char c) {
        return switch(c) {
            case 'o' -> null;
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }
}
