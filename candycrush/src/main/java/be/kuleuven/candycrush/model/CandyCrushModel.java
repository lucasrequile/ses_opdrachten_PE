package be.kuleuven.candycrush.model;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;

public class CandyCrushModel {
    private final String playerName;
    private int score;
    private int highScore;
    private final Board<Candy> board;
    private final ArrayList<Position> clickList;

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

    public int clearMatch(List<Position> match, Board<Candy> board){
        List<Position> matchInternal = new ArrayList<>(match);
        if(matchInternal.isEmpty()){
            return 0;
        }
        Position p = matchInternal.getFirst();
        board.replaceCellAt(p, null);
        fallDownTo(p, board);
        matchInternal.removeFirst();
        int clearedCandies = 1 + clearMatch(match.subList(1, match.size()), board);
        return clearedCandies;
    }

    public void fallDownTo(Position pos, Board<Candy> board){
        if(pos.rowNumber() == 0){
            return;
        }
        Position above = new Position(pos.rowNumber() - 1, pos.columnNumber(), pos.boardSize());
        if(board.getCellAt(above) == null){
            fallDownTo(above, board);
        }if(board.getCellAt(above) != null){
            board.replaceCellAt(pos, board.getCellAt(above));
            board.replaceCellAt(above, null);
            fallDownTo(above, board);
        }else{
            fallDownTo(above, board);
        }
    }

    public boolean updateBoard(Board<Candy> board, Set<List<Position>> matches){
        if(matches.isEmpty()){
            return false;
        }
        List<Position> match = matches.iterator().next();
        clearMatch(match, board);
        increaseScore(match.size());
        matches = findAllMatches(board);
        return updateBoard(board, matches);
    }

