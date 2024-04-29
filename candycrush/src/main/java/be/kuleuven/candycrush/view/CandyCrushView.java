package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;

public class CandyCrushView extends Region {
    private CandyCrushModel model;
    private AnchorPane candyCrushPane;
    private Label currentScoreLabel;
    private int candyPaneWidth;
    private int candyPaneHeight;
    private ArrayList<Candy> candyArray;
    private int width;
    private int height;
    private int candyWidth;
    private int candyHeight;

    public CandyCrushView(CandyCrushModel model, AnchorPane candyCrushPane, int candyPaneWidth, int candyPaneHeight, Label currentScoreLabel) {
        this.model = model;
        this.candyCrushPane = candyCrushPane;
        this.candyPaneWidth = candyPaneWidth;
        this.candyPaneHeight = candyPaneHeight;
        this.currentScoreLabel = currentScoreLabel;
        this.candyArray = model.getCandyArray();
        this.width = model.getBoardSize().width();
        this.height = model.getBoardSize().height();
        this.candyWidth = candyPaneWidth/(candyArray.size() / width);
        this.candyHeight = candyPaneHeight/(candyArray.size() / height);
    }

    public void drawCandies() {
        candyCrushPane.getChildren().clear();
        int i = 0;
        for(Candy c: candyArray){
            Position p = new Position((int)(i/width),(int)(i%width), model.getBoardSize());
            System.out.println(""+i);
            Node n = makeCandyShape(p, c);
            candyCrushPane.getChildren().add(n);
            n.setOnMouseClicked(event -> handleClick(p));
            i++;
        }

    }

    private Node makeCandyShape(Position position, Candy candy){
        if(candy.isSpecial()){
            Rectangle r = new Rectangle(candyWidth, candyHeight);
            r.setFill(candy.getColor());
            int layoutX = position.columnNumber()*candyWidth-candyWidth/2;
            int layoutY = position.rowNumber()*candyHeight-candyHeight/2;
            r.setLayoutX(layoutX);
            r.setLayoutY(layoutY);
            return r;
        }else{
            Circle c = new Circle(candyWidth/2);
            c.setFill(candy.getColor());
            int layoutX = position.columnNumber()*candyWidth;
            int layoutY = position.rowNumber()*candyHeight;
            c.setLayoutX(layoutX);
            c.setLayoutY(layoutY);
            return c;
        }
    }

    private void handleClick(Position p) {
        System.out.println("Clicked on candy at index: " + p.toIndex());
        model.removeSameNeighbours(p);
        update();
    }

    public void update(){
        drawCandies();
        currentScoreLabel.setText(""+ model.getScore());
    }
}
