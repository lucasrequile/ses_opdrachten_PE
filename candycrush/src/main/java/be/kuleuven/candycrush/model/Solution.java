package be.kuleuven.candycrush.model;

import java.util.List;

public record Solution(int score, List<Pair<Position>> currentMoves, Board<Candy> board) {

    public boolean isBetterThan(Solution other) {
        if (this.score() > other.score()) {
            return true;
        } else if (this.score() == other.score()) {
            return this.currentMoves().size() < other.currentMoves().size();
        }
        return false;
    }
    public Board<Candy> getBoard(){
        return board;
    }
    public int getScore(){
        return score;
    }
    public List<Pair<Position>> getCurrentMoves(){
        return currentMoves;
    }
}