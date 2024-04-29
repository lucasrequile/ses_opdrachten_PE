package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.function.Function;

public class Board<T> {
    private final BoardSize boardSize;
    private ArrayList<T> boardCells;
    public Board(BoardSize boardSize){
        this.boardSize = boardSize;
        this.boardCells = new ArrayList<T>();
        for(Position p: boardSize.positions()){
            boardCells.add(null);
        }
    }

    public T getCellAt(Position p){
        return this.boardCells.get(p.toIndex());
    }
    public void replaceCellAt(Position p, T newCell){
        this.boardCells.set(p.toIndex(), newCell);
    }

    public void fill(Function<Position, T> cellCreator) {
        for (Position p : boardSize.positions()) {
            this.replaceCellAt(p, cellCreator.apply(p));
        }
    }
    public Board<T> copyTo(Board<T> other){
        if (!other.boardSize.equals(this.boardSize)) {
            throw new IllegalArgumentException("Cannot copy to a board with a different size");
        }
        other.boardCells = new ArrayList<T>(this.boardCells);
        return other;
    }
    public BoardSize getBoardSize(){
        return this.boardSize;
    }
    public ArrayList<T> getBoardCells(){
        return this.boardCells;
    }
}
