package lumi.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lumi.Lumi;

/**
 * Controls Lumi's main graphical interface.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image lumiImage = new Image(getClass().getResourceAsStream("/images/DaLumi.png"));

    private Lumi lumi;

    /** Binds the scroll position to the height of the dialog container. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Lumi instance used to generate responses.
     *
     * @param lumi Lumi chatbot instance.
     */
    public void setLumi(Lumi lumi) {
        this.lumi = lumi;
    }

    /**
     * Shows the user's input and Lumi's response, then clears the input field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = lumi.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLumiDialog(response, lumiImage)
        );
        userInput.clear();
    }
}
