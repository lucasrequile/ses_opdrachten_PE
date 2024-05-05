package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.*;
import be.kuleuven.candycrush.view.CandyCrushView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CandyCrushGameController {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane candyCrushPane;
    @FXML
    private Button resetbtn;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Label currentScoreLabel;

    private CandyCrushModel model;
    private CandyCrushView view;

    private static int HEIGHT = 10;
    private static int WIDTH = 10;

    private static int CANDY_PANE_WIDTH = 300;
    private static int CANDY_PANE_HEIGHT = 300;

    @FXML
    public void initialize(String name) {
        this.model = new CandyCrushModel(name, new BoardSize(HEIGHT,WIDTH));
        this.view = new CandyCrushView(model, candyCrushPane, CANDY_PANE_WIDTH, CANDY_PANE_HEIGHT, currentScoreLabel);
        candyCrushPane.getChildren().addAll(view);
        currentPlayerLabel.setText(model.getName());
        resetbtn.setOnAction(e->reset());
        reset();
    }
    private void update(){
        view.update();
    }

    private void reset(){
        model.reset();
        update();
    }
}