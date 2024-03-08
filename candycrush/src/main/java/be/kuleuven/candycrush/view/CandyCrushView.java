package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;

public class CandyCrushView extends Region {
    private CandyCrushModel model;
    private AnchorPane candyCrushPane;
    private Label currentScoreLabel;
    private int candyPaneWidth;
    private int candyPaneHeight;

    public CandyCrushView(CandyCrushModel model, AnchorPane candyCrushPane, int candyPaneWidth, int candyPaneHeight, Label currentScoreLabel) {
        this.model = model;
        this.candyCrushPane = candyCrushPane;
        this.candyPaneWidth = candyPaneWidth;
        this.candyPaneHeight = candyPaneHeight;
        this.currentScoreLabel = currentScoreLabel;
    }

    public void drawCandies() {
        ArrayList<Integer> candyArray = model.getCandyArray();
        int width = model.getWidth();
        int height = model.getHeight();
        int candyWidth = candyPaneWidth/(candyArray.size() / width);
        int candyHeight = candyPaneHeight/(candyArray.size() / height);
        candyCrushPane.getChildren().clear();
        int textx = 0;
        int texty = 0;
        for(int i=0; i<height*width; i++ ){
            if(i>=width){
                textx = (i%width)*candyWidth;
                texty= (i/width)*candyHeight;
            }
            Text candyText = new Text(candyArray.get(i)+"");
            candyText.setLayoutX(textx);
            candyText.setLayoutY(texty);
            textx=textx+candyWidth;
            int index = i;
            candyText.setOnMouseClicked(event -> handleClick(index));
            candyCrushPane.getChildren().add(candyText);
        }

    }

    private void handleClick(int index) {
        System.out.println("Clicked on candy at index: " + index);
        model.removeSameNeighbours(index);
        update();
    }

    public void update(){
        drawCandies();
        currentScoreLabel.setText(""+ model.getScore());
    }
}
