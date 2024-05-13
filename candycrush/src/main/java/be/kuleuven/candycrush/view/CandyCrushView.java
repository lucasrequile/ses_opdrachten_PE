package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Map;


public class CandyCrushView extends Region {
    private CandyCrushModel model;
    private AnchorPane candyCrushPane;
    private Label currentScoreLabel;
    private int candyPaneWidth;
    private int candyPaneHeight;
    private int width;
    private int height;
    private int candyWidth;
    private int candyHeight;
    private Map<Position, Candy> candyArray;
    private boolean debugMode = false;

    public CandyCrushView(CandyCrushModel model, AnchorPane candyCrushPane, int candyPaneWidth, int candyPaneHeight, Label currentScoreLabel) {
        this.model = model;
        this.candyCrushPane = candyCrushPane;
        this.candyPaneWidth = candyPaneWidth;
        this.candyPaneHeight = candyPaneHeight;
        this.currentScoreLabel = currentScoreLabel;
        this.width = model.getBoard().getBoardSize().width();
        this.height = model.getBoard().getBoardSize().height();
        this.candyWidth = candyPaneWidth/(model.getBoard().getBoardSize().size() / width);
        this.candyHeight = candyPaneHeight/(model.getBoard().getBoardSize().size() / height);
        this.candyArray = model.getBoard().getBoardCells();
    }

    public void drawCandies() {
        candyCrushPane.getChildren().clear();
        int i = 0;
        for(Map.Entry<Position, Candy> entry : candyArray.entrySet()){
            Position p = entry.getKey();
            Candy c = entry.getValue();
            if(c != null){
                Node n = makeCandyShape(p, c);
                candyCrushPane.getChildren().add(n);
                n.setOnMouseClicked(event -> handleClick(p));
                i++;
            }
        }

    }

    private Node makeCandyShape(Position position, Candy candy){
        Group g = new Group();
        if(candy.isSpecial()){
            Rectangle r = new Rectangle(candyWidth, candyHeight);
            r.setFill(candy.getColor());
            int layoutX = position.columnNumber()*candyWidth-candyWidth/2;
            int layoutY = position.rowNumber()*candyHeight-candyHeight/2;
            r.setLayoutX(layoutX);
            r.setLayoutY(layoutY);
            g.getChildren().add(r);
            if(getSpecial(position) != null && debugMode){
                g.getChildren().add(getSpecial(position));
            }
            return g;
        }
        else{
            Circle c = new Circle(candyWidth/2);
            c.setFill(candy.getColor());
            int layoutX = position.columnNumber()*candyWidth;
            int layoutY = position.rowNumber()*candyHeight;
            c.setLayoutX(layoutX);
            c.setLayoutY(layoutY);
            g.getChildren().add(c);
            if(getSpecial(position) != null && debugMode){
                g.getChildren().add(getSpecial(position));
            }
            return g;
        }
    }
    private ArrayList<Position> horizontalStartingPositions = new ArrayList<>();
    private ArrayList<Position> verticalStartingPositions = new ArrayList<>();

    private void getHorizontalAndVerticalStartingPositions() {
        horizontalStartingPositions.clear();
        verticalStartingPositions.clear();
        model.horizontalStartingPositions(model.getBoard()).forEach(horizontalStartingPositions::add);
        model.verticalStartingPositions(model.getBoard()).forEach(verticalStartingPositions::add);
    }

    public Node getSpecial(Position position){
        Node a = getHorizontal(position);
        Node b = getVertical(position);
        if(a != null && b!=null){
            return new Group(b, a);
        }
        if(a != null){
            return a;
        }
        if(b != null){
            return b;
        }
        return null;
    }
    private Node getHorizontal(Position position){
        for(Position p: horizontalStartingPositions){
            if(p.equals(position)){
                Rectangle r = new Rectangle(candyWidth/3, candyHeight/3);
                r.setFill(Color.BEIGE);
                int layoutX = position.columnNumber()*candyWidth-candyWidth/2;
                int layoutY = position.rowNumber()*candyHeight-candyHeight/2;
                r.setLayoutX(layoutX);
                r.setLayoutY(layoutY);
                return r;
            }
        }
        return null;
    }
    private Node getVertical(Position position){
        for(Position p: verticalStartingPositions){
            if(p.equals(position)){
                Rectangle r = new Rectangle(candyWidth/2, candyHeight/2);
                r.setFill(Color.GRAY);
                int layoutX = position.columnNumber()*candyWidth-candyWidth/2;
                int layoutY = position.rowNumber()*candyHeight-candyHeight/2;
                r.setLayoutX(layoutX);
                r.setLayoutY(layoutY);
                return r;
            }
        }
        return null;
    }


    private void handleClick(Position p) {
        System.out.println("Clicked on candy at index: " + p.toIndex());
        model.handleClick(p);
        update();
    }

    public void toggleDebugMode(){
        debugMode = !debugMode;
    }

    public void update(){
        if(debugMode){
            getHorizontalAndVerticalStartingPositions();
        }
        drawCandies();
        currentScoreLabel.setText(""+ model.getScore());
    }
}
