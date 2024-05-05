package be.kuleuven.candycrush.model;
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
                    clearMatch(match);
                    fallDownTo(p);
                    increaseScore(match.size());
                } );
    }

    public void clearMatch(List<Position> match){
        List<Position> matchInternal = new ArrayList<>(match);
        if(matchInternal.isEmpty()){
            return;
        }
        Position p = matchInternal.getFirst();
        this.board.replaceCellAt(p, null);
        fallDownTo(p);
        matchInternal.removeFirst();
        clearMatch(matchInternal);
    }

    public void fallDownTo(Position pos){
        if(pos.rowNumber() == 0){
            return;
        }
        Position above = new Position(pos.rowNumber() - 1, pos.columnNumber(), pos.boardSize());
        if(this.board.getCellAt(above) == null){
            fallDownTo(above);
        }if(this.board.getCellAt(above) != null){
            this.board.replaceCellAt(pos, this.board.getCellAt(above));
            this.board.replaceCellAt(above, null);
            fallDownTo(above);
        }else{
            fallDownTo(above);
        }
    }

    public boolean updateBoard(){
        Set<List<Position>> matches = findAllMatches();
        if(matches.isEmpty()){
            return false;
        }
        List<Position> match = matches.iterator().next();
        clearMatch(match);
        updateBoard();
        return true;
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions){
        List<Position> positionsList = positions.toList();
        if(positionsList.size() < 2){
            return false;
        }
        return positionsList.stream()
                .limit(2)
                .filter(p -> this.board.getCellAt(p) != null)
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
                .takeWhile(p ->
                        this.board.getCellAt(pos) != null && this.board.getCellAt(p) != null &&
                            this.board.getCellAt(p).equals(this.board.getCellAt(pos)))
                .toList();
    }

    public List<Position> longestMatchDown(Position pos){
        return pos.walkDown()
                .takeWhile(p ->
                    this.board.getCellAt(pos) != null && this.board.getCellAt(p) != null &&
                    this.board.getCellAt(p).equals(this.board.getCellAt(pos))
                )
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
        updateBoard();
    }
}
