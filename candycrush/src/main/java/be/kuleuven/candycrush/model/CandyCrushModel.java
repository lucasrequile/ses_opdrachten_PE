package be.kuleuven.candycrush.model;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CandyCrushModel {
    private String playerName;
    private int score;
    private int highScore;
    private ArrayList<Candy> candyArray;
    private BoardSize boardSize;

    public CandyCrushModel(BoardSize boardSize, String playerName) {
        this.score = 0;
        this.highScore = 0;
        this.playerName = playerName;
        this.boardSize = boardSize;
        candyArray = new ArrayList<Candy>();
        generateCandyArray(boardSize);
    }

    public void generateCandyArray(BoardSize boardSize){
        candyArray.clear();
        for(Position p : boardSize.positions()){
            Candy c = Candy.generateNewCandy();
            candyArray.add(c);
        }
    }
    public void removeSameNeighbours(Position p) {
        Iterable<Position> sameNeighbourPositions = getSameNeighbours(p);
        if(sameNeighbourPositions == null){
            return;
        }else{
            int amountOfNeighbours = 0;
            for (Position neighbourPosition : sameNeighbourPositions) {
                amountOfNeighbours++;
                candyArray.set(neighbourPosition.toIndex(), Candy.generateNewCandy());
            }
            candyArray.set(p.toIndex(), Candy.generateNewCandy());
            increaseScore(amountOfNeighbours+1);
        }
    }

    public Iterable<Position> getSameNeighbours(Position p){
        List<Position> sameNeighbours = new ArrayList<Position>();
        Candy candy = candyArray.get(p.toIndex());
        for(Position neighbourPosition : p.neighborPositions()){
            if(neighbourPosition.rowNumber() < 0 || neighbourPosition.rowNumber() >= boardSize.height() || neighbourPosition.columnNumber() < 0 || neighbourPosition.columnNumber() >= boardSize.width()){
                continue;
            }
            if(candyArray.get(neighbourPosition.toIndex()).equals(candy)){
                sameNeighbours.add(neighbourPosition);
            }
        }
        if(sameNeighbours.size() >= 2){
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


    public ArrayList<Candy> getCandyArray() {
        return candyArray;
    }

    public String getName() {
        return playerName;
    }
    public BoardSize getBoardSize() {
        return boardSize;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void reset(BoardSize boardSize) {
        generateCandyArray(boardSize);
        setScore(0);
    }
}
