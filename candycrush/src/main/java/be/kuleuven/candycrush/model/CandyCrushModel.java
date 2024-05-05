package be.kuleuven.candycrush.model;
import java.lang.Math;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        findAllMatches().stream()
                .filter(match -> match.contains(p))
                .forEach(match -> {
                    match.forEach(pos -> this.board.replaceCellAt(pos, Candy.generateNewCandy()));
                    increaseScore(match.size());
                } );
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions){
        List<Position> positionsList = positions.toList();
        if(positionsList.size() < 2){
            return false;
        }
        return positionsList.stream()
                .limit(2)
                .allMatch(p -> this.board.getCellAt(p)
                        .equals(candy));

    }

    public Stream<Position> horizontalStartingPositions(){
        return this.board.getBoardSize().positions().stream()
                .filter(p -> !firstTwoHaveCandy(this.board.getCellAt(p), p.walkLeft()));
    }

    public Stream<Position> verticalStartingPositions(){
        return this.board.getBoardSize().positions().stream()
                .filter(p -> !firstTwoHaveCandy(this.board.getCellAt(p), p.walkUp()));
    }

    public List<Position> longestMatchToRight(Position pos){
        return pos.walkRight()
                .takeWhile(p -> this.board.getCellAt(p).equals(this.board.getCellAt(pos)))
                .toList();
    }

    public List<Position> longestMatchDown(Position pos){
        return pos.walkDown()
                .takeWhile(p -> this.board.getCellAt(p).equals(this.board.getCellAt(pos)))
                .toList();
    }

    public Set<List<Position>> findAllMatches(){
        List<List<Position>> allMatches = Stream.concat(horizontalStartingPositions(), verticalStartingPositions())
                .flatMap(p -> {
                    List<Position> horizontalMatch = longestMatchToRight(p);
                    List<Position> verticalMatch = longestMatchDown(p);
                    return Stream.of(horizontalMatch, verticalMatch);
                })
                .filter(l -> l.size() > 2)
                .sorted((match1, match2) -> match2.size() - match1.size())
                .toList();

        return allMatches.stream()
                .filter(match -> allMatches.stream()
                        .noneMatch(longerMatch -> longerMatch.size() > match.size() && longerMatch.containsAll(match)))
                .collect(Collectors.toSet());
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
