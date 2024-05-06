package be.kuleuven.candycrush.model;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CandyCrushModel {
    private final String playerName;
    private int score;
    private int highScore;
    private Board<Candy> board;
    private ArrayList<Position> clickList;

    public CandyCrushModel(String playerName, BoardSize boardSize) {
        this.score = 0;
        this.highScore = 0;
        this.playerName = playerName;
        this.clickList = new ArrayList<>();
        this.board = new Board<Candy>(boardSize);
        generateCandyArray();
    }

    public void generateCandyArray(){
        this.board.fill(p -> Candy.generateNewCandy());
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
        increaseScore(match.size());
        return true;
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions){
        List<Position> positionsList = positions.toList();

        if(positionsList.size() < 2){
            return false;
        }

        return positionsList.stream()
                .limit(2)
                .allMatch(p -> this.board.getCellAt(p) != null
                        && this.board.getCellAt(p).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions(){
        return this.board.getBoardSize().positions().stream()
                .filter(p -> !firstTwoHaveCandy(this.board.getCellAt(p), p.walkLeft())) //--WERKT ENKEL VOLLEDIG ZONDER DEZE LIJN. WAAROM???
        ;
    }

    public Stream<Position> verticalStartingPositions(){
        return this.board.getBoardSize().positions().stream()
                .filter(p -> !firstTwoHaveCandy(this.board.getCellAt(p), p.walkUp())) //--WERKT ENKEL VOLLEDIG ZONDER DEZE LIJN. WAAROM???
        ;
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

    public void handleClick(Position p){
        clickList.add(p);
        System.out.println(p.toIndex()+","+";"+this.board.getCellAt(p).isSpecial()+this.board.getCellAt(p).getColor());
        if(clickList.size() == 2){
            swapCandies(clickList.get(0), clickList.get(1));
            clickList.clear();
        }
    }
    public void swapCandies(Position p1, Position p2){
        if(!p1.isAdjacentTo(p2) || !matchAfterSwap(p1, p2)){
            return;
        }else{
            Candy c1 = this.board.getCellAt(p1);
            Candy c2 = this.board.getCellAt(p2);
            this.board.replaceCellAt(p1, c2);
            this.board.replaceCellAt(p2, c1);
        }
        updateBoard();
    }
    public boolean matchAfterSwap(Position p1, Position p2){
        Candy c1 = this.board.getCellAt(p1);
        Candy c2 = this.board.getCellAt(p2);
        this.board.replaceCellAt(p1, c2);
        this.board.replaceCellAt(p2, c1);
        Set<List<Position>> matches = findAllMatches();
        this.board.replaceCellAt(p1, c1);
        this.board.replaceCellAt(p2, c2);
        return !matches.isEmpty();
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

    public void setCandyAt(Position p, Candy candy) {
        this.board.replaceCellAt(p, candy);
    }

    public void reset() {
        generateCandyArray();
        setScore(0);
        updateBoard();
    }
}
