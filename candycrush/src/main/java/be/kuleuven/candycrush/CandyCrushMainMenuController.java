package be.kuleuven.candycrush;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CandyCrushMainMenuController {
    @FXML
    private Button startbtn;
    @FXML
    private TextField nameField;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize(){
        startbtn.setOnAction(e->loginButtonClicked());
    }

    @FXML
    private void loginButtonClicked() {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CandyCrushGameFXML.fxml"));
                Parent root = loader.load();
                CandyCrushGameController controller = loader.getController();
                controller.initialize(name);

                Stage stage = new Stage();
                stage.setTitle("Candy Crush Game");
                stage.setScene(new Scene(root, 640, 480));
                stage.show();

                // Close the login stage
                Stage loginStage = (Stage) nameField.getScene().getWindow();
                loginStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("ENTER A NAME!!!");
        }
    }
}
