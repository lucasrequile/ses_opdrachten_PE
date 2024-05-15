package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaximizeScoreTest {
    private static Candy characterToCandy(char c) {
        return switch(c) {
            case 'o' -> new NormalCandy(0);
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
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

    @Test
    public void testMaximizeScore1() {
        CandyCrushModel model1 = createBoardFromString("""
                                                       @@o#
                                                       o*#o
                                                       @@**
                                                       *#@@""");
        Solution s = model1.maximizeScore(model1.getBoard());
        assertEquals(16, s.getScore());
        assertEquals(4, s.currentMoves().size());
    }

    @Test
    public void testMaximizeScore2() {
        CandyCrushModel model2 = createBoardFromString("""
                                                       #oo##
                                                       #@o@@
                                                       *##o@
                                                       @@*@o
                                                       **#*o""");
        Solution s = model2.maximizeScore(model2.getBoard());
        assertEquals(23, s.getScore());
        assertEquals(7, s.currentMoves().size());
    }

    @Test
    public void testMaximizeScore3() {
        CandyCrushModel model3 = createBoardFromString("""
                                                        #@#oo@
                                                        @**@**
                                                        o##@#o
                                                        @#oo#@
                                                        @*@**@
                                                        *#@##*""");
        Solution s = model3.maximizeScore(model3.getBoard());
        assertEquals(33, s.getScore());
        assertEquals(9, s.currentMoves().size());

    }
}
