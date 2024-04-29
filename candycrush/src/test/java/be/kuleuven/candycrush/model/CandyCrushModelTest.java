package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandyCrushModelTest{
    private CandyCrushModel model;
    private BoardSize boardSize;

    @BeforeEach
    public void setUp() {
        boardSize = new BoardSize(10,10);
        model = new CandyCrushModel(boardSize, "Player1");
    }

    @Test
    public void testCandyGeneration_RightSize() {
        model.generateCandyArray(boardSize);
        assertEquals(100, model.getCandyArray().size());
    }

    @Test
    public void testScoreIncreasing() {
        model.increaseScore(10);
        assertEquals(10, model.getScore());
        model.increaseScore(5);
        assertEquals(15, model.getScore());
    }


    @Test
    public void testRemoveSameNeighbours_under3() {
        model.generateCandyArray(boardSize);
        Candy c1 = new NormalCandy(3);
        Candy c2 = new NormalCandy(2);
        model.getCandyArray().set(0,c1);
        model.getCandyArray().set(1,c1);
        model.getCandyArray().set(2,c2);
        model.getCandyArray().set(10,c2);
        model.getCandyArray().set(11,c2);
        model.getCandyArray().set(12,c2);
        Position p = new Position(0,0,boardSize);
        model.removeSameNeighbours(p);
        assertEquals(c1, model.getCandyArray().get(1));
        assertEquals(c2, model.getCandyArray().get(10));
        assertEquals(c2, model.getCandyArray().get(11));
        assertEquals(c2, model.getCandyArray().get(12));
        assertEquals(0,model.getScore());
    }

    @Test
    public void testRemoveSameNeighbours_above3() {
        model.generateCandyArray(boardSize);
        Candy c1 = new NormalCandy(3);
        model.getCandyArray().set(0,c1);
        model.getCandyArray().set(1,c1);
        model.getCandyArray().set(2,c1);
        model.getCandyArray().set(10,c1);
        model.getCandyArray().set(11,c1);
        model.getCandyArray().set(12,c1);
        Position p = new Position(0,0,boardSize);
        model.removeSameNeighbours(p);
        assertEquals(c1, model.getCandyArray().get(2));
        assertEquals(c1, model.getCandyArray().get(12));
        assertEquals(4,model.getScore());
    }

    @Test
    public void testGameReset() {
        model.generateCandyArray(boardSize);
        model.setScore(50);
        Position p = new Position(0,0,boardSize);
        model.removeSameNeighbours(p);
        model.reset(boardSize);
        assertEquals(0, model.getScore());
        assertEquals(100, model.getCandyArray().size());
    }
    @Test
    public void testName() {
        assertEquals("Player1", model.getName());
    }

    @Test
    public void testInvalidInputsWhenMakingNewBoardSize() {
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(-10, 10));
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(10, -10));
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(0, 10));
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(10, 0));
    }

    @Test
    public void testGetWidthAndHeight() {
        BoardSize boardSize2 = new BoardSize(5,7);
        CandyCrushModel model2 = new CandyCrushModel(boardSize2, "Player1");
        assertEquals(5, model2.getBoardSize().height());
        assertEquals(7, model2.getBoardSize().width());
    }

    @Test
    public void testIsLastColumn() {
        Position position1 = new Position(5, 9, boardSize);
        assertTrue(position1.isLastColumn());
        Position position2 = new Position(5, 8, boardSize);
        assertFalse(position2.isLastColumn());
    }

    @Test
    public void testNeighborPositions() {
        Position position = new Position(5, 5, boardSize);
        Iterable<Position> neighbors = position.neighborPositions();
        for (Position neighbor : neighbors) {
            assertTrue(Math.abs(neighbor.rowNumber() - position.rowNumber()) <= 1);
            assertTrue(Math.abs(neighbor.columnNumber() - position.columnNumber()) <= 1);
        }
    }

    @Test
    public void testFromIndex() {
        Position position = Position.fromIndex(55, boardSize);
        assertEquals(5, position.rowNumber());
        assertEquals(5, position.columnNumber());
    }
    @Test
    public void testToIndex() {
        Position position = new Position(5, 5, boardSize);
        assertEquals(55, position.toIndex());
    }

}