    public boolean firstTwoHaveCandy(Board<Candy> board, Candy candy, Stream<Position> positions){
        List<Position> positionsList = positions.toList();

        if(positionsList.size() < 2){
            return false;
        }

        return positionsList.stream()
                .limit(2)
                .allMatch(p -> board.getCellAt(p) != null
                        && board.getCellAt(p).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions(Board<Candy> board){
        return board.getBoardSize().positions().stream()
                .filter(p -> !firstTwoHaveCandy(board, board.getCellAt(p), p.walkLeft()));
    }

    public Stream<Position> verticalStartingPositions(Board<Candy> board){
        return board.getBoardSize().positions().stream()
                .filter(p -> !firstTwoHaveCandy(board, board.getCellAt(p), p.walkUp()));
    }

    public List<Position> longestMatchToRight(Board<Candy> board, Position pos){
        return pos.walkRight()
                .takeWhile(p ->
                        board.getCellAt(pos) != null && board.getCellAt(p) != null &&
                            board.getCellAt(p).equals(board.getCellAt(pos)))
                .toList();
    }

    public List<Position> longestMatchDown(Board<Candy> board, Position pos){
        return pos.walkDown()
                .takeWhile(p ->
                    board.getCellAt(pos) != null && board.getCellAt(p) != null &&
                    board.getCellAt(p).equals(board.getCellAt(pos))
                )
                .toList();
    }


    public Set<List<Position>> findAllMatches(Board<Candy> board) {
        Set<List<Position>> horizontalMatches = horizontalStartingPositions(board)
                .flatMap(p -> {
                    List<Position> horMatch = longestMatchToRight(board, p);
                    return Stream.of(horMatch);
                })
                .filter(match -> match.size() >2)
                .collect(Collectors.toSet());

        Set<List<Position>> verticalMatches = verticalStartingPositions(board)
                .flatMap(p -> {
                    List<Position> verMatch = longestMatchDown(board, p);
                    return Stream.of(verMatch);
                })
                .filter(match -> match.size() >2)
                .collect(Collectors.toSet());

        Set<List<Position>> combinedMatches = new HashSet<>();
        Iterator<List<Position>> horizontalIterator = horizontalMatches.iterator();
        while (horizontalIterator.hasNext()) {
            List<Position> horizontalMatch = horizontalIterator.next();
            Iterator<List<Position>> verticalIterator = verticalMatches.iterator();
            while (verticalIterator.hasNext()) {
                List<Position> verticalMatch = verticalIterator.next();
                List<Position> combinedMatch = new ArrayList<>(horizontalMatch);
                combinedMatch.retainAll(verticalMatch);
                if (!combinedMatch.isEmpty()) {
                    List<Position> fullCombinedMatch = new ArrayList<>(horizontalMatch);
                    fullCombinedMatch.addAll(verticalMatch);
                    fullCombinedMatch = new ArrayList<>(new HashSet<>(fullCombinedMatch));
                    combinedMatches.add(fullCombinedMatch);

                    horizontalIterator.remove();
                    verticalIterator.remove();
                    break;
                }
            }
        }
        Set<List<Position>> allMatches = new HashSet<>();
        allMatches.addAll(horizontalMatches);
        allMatches.addAll(verticalMatches);
        allMatches.addAll(combinedMatches);

        return allMatches;
    }

    public void handleClick(Position p){
        clickList.add(p);
       if(clickList.size() == 2){
            swapCandies(clickList.get(0), clickList.get(1), board);
            clickList.clear();
        }
    }
    public void swapCandies(Position p1, Position p2, Board<Candy> board){
        if(!p1.isAdjacentTo(p2) || !matchAfterSwap(p1, p2, board)){
            return;
        }else{
            Candy c1 = board.getCellAt(p1);
            Candy c2 = board.getCellAt(p2);
            board.replaceCellAt(p1, c2);
            board.replaceCellAt(p2, c1);
        }
        updateBoard(board, findAllMatches(board));
    }
    public boolean matchAfterSwap(Position p1, Position p2, Board<Candy> board){
        Candy c1 = board.getCellAt(p1);
        Candy c2 = board.getCellAt(p2);
        board.replaceCellAt(p1, c2);
        board.replaceCellAt(p2, c1);
        Set<List<Position>> matches = findAllMatches(board);
        board.replaceCellAt(p1, c1);
        board.replaceCellAt(p2, c2);
        return !matches.isEmpty();
    }

    public ArrayList<Pair<Position>> getAllPossibleSwaps(Board<Candy> board){
        Set<Pair<Position>> allPossibleSwaps = new HashSet<>();
        for (Position p : board.getBoardSize().positions()) {
            for (Position q : p.neighborPositions()) {
                if (p.isAdjacentTo(q)
                        && matchAfterSwap(p, q, board)) {
                    Pair<Position> swap = p.toIndex() < q.toIndex() ? new Pair<>(p, q) : new Pair<>(q, p);
                    allPossibleSwaps.add(swap);
                }
            }
        }
        return new ArrayList<>(allPossibleSwaps);
    }

    public Solution maximizeScore(Board<Candy> board) {
        Solution initialSolution = new Solution(0, new ArrayList<>(), board);
        Solution bestSoFar = find_best_score(initialSolution, null);

        System.out.println("Best Score: " + bestSoFar.getScore());
        System.out.println("Least Swaps: " + bestSoFar.currentMoves().size());
        for(Pair<Position> swap : bestSoFar.currentMoves()){
            System.out.println("Swap: " + swap.getFirst() + " and " + swap.getSecond());
        }
        return bestSoFar;
    }

    public boolean solutionIsComplete(Solution solution){
        return getAllPossibleSwaps(solution.getBoard()).isEmpty();
    }

    public Solution find_best_score(Solution current, Solution bestSoFar) {
        if(solutionIsComplete(current)) {
            if (bestSoFar == null || current.isBetterThan(bestSoFar)) {
                return current;
            } else {
                return bestSoFar;
            }
        }else{
            ArrayList<Pair<Position>> swaps = getAllPossibleSwaps(current.getBoard());
            for(Pair<Position> swap : swaps) {
                Board<Candy> newBoard = new Board<>(current.getBoard().getBoardSize());
                current.getBoard().copyTo(newBoard);
                swapCandies(swap.getFirst(), swap.getSecond(), newBoard);
                updateBoard(newBoard, findAllMatches(newBoard));
                int score = (int) newBoard.getBoardCells().values().stream()
                        .filter(Objects::isNull).count();

                ArrayList<Pair<Position>> newMoves = new ArrayList<>(current.currentMoves());
                newMoves.add(swap);

                Solution newSolution = new Solution(score, newMoves, newBoard);
                bestSoFar = find_best_score(newSolution, bestSoFar);
            }
            return bestSoFar;
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

    public void setCandyAt(Position p, Candy candy) {
        this.board.replaceCellAt(p, candy);
    }

    public void reset() {
        generateCandyArray();
        updateBoard(board, findAllMatches(board));
        setScore(0);
    }
}
