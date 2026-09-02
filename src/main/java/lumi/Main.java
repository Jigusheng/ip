package lumi;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lumi.ui.MainWindow;

/**
 * Displays Lumi's JavaFX user interface using FXML.
 */
public class Main extends Application {
    private final Lumi lumi = new Lumi();

    /**
     * Creates and displays the primary application window.
     *
     * @param stage Primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane mainLayout = fxmlLoader.load();
            Scene scene = new Scene(mainLayout);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setLumi(lumi);
            stage.setTitle("Lumi");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.show();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
