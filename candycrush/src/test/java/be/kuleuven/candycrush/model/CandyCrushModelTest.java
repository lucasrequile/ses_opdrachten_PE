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
        model = new CandyCrushModel("Player1");
    }

    @Test
    public void testCandyGeneration_RightSize() {
        model.generateCandyArray();
        int i = 0;
        for(Position p: model.getBoard().getBoardSize().positions()){
            i++;
        }
        assertEquals(100, i);
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
        model.generateCandyArray();
        Candy c1 = new NormalCandy(3);
        Candy c2 = new NormalCandy(2);
        Position p1 = new Position(0,0,boardSize);
        Position p2 = new Position(0,1,boardSize);
        Position p3 = new Position(0,2,boardSize);
        Position p4 = new Position(1,0,boardSize);
        Position p5 = new Position(1,1,boardSize);
        Position p6 = new Position(1,2,boardSize);
        model.getBoard().replaceCellAt(p1,c1);
        model.getBoard().replaceCellAt(p2,c1);
        model.getBoard().replaceCellAt(p3,c1);
        model.getBoard().replaceCellAt(p4,c2);
        model.getBoard().replaceCellAt(p5,c2);
        model.getBoard().replaceCellAt(p6,c2);
        Position p = new Position(0,0,boardSize);
        model.removeSameNeighbours(p);
        assertEquals(c1, model.getBoard().getCellAt(p1));
        assertEquals(c2, model.getBoard().getCellAt(p4));
        assertEquals(c2, model.getBoard().getCellAt(p5));
        assertEquals(c2, model.getBoard().getCellAt(p6));
        assertEquals(0,model.getScore());
    }

    @Test
    public void testRemoveSameNeighbours_above3() {
        model.generateCandyArray();
        Candy c1 = new NormalCandy(3);
        Position p1 = new Position(0,0,boardSize);
        Position p2 = new Position(0,1,boardSize);
        Position p3 = new Position(0,2,boardSize);
        Position p4 = new Position(1,0,boardSize);
        Position p5 = new Position(1,1,boardSize);
        Position p6 = new Position(1,2,boardSize);
        model.getBoard().replaceCellAt(p1,c1);
        model.getBoard().replaceCellAt(p2,c1);
        model.getBoard().replaceCellAt(p3,c1);
        model.getBoard().replaceCellAt(p4,c1);
        model.getBoard().replaceCellAt(p5,c1);
        model.getBoard().replaceCellAt(p6,c1);
        Position p = new Position(0,0,boardSize);
        model.removeSameNeighbours(p);
        assertEquals(c1, model.getBoard().getCellAt(p3));
        assertEquals(c1, model.getBoard().getCellAt(p6));
        assertEquals(4,model.getScore());
    }

    @Test
    public void testGameReset() {
        model.generateCandyArray();
        model.setScore(50);
        Position p = new Position(0,0,boardSize);
        model.removeSameNeighbours(p);
        model.reset();
        assertEquals(0, model.getScore());
        int i = 0;
        for(Position p2: model.getBoard().getBoardSize().positions()){
            i++;
        }
        assertEquals(100, i);
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