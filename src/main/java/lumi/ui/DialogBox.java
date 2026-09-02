package lumi.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Displays a chat message beside its sender's profile image.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box for a message and its sender.
     *
     * @param message Message to display.
     * @param image Sender's profile image.
     */
    private DialogBox(String message, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException error) {
            error.printStackTrace();
        }

        dialog.setText(message);
        displayPicture.setImage(image);
    }

    /**
     * Creates a right-aligned dialog for a message from the user.
     *
     * @param message User's message.
     * @param image User's profile image.
     * @return Right-aligned user dialog.
     */
    public static DialogBox getUserDialog(String message, Image image) {
        return new DialogBox(message, image);
    }

    /**
     * Creates a left-aligned dialog for a response from Lumi.
     *
     * @param message Lumi's response.
     * @param image Lumi's profile image.
     * @return Left-aligned Lumi dialog.
     */
    public static DialogBox getLumiDialog(String message, Image image) {
        DialogBox dialogBox = new DialogBox(message, image);
        dialogBox.flip();
        return dialogBox;
    }

    /** Flips the image and text so the dialog appears on the left. */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }
}
