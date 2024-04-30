package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class BoardTest {
    private Board<Candy> board;
    @BeforeEach
    public void setUp() {
         board = new Board<Candy>(new BoardSize(10, 10));
    }

    @Test
    public void testFill() {
        board.fill(p -> Candy.generateNewCandy());
        int i = 0;
        for(Position p: board.getBoardSize().positions()){
            i++;
        }
        assertEquals(100, i);
    }
    @Test
    public void testReplaceCellAt() {
        Candy c1 = new NormalCandy(3);
        Position p1 = new Position(0,0,board.getBoardSize());
        board.replaceCellAt(p1,c1);
        assertEquals(c1, board.getCellAt(p1));
    }
    @Test
    public void testGetCellAt() {
        Candy c1 = new NormalCandy(3);
        Position p1 = new Position(0,0,board.getBoardSize());
        board.replaceCellAt(p1,c1);
        assertEquals(c1, board.getCellAt(p1));
    }
    @Test
    public void testGetBoardSize() {
        assertEquals(10, board.getBoardSize().height());
        assertEquals(10, board.getBoardSize().width());
    }
    @Test
    public void testCopyTo() {
        Board<Candy> other = new Board<Candy>(new BoardSize(10, 10));
        board.fill(p -> Candy.generateNewCandy());
        board.copyTo(other);
        assertEquals(board.getBoardCells(), other.getBoardCells());
    }

    @Test
    public void testGenerateReversedBoardCells() {
        Candy c1 = new NormalCandy(3);
        Position p1 = new Position(0,0,board.getBoardSize());
        board.replaceCellAt(p1,c1);
        board.generateReversedBoardCells();
        assertTrue(board.getReverseBoardCells().containsKey(c1));
    }

    @Test
    public void testGetPositionsOfElement() {
        Candy c1 = new NormalCandy(3);
        Position p1 = new Position(0,0,board.getBoardSize());
        board.replaceCellAt(p1,c1);
        assertTrue(board.getPositionsOfElement(c1).iterator().hasNext());
    }
}
