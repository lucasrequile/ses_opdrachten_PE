package be.kuleuven.candycrush.model;

import java.util.ArrayList;

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
    public boolean isLastColumn(){
        return columnNumber == boardSize.width() - 1;
    }
}
