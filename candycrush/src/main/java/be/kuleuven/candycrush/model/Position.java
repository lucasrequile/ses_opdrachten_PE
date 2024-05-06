package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public record Position(int rowNumber, int columnNumber, BoardSize boardSize) {
    public Position {
        if (rowNumber < 0 || rowNumber >= boardSize.height()) {
            throw new IllegalArgumentException("rowNumber must be within the bounds of the board");
        }
        if (columnNumber < 0 || columnNumber >= boardSize.width()) {
            throw new IllegalArgumentException("columnNumber must be within the bounds of the board");
        }
    }

    public int toIndex(){
        return rowNumber * boardSize.width() + columnNumber;
    }

    public static Position fromIndex(int index, BoardSize size){
        if(index < 0 || index >= size.height() * size.width()){
            throw new IllegalArgumentException("index must be within the bounds of the board");
        }
        return new Position(index / size.width(), index % size.width(), size);
    }

    public Iterable<Position> neighborPositions(){
        ArrayList<Position> neighboringPositionsList = new ArrayList<Position>();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(i == 0 && j == 0){
                    continue;
                }
                int newRow = rowNumber + i;
                int newColumn = columnNumber + j;
                if(newRow >= 0 && newRow < boardSize.height() && newColumn >= 0 && newColumn < boardSize.width()){
                    neighboringPositionsList.add(new Position(newRow, newColumn, boardSize));
                }
            }
        }
        return neighboringPositionsList;
    }

    public Stream<Position> walkLeft(){
        return Stream.concat(Stream.of(this), this.boardSize.positions().stream()
                .filter(p -> p.columnNumber < this.columnNumber
                        && p.rowNumber == this.rowNumber
                )
                .sorted(Comparator.comparing(Position::columnNumber).reversed()));
    }

    public Stream<Position> walkRight(){
        return Stream.concat(Stream.of(this), this.boardSize.positions().stream()
                .filter(p -> p.columnNumber > this.columnNumber
                        && p.rowNumber == this.rowNumber
                )
                .sorted(Comparator.comparing(Position::columnNumber)));
    }

    public Stream<Position> walkUp(){
        return Stream.concat(Stream.of(this), this.boardSize.positions().stream()
                .filter(p -> p.rowNumber < this.rowNumber
                        && p.columnNumber == this.columnNumber
                )
                .sorted(Comparator.comparing(Position::rowNumber).reversed()));
    }

    public Stream<Position> walkDown(){
        return Stream.concat(Stream.of(this), this.boardSize.positions().stream()
                .filter(p -> p.rowNumber > this.rowNumber
                        && p.columnNumber == this.columnNumber
                )
                .sorted(Comparator.comparing(Position::rowNumber)));
    }

    public boolean isAdjacentTo(Position other){
        if(this.equals(other)){
            return false;
        }
        if(this.rowNumber == other.rowNumber){
            return Math.abs(this.columnNumber - other.columnNumber) == 1;
        }
        if(this.columnNumber == other.columnNumber){
            return Math.abs(this.rowNumber - other.rowNumber) == 1;
        }
        return false;
    }

    public boolean isLastColumn(){
        return columnNumber == boardSize.width() - 1;
    }
}
