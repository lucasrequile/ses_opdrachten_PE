package be.kuleuven.candycrush.model;

import java.util.*;
import java.util.function.Function;

public class Board<T> {
    private final BoardSize boardSize;
    private Map<Position, T> boardCells;
    private Map<T, ArrayList<Position>> reverseBoardCells;
    public Board(BoardSize boardSize){
        this.boardSize = boardSize;
        this.boardCells = new HashMap<>();
        this.reverseBoardCells = new HashMap<>();
    }

    public T getCellAt(Position p){
        return this.boardCells.get(p);
    }
    public void replaceCellAt(Position p, T newCell){
        T oldCell = this.boardCells.get(p);
        if (oldCell != null && this.reverseBoardCells.containsKey(oldCell)) {
            this.reverseBoardCells.get(oldCell).remove(p);
        }
        if(this.reverseBoardCells.containsKey(newCell)){
            this.reverseBoardCells.get(newCell).add(p);
        }else{
            ArrayList<Position> positions = new ArrayList<>();
            positions.add(p);
            this.reverseBoardCells.put(newCell, positions);
        }
        this.boardCells.put(p, newCell);

    }

    public void fill(Function<Position, T> cellCreator) {
        for (Position p : boardSize.positions()) {
            this.replaceCellAt(p, cellCreator.apply(p));
        }
        generateReversedBoardCells();
    }
    public void copyTo(Board<T> other){
        if (!other.boardSize.equals(this.boardSize)) {
            throw new IllegalArgumentException("Cannot copy to a board with a different size");
        }
        other.setBoardCells(new HashMap<>(this.boardCells));
    }
    public Iterable<Position> getPositionsOfElement(T element){
        if(this.reverseBoardCells.containsKey(element)){
            return Collections.unmodifiableList(this.reverseBoardCells.get(element));
        }else{
            return Collections.unmodifiableList(new ArrayList<>());
        }
    }

    public BoardSize getBoardSize(){
        return this.boardSize;
    }
    public Map<Position,T> getBoardCells(){
        return this.boardCells;
    }
    public Map<T, ArrayList<Position>> getReverseBoardCells(){
        return this.reverseBoardCells;
    }

    public void setBoardCells(Map<Position, T> boardCells){
        this.boardCells = boardCells;
        generateReversedBoardCells();
    }

    public void generateReversedBoardCells(){
        this.reverseBoardCells.clear();
        for (Map.Entry<Position, T> entry : this.boardCells.entrySet()) {
            if(this.reverseBoardCells.containsKey(entry.getValue())){
                this.reverseBoardCells.get(entry.getValue()).add(entry.getKey());
            }else{
                ArrayList<Position> positions = new ArrayList<>();
                positions.add(entry.getKey());
                this.reverseBoardCells.put(entry.getValue(), positions);
            }
        }
    }

    public boolean isEmpty() {
        return this.boardCells.values().stream().allMatch(Objects::isNull);
    }
}
