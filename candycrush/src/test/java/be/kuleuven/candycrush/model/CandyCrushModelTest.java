package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CandyCrushModelTest{
    private CandyCrushModel model;
    private BoardSize boardSize;

    @BeforeEach
    public void setUp() {
        boardSize = new BoardSize(10,10);
        model = new CandyCrushModel("Player1", boardSize);
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
    //test the new stream-based methods
    @Test
    public void testWalkLeft() {
        Position position = new Position(5, 5, boardSize);
        Stream<Position> left = position.walkLeft();
        long count = left.count();
        assertEquals(6, count);
    }

    @Test
    public void testWalkRight() {
        Position position = new Position(5, 5, boardSize);
        Stream<Position> right = position.walkRight();
        long count = right.count();
        assertEquals(5, count);
    }

    @Test
    public void testWalkUp() {
        Position position = new Position(5, 5, boardSize);
        Stream<Position> up = position.walkUp();
        long count = up.count();
        assertEquals(6, count);
    }

    @Test
    public void testWalkDown() {
        Position position = new Position(5, 5, boardSize);
        Stream<Position> down = position.walkDown();
        long count = down.count();
        assertEquals(5, count);
    }

    @Test
    public void testFirstTwoHaveCandy() {
        Position position1 = new Position(5, 5, boardSize);
        Position position2 = new Position(5, 6, boardSize);
        Position position3 = new Position(5, 7, boardSize);
        Position position4 = new Position(6, 5, boardSize);
        model.getBoard().replaceCellAt(position1, new NormalCandy(3));
        model.getBoard().replaceCellAt(position2, new NormalCandy(3));
        model.getBoard().replaceCellAt(position3, new NormalCandy(3));
        model.getBoard().replaceCellAt(position4, new NormalCandy(2));
        assertTrue(model.firstTwoHaveCandy(model.getBoard(), new NormalCandy(3), position1.walkRight()));
        assertFalse(model.firstTwoHaveCandy(model.getBoard(), new NormalCandy(3), position1.walkDown()));
    }

    @Test
    public void testLongestMatchRight() {
        model.getBoard().fill((pos) -> new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,0, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(0,1, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(0,2, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(0,3, model.getBoard().getBoardSize()), new NormalCandy(3));
        assertEquals(3, model.longestMatchToRight(model.getBoard(), new Position(0,0, model.getBoard().getBoardSize())).size());
        assertEquals(2, model.longestMatchToRight(model.getBoard(), new Position(0,1, model.getBoard().getBoardSize())).size());
        assertEquals(1, model.longestMatchToRight(model.getBoard(), new Position(0,3, model.getBoard().getBoardSize())).size());
    }

    @Test
    public void testLongestMatchDown(){
        model.getBoard().fill((pos) -> new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,0, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(1,0, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(2,0, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(3,0, model.getBoard().getBoardSize()), new NormalCandy(3));
        assertEquals(3, model.longestMatchDown(model.getBoard(), new Position(0,0, model.getBoard().getBoardSize())).size());
        assertEquals(2, model.longestMatchDown(model.getBoard(), new Position(1,0, model.getBoard().getBoardSize())).size());
        assertEquals(1, model.longestMatchDown(model.getBoard(), new Position(3,0, model.getBoard().getBoardSize())).size());
    }

    @Test
    public void testFindAllMatches(){
        model.getBoard().fill((pos) -> new NormalCandy(0));
        int size = model.findAllMatches(model.getBoard()).size();
        model.getBoard().replaceCellAt(new Position(0,0, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(0,1, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(0,2, model.getBoard().getBoardSize()), new NormalCandy(1));
        model.getBoard().replaceCellAt(new Position(0,3, model.getBoard().getBoardSize()), new NormalCandy(3));
        model.getBoard().replaceCellAt(new Position(0,4, model.getBoard().getBoardSize()), new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,5, model.getBoard().getBoardSize()), new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,6, model.getBoard().getBoardSize()), new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,7, model.getBoard().getBoardSize()), new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,8, model.getBoard().getBoardSize()), new NormalCandy(2));
        model.getBoard().replaceCellAt(new Position(0,9, model.getBoard().getBoardSize()), new NormalCandy(2));
        assertEquals(size+1, model.findAllMatches(model.getBoard()).size());
    }
}