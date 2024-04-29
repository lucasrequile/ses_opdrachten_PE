package be.kuleuven.candycrush.model;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CandyCrushModel {
    private final String playerName;
    private int score;
    private int highScore;
    private final Board<Candy> board;

    public CandyCrushModel(String playerName) {
        this.score = 0;
        this.highScore = 0;
        this.playerName = playerName;
        BoardSize boardSize = new BoardSize(10, 10);
        this.board = new Board<Candy>(boardSize);
        generateCandyArray();
    }

    public void generateCandyArray(){
        this.board.fill(p -> Candy.generateNewCandy());
    }
    public void removeSameNeighbours(Position p) {
        ArrayList<Position> sameNeighbourPositions = (ArrayList<Position>) getSameNeighbours(p);
        if(sameNeighbourPositions == null){
            return;
        }else{
            for (Position neighbourPosition : sameNeighbourPositions) {
                this.board.replaceCellAt(neighbourPosition, Candy.generateNewCandy());
            }
            increaseScore(sameNeighbourPositions.size());
        }
    }

    public Iterable<Position> getSameNeighbours(Position p){
        List<Position> sameNeighbours = new ArrayList<Position>();
        for(Position neighbourPosition : p.neighborPositions()){
            if(neighbourPosition.rowNumber() < 0 || neighbourPosition.rowNumber() >= board.getBoardSize().height() || neighbourPosition.columnNumber() < 0 || neighbourPosition.columnNumber() >= board.getBoardSize().width()){
                continue;
            }
            if(this.board.getCellAt(neighbourPosition).equals(board.getCellAt(p))){
                sameNeighbours.add(neighbourPosition);
            }
        }
        sameNeighbours.add(p);
        if(sameNeighbours.size() >= 3){
            return sameNeighbours;
        }else{
            return null;
        }
    }

    public void increaseScore(int amount) {
        this.score += amount;
        if (this.score > this.highScore) {
            this.highScore = this.score;
        }
    }

    public int getScore() {
        return this.score;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public Board<Candy> getBoard() {
        return this.board;
    }

    public String getName() {
        return playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void reset() {
        generateCandyArray();
        setScore(0);
    }
}
